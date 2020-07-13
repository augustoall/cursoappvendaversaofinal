package br.com.pauloceami.cursoappvendas.versaofinal.Controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroDao;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

public class Parametro extends Activity {


    private EditText TxtCodigoVendedor;
    private EditText TxtDesconto;
    private EditText TxtUrlBase;
    private RadioGroup RgImportarTodosClientes;
    private CheckBox CkEstoqueNegativo;
    private SqliteParametroBean parBean;
    private SqliteParametroDao parDao;
    private AlertDialog alerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametro);

        declaraObjetos();

        inicializa_activity();


    }

    public void SalvarParametro(View v) {


        String importarcliente = null;
        String servidor = null;
        declaraObjetos();

        parBean.setP_usu_codigo(Integer.parseInt(TxtCodigoVendedor.getText().toString()));
        parBean.setP_desconto_do_vendedor(Integer.parseInt(TxtDesconto.getText().toString()));
        parBean.setP_url_base(TxtUrlBase.getText().toString());
        // parBean.setP_end_ip_local(TxtEndLocal.getText().toString());

        /*
        switch (RgServidor.getCheckedRadioButtonId()) {
            case R.id.RbEndRemoto:
                servidor = "R";
                break;
            case R.id.RbEndLocal:
                servidor = "L";
                break;
        }
        */
        // parBean.setP_qual_endereco_ip(servidor);


        switch (RgImportarTodosClientes.getCheckedRadioButtonId()) {
            case R.id.RbSim:
                importarcliente = "S";
                break;
            case R.id.RbNao:
                importarcliente = "N";
                break;
        }
        parBean.setP_importar_todos_clientes(importarcliente);


        if (CkEstoqueNegativo.isChecked()) {
            parBean.setP_trabalhar_com_estoque_negativo("S");
        }

        if (!CkEstoqueNegativo.isChecked()) {
            parBean.setP_trabalhar_com_estoque_negativo("N");
        }


        parDao.atualizaParametro(parBean);
        Util.msg_toast_personal(this, "Parametro atualizado com sucesso", Util.SUCESSO);
        finish();

    }

    public void resetar(View v) {
        chamar_alerta();
    }

    private void chamar_alerta() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atençao");
        builder.setMessage("Esta opçao vai apagar todas as configurações do parâmetro, sera necessário conectar na internet novamente para pegar os dados do usuario no servidor.");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                parDao = new SqliteParametroDao(getApplicationContext());
                parDao.reset_parametro();
                finish();

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });

        alerta = builder.create();
        alerta.show();
    }


    private void inicializa_activity() {

        parBean = parDao.busca_parametros();

        if (parBean != null) {

            TxtCodigoVendedor.setText(parBean.getP_usu_codigo().toString());
            TxtDesconto.setText(parBean.getP_desconto_do_vendedor().toString());
            TxtUrlBase.setText(parBean.getP_url_base());
            // TxtEndLocal.setText(parBean.getP_end_ip_local());

                /*
                            int opcaoServidor = RgServidor.getCheckedRadioButtonId();
                            if (parBean.getP_qual_endereco_ip().equals("L")) {
                                opcaoServidor = R.id.RbEndLocal;
                            }

                            if (parBean.getP_qual_endereco_ip().equals("R")) {
                                opcaoServidor = R.id.RbEndRemoto;
                            }
                            RgServidor.check(opcaoServidor);
                */

            int opcaoTodosClientes = RgImportarTodosClientes.getCheckedRadioButtonId();
            if (parBean.getP_importar_todos_clientes().equals("S")) {
                opcaoTodosClientes = R.id.RbSim;
            }
            if (parBean.getP_importar_todos_clientes().equals("N")) {
                opcaoTodosClientes = R.id.RbNao;
            }
            RgImportarTodosClientes.check(opcaoTodosClientes);

            if (parBean.getP_trabalhar_com_estoque_negativo().equals("S")) {
                CkEstoqueNegativo.setChecked(true);
            }

            if (parBean.getP_trabalhar_com_estoque_negativo().equals("N")) {
                CkEstoqueNegativo.setChecked(false);
            }


        }
    }


    private void declaraObjetos() {
        TxtCodigoVendedor = (EditText) findViewById(R.id.TxtCodigoVendedor);
        TxtDesconto = (EditText) findViewById(R.id.TxtDesconto);
        TxtUrlBase = (EditText) findViewById(R.id.TxtUrlBase);


        RgImportarTodosClientes = (RadioGroup) findViewById(R.id.RgImportarTodosClientes);
        CkEstoqueNegativo = (CheckBox) findViewById(R.id.CkEstoqueNegativo);
        parBean = new SqliteParametroBean();
        parDao = new SqliteParametroDao(this);

    }


}
