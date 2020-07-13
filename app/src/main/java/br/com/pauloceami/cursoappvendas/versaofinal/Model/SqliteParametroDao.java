package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SqliteParametroDao {


    private Context ctx;
    private String sql;
    private boolean gravacao;


    public SqliteParametroDao(Context ctx) {
        this.ctx = ctx;
    }


    public boolean gravar_parametro(SqliteParametroBean param) {


        try {

            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            gravacao = false;

            sql = "insert into PARAMETROS (" +
                    "p_usu_codigo," +
                    "p_importar_todos_clientes," +
                    "p_url_base," +
                   // "p_end_ip_remoto," +
                    "p_trabalhar_com_estoque_negativo," +
                    "p_desconto_do_vendedor ," +
                    "p_usuario," +
                    "p_senha" +
                  //  "p_qual_endereco_ip" +
                    " )  " +
                    "values (?,?,?,?,?,?,?) ";

            SQLiteStatement stmt = db.compileStatement(sql);



            stmt.bindLong(1, param.getP_usu_codigo());
            stmt.bindString(2, param.getP_importar_todos_clientes());
            stmt.bindString(3, param.getP_url_base());
            //stmt.bindString(4, param.getP_end_ip_remoto());
            stmt.bindString(4, param.getP_trabalhar_com_estoque_negativo());
            stmt.bindLong(5, param.getP_desconto_do_vendedor());
            stmt.bindString(6, param.getP_usuario());
            stmt.bindString(7, param.getP_senha());
            //stmt.bindString(9,param.getP_qual_endereco_ip());

            if (stmt.executeInsert() > 0) {
                gravacao = true;
                sql = "";
            }

        } catch (SQLiteException e) {
            Log.d("Script", e.getMessage());
            gravacao = false;
        }


        return gravacao;
    }

    public SqliteParametroBean busca_parametros() {

        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        SqliteParametroBean parametro = null;

        sql = "select * from PARAMETROS";

        try {

            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {

                parametro = new SqliteParametroBean();
                parametro.setP_usu_codigo(cursor.getInt(cursor.getColumnIndex(parametro.P_CODIGO_USUARIO)));
                parametro.setP_importar_todos_clientes(cursor.getString(cursor.getColumnIndex(parametro.P_IMPORTAR_TODOS_CLIENTES)));
                parametro.setP_url_base(cursor.getString(cursor.getColumnIndex(parametro.P_URL_BASE)));
               // parametro.setP_end_ip_remoto(cursor.getString(cursor.getColumnIndex(parametro.P_ENDERECO_IP_REMOTO)));
                parametro.setP_trabalhar_com_estoque_negativo(cursor.getString(cursor.getColumnIndex(parametro.P_ESTOQUE_NEGATIVO)));
                parametro.setP_desconto_do_vendedor(cursor.getInt(cursor.getColumnIndex(parametro.P_DESCONTO_VENDEDOR)));
                parametro.setP_usuario(cursor.getString(cursor.getColumnIndex(parametro.P_USUARIO)));
                parametro.setP_senha(cursor.getString(cursor.getColumnIndex(parametro.P_SENHA)));
               // parametro.setP_qual_endereco_ip(cursor.getString(cursor.getColumnIndex(parametro.P_QUAL_END_IP)));

            }

        } catch (SQLiteException e) {
            Log.d("Script", e.getMessage());
        }finally {
            db.close();

        }

        return parametro;
    }


    public void atualizaParametro(SqliteParametroBean param) {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        sql = "update  PARAMETROS set " +
                "p_usu_codigo=?," +
                "p_importar_todos_clientes=?," +
                "p_url_base=?," +
                //"p_end_ip_remoto=?," +
                "p_trabalhar_com_estoque_negativo=?," +
                "p_desconto_do_vendedor=? " ;
               // "p_qual_endereco_ip=?  ";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {

            stmt.bindLong(1, param.getP_usu_codigo());
            stmt.bindString(2, param.getP_importar_todos_clientes());
            stmt.bindString(3, param.getP_url_base());
           // stmt.bindString(4, param.getP_end_ip_remoto());
            stmt.bindString(4, param.getP_trabalhar_com_estoque_negativo());
            stmt.bindLong(5, param.getP_desconto_do_vendedor());
           // stmt.bindString(7, param.getP_qual_endereco_ip());
            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("Script", e.getMessage());
        }finally {
            db.close();
            stmt.close();
        }

    }




    public void reset_parametro(){
        try {
            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            sql = "delete from PARAMETROS";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.executeUpdateDelete();

        }catch (SQLiteException e){
            Log.d("Script", e.getMessage());
        }

    }


}


















