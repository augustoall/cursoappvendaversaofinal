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

public class SqliteChequeDao {

    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;


    public SqliteChequeDao(Context ctx) {
        this.ctx = ctx;
    }


    public boolean gravar_cheque(SqliteChequeBean cheque) {
        try {

            db = new Db(ctx).getWritableDatabase();
            gravacao = false;
            sql = "INSERT INTO CHEQUES (ch_cli_codigo,ch_numero_cheque,ch_contato,ch_cpf_dono,ch_nome_do_dono,ch_nome_banco,ch_vencimento,ch_valor_cheque,ch_proprio,vendac_chave,ch_enviado,ch_dataCadastro) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            stmt = db.compileStatement(sql);
            stmt.bindLong(1, cheque.getCh_cli_codigo());
            stmt.bindString(2, cheque.getCh_numero_cheque());
            stmt.bindString(3, cheque.getCh_contato());
            stmt.bindString(4, cheque.getCh_cpf_dono());
            stmt.bindString(5, cheque.getCh_nome_do_dono());
            stmt.bindString(6, cheque.getCh_nome_banco());
            stmt.bindString(7, cheque.getCh_vencimento());
            stmt.bindDouble(8, cheque.getCh_valor_cheque().doubleValue());
            stmt.bindString(9, cheque.getCh_proprio());
            stmt.bindString(10, cheque.getVendac_chave());
            stmt.bindString(11, cheque.getCh_enviado());
            stmt.bindString(12, cheque.getCh_dataCadastro());

            if (stmt.executeInsert() > 0) {
                gravacao = true;
                sql = "";
            }


            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("gravar_cheque", e.getMessage());
            gravacao = false;
        } finally {
            db.close();
            stmt.close();
        }

        return gravacao;
    }

    public List<SqliteChequeBean> buscar_todos_os_cheques() {
        ArrayList<SqliteChequeBean> lista_de_cheques = new ArrayList<SqliteChequeBean>();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("SELECT * FROM CHEQUES", null);
            while (cursor.moveToNext()) {
                SqliteChequeBean cheque = new SqliteChequeBean();
                cheque.setCh_codigo(cursor.getInt(cursor.getColumnIndex(cheque.CODIGO_DO_CHEQUE)));
                cheque.setCh_cli_codigo(cursor.getInt(cursor.getColumnIndex(cheque.CODIGO_DO_CLIENTE)));
                cheque.setCh_numero_cheque(cursor.getString(cursor.getColumnIndex(cheque.NUMERO_DO_CHEQUE)));
                cheque.setCh_contato(cursor.getString(cursor.getColumnIndex(cheque.CONTATO_DO_CHEQUE)));
                cheque.setCh_cpf_dono(cursor.getString(cursor.getColumnIndex(cheque.CPF_DONO_CHEQUE)));
                cheque.setCh_nome_do_dono(cursor.getString(cursor.getColumnIndex(cheque.NOME_DONO_CHEQUE)));
                cheque.setCh_nome_banco(cursor.getString(cursor.getColumnIndex(cheque.NOME_DO_BANCO)));
                cheque.setCh_vencimento(cursor.getString(cursor.getColumnIndex(cheque.VENCIMENTO_DO_CHEQUE)));
                cheque.setCh_valor_cheque(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(cheque.VALOR_DO_CHEQUE))));
                cheque.setCh_proprio(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_PROPRIO)));
                cheque.setVendac_chave(cursor.getString(cursor.getColumnIndex(cheque.CHAVE_DA_VENDA)));
                cheque.setCh_enviado(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_ENVIADO)));
                cheque.setCh_dataCadastro(cursor.getString(cursor.getColumnIndex(cheque.DATA_CADASTRO_CHEQUE)));
                lista_de_cheques.add(cheque);
            }
        } catch (SQLiteException e) {
            Log.d("script", e.getMessage());
        } finally {
            db.close();
        }
        return lista_de_cheques;
    }

    public void atualizar_chave_da_venda_no_cheque(String vendac_chave, Integer cli_codigo, String data_cadastro) {
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "UPDATE CHEQUES SET vendac_chave = ? where vendac_chave = '' AND ch_cli_codigo = ? AND ch_dataCadastro = ?";
            stmt = db.compileStatement(sql);
            stmt.bindString(1, vendac_chave);
            stmt.bindLong(2, cli_codigo);
            stmt.bindString(3, data_cadastro);
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("atualizar_chave_", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }


    public void excluir_cheque(int ch_cli_codigo,String ch_dataCadastro){
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        String sql = "DELETE FROM CHEQUES WHERE where vendac_chave = '' AND ch_cli_codigo = ? AND ch_dataCadastro = ?  ";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindLong(1,ch_cli_codigo);
            stmt.bindString(2,ch_dataCadastro);
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("excluir_cheque", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }


}
