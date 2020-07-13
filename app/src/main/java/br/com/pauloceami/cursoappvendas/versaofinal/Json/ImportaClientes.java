package br.com.pauloceami.cursoappvendas.versaofinal.Json;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.CustomJsonObjectRequest;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

/**
 * Created by JAVA on 26/08/2015.
 */
public class ImportaClientes {

    private static final String TAG_SUCESSO = "sucesso";
    private static final String TAG_MENSAGEM = "mensagem";
    private static final String TAG_REQUEST = "tag";
    private static JSONArray clientes_array = null;

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


    public static void importar_clientes_do_servidor(String url, RequestQueue rq, final Context ctx, String p_importar_todos_clientes, Integer usu_codigo) {
        Map<String, String> prm = new HashMap<String, String>();
        prm.put("exportar_todos", p_importar_todos_clientes);
        prm.put("usu_codigo", usu_codigo.toString());
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                url,
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
                                SqliteClienteDao clienteDao = new SqliteClienteDao(ctx);
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

                                clientesBean2 = clienteDao.buscar_cliente_pelo_codigo(C_CODIGO_CLIENTE);

                                if (clientesBean2 == null) {
                                    clienteDao.gravar_cliente(clientesBean1);
                                    Util.log("cliente " + clientesBean1.getCli_nome() +" "+ i + " foi adicionado ");
                                }
                                if (clientesBean2 != null) {
                                    clienteDao.atualiza_cliente(clientesBean1);
                                    Util.log("cliente " + clientesBean1.getCli_nome() +" "+ i + " foi alterado ");
                                }
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
}
