package br.com.pauloceami.cursoappvendas.versaofinal.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;


public class BaixaContaReceberAdapter extends BaseAdapter {

    private Context ctx;
    private List<SqliteConRecBean> parcelas;


    public BaixaContaReceberAdapter(Context ctx, List<SqliteConRecBean> parcelas) {
        this.ctx = ctx;
        this.parcelas = parcelas;
    }

    @Override
    public int getCount() {
        return parcelas.size();
    }

    @Override
    public Object getItem(int position) {
        return parcelas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SqliteConRecBean rec = (SqliteConRecBean) getItem(position);
        LayoutInflater lf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = lf.inflate(android.R.layout.simple_list_item_multiple_choice, null);
        TextView txv_valor = (TextView) view.findViewById(android.R.id.text1);

        txv_valor.setTextSize(12);

        if (rec.getRec_valorpago() > 0) {
            txv_valor.setTextColor(Color.GREEN);
            txv_valor.setText("Parc.: " + rec.getRec_numparcela().toString() + " * Venc.: " +
                    Util.FormataDataDDMMAAAA(rec.getRec_datavencimento()) + " * R$ : " +
                    rec.getRec_valor_receber());
        } else {
            txv_valor.setText("Parc.: " + rec.getRec_numparcela().toString() + " * Venc.: " +
                    Util.FormataDataDDMMAAAA(rec.getRec_datavencimento()) + " * R$ : " +
                    rec.getRec_valor_receber());
        }

        return view;
    }
}
