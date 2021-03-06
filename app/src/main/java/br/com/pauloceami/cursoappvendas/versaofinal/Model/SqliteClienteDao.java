package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;

/**
 * Created by JAVA on 24/08/2015.
 */
public class SqliteClienteDao {

    private Context ctx;
    private String sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public static final int NOME_DO_CLIENTE = 1;
    public static final int NOME_FANTASIA = 2;
    public static final int NOME_CIDADE = 3;
    public static final int NOME_BAIRRO = 4;

    public SqliteClienteDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravar_cliente(SqliteClienteBean cliente) {
        gravacao = false;
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "insert into CLIENTES (cli_codigo,cli_nome,cli_fantasia,cli_endereco,cli_bairro,cli_cep,cid_nome,cli_contato,cli_nascimento,cli_cpfcnpj,cli_rginscricaoest,cli_email,cli_enviado,cli_chave)  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)      ";
            stmt = db.compileStatement(sql);
            stmt.bindLong(1, cliente.getCli_codigo());
            stmt.bindString(2, cliente.getCli_nome());
            stmt.bindString(3, cliente.getCli_fantasia());
            stmt.bindString(4, cliente.getCli_endereco());
            stmt.bindString(5, cliente.getCli_bairro());
            stmt.bindString(6, cliente.getCli_cep());
            stmt.bindString(7, cliente.getCid_nome());
            stmt.bindString(8, cliente.getCli_contato());
            stmt.bindString(9, cliente.getCli_nascimento());
            stmt.bindString(10, cliente.getCli_cpfcnpj());
            stmt.bindString(11, cliente.getCli_rginscricaoest());
            stmt.bindString(12, cliente.getCli_email());
            stmt.bindString(13, cliente.getCli_enviado());
            stmt.bindString(14, cliente.getCli_chave());
            if (stmt.executeInsert() > 0) {
                gravacao = true;
            }
        } catch (SQLiteException e) {
            gravacao = false;
            Util.log("SQLiteException gravar_cliente" + e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
        return gravacao;
    }

    public void atualiza_cliente(SqliteClienteBean cliente) {
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "update CLIENTES set cli_nome=?,cli_fantasia=?,cli_endereco=?,cli_bairro=?,cli_cep=?,cid_nome=?,cli_contato=?,cli_nascimento=?,cli_cpfcnpj=?,cli_rginscricaoest=?,cli_email=?,cli_enviado=?  where cli_codigo =?   ";
            stmt = db.compileStatement(sql);
            stmt.bindString(1, cliente.getCli_nome());
            stmt.bindString(2, cliente.getCli_fantasia());
            stmt.bindString(3, cliente.getCli_endereco());
            stmt.bindString(4, cliente.getCli_bairro());
            stmt.bindString(5, cliente.getCli_cep());
            stmt.bindString(6, cliente.getCid_nome());
            stmt.bindString(7, cliente.getCli_contato());
            stmt.bindString(8, cliente.getCli_nascimento());
            stmt.bindString(9, cliente.getCli_cpfcnpj());
            stmt.bindString(10, cliente.getCli_rginscricaoest());
            stmt.bindString(11, cliente.getCli_email());
            stmt.bindString(12, cliente.getCli_enviado());
            stmt.bindLong(13, cliente.getCli_codigo());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Util.log("SQLiteException atualiza_cliente" + e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

    public void atualiza_cli_codigo_pela_CHAVE(SqliteClienteBean cliente) {
        try {

            db = new Db(ctx).getWritableDatabase();
            sql = "update CLIENTES set cli_codigo=?, cli_enviado = ? where cli_chave =?   ";
            stmt = db.compileStatement(sql);
            stmt.bindLong(1, cliente.getCli_codigo());
            stmt.bindString(2, "S");
            stmt.bindString(3, cliente.getCli_chave());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Util.log("SQLiteException atualiza_cli_codigo_pela_CHAVE" + e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

    public SqliteClienteBean buscar_cliente_pelo_codigo(String cli_codigo) {
        db = new Db(ctx).getReadableDatabase();
        SqliteClienteBean clientes = null;
        try {
            cursor = db.rawQuery("select * from CLIENTES   where cli_codigo = ? ", new String[]{cli_codigo});
            if (cursor.moveToFirst()) {
                clientes = new SqliteClienteBean();
                clientes.setCli_codigo(cursor.getInt(cursor.getColumnIndex(clientes.C_CODIGO_CLIENTE)));
                clientes.setCli_nome(cursor.getString(cursor.getColumnIndex(clientes.C_NOME_DO_CLIENTE)));
                clientes.setCli_fantasia(cursor.getString(cursor.getColumnIndex(clientes.C_NOME_FANTASIA)));
                clientes.setCli_endereco(cursor.getString(cursor.getColumnIndex(clientes.C_ENDERECO_CLIENTE)));
                clientes.setCli_bairro(cursor.getString(cursor.getColumnIndex(clientes.C_BAIRRO_CLIENTE)));
                clientes.setCli_cep(cursor.getString(cursor.getColumnIndex(clientes.C_CEP_CLIENTE)));
                clientes.setCid_nome(cursor.getString(cursor.getColumnIndex(clientes.C_CIDADE_CLIENTE)));
                clientes.setCli_contato(cursor.getString(cursor.getColumnIndex(clientes.C_CONTATO_CLIENTE)));
                clientes.setCli_nascimento(cursor.getString(cursor.getColumnIndex(clientes.C_DATA_NASCIMENTO)));
                clientes.setCli_cpfcnpj(cursor.getString(cursor.getColumnIndex(clientes.C_CNPJCPF)));
                clientes.setCli_rginscricaoest(cursor.getString(cursor.getColumnIndex(clientes.C_RGINSCRICAO_ESTADUAL)));
                clientes.setCli_email(cursor.getString(cursor.getColumnIndex(clientes.C_EMAIL_CLIENTE)));
                clientes.setCli_enviado(cursor.getString(cursor.getColumnIndex(clientes.C_ENVIADO)));
                clientes.setCli_chave(cursor.getString(cursor.getColumnIndex(clientes.C_CHAVE_DO_CLIENTE)));
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_cliente_pelo_codigo" + e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return clientes;
    }


    public List<SqliteClienteBean> buscar_clientes_nao_enviados() {
        List<SqliteClienteBean> lista_de_clientes = new ArrayList<SqliteClienteBean>();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("select * from CLIENTES where cli_enviado = 'N' ", null);
            while (cursor.moveToNext()) {
                SqliteClienteBean clientes = new SqliteClienteBean();
                clientes.setCli_codigo(cursor.getInt(cursor.getColumnIndex(clientes.C_CODIGO_CLIENTE)));
                clientes.setCli_nome(cursor.getString(cursor.getColumnIndex(clientes.C_NOME_DO_CLIENTE)));
                clientes.setCli_fantasia(cursor.getString(cursor.getColumnIndex(clientes.C_NOME_FANTASIA)));
                clientes.setCli_endereco(cursor.getString(cursor.getColumnIndex(clientes.C_ENDERECO_CLIENTE)));
                clientes.setCli_bairro(cursor.getString(cursor.getColumnIndex(clientes.C_BAIRRO_CLIENTE)));
                clientes.setCli_cep(cursor.getString(cursor.getColumnIndex(clientes.C_CEP_CLIENTE)));
                clientes.setCid_nome(cursor.getString(cursor.getColumnIndex(clientes.C_CIDADE_CLIENTE)));
                clientes.setCli_contato(cursor.getString(cursor.getColumnIndex(clientes.C_CONTATO_CLIENTE)));
                clientes.setCli_nascimento(cursor.getString(cursor.getColumnIndex(clientes.C_DATA_NASCIMENTO)));
                clientes.setCli_cpfcnpj(cursor.getString(cursor.getColumnIndex(clientes.C_CNPJCPF)));
                clientes.setCli_rginscricaoest(cursor.getString(cursor.getColumnIndex(clientes.C_RGINSCRICAO_ESTADUAL)));
                clientes.setCli_email(cursor.getString(cursor.getColumnIndex(clientes.C_EMAIL_CLIENTE)));
                clientes.setCli_enviado(cursor.getString(cursor.getColumnIndex(clientes.C_ENVIADO)));
                clientes.setCli_chave(cursor.getString(cursor.getColumnIndex(clientes.C_CHAVE_DO_CLIENTE)));
                lista_de_clientes.add(clientes);
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_clientes_nao_enviados" + e.getMessage());
        } finally {
            db.close();
        }
        return lista_de_clientes;
    }

    public Cursor buscar_todos_cliente() {

        SqliteClienteBean clientes = new SqliteClienteBean();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.query("CLIENTES", new String[]{
                    clientes.C_CODIGO_CLIENTE + " as _id",
                    clientes.C_NOME_DO_CLIENTE,
                    clientes.C_NOME_FANTASIA,
                    clientes.C_ENDERECO_CLIENTE,
                    clientes.C_BAIRRO_CLIENTE,
                    clientes.C_CEP_CLIENTE,
                    clientes.C_CIDADE_CLIENTE,
                    clientes.C_CONTATO_CLIENTE,
                    clientes.C_DATA_NASCIMENTO,
                    clientes.C_CNPJCPF,
                    clientes.C_RGINSCRICAO_ESTADUAL,
                    clientes.C_EMAIL_CLIENTE,
                    clientes.C_ENVIADO,
                    clientes.C_CHAVE_DO_CLIENTE}, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_todos_cliente" + e.getMessage());
        }
        return cursor;

    }

    public Cursor buscar_cliente_na_pesquisa_edittext(String valor_campo, int field) {
        SqliteClienteBean clientes = new SqliteClienteBean();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        Cursor cursor = null;
        try {
            if (valor_campo == null || valor_campo.length() == 0) {
                cursor = db.query("CLIENTES", new String[]{clientes.C_CODIGO_CLIENTE + " as _id", clientes.C_NOME_DO_CLIENTE, clientes.C_NOME_FANTASIA, clientes.C_ENDERECO_CLIENTE, clientes.C_BAIRRO_CLIENTE, clientes.C_CEP_CLIENTE, clientes.C_CIDADE_CLIENTE, clientes.C_CONTATO_CLIENTE, clientes.C_DATA_NASCIMENTO, clientes.C_CNPJCPF, clientes.C_RGINSCRICAO_ESTADUAL, clientes.C_EMAIL_CLIENTE, clientes.C_ENVIADO, clientes.C_CHAVE_DO_CLIENTE}, null, null, null, null, null);
            } else {
                switch (field) {
                    case NOME_DO_CLIENTE:
                        cursor = db.query("CLIENTES", new String[]{clientes.C_CODIGO_CLIENTE + " as _id", clientes.C_NOME_DO_CLIENTE, clientes.C_NOME_FANTASIA, clientes.C_ENDERECO_CLIENTE, clientes.C_BAIRRO_CLIENTE, clientes.C_CEP_CLIENTE, clientes.C_CIDADE_CLIENTE, clientes.C_CONTATO_CLIENTE, clientes.C_DATA_NASCIMENTO, clientes.C_CNPJCPF, clientes.C_RGINSCRICAO_ESTADUAL, clientes.C_EMAIL_CLIENTE, clientes.C_ENVIADO, clientes.C_CHAVE_DO_CLIENTE}, clientes.C_NOME_DO_CLIENTE + "   like  '%" + valor_campo + "%'", null, null, null, null, null);
                        break;
                    case NOME_FANTASIA:
                        cursor = db.query("CLIENTES", new String[]{clientes.C_CODIGO_CLIENTE + " as _id", clientes.C_NOME_DO_CLIENTE, clientes.C_NOME_FANTASIA, clientes.C_ENDERECO_CLIENTE, clientes.C_BAIRRO_CLIENTE, clientes.C_CEP_CLIENTE, clientes.C_CIDADE_CLIENTE, clientes.C_CONTATO_CLIENTE, clientes.C_DATA_NASCIMENTO, clientes.C_CNPJCPF, clientes.C_RGINSCRICAO_ESTADUAL, clientes.C_EMAIL_CLIENTE, clientes.C_ENVIADO, clientes.C_CHAVE_DO_CLIENTE}, clientes.C_NOME_FANTASIA + "   like  '%" + valor_campo + "%'", null, null, null, null, null);
                        break;
                    case NOME_CIDADE:
                        cursor = db.query("CLIENTES", new String[]{clientes.C_CODIGO_CLIENTE + " as _id", clientes.C_NOME_DO_CLIENTE, clientes.C_NOME_FANTASIA, clientes.C_ENDERECO_CLIENTE, clientes.C_BAIRRO_CLIENTE, clientes.C_CEP_CLIENTE, clientes.C_CIDADE_CLIENTE, clientes.C_CONTATO_CLIENTE, clientes.C_DATA_NASCIMENTO, clientes.C_CNPJCPF, clientes.C_RGINSCRICAO_ESTADUAL, clientes.C_EMAIL_CLIENTE, clientes.C_ENVIADO, clientes.C_CHAVE_DO_CLIENTE}, clientes.C_CIDADE_CLIENTE + "   like  '%" + valor_campo + "%'", null, null, null, null, null);
                        break;
                    case NOME_BAIRRO:
                        cursor = db.query("CLIENTES", new String[]{clientes.C_CODIGO_CLIENTE + " as _id", clientes.C_NOME_DO_CLIENTE, clientes.C_NOME_FANTASIA, clientes.C_ENDERECO_CLIENTE, clientes.C_BAIRRO_CLIENTE, clientes.C_CEP_CLIENTE, clientes.C_CIDADE_CLIENTE, clientes.C_CONTATO_CLIENTE, clientes.C_DATA_NASCIMENTO, clientes.C_CNPJCPF, clientes.C_RGINSCRICAO_ESTADUAL, clientes.C_EMAIL_CLIENTE, clientes.C_ENVIADO, clientes.C_CHAVE_DO_CLIENTE}, clientes.C_BAIRRO_CLIENTE + "   like  '%" + valor_campo + "%'", null, null, null, null, null);
                        break;
                }
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_cliente_na_pesquisa_edittext" + e.getMessage());
        }
        return cursor;
    }
}





















