package br.com.pauloceami.cursoappvendas.versaofinal.Principal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import br.com.pauloceami.cursoappvendas.versaofinal.Controllers.Lista_clientes;
import br.com.pauloceami.cursoappvendas.versaofinal.Json.ExecutaImports;
import br.com.pauloceami.cursoappvendas.versaofinal.Json.VolleySingleton;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroDao;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;


public class Dashboard extends Activity {


    SqliteParametroDao parDao;
    SqliteParametroBean parBean;
    private RequestQueue rq;
    // Progress dialog
    private ProgressDialog pDialog;


    @BindView(R.id.img_3g)
    ImageView img_3g;

    @BindView(R.id.img_wifi)
    ImageView img_wifi;

    @BindView(R.id.txv_sem_conexao)
    TextView txv_sem_conexao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_constraint);
        parDao = new SqliteParametroDao(this);
        parBean = new SqliteParametroBean();
        parBean = parDao.busca_parametros();
        rq = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        ButterKnife.bind(this);

        updateNetwork();

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateNetwork();
    }

    public void updateNetwork() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                img_wifi.setVisibility(View.VISIBLE);
                img_3g.setVisibility(View.GONE);
                txv_sem_conexao.setVisibility(View.GONE);

            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                img_wifi.setVisibility(View.GONE);
                img_3g.setVisibility(View.VISIBLE);
                txv_sem_conexao.setVisibility(View.GONE);
            }
        } else {
            img_wifi.setVisibility(View.GONE);
            img_3g.setVisibility(View.GONE);
            txv_sem_conexao.setVisibility(View.VISIBLE);
        }
    }

    public void ExecParamentro(View v) {
        parBean = parDao.busca_parametros();
        if (parBean == null) {
            Util.msg_toast_personal(this, "É NECESSARIO CONFIGURAR OS PARAMETROS NOVAMENTE", Util.ALERTA);
            finish();
        } else {
            Intent Parametro = new Intent(getBaseContext(), br.com.pauloceami.cursoappvendas.versaofinal.Controllers.Parametro.class);
            startActivity(Parametro);
        }
    }

    public void lista_cliente(View v) {
        Intent RecyclerViewCliente = new Intent(getBaseContext(), br.com.pauloceami.cursoappvendas.versaofinal.Controllers.RecyclerViewCliente.class);
        startActivity(RecyclerViewCliente);
    }

    public void ALTERAR_CLIENTE(View v) {
        Intent Lista_clientes = new Intent(getBaseContext(), br.com.pauloceami.cursoappvendas.versaofinal.Controllers.Lista_clientes.class);
        Lista_clientes.putExtra("TELA_QUE_CHAMOU", "ALTERAR_CLIENTE");
        startActivity(Lista_clientes);
    }

    public void CONTAS_RECEBER(View v) {
        Intent Lista_clientes = new Intent(getBaseContext(), Lista_clientes.class);
        Lista_clientes.putExtra("TELA_QUE_CHAMOU", "CONTAS_RECEBER");
        startActivity(Lista_clientes);
    }


    public void VENDER_PRODUTOS(View v) {
        Intent Lista_clientes = new Intent(getBaseContext(), Lista_clientes.class);
        Lista_clientes.putExtra("TELA_QUE_CHAMOU", "VENDER_PRODUTOS");
        startActivity(Lista_clientes);
    }


    public void cadastrarcliente(View v) {
        Intent AdministraCliente = new Intent(getBaseContext(), br.com.pauloceami.cursoappvendas.versaofinal.Controllers.AdministraCliente.class);
        startActivity(AdministraCliente);
    }


    public void chamar_import(View v) {
        if (Util.checarConexaoCelular(getBaseContext())) {
            Intent ExecutaImports = new Intent(getBaseContext(), ExecutaImports.class);
            startActivity(ExecutaImports);
        } else {
            Util.msg_toast_personal(this, "É NECESSARIO CONEXÃO COM INTERNET PARA EXECUTAR ESTA AÇÃO", Util.ALERTA);
        }
    }

    public void chamar_export(View v) {
        if (Util.checarConexaoCelular(getBaseContext())) {
            Intent ExecutaExports = new Intent(getBaseContext(), br.com.pauloceami.cursoappvendas.versaofinal.Json.ExecutaExports.class);
            startActivity(ExecutaExports);
        } else {
            Util.msg_toast_personal(this, "É NECESSARIO CONEXÃO COM INTERNET PARA EXECUTAR ESTA AÇÃO", Util.ALERTA);
        }
    }
}
