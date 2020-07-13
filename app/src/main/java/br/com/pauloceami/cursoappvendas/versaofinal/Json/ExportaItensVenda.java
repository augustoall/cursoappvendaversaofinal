package br.com.pauloceami.cursoappvendas.versaofinal.Json;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaDBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.Sqlite_VENDADAO;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.CustomJsonObjectRequest;


public class ExportaItensVenda {

    private static final String TAG_REQUEST = "tag";

    public static void exportar_vendaD(String url, RequestQueue rq, final Context ctx) {


        List<SqliteVendaDBean> liste_de_itens = new Sqlite_VENDADAO(ctx).buscar_itens_das_vendas_nao_enviadas();

        for (SqliteVendaDBean item : liste_de_itens  ){

            Map<String, String> vendad_item = new HashMap<String, String>();
            vendad_item.put("vendac_chave", item.getVendac_chave() );
            vendad_item.put("vendad_nro_item", item.getVendad_nro_item().toString() );
            vendad_item.put("vendad_ean", item.getVendad_ean());
            vendad_item.put("vendad_prd_codigo", item.getVendad_prd_codigo().toString() );
            vendad_item.put("vendad_prd_descricao", item.getVendad_prd_descricao() );
            vendad_item.put("vendad_quantidade", item.getVendad_quantidade().toString() );
            vendad_item.put("vendad_preco_venda", item.getVendad_preco_venda().toString() );
            vendad_item.put("vendad_total", item.getVendad_total().toString() );


            CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, vendad_item, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            request.setTag(TAG_REQUEST);
            rq.add(request);


        }


    }


}
