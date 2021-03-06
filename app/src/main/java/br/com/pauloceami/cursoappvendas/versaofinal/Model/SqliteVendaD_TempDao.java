package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

/**
 * Created by JAVA on 07/09/2015.
 */
public class SqliteVendaD_TempDao {


    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public SqliteVendaD_TempDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean insere_item(SqliteVendaD_TempBean item) {

        gravacao = false;
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "insert into VENDAD_TEMP (vendad_eanTEMP,vendad_prd_codigoTEMP,vendad_prd_descricaoTEMP,vendad_quantidadeTEMP,vendad_preco_vendaTEMP,vendad_totalTEMP) values (?,?,?,?,?,?) ";
            stmt = db.compileStatement(sql);
            stmt.bindString(1, item.getVendad_eanTEMP());
            stmt.bindLong(2, item.getVendad_prd_codigoTEMP());
            stmt.bindString(3, item.getVendad_prd_descricaoTEMP());
            stmt.bindDouble(4, item.getVendad_quantidadeTEMP().doubleValue());
            stmt.bindDouble(5, item.getVendad_preco_vendaTEMP().doubleValue());
            stmt.bindDouble(6, item.getVendad_totalTEMP().doubleValue());
            if (stmt.executeInsert() > 0) {
                gravacao = true;
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException insere_item" + e.getMessage());
            gravacao = false;
        } finally {
            db.close();
            stmt.close();
        }
        return gravacao;
    }

    public void excluir_um_item_da_venda(SqliteVendaD_TempBean item) {
        db = new Db(ctx).getWritableDatabase();
        try {
            db.delete("VENDAD_TEMP", "vendad_prd_codigoTEMP = ?", new String[]{item.getVendad_prd_codigoTEMP().toString()});
        } catch (SQLiteException e) {
            Util.log("SQLiteException excluir_um_item_da_venda" + e.getMessage());
        } finally {
            db.close();
        }
    }


    public void excluir_itens() {
        db = new Db(ctx).getWritableDatabase();
        try {
            db.delete("VENDAD_TEMP", null, null);
        } catch (SQLiteException e) {
            Util.log("SQLiteException excluir_um_item_da_venda" + e.getMessage());
        } finally {
            db.close();
        }
    }


    public SqliteVendaD_TempBean buscar_item_na_venda(SqliteVendaD_TempBean item) {
        SqliteVendaD_TempBean produto = null;
        db = new Db(ctx).getReadableDatabase();
        try {
            sql = "select * from VENDAD_TEMP where vendad_prd_codigoTEMP = ? ";
            cursor = db.rawQuery(sql, new String[]{item.getVendad_prd_codigoTEMP().toString()});
            if (cursor.moveToFirst()) {
                produto = new SqliteVendaD_TempBean();
                produto.setVendad_eanTEMP(cursor.getString(cursor.getColumnIndex(produto.TEMP_EAN)));
                produto.setVendad_prd_codigoTEMP(cursor.getInt(cursor.getColumnIndex(produto.TEMP_CODPRODUTO)));
                produto.setVendad_prd_descricaoTEMP(cursor.getString(cursor.getColumnIndex(produto.TEMP_DESCRICAOPROD)));
                produto.setVendad_quantidadeTEMP(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(produto.TEMP_QUANTVENDIDA))));
                produto.setVendad_preco_vendaTEMP(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(produto.TEMP_PRECOPRODUTO))));
                produto.setVendad_totalTEMP(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(produto.TEMP_TOTALPRODUTO))));
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_item_na_venda" + e.getMessage());
        }
        return produto;
    }


    public List<SqliteVendaD_TempBean> busca_todos_itens_da_venda() {
        List<SqliteVendaD_TempBean> lista_de_itens_vendidos = new ArrayList<SqliteVendaD_TempBean>();
        db = new Db(ctx).getReadableDatabase();
        try {
            sql = "select * from VENDAD_TEMP";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                SqliteVendaD_TempBean items = new SqliteVendaD_TempBean();
                items.setVendad_eanTEMP(cursor.getString(cursor.getColumnIndex(items.TEMP_EAN)));
                items.setVendad_prd_codigoTEMP(cursor.getInt(cursor.getColumnIndex(items.TEMP_CODPRODUTO)));
                items.setVendad_prd_descricaoTEMP(cursor.getString(cursor.getColumnIndex(items.TEMP_DESCRICAOPROD)));
                items.setVendad_quantidadeTEMP(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(items.TEMP_QUANTVENDIDA))));
                items.setVendad_preco_vendaTEMP(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(items.TEMP_PRECOPRODUTO))));
                items.setVendad_totalTEMP(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(items.TEMP_TOTALPRODUTO))));
                lista_de_itens_vendidos.add(items);
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException busca_todos_itens_da_venda" + e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return lista_de_itens_vendidos;
    }


}













