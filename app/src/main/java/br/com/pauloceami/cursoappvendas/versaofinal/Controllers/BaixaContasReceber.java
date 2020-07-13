package br.com.pauloceami.cursoappvendas.versaofinal.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.pauloceami.cursoappvendas.versaofinal.Adapters.BaixaContaReceberAdapter;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteHistoricoPagamentoDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteHistoricoPagamentosBean;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

public class BaixaContasReceber extends AppCompatActivity {

    private Intent CLI_CODIGO_INTENT;
    private Intent VENDAC_CHAVE_INTENT;
    private Integer CLI_CODIGO;
    private String VENDAC_CHAVE;
    private ListView rec_lv_contas;
    private TextView rec_txv_info_cliente;
    private EditText rec_txt_valor_a_pagar;
    private ImageView rec_btn_receber;
    private CheckBox rec_check_marcar_tudo;
    private TextView rec_txv_total;
    private SparseBooleanArray sparse_titulos_marcados;
    private double SOMA;
    private List<SqliteConRecBean> titulos_em_aberto;
    private Integer parcelas_selecionadas = 0;
    private String FORMA_PAGAMENTO_ESCOLHIDA = "DINHEIRO";
    private SimpleDateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baixa_contas_receber);

        declaraObjetos();

        CLI_CODIGO_INTENT = getIntent();
        VENDAC_CHAVE_INTENT = getIntent();

        VENDAC_CHAVE = VENDAC_CHAVE_INTENT.getStringExtra("VENDAC_CHAVE");
        CLI_CODIGO = CLI_CODIGO_INTENT.getIntExtra("CLI_CODIGO", 0);


        titulos_em_aberto = new SqliteConRecDao(this).busca_parcelas_do_cliente(CLI_CODIGO, VENDAC_CHAVE);

        rec_lv_contas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        rec_lv_contas.setAdapter(new BaixaContaReceberAdapter(this, titulos_em_aberto));


        rec_lv_contas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                calcula_parcelas_marcadas();
            }
        });


        rec_check_marcar_tudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marca_e_desmarca_todos_items_listview();
            }
        });


    }


    private void declaraObjetos() {
        rec_lv_contas = (ListView) findViewById(R.id.rec_lv_contas);
        rec_txv_info_cliente = (TextView) findViewById(R.id.rec_txv_info_cliente);
        rec_txt_valor_a_pagar = (EditText) findViewById(R.id.rec_txt_valor_a_pagar);
        rec_btn_receber = (ImageView) findViewById(R.id.rec_btn_receber);
        rec_check_marcar_tudo = (CheckBox) findViewById(R.id.rec_check_marcar_tudo);
        rec_txv_total = (TextView) findViewById(R.id.rec_txv_total);

    }


    private void marca_e_desmarca_todos_items_listview() {
        if (rec_check_marcar_tudo.isChecked()) {
            for (int i = 0; i < rec_lv_contas.getCount(); i++) {
                rec_lv_contas.setItemChecked(i, true);
                calcula_parcelas_marcadas();
            }
        } else {
            for (int i = 0; i < rec_lv_contas.getCount(); i++) {
                rec_lv_contas.setItemChecked(i, false);
                calcula_parcelas_marcadas();
            }
        }
    }


    public boolean validar() {

        //  boolean validou = true;

        if (rec_txt_valor_a_pagar.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "campo vazio", Toast.LENGTH_SHORT).show();
            return false;
        }


        // pegar as posições marcadas
        sparse_titulos_marcados = rec_lv_contas.getCheckedItemPositions();

        // pode haver mais de um check marcado , por isso precisamos do for
        // caso contrario poderiamos usar sparse_titulos_marcados.keyAt(0).get...
        for (int i = 0; i < sparse_titulos_marcados.size(); i++) {

            double valor_pago = Double.valueOf(rec_txt_valor_a_pagar.getText().toString());
            double valor_parcela = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_valor_receber();

            Log.i("script", "valor pago :::" + valor_pago);
            Log.i("script", "valor parcela :::" + valor_parcela);


            if (valor_pago > valor_parcela && sparse_titulos_marcados.size() == 1) {
                Toast.makeText(this, "o valor nao pode ser maior", Toast.LENGTH_SHORT).show();
                return false;
            }


            // comente esta lógica caso queira receber valores menores na parcela
            //if (valor_pago < valor_parcela && sparse_titulos_marcados.size() == 1) {
            //    Toast.makeText(this, "o valor nao pode ser menor", Toast.LENGTH_SHORT).show();
            //    return false;
            // }

        }


        return true;
    }


    public void recebe_conta(View v) {

        if (!validar()) {
            return;
        }

        Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
        Date data_pagamento = calendar.getTime();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        for (int i = 0; i < parcelas_selecionadas; i++) {

            if (parcelas_selecionadas == 1) {

                if (sparse_titulos_marcados.valueAt(i)) {

                    // aqui dentro as parcelas estao selecionadas

                    double valor_pago = Double.valueOf(rec_txt_valor_a_pagar.getText().toString());
                    double valor_parcela = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_valor_receber();


                    // variaveis do historico de pagamento da parcela
                    int num_parcela = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_numparcela();
                    double val_real_parc = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_valor_receber();
                    double val_pago_dia = valor_pago;
                    double restante = valor_parcela - valor_pago;
                    String datapagamento = dateFormat.format(data_pagamento);
                    String nomecliente = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_cli_nome();
                    String como_pagou = FORMA_PAGAMENTO_ESCOLHIDA;
                    String chave_venda = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getVendac_chave();
                    String enviado = "N";

                    if (valor_pago == valor_parcela) {
                        baixar_parcela(i, valor_pago, data_pagamento);
                        gerar_historico(num_parcela, datapagamento, nomecliente, como_pagou, chave_venda, enviado, new BigDecimal(val_real_parc), new BigDecimal(valor_pago), BigDecimal.ZERO);
                    }

                    if (valor_pago < valor_parcela) {

                        SqliteConRecBean rec =  new SqliteConRecBean();
                        rec.setRec_valor_receber(restante);
                        rec.setVendac_chave(chave_venda);
                        rec.setRec_numparcela(num_parcela);
                        new SqliteConRecDao(this).atualiza_valorparcela(rec);


                        gerar_historico(num_parcela, datapagamento, nomecliente, como_pagou, chave_venda, enviado, new BigDecimal(val_real_parc), new BigDecimal(val_pago_dia), new BigDecimal(restante));
                    }
                }
            }


            if (parcelas_selecionadas > 1) {
                if (sparse_titulos_marcados.valueAt(i)) {
                    // variaveis do historico de pagamento da parcela
                    int num_parcela = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_numparcela();
                    double val_real_parc = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_valor_receber();
                    String datapagamento = dateFormat.format(data_pagamento);
                    String nomecliente = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_cli_nome();
                    String como_pagou = FORMA_PAGAMENTO_ESCOLHIDA;
                    String chave_venda = titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getVendac_chave();
                    String enviado = "N";

                    baixar_parcela(i,titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_valor_receber(), data_pagamento);
                    gerar_historico(num_parcela, datapagamento, nomecliente, como_pagou, chave_venda, enviado, new BigDecimal(val_real_parc), new BigDecimal(val_real_parc), BigDecimal.ZERO);
                }
            }

            Toast.makeText(this, "baixa efetuada com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void baixar_parcela(int i, double valor_pago, Date data_pagamento) {

        SqliteConRecDao recDao = new SqliteConRecDao(this);
        SqliteConRecBean recBean = new SqliteConRecBean();
        recBean.setRec_valorpago(valor_pago);
        recBean.setRec_data_que_pagou(dateFormat.format(data_pagamento));
        recBean.setRec_recebeu_com("DINHEIRO");
        recBean.setRec_enviado("N");
        recBean.setVendac_chave(titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getVendac_chave());
        recBean.setRec_numparcela(titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_numparcela());
        recDao.baixar_parcela_cliente_integral(recBean);
    }

    private void gerar_historico(
            int num_parcela,
            String datapagamento,
            String nomecliente,
            String como_pagou,
            String chave_venda,
            String enviado,
            BigDecimal hist_valor_real_parcela,
            BigDecimal hist_valor_pago_no_dia,
            BigDecimal zero) {
        // gerar o historico de pagamento da parcela paga
        SqliteHistoricoPagamentosBean pgtoBean = new SqliteHistoricoPagamentosBean();
        SqliteHistoricoPagamentoDao pgtoDao = new SqliteHistoricoPagamentoDao(this);

        pgtoBean.setHist_numero_parcela(num_parcela);
        pgtoBean.setHist_valor_real_parcela(hist_valor_real_parcela);
        pgtoBean.setHist_valor_pago_no_dia(hist_valor_pago_no_dia);

        pgtoBean.setHist_restante_a_pagar(zero);
        pgtoBean.setHist_data_do_pagamento(datapagamento);
        pgtoBean.setHist_nome_cliente(nomecliente);

        pgtoBean.setHist_como_pagou(como_pagou);
        pgtoBean.setVendac_chave(chave_venda);
        pgtoBean.setHist_enviado(enviado);

        pgtoDao.grava_historico(pgtoBean);
    }


    public void calcula_parcelas_marcadas() {

        parcelas_selecionadas = 0;

        sparse_titulos_marcados = rec_lv_contas.getCheckedItemPositions();
        SOMA = 0;

        for (int i = 0; i < sparse_titulos_marcados.size(); i++) {

            if (sparse_titulos_marcados.valueAt(i)) {

                parcelas_selecionadas++;

                if (titulos_em_aberto.get(i).getRec_valorpago() > 0) {
                    rec_lv_contas.setItemChecked(i, false);
                } else {
                    SOMA = SOMA + titulos_em_aberto.get(sparse_titulos_marcados.keyAt(i)).getRec_valor_receber();
                }

            }


        }
        rec_txv_total.setText("Total R$.:" + Util.formatDouble(SOMA));
        rec_txt_valor_a_pagar.setText("" + Util.formatDouble(SOMA));

        if (parcelas_selecionadas > 1) {
            rec_txt_valor_a_pagar.setEnabled(false);
        } else {
            rec_txt_valor_a_pagar.requestFocus();
            rec_txt_valor_a_pagar.selectAll();
            rec_txt_valor_a_pagar.setEnabled(true);
        }


    }


}
