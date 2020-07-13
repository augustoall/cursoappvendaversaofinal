package br.com.pauloceami.cursoappvendas.versaofinal.Controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import br.com.pauloceami.cursoappvendas.versaofinal.Adapters.ListaItensTemporariosAdapter;
import br.com.pauloceami.cursoappvendas.versaofinal.Interfaces.iPagamento;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteChequeDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteProdutoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteProdutoDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaCBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaD_TempBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaD_TempDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.Sqlite_VENDADAO;
import br.com.pauloceami.cursoappvendas.versaofinal.Pagamento.Avista;
import br.com.pauloceami.cursoappvendas.versaofinal.Pagamento.Mensal;
import br.com.pauloceami.cursoappvendas.versaofinal.Pagamento.Quinzenal;
import br.com.pauloceami.cursoappvendas.versaofinal.Pagamento.Semanal;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Gps;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

public class VenderProdutos extends Activity {

    private BigDecimal TOTAL_DA_VENDA;
    private Intent CLI_CODIGO_INTENT;
    private Integer CLI_CODIGO;
    private ListView ListView_ItensVendidos;
    private List<SqliteVendaD_TempBean> itens_temp = new ArrayList<SqliteVendaD_TempBean>();
    private TextView venda_txv_total_da_Venda;
    private EditText venda_txt_desconto;
    private Integer DESCONTO_PADRAO_VENDEDOR;
    private SimpleDateFormat dateFormatterBR;
    private SimpleDateFormat dateFormatterUSA;
    private DatePickerDialog datePicker;
    private TextView venda_txv_datavenda;
    private TextView venda_txv_dataentrega, venda_txv_desconto;
    private String DATA_DE_ENTREGA;
    private SqliteClienteBean cliBean;
    private Toolbar toolbar;
    private SqliteConfPagamentoDao confDao;
    private SqliteConfPagamentoBean confBean;
    private SqliteVendaCBean vendaCBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vender_produtos);
        Log.i("script", "onCreate");
        declaraObjetos();
        DATA_DE_ENTREGA = "";
        setDateTimeField();
        atualiza_listview_e_calcula_total();
        CLI_CODIGO_INTENT = getIntent();
        CLI_CODIGO = CLI_CODIGO_INTENT.getIntExtra("CLI_CODIGO", 0);
        cliBean = new SqliteClienteBean();
        cliBean = new SqliteClienteDao(getApplicationContext()).buscar_cliente_pelo_codigo(CLI_CODIGO.toString());
        TextView venda_txv_nome_cliente = (TextView) findViewById(R.id.venda_txv_nome_cliente);
        venda_txv_nome_cliente.setText("Cliente: " + cliBean.getCli_nome().toString());
        DESCONTO_PADRAO_VENDEDOR = new SqliteParametroDao(this).busca_parametros().getP_desconto_do_vendedor();
        toolbar = (Toolbar) findViewById(R.id.inc_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item_addproduto:
                        Intent Lista_produtos = new Intent(getBaseContext(), Lista_produtos.class);
                        startActivity(Lista_produtos);
                        break;
                    case R.id.item_dataentrega:
                        datePicker.show();
                        break;
                    case R.id.item_formapgto:

                        itens_temp = new SqliteVendaD_TempDao(getApplicationContext()).busca_todos_itens_da_venda();
                        if (!itens_temp.isEmpty()) {
                            Intent it = new Intent(getBaseContext(), ConfPagamento.class);
                            it.putExtra("SUBTOTAL_VENDA", TOTAL_DA_VENDA.doubleValue());
                            it.putExtra("CLI_CODIGO", CLI_CODIGO);
                            startActivity(it);
                        } else {
                            Util.msg_toast_personal(getBaseContext(), "Adicione itens na venda", Util.PADRAO);
                        }


                        break;
                }
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.menu_vender_produtos);
        toolbar.findViewById(R.id.finalizar_venda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalzarvenda();
            }
        });
    }

    private void setDateTimeField() {
        dateFormatterBR = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateFormatterUSA = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                venda_txv_dataentrega.setText("Data de Entrega: " + dateFormatterBR.format(newDate.getTime()));
                DATA_DE_ENTREGA = dateFormatterUSA.format(newDate.getTime());
                Util.log(DATA_DE_ENTREGA);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onResume() {
        super.onResume();
        atualiza_listview_e_calcula_total();
        obterConfiguracoesPagamento();
    }

    private void obterConfiguracoesPagamento() {
        confBean = confDao.busca_CONFPAGAMENTO_sem_chave();
        if (confBean != null) {
            if (confBean.isAvista()) {
                venda_txv_desconto.setVisibility(View.VISIBLE);
                venda_txt_desconto.setVisibility(View.VISIBLE);
                venda_txt_desconto.setText("0");
            }
        }
    }


    public boolean verifica_limite_desconto() {
        if (confBean.isAvista()) {
            if (Integer.parseInt(venda_txt_desconto.getText().toString()) > DESCONTO_PADRAO_VENDEDOR) {
                Util.msg_toast_personal(getBaseContext(), "Limite de desconto incompatível", Util.PADRAO);
                return false;
            }
        }
        return true;
    }

    private void finalzarvenda() {

        if (itens_temp.isEmpty()) {
            Toast.makeText(this, "Não ha itens no pedido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (DATA_DE_ENTREGA.equals("")) {
            Toast.makeText(this, "Informe a data de entrega", Toast.LENGTH_SHORT).show();
            // voce pode abrir diretamente o dialog de data aqui
            return;
        }

        if (confBean == null) {
            Toast.makeText(this, "Informe a forma de pagamento", Toast.LENGTH_SHORT).show();
           // voce pode abrir a tela de configurar a forma de pagamento aqui
            return;
        }


        if (verifica_limite_desconto()) {
            Gps gps = new Gps(getApplicationContext());
            vendaCBean = new SqliteVendaCBean();
            Random numero_aleatorio = new Random();
            Integer chave = numero_aleatorio.nextInt(90000);
            vendaCBean.setVendac_chave(String.valueOf(CLI_CODIGO + chave));
            vendaCBean.setVendac_datahoravenda(Util.DataHojeComHorasUSA());
            vendaCBean.setVendac_previsaoentrega(DATA_DE_ENTREGA);
            vendaCBean.setVendac_cli_codigo(CLI_CODIGO);
            vendaCBean.setVendac_cli_nome(cliBean.getCli_nome());
            vendaCBean.setVendac_usu_codigo(new SqliteParametroDao(getApplicationContext()).busca_parametros().getP_usu_codigo());
            vendaCBean.setVendac_usu_nome(new SqliteParametroDao(getApplicationContext()).busca_parametros().getP_usuario());
            vendaCBean.setVendac_formapgto(confBean.getConf_tipo_pagamento());
            vendaCBean.setVendac_valor(BigDecimal.ZERO);
            if (confBean.isAvista()) {
                BigDecimal PERCENTUAL_DESCONTO = new BigDecimal(venda_txt_desconto.getText().toString());
                BigDecimal VALOR_DESCONTO = PERCENTUAL_DESCONTO.multiply(TOTAL_DA_VENDA).divide(new BigDecimal(100));
                vendaCBean.setVendac_desconto(VALOR_DESCONTO.setScale(2, BigDecimal.ROUND_UP));
            } else {
                vendaCBean.setVendac_desconto(BigDecimal.ZERO);
            }
            vendaCBean.setVendac_pesototal(BigDecimal.ZERO);
            vendaCBean.setVendac_enviada("N");
            vendaCBean.setVendac_latitude(gps.getLatitude());
            vendaCBean.setVendac_longitude(gps.getLongitude());
            Sqlite_VENDADAO gravavenda = new Sqlite_VENDADAO(getApplicationContext());

            Long venda_ok = gravavenda.grava_venda(vendaCBean, itens_temp);

            if (venda_ok > 0) {

                gerar_parcelas_venda();

                // atualizando a chave da venda nas configuracoes de pagamento
                new SqliteConfPagamentoDao(this).AtualizaVendac_chave_CONFPAGAMENTO(vendaCBean.getVendac_chave());

                // dando baixa no estoque no sqlite
                baixa_estoque();

                // verificar se esta venda foi feita com cheque para atualizar a chave da venda
                if (confBean.isCheque())
                    new SqliteChequeDao(this).atualizar_chave_da_venda_no_cheque(vendaCBean.getVendac_chave(), CLI_CODIGO, Util.DataHojeSemHorasUSA());

            }
            finish();
        }


    }

    private void baixa_estoque() {

        for (SqliteVendaD_TempBean produto_vendido : itens_temp) {

            // buscando a quantidade existente em estoque
            BigDecimal qtd_existente = new SqliteProdutoDao(this).
                    buscar_produto_pelo_codigo(produto_vendido.getVendad_prd_codigoTEMP().toString())
                    .getPrd_quantidade();

            // atualizando o produto para a nova quantidade
            new SqliteProdutoDao(this).atualiza_estoque(
                    new SqliteProdutoBean(
                            produto_vendido.getVendad_prd_codigoTEMP(),
                            qtd_existente.subtract(produto_vendido.getVendad_quantidadeTEMP())));


        }
    }

    public void gerar_parcelas_venda() {

        if (confBean.isMensal()) {
            iPagamento mensal = new Mensal();
            mensal.gerar_parcela(confBean, vendaCBean, this);
        }
        if (confBean.isQuinzenal()) {
            iPagamento quin = new Quinzenal();
            quin.gerar_parcela(confBean, vendaCBean, this);
        }
        if (confBean.isSemanal()) {
            iPagamento sem = new Semanal();
            sem.gerar_parcela(confBean, vendaCBean, this);
        }
        if (confBean.isAvista()) {
            iPagamento avista = new Avista();
            avista.gerar_parcela(confBean, vendaCBean, this);
        }
    }

    public void declaraObjetos() {
        confBean = new SqliteConfPagamentoBean();
        confDao = new SqliteConfPagamentoDao(this);
        venda_txv_total_da_Venda = (TextView) findViewById(R.id.venda_txv_total_da_Venda);
        ListView_ItensVendidos = (ListView) findViewById(R.id.ListView_ItensVendidos);
        venda_txv_dataentrega = (TextView) findViewById(R.id.venda_txv_dataentrega);
        venda_txv_datavenda = (TextView) findViewById(R.id.venda_txv_datavenda);
        ListView_ItensVendidos = (ListView) findViewById(R.id.ListView_ItensVendidos);
        venda_txt_desconto = (EditText) findViewById(R.id.venda_txt_desconto);
        venda_txv_desconto = (TextView) findViewById(R.id.venda_txv_desconto);
    }

    public void atualiza_listview_e_calcula_total() {
        declaraObjetos();
        venda_txv_datavenda.setText("Data/Hora Venda : " + Util.DataHojeComHorasBR());
        itens_temp = new SqliteVendaD_TempDao(getApplicationContext()).busca_todos_itens_da_venda();
        ListView_ItensVendidos.setAdapter(new ListaItensTemporariosAdapter(getApplicationContext(), itens_temp));
        if (!itens_temp.isEmpty()) {
            TOTAL_DA_VENDA = BigDecimal.ZERO;
            for (SqliteVendaD_TempBean item : itens_temp) {
                TOTAL_DA_VENDA = TOTAL_DA_VENDA.add(item.getVendad_quantidadeTEMP().multiply(item.getVendad_preco_vendaTEMP()));
            }
            venda_txv_total_da_Venda.setText(TOTAL_DA_VENDA.setScale(2, BigDecimal.ROUND_UP).toString());
        } else {
            venda_txv_total_da_Venda.setText("0.00");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new SqliteVendaD_TempDao(getApplicationContext()).excluir_itens();
    }
}