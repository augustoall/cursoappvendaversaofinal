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
 * Created by JAVA on 28/08/2015.
 */
public class SqliteProdutoDao {

    public static final int DESCRICAO_PRODUTO = 1;
    public static final int CODIGO_BARRAS_PRODUTO = 2;
    public static final int CATEGORIA_PRODUTO = 3;


    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;


    public SqliteProdutoDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravar_produto(SqliteProdutoBean produto) {
        gravacao = false;
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "insert into PRODUTOS (prd_codigo,prd_EAN13,prd_descricao,prd_unmedida,prd_custo,prd_quantidade,prd_preco,prd_categoria)  values (?,?,?,?,?,?,?,?) ";
            stmt = db.compileStatement(sql);
            stmt.bindLong(1, produto.getPrd_codigo());
            stmt.bindString(2, produto.getPrd_EAN13());
            stmt.bindString(3, produto.getPrd_descricao());
            stmt.bindString(4, produto.getPrd_unmedida());
            stmt.bindDouble(5, produto.getPrd_custo().doubleValue());
            stmt.bindDouble(6, produto.getPrd_quantidade().doubleValue());
            stmt.bindDouble(7, produto.getPrd_preco().doubleValue());
            stmt.bindString(8, produto.getPrd_categoria());
            if (stmt.executeInsert() > 0) {
                gravacao = true;
            }

        } catch (SQLiteException e) {
            gravacao = false;
            Util.log("SQLiteException gravar_produto" + e.getMessage());

        } finally {
            db.close();
            stmt.close();
        }
        return gravacao;
    }

    public void atualiza_produto(SqliteProdutoBean produto) {
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "update PRODUTOS set prd_EAN13=?, prd_descricao=?,prd_unmedida=?,prd_custo=?, prd_quantidade=?,prd_preco=?,prd_categoria=?  where prd_codigo =?  ";
            stmt = db.compileStatement(sql);
            stmt.bindString(1, produto.getPrd_EAN13());
            stmt.bindString(2, produto.getPrd_descricao());
            stmt.bindString(3, produto.getPrd_unmedida());
            stmt.bindDouble(4, produto.getPrd_custo().doubleValue());
            stmt.bindDouble(5, produto.getPrd_quantidade().doubleValue());
            stmt.bindDouble(6, produto.getPrd_preco().doubleValue());
            stmt.bindString(7, produto.getPrd_categoria());
            stmt.bindLong(8, produto.getPrd_codigo());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Util.log("SQLiteException atualiza_produto" + e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

    public void atualiza_estoque(SqliteProdutoBean produto) {
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "update PRODUTOS set prd_quantidade=?  where prd_codigo =?  ";
            stmt = db.compileStatement(sql);
            stmt.bindDouble(1, produto.getPrd_quantidade().doubleValue());
            stmt.bindLong(2, produto.getPrd_codigo());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Util.log("SQLiteException atualiza_estoque" + e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }


    public SqliteProdutoBean buscar_produto_pelo_codigo(String prd_codigo) {
        db = new Db(ctx).getReadableDatabase();
        SqliteProdutoBean produto = null;
        try {
            cursor = db.rawQuery("select * from PRODUTOS   where prd_codigo = ? ", new String[]{prd_codigo});
            if (cursor.moveToFirst()) {
                produto = new SqliteProdutoBean();
                produto.setPrd_codigo(cursor.getInt(cursor.getColumnIndex(produto.P_CODIGO_PRODUTO)));
                produto.setPrd_EAN13(cursor.getString(cursor.getColumnIndex(produto.P_CODIGO_BARRAS)));
                produto.setPrd_descricao(cursor.getString(cursor.getColumnIndex(produto.P_DESCRICAO_PRODUTO)));
                produto.setPrd_unmedida(cursor.getString(cursor.getColumnIndex(produto.P_UNIDADE_MEDIDA)));
                produto.setPrd_custo(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(produto.P_CUSTO_PRODUTO))));
                produto.setPrd_quantidade(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(produto.P_QUANTIDADE_PRODUTO))));
                produto.setPrd_preco(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(produto.P_PRECO_PRODUTO))));
                produto.setPrd_categoria(cursor.getString(cursor.getColumnIndex(produto.P_CATEGORIA_PRODUTO)));

            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_produto_pelo_codigo" + e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }

        return produto;
    }

    public List<SqliteProdutoBean> listar_todos_produtos() {
        List<SqliteProdutoBean> lista_de_produtos = new ArrayList<SqliteProdutoBean>();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("select * from PRODUTOS  ", null);
            while (cursor.moveToNext()) {
                SqliteProdutoBean prd = new SqliteProdutoBean();
                prd.setPrd_codigo(cursor.getInt(cursor.getColumnIndex(prd.P_CODIGO_PRODUTO)));
                prd.setPrd_EAN13(cursor.getString(cursor.getColumnIndex(prd.P_CODIGO_BARRAS)));
                prd.setPrd_descricao(cursor.getString(cursor.getColumnIndex(prd.P_DESCRICAO_PRODUTO)));
                prd.setPrd_unmedida(cursor.getString(cursor.getColumnIndex(prd.P_UNIDADE_MEDIDA)));
                prd.setPrd_custo(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.P_CUSTO_PRODUTO))));
                prd.setPrd_quantidade(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.P_QUANTIDADE_PRODUTO))));
                prd.setPrd_preco(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(prd.P_PRECO_PRODUTO))));
                prd.setPrd_categoria(cursor.getString(cursor.getColumnIndex(prd.P_CATEGORIA_PRODUTO)));
                lista_de_produtos.add(prd);
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException listar_todos_produtos" + e.getMessage());
        } finally {
            db.close();
        }
        return lista_de_produtos;
    }

    public Cursor buscar_produtos() {

        SqliteProdutoBean produto = new SqliteProdutoBean();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.query("PRODUTOS", new String[]{
                    produto.P_CODIGO_PRODUTO + " as _id",produto.P_CODIGO_BARRAS,produto.P_DESCRICAO_PRODUTO,produto.P_UNIDADE_MEDIDA,produto.P_CUSTO_PRODUTO,produto.P_QUANTIDADE_PRODUTO,produto.P_PRECO_PRODUTO,produto.P_CATEGORIA_PRODUTO}, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_produtos" + e.getMessage());
        }
        return cursor;
    }


    public Cursor buscar_produto_na_pesquisa_edittext(String valor_campo, int field) {
        SqliteProdutoBean produto = new SqliteProdutoBean();
        db = new Db(ctx).getReadableDatabase();
        Cursor cursor = null;
        try {
            if (valor_campo == null || valor_campo.length() == 0) {
                cursor = db.query("PRODUTOS", new String[]{
                        produto.P_CODIGO_PRODUTO + " as _id",produto.P_CODIGO_BARRAS,produto.P_DESCRICAO_PRODUTO,produto.P_UNIDADE_MEDIDA,produto.P_CUSTO_PRODUTO,produto.P_QUANTIDADE_PRODUTO,produto.P_PRECO_PRODUTO,produto.P_CATEGORIA_PRODUTO}, null, null, null, null, null);
            } else {
                switch (field) {
                    case DESCRICAO_PRODUTO:
                        cursor = db.query("PRODUTOS", new String[]{produto.P_CODIGO_PRODUTO + " as _id", produto.P_CODIGO_BARRAS, produto.P_DESCRICAO_PRODUTO, produto.P_UNIDADE_MEDIDA, produto.P_CUSTO_PRODUTO, produto.P_QUANTIDADE_PRODUTO, produto.P_PRECO_PRODUTO, produto.P_CATEGORIA_PRODUTO}, produto.P_DESCRICAO_PRODUTO + "   like  '%" + valor_campo + "%'", null, null, null, null);
                        break;
                    case CODIGO_BARRAS_PRODUTO:
                        cursor = db.query("PRODUTOS", new String[]{produto.P_CODIGO_PRODUTO + " as _id", produto.P_CODIGO_BARRAS, produto.P_DESCRICAO_PRODUTO, produto.P_UNIDADE_MEDIDA, produto.P_CUSTO_PRODUTO, produto.P_QUANTIDADE_PRODUTO, produto.P_PRECO_PRODUTO, produto.P_CATEGORIA_PRODUTO}, produto.P_CODIGO_BARRAS + "   like  '%" + valor_campo + "%'", null, null, null, null);
                        break;
                    case CATEGORIA_PRODUTO:
                        cursor = db.query("PRODUTOS", new String[]{produto.P_CODIGO_PRODUTO + " as _id", produto.P_CODIGO_BARRAS, produto.P_DESCRICAO_PRODUTO, produto.P_UNIDADE_MEDIDA, produto.P_CUSTO_PRODUTO, produto.P_QUANTIDADE_PRODUTO, produto.P_PRECO_PRODUTO, produto.P_CATEGORIA_PRODUTO}, produto.P_CATEGORIA_PRODUTO + "   like  '%" + valor_campo + "%'", null, null, null, null);
                        break;
                }
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_produto_na_pesquisa_edittext" + e.getMessage());
        }
        return cursor;
    }


}
