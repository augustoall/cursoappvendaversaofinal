package br.com.pauloceami.cursoappvendas.versaofinal.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroDao;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.CustomJsonObjectRequest;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;


public class Principal extends AppCompatActivity {

//    private static final String URL_BASE = "http://cursoappvendas.tempsite.ws/";
    private static final String URL_BASE = "https://platinumweb.com.br//";

    private static final String URL_REGISTRO = URL_BASE + "json/registrar/registrar_usuario.php";

    private static final String CODIGO_USUARIO = "usu_codigo";
    private static final String DESCONTO_USUARIO = "usu_desconto";
    private static final String USUARIO = "usu_usuario";
    private static final String SENHA = "usu_senha";
    private JSONArray usuario_array = null;
    private static final String TAG_REQUEST = "tag";
    private Map<String, String> params;
    private Button btn_registrar;
    private EditText txt_usuario;
    private EditText txt_senha;
    private RequestQueue rq;
    private static final String TAG_SUCESSO = "sucesso";
    private static final String TAG_MENSAGEM = "mensagem";
    private static final String TAG_USUARIO_JSON_ARRAY = "usuario_array";
    private SqliteParametroBean parBean;
    private SqliteParametroDao parDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        declaraObjetos();

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validar_campos()) {
                    return;
                }

                Log.i("script", "passou na validacao");


                parBean = parDao.busca_parametros();

                if (parBean != null) {
                    validar_usuario_e_entrar_no_sistema();
                } else {

                    if (Util.checarConexaoCelular(getBaseContext())) {
                        registrar_usuario_web();
                    } else {
                        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(i);
                        Toast.makeText(getBaseContext(), "Sem conexao com internet", Toast.LENGTH_LONG).show();
                    }
                }
            }

        });

    }

    private void entrar_na_tela_dashboard() {
        Intent Dashboard = new Intent(getBaseContext(), br.com.pauloceami.cursoappvendas.versaofinal.Principal.Dashboard.class);
        startActivity(Dashboard);
        finish();
    }

    private void validar_usuario_e_entrar_no_sistema() {

        declaraObjetos();
        String usuario_digitado = txt_usuario.getText().toString();
        String senha_digitada = txt_senha.getText().toString();

        parBean = parDao.busca_parametros();
        if (usuario_digitado.trim().equals(parBean.getP_usuario()) && senha_digitada.trim().equals(parBean.getP_senha())) {

            entrar_na_tela_dashboard();

            Util.msg_toast_personal(this, "VOCE ENTROU NO SISTEMA", Util.SUCESSO);

        } else {

            Util.msg_toast_personal(this, "A SENHA ESTA INCORRETA", Util.ERRO);

        }

    }

    private void registrar_usuario_web() {
        params = new HashMap<String, String>();
        params.put("usu_usuario", txt_usuario.getText().toString());
        params.put("usu_senha", txt_senha.getText().toString());
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                URL_REGISTRO,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int sucesso = (Integer) response.get(TAG_SUCESSO);
                            String mensagem = (String) response.get(TAG_MENSAGEM);

                            switch (sucesso) {
                                case 1:
                                    usuario_array = response.getJSONArray(TAG_USUARIO_JSON_ARRAY);
                                    for (int i = 0; i < usuario_array.length(); i++) {
                                        JSONObject usuarioObj = usuario_array.getJSONObject(i);

                                        parBean = new SqliteParametroBean();
                                        parDao = new SqliteParametroDao(Principal.this);
                                        parBean.setP_usu_codigo(usuarioObj.getInt(CODIGO_USUARIO));
                                        parBean.setP_desconto_do_vendedor(usuarioObj.getInt(DESCONTO_USUARIO));
                                        parBean.setP_usuario(usuarioObj.getString(USUARIO));
                                        parBean.setP_senha(usuarioObj.getString(SENHA));
                                        parBean.setP_url_base(URL_BASE);
                                        //parBean.setP_end_ip_local(IP_LOCAL);
                                        parBean.setP_importar_todos_clientes("S");
                                        parBean.setP_trabalhar_com_estoque_negativo("N");
                                        // parBean.setP_qual_endereco_ip("L");
                                        parDao.gravar_parametro(parBean);

                                    }

                                    entrar_na_tela_dashboard();
                                    Toast.makeText(getBaseContext(), mensagem, Toast.LENGTH_LONG).show();
                                    break;
                                case 2:
                                    Toast.makeText(getBaseContext(), mensagem, Toast.LENGTH_LONG).show();
                                    break;
                            }

                        } catch (JSONException e) {
                            Toast.makeText(Principal.this, "JSONException : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Principal.this, "onErrorResponse : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        request.setTag(TAG_REQUEST);
        rq.add(request);
    }


    private void declaraObjetos() {
        txt_usuario = (EditText) findViewById(R.id.txt_usuario);
        txt_senha = (EditText) findViewById(R.id.txt_senha);
        btn_registrar = (Button) findViewById(R.id.btn_registrar);
        rq = Volley.newRequestQueue(Principal.this);
        parBean = new SqliteParametroBean();
        parDao = new SqliteParametroDao(getBaseContext());
    }

    private boolean validar_campos() {
        declaraObjetos();
        if (txt_usuario.getText().toString().length() <= 2) {
            txt_usuario.setError("seu nome de usuario esta incompleto");
            txt_usuario.requestFocus();
            return false;
        } else if (txt_senha.getText().toString().trim().length() <= 2) {
            txt_senha.setError("sua senha precisa ter pelo menos 2 caracteres");
            txt_senha.requestFocus();
            return false;

        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        rq.cancelAll(TAG_REQUEST);
    }
}