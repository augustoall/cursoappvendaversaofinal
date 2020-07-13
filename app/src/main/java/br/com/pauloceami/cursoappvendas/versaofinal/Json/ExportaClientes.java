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
import java.util.List;
import java.util.Map;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.Sqlite_VENDADAO;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.CustomJsonObjectRequest;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

/**
 * Created by JAVA on 21/09/2015.
 */
public class ExportaClientes {

    private static final String TAG_REQUEST = "tag";
    private static final String TAG_SUCESSO = "sucesso";
    private static final String TAG_CHAVE_VENDA = "vendac_chave";
    private static JSONArray clientes_array = null;


    public static void exporta_clientes_e_atualiza_cli_codigo_na_venda(String url, RequestQueue rq, final Context ctx) {


        List<SqliteClienteBean> clientes = new SqliteClienteDao(ctx).buscar_clientes_nao_enviados();

        for (SqliteClienteBean cli : clientes) {

            Map<String, String> prm = new HashMap<String, String>();

            prm.put("cli_codigo", cli.getCli_codigo().toString());
            prm.put("cli_nome", cli.getCli_nome());
            prm.put("cli_fantasia", cli.getCli_fantasia());
            prm.put("cli_endereco", cli.getCli_endereco());
            prm.put("usu_codigo", new SqliteParametroDao(ctx).busca_parametros().getP_usu_codigo().toString());
            prm.put("cli_bairro", cli.getCli_bairro());
            prm.put("cli_cep", cli.getCli_cep());
            prm.put("cid_codigo", String.valueOf(0));
            prm.put("cli_contato", cli.getCli_contato());
            prm.put("cli_nascimento", cli.getCli_nascimento());
            prm.put("cli_cpfcnpj", cli.getCli_cpfcnpj());
            prm.put("cli_rginscricaoest", cli.getCli_rginscricaoest());
            prm.put("cli_email", cli.getCli_email());
            prm.put("cli_chave", cli.getCli_chave());

            CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                    Request.Method.POST,
                    url,
                    prm,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            try {

                                Integer sucesso = (Integer)response.get("sucesso");

                                if(sucesso == 1){

                                    String codigo_cliente = (String)response.get("cli_codigo");
                                    String chave = (String)response.get("cli_chave");
                                    SqliteClienteBean clienteBean = new SqliteClienteBean();
                                    SqliteClienteDao clienteDao = new SqliteClienteDao(ctx);
                                    clienteBean.setCli_codigo(Integer.parseInt(codigo_cliente));
                                    clienteBean.setCli_chave(chave);
                                    clienteDao.atualiza_cli_codigo_pela_CHAVE(clienteBean);

                                    //atualiza o campo vendac_cli_codigo na venda quando o cliente e cadastrado off-line
                                    new Sqlite_VENDADAO(ctx).atualiza_cli_codigo_cliente_offline(
                                            Integer.parseInt(codigo_cliente),
                                            Integer.parseInt(chave));

                                }


                            }catch (JSONException e){
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


}
