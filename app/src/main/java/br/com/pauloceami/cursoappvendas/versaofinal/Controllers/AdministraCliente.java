package br.com.pauloceami.cursoappvendas.versaofinal.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import br.com.pauloceami.cursoappvendas.versaofinal.Json.VolleySingleton;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroDao;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.CustomJsonObjectRequest;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;


public class AdministraCliente extends Activity {


    private static String C_CODIGO_CLIENTE;
    private static String C_NOME_DO_CLIENTE;
    private static String C_NOME_FANTASIA;
    private static String C_ENDERECO_CLIENTE;
    private static String C_BAIRRO_CLIENTE;
    private static String C_CEP_CLIENTE;
    private static String C_CIDADE_CLIENTE;
    private static String C_CONTATO_CLIENTE;
    private static String C_DATA_NASCIMENTO;
    private static String C_CNPJCPF;
    private static String C_RGINSCRICAO_ESTADUAL;
    private static String C_EMAIL_CLIENTE;
    private static String C_ENVIADO;
    private static String C_CHAVE_DO_CLIENTE;

    private Intent FLAG_ALTERAR_INTENT;
    private boolean FLAG_ALTERAR;

    private Intent CLI_CODIGO_INTENT;
    private Integer CLI_CODIGO;

    private Button btn_administra_cliente;
    private EditText txt_cli_codigo;
    private EditText txt_cli_nome;
    private EditText txt_cli_fantasia;
    private EditText txt_cli_endereco;
    private EditText txt_cli_bairro;
    private EditText txt_cli_cep;
    private EditText txt_cid_nome;
    private EditText txt_cli_contato;
    private EditText txt_cli_nascimento;
    private EditText txt_cli_cpfcnpj;
    private EditText txt_cli_rginscricaoest;
    private EditText txt_cli_email;
    private EditText txt_cli_chave;
    private RequestQueue rq;
    public static final String TAG_REQUEST = "tag";
    private JSONArray clientes_array;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administra_cliente);

        declaraObjetos();
        rq = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();
        CLI_CODIGO_INTENT = getIntent();
        CLI_CODIGO = CLI_CODIGO_INTENT.getIntExtra("CLI_CODIGO", 0);

        FLAG_ALTERAR_INTENT = getIntent();
        FLAG_ALTERAR = FLAG_ALTERAR_INTENT.getBooleanExtra("FLAG_ALTERAR", false);

        if (FLAG_ALTERAR) {
            btn_administra_cliente.setText("Alterar Cliente");
            SqliteClienteBean cliBean = new SqliteClienteBean();
            cliBean = new SqliteClienteDao(getApplicationContext()).buscar_cliente_pelo_codigo(CLI_CODIGO.toString());
            txt_cli_codigo.setText(cliBean.getCli_codigo().toString());
            txt_cli_nome.setText(cliBean.getCli_nome());
            txt_cli_fantasia.setText(cliBean.getCli_fantasia());
            txt_cli_endereco.setText(cliBean.getCli_endereco());
            txt_cli_bairro.setText(cliBean.getCli_bairro());
            txt_cli_cep.setText(cliBean.getCli_cep());
            txt_cid_nome.setText(cliBean.getCid_nome());
            txt_cli_contato.setText(cliBean.getCli_contato());
            txt_cli_nascimento.setText(Util.FormataDataDDMMAAAA(cliBean.getCli_nascimento()));
            txt_cli_cpfcnpj.setText(cliBean.getCli_cpfcnpj());
            txt_cli_rginscricaoest.setText(cliBean.getCli_rginscricaoest());
            txt_cli_email.setText(cliBean.getCli_email());
            txt_cli_chave.setText(cliBean.getCli_chave());

        } else {
            btn_administra_cliente.setText("Cadastrar Cliente");
        }


        btn_administra_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validou_campos()) {


                    if (Util.checarConexaoCelular(getApplicationContext())) {

                        if (FLAG_ALTERAR) {

                            alteraou_ou_grava_cliente_no_sqlite();

                        } else {

                            cadastrar_cliente_no_servidor_e_retorna_pro_android();

                        }


                    } else {

                        alteraou_ou_grava_cliente_no_sqlite();

                    }

                }
            }
        });


    }

    public void cadastrar_cliente_no_servidor_e_retorna_pro_android() {

        declaraObjetos();
        String UrlCadastroCliente = Util.getServidor(getApplicationContext()) + "json/exportar/cad_cliente_android.php";

        Map<String, String> prm = new HashMap<String, String>();

        prm.put("cli_nome", txt_cli_nome.getText().toString().trim());
        prm.put("cli_fantasia", txt_cli_fantasia.getText().toString().trim());
        prm.put("cli_endereco", txt_cli_endereco.getText().toString().trim());
        prm.put("usu_codigo", new SqliteParametroDao(this).busca_parametros().getP_usu_codigo().toString());
        prm.put("cli_bairro", txt_cli_bairro.getText().toString().trim());
        prm.put("cli_cep", txt_cli_cep.getText().toString().trim());
        prm.put("cid_codigo", String.valueOf(0));
        prm.put("cli_contato", txt_cli_contato.getText().toString().trim());
        prm.put("cli_nascimento", Util.FormataDataAAAAMMDD(txt_cli_nascimento.getText().toString().trim()));
        prm.put("cli_cpfcnpj", txt_cli_cpfcnpj.getText().toString().trim());
        prm.put("cli_rginscricaoest", txt_cli_rginscricaoest.getText().toString().trim());
        prm.put("cli_email", txt_cli_email.getText().toString().trim());
        String chave = txt_cli_nome.getText().toString().trim() + txt_cli_email.getText().toString().trim();
        String cli_chave = Util.stringHexa(Util.gerarHash(chave, "MD5"));
        prm.put("cli_chave", cli_chave);

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                UrlCadastroCliente,
                prm,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            clientes_array = response.getJSONArray("clientes_array");

                            for (int i = 0; i < clientes_array.length(); i++) {

                                JSONObject cli = clientes_array.getJSONObject(i);
                                SqliteClienteBean clientesBean1 = new SqliteClienteBean();
                                SqliteClienteBean clientesBean2 = new SqliteClienteBean();
                                SqliteClienteDao clienteDao = new SqliteClienteDao(getApplicationContext());

                                // obtendo dados do servidor
                                C_CODIGO_CLIENTE = cli.getString(clientesBean1.C_CODIGO_CLIENTE);
                                C_NOME_DO_CLIENTE = cli.getString(clientesBean1.C_NOME_DO_CLIENTE);
                                C_NOME_FANTASIA = cli.getString(clientesBean1.C_NOME_FANTASIA);
                                C_ENDERECO_CLIENTE = cli.getString(clientesBean1.C_ENDERECO_CLIENTE);
                                C_BAIRRO_CLIENTE = cli.getString(clientesBean1.C_BAIRRO_CLIENTE);
                                C_CEP_CLIENTE = cli.getString(clientesBean1.C_CEP_CLIENTE);
                                C_CIDADE_CLIENTE = cli.getString(clientesBean1.C_CIDADE_CLIENTE);
                                C_CONTATO_CLIENTE = cli.getString(clientesBean1.C_CONTATO_CLIENTE);
                                C_DATA_NASCIMENTO = cli.getString(clientesBean1.C_DATA_NASCIMENTO);
                                C_CNPJCPF = cli.getString(clientesBean1.C_CNPJCPF);
                                C_RGINSCRICAO_ESTADUAL = cli.getString(clientesBean1.C_RGINSCRICAO_ESTADUAL);
                                C_EMAIL_CLIENTE = cli.getString(clientesBean1.C_EMAIL_CLIENTE);
                                C_ENVIADO = "S";
                                C_CHAVE_DO_CLIENTE = cli.getString(clientesBean1.C_CHAVE_DO_CLIENTE);

                                clientesBean1.setCli_codigo(Integer.parseInt(C_CODIGO_CLIENTE));
                                clientesBean1.setCli_nome(C_NOME_DO_CLIENTE);
                                clientesBean1.setCli_fantasia(C_NOME_FANTASIA);
                                clientesBean1.setCli_endereco(C_ENDERECO_CLIENTE);
                                clientesBean1.setCli_bairro(C_BAIRRO_CLIENTE);
                                clientesBean1.setCli_cep(C_CEP_CLIENTE);
                                clientesBean1.setCid_nome(C_CIDADE_CLIENTE);
                                clientesBean1.setCli_contato(C_CONTATO_CLIENTE);
                                clientesBean1.setCli_nascimento(C_DATA_NASCIMENTO);
                                clientesBean1.setCli_cpfcnpj(C_CNPJCPF);
                                clientesBean1.setCli_rginscricaoest(C_RGINSCRICAO_ESTADUAL);
                                clientesBean1.setCli_email(C_EMAIL_CLIENTE);
                                clientesBean1.setCli_enviado(C_ENVIADO);
                                clientesBean1.setCli_chave(C_CHAVE_DO_CLIENTE);

                                clienteDao.gravar_cliente(clientesBean1);

                                finish();;

                            }


                        } catch (JSONException e) {
                            Util.log("Erro JSONException ::: " + e.getMessage());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Util.log("Erro VolleyError ::: " + error.getMessage());
                    }
                });
        request.setTag(TAG_REQUEST);
        rq.add(request);


    }


    public void alteraou_ou_grava_cliente_no_sqlite() {

        declaraObjetos();
        Util.msg_toast_personal(this, "gravando no sqlite", Util.ALERTA);

        SqliteClienteBean cliBean = new SqliteClienteBean();
        SqliteClienteDao cliDao = new SqliteClienteDao(this);

        cliBean.setCli_nome(txt_cli_nome.getText().toString().trim());
        cliBean.setCli_fantasia(txt_cli_fantasia.getText().toString().trim());
        cliBean.setCli_endereco(txt_cli_endereco.getText().toString().trim());
        cliBean.setCli_bairro(txt_cli_bairro.getText().toString().trim());
        cliBean.setCli_cep(txt_cli_cep.getText().toString().trim());
        cliBean.setCli_contato(txt_cli_contato.getText().toString().trim());
        cliBean.setCli_nascimento(Util.FormataDataAAAAMMDD(txt_cli_nascimento.getText().toString().trim()));
        cliBean.setCli_cpfcnpj(txt_cli_cpfcnpj.getText().toString().trim());
        cliBean.setCli_rginscricaoest(txt_cli_rginscricaoest.getText().toString().trim());
        cliBean.setCli_email(txt_cli_email.getText().toString().trim());
        cliBean.setCli_enviado("N");

        if (FLAG_ALTERAR) {

            cliBean.setCid_nome(txt_cid_nome.getText().toString().trim());
            cliBean.setCli_chave(CLI_CODIGO.toString());
            cliBean.setCli_codigo(CLI_CODIGO);
            cliDao.atualiza_cliente(cliBean);

        } else {

            Random gerador = new Random();
            Integer cli_codigo = gerador.nextInt(500000);
            cliBean.setCid_nome("*");
            cliBean.setCli_codigo(cli_codigo);
            cliBean.setCli_chave(cli_codigo.toString());
            cliDao.gravar_cliente(cliBean);
        }


    }


    public boolean validou_campos() {

        declaraObjetos();

        if (txt_cli_nome.getText().toString().length() <= 2) {
            txt_cli_nome.setError("nome do cliente incorreto");
            txt_cli_nome.requestFocus();
            return false;
        }

        if (txt_cli_fantasia.getText().toString().length() <= 2) {
            txt_cli_fantasia.setError("fantasia do cliente incorreto");
            txt_cli_fantasia.requestFocus();
            return false;
        }


        if (txt_cli_endereco.getText().toString().length() <= 2) {
            txt_cli_endereco.setError("endereco do cliente incorreto");
            txt_cli_endereco.requestFocus();
            return false;
        }


        if (txt_cli_bairro.getText().toString().length() <= 2) {
            txt_cli_bairro.setError("bairro do cliente incorreto");
            txt_cli_bairro.requestFocus();
            return false;
        }


        if (txt_cli_cep.getText().toString().length() <= 2) {
            txt_cli_cep.setError("cep do cliente incorreto");
            txt_cli_cep.requestFocus();
            return false;
        }


        if (txt_cli_contato.getText().toString().length() <= 2) {
            txt_cli_contato.setError("cotnato do cliente incorreto");
            txt_cli_contato.requestFocus();
            return false;
        }


        if (txt_cli_contato.getText().toString().length() <= 2) {
            txt_cli_contato.setError("cotnato do cliente incorreto");
            txt_cli_contato.requestFocus();
            return false;
        }


        if (txt_cli_rginscricaoest.getText().toString().length() <= 2) {
            txt_cli_rginscricaoest.setError("cotnato do cliente incorreto");
            txt_cli_rginscricaoest.requestFocus();
            return false;
        }


        if (!Util.validaEmail(txt_cli_email.getText().toString())) {
            txt_cli_email.setError("cotnato do cliente incorreto");
            txt_cli_email.requestFocus();
            return false;
        }


        // VALIDACAO DO CPF E CNPJ

        int TamanhoCPFCNPJ = txt_cli_cpfcnpj.getText().toString().length();
        String cli_cpfcnpj = txt_cli_cpfcnpj.getText().toString().trim();

        if (TamanhoCPFCNPJ == 12 || TamanhoCPFCNPJ == 13 || TamanhoCPFCNPJ < 11) {
            txt_cli_cpfcnpj.setError("VALOR INVALIDO");
            txt_cli_cpfcnpj.requestFocus();
            return false;
        }

        if (TamanhoCPFCNPJ == 11) {
            if (!Util.validaCPF(cli_cpfcnpj)) {
                txt_cli_cpfcnpj.setError("CPF INVALIDO");
                txt_cli_cpfcnpj.requestFocus();
                return false;
            }
        }

        if (TamanhoCPFCNPJ == 14) {
            if (!Util.validaCNPJ(cli_cpfcnpj)) {
                txt_cli_cpfcnpj.setError("CNPJ INVALIDO");
                txt_cli_cpfcnpj.requestFocus();
                return false;
            }
        }


        return true;

    }


    public void declaraObjetos() {
        btn_administra_cliente = (Button) findViewById(R.id.btn_administra_cliente);

        txt_cli_codigo = (EditText) findViewById(R.id.txt_cli_codigo);

        txt_cli_nome = (EditText) findViewById(R.id.txt_cli_nome);
        txt_cli_fantasia = (EditText) findViewById(R.id.txt_cli_fantasia);

        txt_cli_endereco = (EditText) findViewById(R.id.txt_cli_endereco);
        txt_cli_bairro = (EditText) findViewById(R.id.txt_cli_bairro);
        txt_cli_cep = (EditText) findViewById(R.id.txt_cli_cep);

        txt_cid_nome = (EditText) findViewById(R.id.txt_cid_nome);
        txt_cli_contato = (EditText) findViewById(R.id.txt_cli_contato);
        txt_cli_nascimento = (EditText) findViewById(R.id.txt_cli_nascimento);

        txt_cli_cpfcnpj = (EditText) findViewById(R.id.txt_cli_cpfcnpj);
        txt_cli_rginscricaoest = (EditText) findViewById(R.id.txt_cli_rginscricaoest);
        txt_cli_email = (EditText) findViewById(R.id.txt_cli_email);

        txt_cli_chave = (EditText) findViewById(R.id.txt_cli_chave);

    }


}
