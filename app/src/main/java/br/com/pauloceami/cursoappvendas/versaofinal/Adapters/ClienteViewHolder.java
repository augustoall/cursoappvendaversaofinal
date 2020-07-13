package br.com.pauloceami.cursoappvendas.versaofinal.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.pauloceami.cursoappvendas.versaofinal.R;


public class ClienteViewHolder extends RecyclerView.ViewHolder {

    private Context ctx;
    public TextView txv_clicodigo;
    public TextView txv_cli_nome;
    public TextView txv_cli_fantasia;
    public TextView txv_cli_bairro;

    public ClienteViewHolder(Context ctx,View itemView) {
        super(itemView);

        this.ctx = ctx;
        txv_clicodigo = (TextView)itemView.findViewById(R.id.txv_clicodigo);
        txv_cli_nome = (TextView)itemView.findViewById(R.id.txv_cli_nome);
        txv_cli_fantasia = (TextView)itemView.findViewById(R.id.txv_cli_fantasia);
        txv_cli_bairro = (TextView)itemView.findViewById(R.id.txv_cli_bairro);


    }
}
