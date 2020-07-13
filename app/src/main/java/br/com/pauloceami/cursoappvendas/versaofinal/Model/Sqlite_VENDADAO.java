package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;


public class Sqlite_VENDADAO {


    private Context ctx;
    private String sql;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;


    public Sqlite_VENDADAO(Context ctx) {
        this.ctx = ctx;
    }

    public Long grava_venda(SqliteVendaCBean venda, List<SqliteVendaD_TempBean> itens_temp) {


        int numero_item = 1;
        for (SqliteVendaD_TempBean item_transf : itens_temp) {
            SqliteVendaDBean vendaDBean = new SqliteVendaDBean();
            vendaDBean.setVendac_chave(venda.getVendac_chave());
            vendaDBean.setVendad_nro_item(numero_item);
            vendaDBean.setVendad_ean(item_transf.getVendad_eanTEMP());
            vendaDBean.setVendad_prd_codigo(item_transf.getVendad_prd_codigoTEMP());
            vendaDBean.setVendad_prd_descricao(item_transf.getVendad_prd_descricaoTEMP());
            vendaDBean.setVendad_quantidade(item_transf.getVendad_quantidadeTEMP());
            vendaDBean.setVendad_preco_venda(item_transf.getVendad_preco_vendaTEMP());
            vendaDBean.setVendad_total(item_transf.getSubTotal());
            venda.getItens_da_venda().add(vendaDBean);
            new SqliteVendaD_TempDao(ctx).excluir_um_item_da_venda(item_transf);
            numero_item++;
        }

        // aqui comeca a gravacao das 2 tabelas vendac e vendad

        long id_venda = -1;
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        db.beginTransaction();

        try {

            ContentValues vendaC = new ContentValues();
            vendaC.put(venda.CHAVE_DA_VENDA, venda.getVendac_chave());
            vendaC.put(venda.DATA_HORA_DA_VENDA, venda.getVendac_datahoravenda());
            vendaC.put(venda.PREVISAO_ENTREGA, venda.getVendac_previsaoentrega());
            vendaC.put(venda.CODIGO_DO_CLIENTE, venda.getVendac_cli_codigo());
            vendaC.put(venda.NOME_DO_CLIENTE, venda.getVendac_cli_nome());
            vendaC.put(venda.CODIGO_DO_USUARIO_VENDEDOR, venda.getVendac_usu_codigo());
            vendaC.put(venda.NOME_DO_USUARIO_VENDEDOR, venda.getVendac_usu_nome());
            vendaC.put(venda.FORMA_DE_PAGAMENTO, venda.getVendac_formapgto());
            vendaC.put(venda.VALOR_DA_VENDA, venda.getTotal().setScale(2,BigDecimal.ROUND_DOWN).toString());
            vendaC.put(venda.DESCONTO, venda.getVendac_desconto().toString());
            vendaC.put(venda.PESO_TOTAL_DOS_PRODUTOS, venda.getVendac_pesototal().toString());
            vendaC.put(venda.VENDA_ENVIADA_SERVIDOR, venda.getVendac_enviada());
            vendaC.put(venda.LATITUDE, venda.getVendac_latitude());
            vendaC.put(venda.LONGITUDE, venda.getVendac_longitude());

            id_venda = db.insert("VENDAC", null, vendaC);

            if (id_venda != -1) {
                boolean Erro = false;
                for (int i = 0; i < venda.getItens_da_venda().size(); i++) {
                    SqliteVendaDBean vendaD_item = (SqliteVendaDBean) venda.getItens_da_venda().get(i);
                    ContentValues vendaD = new ContentValues();
                    vendaD.put(vendaD_item.CHAVE_DA_VENDA, vendaD_item.getVendac_chave());
                    vendaD.put(vendaD_item.NUMERO_ITEM, vendaD_item.getVendad_nro_item());
                    vendaD.put(vendaD_item.EAN, vendaD_item.getVendad_ean());
                    vendaD.put(vendaD_item.CODPRODUTO, vendaD_item.getVendad_prd_codigo());
                    vendaD.put(vendaD_item.DESCRICAOPROD, vendaD_item.getVendad_prd_descricao());
                    vendaD.put(vendaD_item.QUANTVENDIDA, vendaD_item.getVendad_quantidade().toString());
                    vendaD.put(vendaD_item.PRECOPRODUTO, vendaD_item.getVendad_preco_venda().setScale(2,BigDecimal.ROUND_UP).doubleValue());
                    vendaD.put(vendaD_item.TOTALPRODUTO, vendaD_item.getSubTotal().setScale(2,BigDecimal.ROUND_UP).doubleValue());

                    if (db.insert("VENDAD", null, vendaD) == -1) {
                        Erro = true;
                        break;
                    }
                }

                if (!Erro) {
                    db.setTransactionSuccessful();
                }
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException grava_venda" + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }


        return id_venda;
    }



    public List<SqliteVendaCBean> lista_pedidos_do_cliente(Integer cli_codigo) {
        List<SqliteVendaCBean> lista_registros_vendaC = new ArrayList<SqliteVendaCBean>();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("select * from VENDAC where vendac_cli_codigo = ? order by vendac_datahoravenda desc ", new String[]{cli_codigo.toString()});
            while (cursor.moveToNext()) {

                SqliteVendaCBean vendac = new SqliteVendaCBean();
                vendac.setVendac_chave(cursor.getString(cursor.getColumnIndex(vendac.CHAVE_DA_VENDA)));
                vendac.setVendac_datahoravenda(cursor.getString(cursor.getColumnIndex(vendac.DATA_HORA_DA_VENDA)));
                vendac.setVendac_previsaoentrega(cursor.getString(cursor.getColumnIndex(vendac.PREVISAO_ENTREGA)));
                vendac.setVendac_cli_codigo(cursor.getInt(cursor.getColumnIndex(vendac.CODIGO_DO_CLIENTE)));
                vendac.setVendac_cli_nome(cursor.getString(cursor.getColumnIndex(vendac.NOME_DO_CLIENTE)));
                vendac.setVendac_usu_codigo(cursor.getInt(cursor.getColumnIndex(vendac.CODIGO_DO_USUARIO_VENDEDOR)));
                vendac.setVendac_usu_nome(cursor.getString(cursor.getColumnIndex(vendac.NOME_DO_USUARIO_VENDEDOR)));
                vendac.setVendac_formapgto(cursor.getString(cursor.getColumnIndex(vendac.FORMA_DE_PAGAMENTO)));
                vendac.setVendac_valor(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendac.VALOR_DA_VENDA))).setScale(2,BigDecimal.ROUND_DOWN));
                vendac.setVendac_desconto(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendac.DESCONTO))));
                vendac.setVendac_pesototal(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendac.PESO_TOTAL_DOS_PRODUTOS))));
                vendac.setVendac_enviada(cursor.getString(cursor.getColumnIndex(vendac.VENDA_ENVIADA_SERVIDOR)));
                vendac.setVendac_latitude(cursor.getDouble(cursor.getColumnIndex(vendac.LATITUDE)));
                vendac.setVendac_longitude(cursor.getDouble(cursor.getColumnIndex(vendac.LONGITUDE)));
                lista_registros_vendaC.add(vendac);
            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_vendas_nao_enviadas" + e.getMessage());
        } finally {
            db.close();
        }
        return lista_registros_vendaC;
    }





    public List<SqliteVendaCBean> buscar_vendas_nao_enviadas() {
        List<SqliteVendaCBean> lista_registros_vendaC = new ArrayList<SqliteVendaCBean>();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("select * from VENDAC where vendac_enviada = 'N'", null);
            while (cursor.moveToNext()) {

                SqliteVendaCBean vendac = new SqliteVendaCBean();
                vendac.setVendac_chave(cursor.getString(cursor.getColumnIndex(vendac.CHAVE_DA_VENDA)));
                vendac.setVendac_datahoravenda(cursor.getString(cursor.getColumnIndex(vendac.DATA_HORA_DA_VENDA)));
                vendac.setVendac_previsaoentrega(cursor.getString(cursor.getColumnIndex(vendac.PREVISAO_ENTREGA)));
                vendac.setVendac_cli_codigo(cursor.getInt(cursor.getColumnIndex(vendac.CODIGO_DO_CLIENTE)));
                vendac.setVendac_cli_nome(cursor.getString(cursor.getColumnIndex(vendac.NOME_DO_CLIENTE)));
                vendac.setVendac_usu_codigo(cursor.getInt(cursor.getColumnIndex(vendac.CODIGO_DO_USUARIO_VENDEDOR)));
                vendac.setVendac_usu_nome(cursor.getString(cursor.getColumnIndex(vendac.NOME_DO_USUARIO_VENDEDOR)));
                vendac.setVendac_formapgto(cursor.getString(cursor.getColumnIndex(vendac.FORMA_DE_PAGAMENTO)));
                vendac.setVendac_valor(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendac.VALOR_DA_VENDA))).setScale(2,BigDecimal.ROUND_DOWN));
                vendac.setVendac_desconto(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendac.DESCONTO))));
                vendac.setVendac_pesototal(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendac.PESO_TOTAL_DOS_PRODUTOS))));
                vendac.setVendac_enviada(cursor.getString(cursor.getColumnIndex(vendac.VENDA_ENVIADA_SERVIDOR)));
                vendac.setVendac_latitude(cursor.getDouble(cursor.getColumnIndex(vendac.LATITUDE)));
                vendac.setVendac_longitude(cursor.getDouble(cursor.getColumnIndex(vendac.LONGITUDE)));
                lista_registros_vendaC.add(vendac);


            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_vendas_nao_enviadas" + e.getMessage());
        } finally {
            db.close();
        }
        return lista_registros_vendaC;
    }


    // select em todos os itens das vendas que ainda nao foram exportadas
    public List<SqliteVendaDBean> buscar_itens_das_vendas_nao_enviadas() {
        List<SqliteVendaDBean> lista_registros_vendaD = new ArrayList<SqliteVendaDBean>();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("select * from VENDAD where vendac_chave = (select vendac_chave from VENDAC where  vendac_enviada = 'N')", null);
            while (cursor.moveToNext()) {

                SqliteVendaDBean vendad = new SqliteVendaDBean();

                vendad.setVendac_chave(cursor.getString(cursor.getColumnIndex(vendad.CHAVE_DA_VENDA)));
                vendad.setVendad_nro_item(cursor.getInt(cursor.getColumnIndex(vendad.NUMERO_ITEM)));
                vendad.setVendad_ean(cursor.getString(cursor.getColumnIndex(vendad.EAN)));
                vendad.setVendad_prd_codigo(cursor.getInt(cursor.getColumnIndex(vendad.CODPRODUTO)));
                vendad.setVendad_prd_descricao(cursor.getString(cursor.getColumnIndex(vendad.DESCRICAOPROD)));
                vendad.setVendad_quantidade(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendad.QUANTVENDIDA))));
                vendad.setVendad_preco_venda(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendad.PRECOPRODUTO))));
                vendad.setVendad_total(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(vendad.TOTALPRODUTO))));
                lista_registros_vendaD.add(vendad);


            }
        } catch (SQLiteException e) {
            Util.log("SQLiteException buscar_itens_das_vendas_nao_enviadas" + e.getMessage());
        } finally {
            db.close();
        }
        return lista_registros_vendaD;
    }


    public void atualiza_vendac_enviada(String vendac_chave) {
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "update VENDAC set vendac_enviada = 'S' where vendac_chave = ?   ";

            stmt = db.compileStatement(sql);
            stmt.bindString(1, vendac_chave);
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Util.log("SQLiteException atualiza_cliente" + e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

    public void atualiza_cli_codigo_cliente_offline(int NOVO_CODIGO, int CHAVE_ENVIADA_MYSQL) {
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "update VENDAC set vendac_cli_codigo = (select cli_codigo from clientes where cli_codigo = ? ) where vendac_cli_codigo = ? ";

            stmt = db.compileStatement(sql);
            stmt.bindLong(1,NOVO_CODIGO);
            stmt.bindLong(2,CHAVE_ENVIADA_MYSQL);
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Util.log("SQLiteException atualiza_cli_codigo_cliente_offline" + e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }


}
