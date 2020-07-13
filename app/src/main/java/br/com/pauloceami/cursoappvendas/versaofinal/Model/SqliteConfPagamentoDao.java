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

public class SqliteConfPagamentoDao {


    private Context ctx;
    private String Sql;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;
    private boolean gravou;

    public SqliteConfPagamentoDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravar_CONFPAGAMENTO(SqliteConfPagamentoBean pagamento) {
        db = new Db(ctx).getWritableDatabase();
        gravou = false;
        try {
            Sql = "INSERT INTO CONFPAGAMENTO  (conf_sementrada_comentrada,conf_tipo_pagamento,conf_recebeucom_din_chq_car,conf_valor_recebido,conf_parcelas,conf_vencimento_gerencianet,conf_valor_parcela_gerencianet,vendac_chave,conf_enviado) VALUES (?,?,?,?,?,?,?,?,?)";
            stmt = db.compileStatement(Sql);
            stmt.bindString(1, pagamento.getConf_sementrada_comentrada());
            stmt.bindString(2, pagamento.getConf_tipo_pagamento());
            stmt.bindString(3, pagamento.getConf_recebeucom_din_chq_car());
            stmt.bindDouble(4, pagamento.getConf_valor_recebido().doubleValue());
            stmt.bindLong(5, pagamento.getConf_parcelas());
            stmt.bindString(6, pagamento.getConf_vencimento_gerencianet());
            stmt.bindDouble(7, pagamento.getConf_valor_parcela_gerencianet().doubleValue());
            stmt.bindString(8, pagamento.getVendac_chave());
            stmt.bindString(9, pagamento.getConf_enviado());

            if (stmt.executeInsert() > 0) {
                gravou = true;
            }

        } catch (SQLiteException e) {
            Log.d("gravar_CONFPAGAMENTO", e.getMessage());
            gravou = false;
        } finally {
            db.close();
            stmt.close();
        }

        return gravou;
    }


    public void AtualizaVendac_chave_CONFPAGAMENTO(String vendac_chave) {
        try {
            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            String sql = "UPDATE CONFPAGAMENTO set vendac_chave = ? where vendac_chave  = '' ";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, vendac_chave);
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("AtualizaVendac_chaveC", e.getMessage());
        }
    }

    public List<SqliteConfPagamentoBean> getAllPagamentos() {
        List<SqliteConfPagamentoBean> lista = new ArrayList<SqliteConfPagamentoBean>();
        try {
            db = new Db(ctx).getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM CONFPAGAMENTO WHERE conf_enviado = 'N' ", null);
            while (cursor.moveToNext()) {
                SqliteConfPagamentoBean conf = new SqliteConfPagamentoBean();
                conf.setConf_codigo(cursor.getInt(cursor.getColumnIndex(conf.CONF_CODIGO_CONFPAGAMENTO)));
                conf.setConf_parcelas(cursor.getInt(cursor.getColumnIndex(conf.CONF_QUANTIDADE_PARCELAS)));
                conf.setConf_recebeucom_din_chq_car(cursor.getString(cursor.getColumnIndex(conf.CONF_DINHEIRO_CARTAO_CHEQUE)));
                conf.setConf_sementrada_comentrada(cursor.getString(cursor.getColumnIndex(conf.CONF_SEMENTADA_COMENTRADA)));
                conf.setConf_tipo_pagamento(cursor.getString(cursor.getColumnIndex(conf.CONF_TIPO_DO_PAGAMENTO)));
                conf.setConf_valor_recebido(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(conf.CONF_VALOR_RECEBIDO))));
                conf.setVendac_chave(cursor.getString(cursor.getColumnIndex(conf.CONF_VENDAC_CHAVE)));
                conf.setConf_enviado(cursor.getString(cursor.getColumnIndex(conf.CONF_ENVIADO)));


                conf.setConf_vencimento_gerencianet(cursor.getString(cursor.getColumnIndex(conf.CONF_VENCIMENTO_GERENCIANET)));
                conf.setConf_valor_parcela_gerencianet(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(conf.CONF_VALOR_PARCELA_GERENCIANET))));


                lista.add(conf);
            }
        } catch (SQLiteException e) {
            Log.d("busca_todos_confpag", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }

        return lista;
    }

    public SqliteConfPagamentoBean busca_CONFPAGAMENTO_sem_chave() {
        SqliteConfPagamentoBean conf = null;
        try {
            db = new Db(ctx).getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM CONFPAGAMENTO WHERE vendac_chave = '' ", null);
            if (cursor.moveToFirst()) {
                conf = new SqliteConfPagamentoBean();
                conf.setConf_codigo(cursor.getInt(cursor.getColumnIndex(conf.CONF_CODIGO_CONFPAGAMENTO)));
                conf.setConf_parcelas(cursor.getInt(cursor.getColumnIndex(conf.CONF_QUANTIDADE_PARCELAS)));
                conf.setConf_recebeucom_din_chq_car(cursor.getString(cursor.getColumnIndex(conf.CONF_DINHEIRO_CARTAO_CHEQUE)));
                conf.setConf_sementrada_comentrada(cursor.getString(cursor.getColumnIndex(conf.CONF_SEMENTADA_COMENTRADA)));
                conf.setConf_tipo_pagamento(cursor.getString(cursor.getColumnIndex(conf.CONF_TIPO_DO_PAGAMENTO)));
                conf.setConf_valor_recebido(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(conf.CONF_VALOR_RECEBIDO))));
                conf.setVendac_chave(cursor.getString(cursor.getColumnIndex(conf.CONF_VENDAC_CHAVE)));
                conf.setConf_enviado(cursor.getString(cursor.getColumnIndex(conf.CONF_ENVIADO)));

            }
        } catch (SQLiteException e) {
            Log.d("busca_CONFPAGAMENTO_sem", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }

        return conf;
    }

    public void excluir_CONFPAGAMENTO() {
        db = new Db(ctx).getWritableDatabase();
        Sql = "DELETE FROM CONFPAGAMENTO WHERE vendac_chave = '' ";
        try {
            stmt = db.compileStatement(Sql);
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("excluir_CONFPAGAMENTO", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }


    public void excluir_CONFPAGAMENTO(String vendac_chave) {
        db = new Db(ctx).getWritableDatabase();
        try {
            db.delete("CONFPAGAMENTO", "vendac_chave = ?", new String[]{vendac_chave});
        } catch (SQLiteException e) {
            Log.d("deletevendac", e.getMessage());
        } finally {
            db.close();
        }

    }


}
