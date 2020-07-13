package br.com.pauloceami.cursoappvendas.versaofinal.Json;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteProdutoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteProdutoDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.CustomJsonObjectRequest;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

/**
 * Created by JAVA on 28/08/2015.
 */
public class ImportaProdutos {

    private static final String TAG_SUCESSO = "sucesso";
    private static final String TAG_MENSAGEM = "mensagem";
    private static final String TAG_REQUEST = "tag";
    private static JSONArray produtos_array = null;
    public static String P_CODIGO_PRODUTO;
    public static String P_CODIGO_BARRAS;
    public static String P_DESCRICAO_PRODUTO;
    public static String P_UNIDADE_MEDIDA;
    public static String P_CUSTO_PRODUTO;
    public static String P_QUANTIDADE_PRODUTO;
    public static String P_PRECO_PRODUTO;
    public static String P_CATEGORIA_PRODUTO;

    public static void importar_produtos_do_servidor(String key,String url, RequestQueue rq, final Context ctx) {
        Map<String, String> prm = new HashMap<String, String>();
        prm.put("key",key);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                url,
                prm,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            produtos_array = response.getJSONArray("produtos_array");
                            for (int i = 0; i < produtos_array.length(); i++) {
                                JSONObject produto = produtos_array.getJSONObject(i);
                                SqliteProdutoBean prodBean1 = new SqliteProdutoBean();
                                SqliteProdutoBean prodBean2 = new SqliteProdutoBean();
                                SqliteProdutoDao prodDao = new SqliteProdutoDao(ctx);

                                // obtendo dados do servidor
                                P_CODIGO_PRODUTO = produto.getString(prodBean1.P_CODIGO_PRODUTO);
                                P_CODIGO_BARRAS = produto.getString(prodBean1.P_CODIGO_BARRAS);
                                P_DESCRICAO_PRODUTO = produto.getString(prodBean1.P_DESCRICAO_PRODUTO);
                                P_UNIDADE_MEDIDA = produto.getString(prodBean1.P_UNIDADE_MEDIDA);
                                P_CUSTO_PRODUTO = produto.getString(prodBean1.P_CUSTO_PRODUTO);
                                P_QUANTIDADE_PRODUTO = produto.getString(prodBean1.P_QUANTIDADE_PRODUTO);
                                P_PRECO_PRODUTO = produto.getString(prodBean1.P_PRECO_PRODUTO);
                                P_CATEGORIA_PRODUTO = produto.getString(prodBean1.P_CATEGORIA_PRODUTO);

                                prodBean1.setPrd_codigo(Integer.parseInt(P_CODIGO_PRODUTO));
                                prodBean1.setPrd_EAN13(P_CODIGO_BARRAS);
                                prodBean1.setPrd_descricao(P_DESCRICAO_PRODUTO);
                                prodBean1.setPrd_unmedida(P_UNIDADE_MEDIDA);
                                prodBean1.setPrd_custo(new BigDecimal(P_CUSTO_PRODUTO));
                                prodBean1.setPrd_quantidade(new BigDecimal(P_QUANTIDADE_PRODUTO));
                                prodBean1.setPrd_preco(new BigDecimal(P_PRECO_PRODUTO));
                                prodBean1.setPrd_categoria(P_CATEGORIA_PRODUTO);

                                prodBean2 = prodDao.buscar_produto_pelo_codigo(P_CODIGO_PRODUTO);

                                if (prodBean2 == null) {
                                    prodDao.gravar_produto(prodBean1);
                                    Util.log("produto " + prodBean1.getPrd_descricao() + " " + i + " foi adicionado ");
                                }
                                if (prodBean2 != null) {
                                    prodDao.atualiza_produto(prodBean1);
                                    Util.log("produtos " + prodBean1.getPrd_descricao() + " " + i + " foi alterado ");
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
