package br.com.pauloceami.cursoappvendas.versaofinal.Json;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroDao;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

public class ExecutaImports extends Activity {

    private SqliteParametroBean parBean;
    private SqliteParametroDao parDao;
    private RequestQueue rq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.executa_imports);

        parBean = new SqliteParametroBean();
        parDao = new SqliteParametroDao(this);
        parBean = parDao.busca_parametros();
        rq = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();


    }


    public void importadados(View v) throws Exception {

        String key = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Button btn_importardados = (Button) findViewById(R.id.btn_importardados);
        btn_importardados.setVisibility(View.GONE);

        String url_cliente = Util.getServidor(getApplicationContext()) + "json/exportar/exporta_cliente.php";
        ImportaClientes.importar_clientes_do_servidor(url_cliente, rq, getApplicationContext(), parBean.getP_importar_todos_clientes(), parBean.getP_usu_codigo());

        String url_produto = Util.getServidor(getApplicationContext()) + "json/exportar/exporta_produto.php";
        ImportaProdutos.importar_produtos_do_servidor(key,url_produto,rq,getApplicationContext());


    }


    @Override
    public void onStop() {
        super.onStop();
        rq.cancelAll("tag");
    }


}
