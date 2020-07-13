package br.com.pauloceami.cursoappvendas.versaofinal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaCBean;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;


public class ListaPedidosClienteAdapter extends BaseAdapter {


    private Context ctx;
    private List<SqliteVendaCBean> lista_de_pedidos ;

    public ListaPedidosClienteAdapter(Context ctx, List<SqliteVendaCBean> lista_de_pedidos) {
        this.ctx = ctx;
        this.lista_de_pedidos = lista_de_pedidos;
    }

    @Override
    public int getCount() {
        return lista_de_pedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return lista_de_pedidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        SqliteVendaCBean venda = (SqliteVendaCBean)getItem(position);

        LayoutInflater layout = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layout.inflate(R.layout.lista_pedidos_cliente_item,null);
        TextView numpedido = (TextView)v.findViewById(R.id.txvVendacChave);
        TextView Data = (TextView)v.findViewById(R.id.txvDataVenda);
        TextView Valor = (TextView)v.findViewById(R.id.txvValor);

        numpedido.setText(venda.getVendac_chave());
        Data.setText(Util.FormataDataDDMMAAAA(venda.getVendac_datahoravenda()));
        Valor.setText(venda.getVendac_valor().setScale(2, BigDecimal.ROUND_UP).toString() + " * " + venda.getVendac_formapgto());


        return v;
    }
}
