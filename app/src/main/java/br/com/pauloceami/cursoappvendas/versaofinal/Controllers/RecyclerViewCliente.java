package br.com.pauloceami.cursoappvendas.versaofinal.Controllers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.Adapters.ClienteAdapter;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteDao;
import br.com.pauloceami.cursoappvendas.versaofinal.R;

public class RecyclerViewCliente extends Activity {

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_cliente);

        SqliteClienteBean cliBean = new SqliteClienteBean();
        SqliteClienteDao cliDao = new SqliteClienteDao(this);

        List<SqliteClienteBean> lista = cliDao.buscar_clientes_nao_enviados();
        RecyclerView recyclerviewcliente = (RecyclerView) findViewById(R.id.recyclerviewcliente);
        recyclerviewcliente.setHasFixedSize(true);
        recyclerviewcliente.setAdapter(new ClienteAdapter(this, (ArrayList<SqliteClienteBean>) lista));
        recyclerviewcliente.setItemAnimator(new DefaultItemAnimator());
        recyclerviewcliente.setLayoutManager(new LinearLayoutManager(RecyclerViewCliente.this));


    }


}
