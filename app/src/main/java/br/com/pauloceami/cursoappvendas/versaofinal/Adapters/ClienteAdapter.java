package br.com.pauloceami.cursoappvendas.versaofinal.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.R;


public class ClienteAdapter extends RecyclerView.Adapter<ClienteViewHolder> {

    private Context ctx;
    private ArrayList<SqliteClienteBean> itens;


    public ClienteAdapter(Context ctx, ArrayList<SqliteClienteBean> itens) {
        this.ctx = ctx;
        this.itens = itens;
    }


    @Override
    public ClienteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.recycler_view_cliente_item,viewGroup,false);
        ClienteViewHolder viewHolder =  new ClienteViewHolder(ctx,view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClienteViewHolder clienteViewHolder, int posicao) {
        SqliteClienteBean cliente = itens.get(posicao);
        clienteViewHolder.txv_clicodigo.setText(cliente.getCli_codigo().toString());
        clienteViewHolder.txv_cli_nome.setText(cliente.getCli_nome());
        clienteViewHolder.txv_cli_fantasia.setText(cliente.getCli_fantasia());
        clienteViewHolder.txv_cli_bairro.setText(cliente.getCli_bairro());

    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}
