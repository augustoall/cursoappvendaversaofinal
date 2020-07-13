package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAVA-NOT-I3 on 28/01/2018.
 */

public class SqliteHistoricoPagamentoDao {


    private Context ctx;
    private boolean gravou;
    private String sql;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public SqliteHistoricoPagamentoDao(Context ctx) {
        this.ctx = ctx;
    }


    public boolean grava_historico(SqliteHistoricoPagamentosBean pgto) {
        try {
            gravou = false;
            db = new Db(ctx).getWritableDatabase();
            sql = "insert into HISTPAGAMENTO (" +

                    "hist_numero_parcela," +
                    "hist_valor_real_parcela," +
                    "hist_valor_pago_no_dia," +
                    "hist_restante_a_pagar," +
                    "hist_data_do_pagamento," +
                    "hist_nome_cliente," +
                    "hist_como_pagou," +
                    "vendac_chave," +
                    "hist_enviado) values (?,?,?,?,?,?,?,?,?)";
            stmt = db.compileStatement(sql);

            stmt.bindLong(1, pgto.getHist_numero_parcela());
            stmt.bindDouble(2, pgto.getHist_valor_real_parcela().doubleValue());
            stmt.bindDouble(3, pgto.getHist_valor_pago_no_dia().doubleValue());

            stmt.bindDouble(4, pgto.getHist_restante_a_pagar().doubleValue());
            stmt.bindString(5, pgto.getHist_data_do_pagamento());
            stmt.bindString(6, pgto.getHist_nome_cliente());

            stmt.bindString(7, pgto.getHist_como_pagou());
            stmt.bindString(8, pgto.getVendac_chave());
            stmt.bindString(9, pgto.getHist_enviado());


            if (stmt.executeInsert() > 0) {
                gravou = true;
                sql = "";
            }
            stmt.clearBindings();
        } catch (SQLiteException e) {
            gravou = false;
            Log.d("grava_historico", e.getMessage());

        } finally {
            db.close();
            stmt.close();
        }
        return gravou;
    }


    public List<SqliteHistoricoPagamentosBean> getAll() {
        List<SqliteHistoricoPagamentosBean> historicos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            sql = "select * from HISTPAGAMENTO";
            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {

                SqliteHistoricoPagamentosBean hist = new SqliteHistoricoPagamentosBean();

                hist.setHist_codigo("1");
                hist.setHist_numero_parcela(1);
                hist.setHist_valor_real_parcela(new BigDecimal(1.3));
                hist.setHist_valor_pago_no_dia(new BigDecimal(1.3));

                hist.setHist_restante_a_pagar(new BigDecimal(1.3));
                hist.setHist_data_do_pagamento("2018-01-01");
                hist.setHist_nome_cliente("");

                hist.setHist_como_pagou("DINHEIRO");
                hist.setVendac_chave("56484684");


                historicos.add(hist);
            }

        } catch (SQLiteException e) {
            Log.d("getAll", e.getMessage());
        } finally {
            db.close();
        }
        return historicos;
    }


}
