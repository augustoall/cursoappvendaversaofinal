package br.com.pauloceami.cursoappvendas.versaofinal.Json;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaCBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.Sqlite_VENDADAO;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.CustomJsonObjectRequest;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

public class ExportaVenda {

    private static final String TAG_REQUEST = "tag";
    private static final String TAG_SUCESSO = "sucesso";
    private static final String TAG_CHAVE_VENDA = "vendac_chave";

    public static List<String> exportar_vendaC(String url, RequestQueue rq, final Context ctx) {

        List<SqliteVendaCBean> lista_vendac = new Sqlite_VENDADAO(ctx).buscar_vendas_nao_enviadas();

        for (SqliteVendaCBean vendac : lista_vendac) {

            Map<String, String> vendac_item = new HashMap<String, String>();
            vendac_item.put("vendac_chave", vendac.getVendac_chave());
            vendac_item.put("vendac_datahoravenda", vendac.getVendac_datahoravenda());
            vendac_item.put("vendac_previsaoentrega", vendac.getVendac_previsaoentrega());
            vendac_item.put("vendac_cli_codigo", vendac.getVendac_cli_codigo().toString());
            vendac_item.put("vendac_cli_nome", vendac.getVendac_cli_nome());
            vendac_item.put("vendac_usu_codigo", vendac.getVendac_usu_codigo().toString());
            vendac_item.put("vendac_usu_nome", vendac.getVendac_usu_nome());
            vendac_item.put("vendac_formapgto", vendac.getVendac_formapgto());
            vendac_item.put("vendac_valor", vendac.getVendac_valor().toString());
            vendac_item.put("vendac_desconto", vendac.getVendac_desconto().toString());
            vendac_item.put("vendac_pesototal", vendac.getVendac_pesototal().toString());
            vendac_item.put("vendac_latitude", String.valueOf(vendac.getVendac_latitude()));
            vendac_item.put("vendac_longitude", String.valueOf(vendac.getVendac_longitude()));

            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, vendac_item, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        int suceceso = (Integer)response.get(TAG_SUCESSO);

                        if(suceceso == 1){

                            String chave = (String)response.get(TAG_CHAVE_VENDA);
                            new Sqlite_VENDADAO(ctx).atualiza_vendac_enviada(chave);

                        }

                        if(suceceso == 2){
                            Util.log("Erro ao tentar gravar venda");
                        }




                    } catch (JSONException e) {
                        Util.log("Erro JSONException ::: " + e.getMessage());
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.log("Erro VolleyError ::: " + error.getMessage());
                }
            });
            request.setTag(TAG_REQUEST);
            rq.add(request);
        }
        return null;
    }
}
