package br.com.pauloceami.cursoappvendas.versaofinal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaD_TempBean;
import br.com.pauloceami.cursoappvendas.versaofinal.R;


/**
 * Created by JAVA on 07/09/2015.
 */
public class ListaItensTemporariosAdapter extends BaseAdapter {


    private Context ctx;
    private List<SqliteVendaD_TempBean> listaItensTemporarios;

    public ListaItensTemporariosAdapter(Context ctx, List<SqliteVendaD_TempBean> listaItensTemporarios) {
        this.ctx = ctx;
        this.listaItensTemporarios = listaItensTemporarios;
    }

    @Override
    public int getCount() {
        return listaItensTemporarios.size();
    }

    @Override
    public Object getItem(int posicao) {
        return listaItensTemporarios.get(posicao);
    }

    @Override
    public long getItemId(int posicao) {
        return posicao;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {

        SqliteVendaD_TempBean item = (SqliteVendaD_TempBean)getItem(posicao);
        LayoutInflater layout = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layout.inflate(R.layout.prod_listview_itenstemp_item,null);

        TextView descriacao = (TextView) v.findViewById(R.id.prod_lisview_descricao);
        TextView quantidade = (TextView) v.findViewById(R.id.prod_lisview_quantidade);
        TextView preco = (TextView) v.findViewById(R.id.prod_lisview_preco);
        TextView total = (TextView) v.findViewById(R.id.prod_lisview_total);

        descriacao.setText(item.getVendad_prd_descricaoTEMP().toString());
        quantidade.setText(item.getVendad_quantidadeTEMP().toString());

        preco.setText(item.getVendad_preco_vendaTEMP().setScale(2, RoundingMode.HALF_UP).toString());
        total.setText(item.getVendad_totalTEMP().setScale(2,RoundingMode.UP).toString());


        return v;
    }
}
