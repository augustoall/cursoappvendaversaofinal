package br.com.pauloceami.cursoappvendas.versaofinal.Controllers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteDao;
import br.com.pauloceami.cursoappvendas.versaofinal.R;

//import android.support.v7.app.ActionBarActivity;

public class Lista_clientes extends AppCompatActivity {


    public static final String TELA_ALTERAR_CLIENTE = "ALTERAR_CLIENTE";
    public static final String TELA_DE_VENDAS = "VENDER_PRODUTOS";
    public static final String TELA_DE_CONTAS_RECEBER = "CONTAS_RECEBER";
    public static String PESQUISAR_CLIENTE_NOME = "Pesquisar cliente pelo Nome";
    public static String PESQUISAR_CLIENTE_FANTASIA = "Pesquisar cliente pelo Nome Fantasia";
    public static String PESQUISAR_CLIENTE_CIDADE = "Pesquisar cliente pela Cidade";
    public static String PESQUISAR_CLIENTE_BAIRRO = "Pesquisar cliente pelo Bairro";
    private Spinner adm_sp_filtrarcliente;
    private SimpleCursorAdapter adapter;
    private List<String> array_spinner = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    private String selecao_spinner;
    private Cursor cursor;
    private EditText adm_txt_pesquisacliente;
    private ListView adm_listview_cliente;
    private Intent TELA_QUE_CHAMOU_INTENT;
    private String TELA_QUE_CHAMOU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_clientes);


        TELA_QUE_CHAMOU_INTENT = getIntent();
        TELA_QUE_CHAMOU = TELA_QUE_CHAMOU_INTENT.getStringExtra("TELA_QUE_CHAMOU");


        array_spinner.add(PESQUISAR_CLIENTE_NOME);
        array_spinner.add(PESQUISAR_CLIENTE_FANTASIA);
        array_spinner.add(PESQUISAR_CLIENTE_CIDADE);
        array_spinner.add(PESQUISAR_CLIENTE_BAIRRO);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array_spinner);
        adm_sp_filtrarcliente = (Spinner) findViewById(R.id.adm_sp_filtrarcliente);
        adm_sp_filtrarcliente.setAdapter(arrayAdapter);

        adm_sp_filtrarcliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View view, int posicao, long id) {

                selecao_spinner = spinner.getItemAtPosition(posicao).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mostrar_clientes_listview();

    }


    private void mostrar_clientes_listview() {

        SqliteClienteBean cliBean = new SqliteClienteBean();
        SqliteClienteDao cliDao = new SqliteClienteDao(this);

        final Cursor cursor = cliDao.buscar_todos_cliente();


        String[] colunas = new String[]{cliBean.C_CODIGO_CLIENTE_CURSOR, cliBean.C_NOME_DO_CLIENTE, cliBean.C_NOME_FANTASIA, cliBean.C_CIDADE_CLIENTE, cliBean.C_BAIRRO_CLIENTE, cliBean.C_CONTATO_CLIENTE};
        int[] para = new int[]{R.id.adm_txv_clicodigo, R.id.adm_txv_cli_nome, R.id.adm_txv_cli_fantasia, R.id.adm_txv_cli_cidade, R.id.adm_txv_cli_bairro, R.id.adm_txv_cli_contato};

        adapter = new SimpleCursorAdapter(this, R.layout.lista_cliente_item, cursor, colunas, para, 0);
        adm_listview_cliente = (ListView) findViewById(R.id.adm_listview_cliente);
        adm_listview_cliente.setAdapter(adapter);
        adm_listview_cliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listview, View view, int posicao, long id) {

                Cursor cliente_cursor = (Cursor) listview.getItemAtPosition(posicao);

                switch (TELA_QUE_CHAMOU) {

                    case TELA_ALTERAR_CLIENTE:

                        Intent AdministraCliente = new Intent(getBaseContext(), AdministraCliente.class);
                        AdministraCliente.putExtra("CLI_CODIGO", cliente_cursor.getInt(cursor.getColumnIndex("_id")));
                        AdministraCliente.putExtra("FLAG_ALTERAR", true);
                        startActivity(AdministraCliente);
                        finish();
                        break;

                    case TELA_DE_VENDAS:

                        Intent VenderProdutos = new Intent(getBaseContext(), br.com.pauloceami.cursoappvendas.versaofinal.Controllers.VenderProdutos.class);
                        VenderProdutos.putExtra("CLI_CODIGO", cliente_cursor.getInt(cursor.getColumnIndex("_id")));
                        startActivity(VenderProdutos);
                        finish();

                        break;

                    case TELA_DE_CONTAS_RECEBER:

                        Intent ListaPedidosClientes = new Intent(getBaseContext(), ListaPedidosClientes.class);
                        ListaPedidosClientes.putExtra("CLI_CODIGO", cliente_cursor.getInt(cursor.getColumnIndex("_id")));
                        startActivity(ListaPedidosClientes);

                        break;


                }
            }
        });

        adm_txt_pesquisacliente = (EditText) findViewById(R.id.adm_txt_pesquisacliente);
        adm_txt_pesquisacliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence texto_digitado, int start, int before, int count) {
                adapter.getFilter().filter(texto_digitado);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        adapter.setFilterQueryProvider(new FilterQueryProvider() {

            private Cursor cursor;

            @Override
            public Cursor runQuery(CharSequence valor) {
                SqliteClienteDao cli = new SqliteClienteDao(getApplicationContext());

                if (selecao_spinner == PESQUISAR_CLIENTE_NOME) {
                    this.cursor = cli.buscar_cliente_na_pesquisa_edittext(valor.toString(), cli.NOME_DO_CLIENTE);
                }

                if (selecao_spinner == PESQUISAR_CLIENTE_FANTASIA) {
                    this.cursor = cli.buscar_cliente_na_pesquisa_edittext(valor.toString(), cli.NOME_FANTASIA);
                }

                if (selecao_spinner == PESQUISAR_CLIENTE_CIDADE) {
                    this.cursor = cli.buscar_cliente_na_pesquisa_edittext(valor.toString(), cli.NOME_CIDADE);
                }

                if (selecao_spinner == PESQUISAR_CLIENTE_BAIRRO) {
                    this.cursor = cli.buscar_cliente_na_pesquisa_edittext(valor.toString(), cli.NOME_BAIRRO);
                }
                return cursor;
            }
        });
    }

}
