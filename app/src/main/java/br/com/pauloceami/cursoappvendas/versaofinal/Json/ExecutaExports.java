package br.com.pauloceami.cursoappvendas.versaofinal.Json;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;

import java.io.IOException;
import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.API.ApiService;
import br.com.pauloceami.cursoappvendas.versaofinal.API.RetrofitClient;
import br.com.pauloceami.cursoappvendas.versaofinal.API.RetrofitConfPagamentoRetorno;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteProdutoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExecutaExports extends AppCompatActivity {

    private RequestQueue rq;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.executa_exports);
        rq = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();

        apiService = RetrofitClient.getRetrofitClient().create(ApiService.class);


    }


    private void postPagamento() {

        List<SqliteConfPagamentoBean> listPagamentos = new SqliteConfPagamentoDao(this)
                .getAllPagamentos();

        for (SqliteConfPagamentoBean conf : listPagamentos) {

            Call<RetrofitConfPagamentoRetorno> request = apiService.postConfPagamento(
                    conf.getConf_sementrada_comentrada(),
                    conf.getConf_tipo_pagamento(),
                    conf.getConf_recebeucom_din_chq_car(),
                    conf.getConf_valor_recebido().toString(),
                    conf.getConf_parcelas().toString(),
                    conf.getVendac_chave(),
                    conf.getConf_vencimento_gerencianet(),
                    conf.getConf_valor_parcela_gerencianet().toString()

            );

            request.enqueue(new Callback<RetrofitConfPagamentoRetorno>() {
                @Override
                public void onResponse(Call<RetrofitConfPagamentoRetorno> call, Response<RetrofitConfPagamentoRetorno> response) {

                    if (!response.isSuccessful()) {
                        Log.i("script", String.valueOf(response.code()));
                    }


                    RetrofitConfPagamentoRetorno retorno = response.body();


                    Log.i("script", "chave enviada" + retorno.vendac_chave);


                    new SqliteConfPagamentoDao(getApplicationContext())
                            .excluir_CONFPAGAMENTO(retorno.vendac_chave);


                }

                @Override
                public void onFailure(Call<RetrofitConfPagamentoRetorno> call, Throwable t) {
                    // ocorre quando voce tem problemas de conexao
                    Log.i("script", "onFailure" + t.getMessage().toString() + "stacktrace" + t.getStackTrace());
                }
            });
        }

    }


    public void exportardados(View v) {

        Button btn_exportadados = (Button) findViewById(R.id.btn_exportadados);
        btn_exportadados.setVisibility(View.GONE);

        String url_cliente = Util.getServidor(getApplicationContext()) + "json/importar/cad_cliente_android_offline.php";


        ExportaClientes.exporta_clientes_e_atualiza_cli_codigo_na_venda(url_cliente, rq, this);

        String url_exportar_vendaC = Util.getServidor(getApplicationContext()) + "json/importar/exportar_vendac.php";
        String url_exportar_vendaD = Util.getServidor(getApplicationContext()) + "json/importar/exportar_vendad.php";

        ExportaVenda.exportar_vendaC(url_exportar_vendaC, rq, this);


        ExportaItensVenda.exportar_vendaD(url_exportar_vendaD, rq, this);


        postPagamento();

    }



}
