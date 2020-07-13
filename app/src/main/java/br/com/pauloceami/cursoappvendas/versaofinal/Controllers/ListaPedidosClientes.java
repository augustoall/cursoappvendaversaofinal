package br.com.pauloceami.cursoappvendas.versaofinal.Controllers;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.pauloceami.cursoappvendas.versaofinal.Adapters.ListaPedidosClienteAdapter;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaCBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.Sqlite_VENDADAO;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

public class ListaPedidosClientes extends AppCompatActivity {

    private Intent CLI_CODIGO_INTENT;
    private Integer CLI_CODIGO;

    private LayoutInflater inflater;
    private Builder alerta;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_pedidos_clientes);
        CLI_CODIGO_INTENT = getIntent();
        CLI_CODIGO = CLI_CODIGO_INTENT.getIntExtra("CLI_CODIGO", 0);
        mostra_pedidos(CLI_CODIGO);
    }


    private void mostra_pedidos(final Integer cli_codigo) {

        SqliteClienteBean cliente = new SqliteClienteDao(getApplicationContext()).buscar_cliente_pelo_codigo(cli_codigo.toString());

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.lista_pedidos_clientes, null);

        alerta = new Builder(this);
        alerta.setTitle("Pedido(s) de :" + cliente.getCli_fantasia());
        alerta.setCancelable(false);

        alerta.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alerta.setView(view);

        final ListView listView_pedidos = (ListView)view.findViewById(R.id.lista_pedidos_cliente);
        Sqlite_VENDADAO vDao = new Sqlite_VENDADAO(getApplicationContext());
        listView_pedidos.setAdapter(new ListaPedidosClienteAdapter(getApplicationContext(),vDao.lista_pedidos_do_cliente(cli_codigo)));


        listView_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listview, View view, int position, long id) {

                SqliteVendaCBean vendaCBean = (SqliteVendaCBean) listview.getAdapter().getItem(position);

                Util.msg_toast_personal(getApplicationContext(), vendaCBean.getVendac_chave(), Util.ALERTA);

                Intent BaixaContasReceber = new Intent(getBaseContext(), br.com.pauloceami.cursoappvendas.versaofinal.Controllers.BaixaContasReceber.class);
                BaixaContasReceber.putExtra("CLI_CODIGO",cli_codigo);
                BaixaContasReceber.putExtra("VENDAC_CHAVE",vendaCBean.getVendac_chave());
                startActivity(BaixaContasReceber);



            }
        });


        alertDialog = alerta.create();
        alertDialog.show();

    }


}
