package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Db extends SQLiteOpenHelper {


    public static String Dbname = "curso_appvendas.db";
    public static int versao = 4;

    public Db(Context ctx) {
        super(ctx, Dbname, null, versao);
    }


    private static String SQL_CLIENTE = "CREATE TABLE [CLIENTES] (" +
            "cli_codigo INTERGER," +
            "cli_nome VARCHAR DEFAULT 50," +
            "cli_fantasia VARCHAR DEFAULT 50," +
            "cli_endereco VARCHAR DEFAULT 50 ," +
            "cli_bairro CHAR DEFAULT 50," +
            "cli_cep CHAR DEFAULT 50," +
            "cid_nome VARCHAR DEFAULT 30," +
            "cli_contato  VARCHAR DEFAULT 20," +
            "cli_nascimento  VARCHAR DEFAULT 13," +
            "cli_cpfcnpj VARCHAR DEFAULT 25," +
            "cli_rginscricaoest VARCHAR DEFAULT 40," +
            "cli_email VARCHAR DEFAULT 50," +
            "cli_enviado CHAR DEFAULT 1 ," +
            "cli_chave VARCHAR DEFAULT 100 );";


    private static String SQL_PRODUTOS = "CREATE TABLE [PRODUTOS] (" +
            "prd_codigo INTEGER," +
            "prd_EAN13 VARCHAR DEFAULT 15," +
            "prd_descricao VARCHAR DEFAULT 50," +
            "prd_unmedida VARCHAR DEFAULT 10," +
            "prd_custo DECIMAL(10,2)," +
            "prd_quantidade DECIMAL(10,2)," +
            "prd_preco DECIMAL (10,2)," +
            "prd_categoria VARCHAR DEFAULT 30 )";


    private static String SQL_VENDA_C = "CREATE TABLE [VENDAC](" +
            "vendac_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "vendac_chave VARCHAR DEFAULT 70," +
            "vendac_datahoravenda DATETIME," +
            "vendac_previsaoentrega DATE," +
            "vendac_cli_codigo INTEGER," +
            "vendac_cli_nome VARCHAR DEFAULT 50," +
            "vendac_usu_codigo INTEGER," +
            "vendac_usu_nome VARCHAR DEFAULT 50," +
            "vendac_formapgto VARCHAR DEFAULT 50," +
            "vendac_valor DECIMAL(10,2)," +
            "vendac_desconto DECIMAL(10,2), " +
            "vendac_pesototal DECIMAL(10,2)," +
            "vendac_enviada  CHAR DEFAULT 1,  " +
            "vendac_latitude DOUBLE," +
            "vendac_longitude DOUBLE );";

    private static String SQL_VENDA_D = "CREATE TABLE [VENDAD] (" +
            "vendac_chave VARCHAR DEFAULT 70," +
            "vendad_nro_item INTEGER," +
            "vendad_ean VARCHAR DEFAULT 50," +
            "vendad_prd_codigo INTEGER," +
            "vendad_prd_descricao VARCHAR DEFAULT 50," +
            "vendad_quantidade DECIMAL(10,2), " +
            "vendad_preco_venda DECIMAL(10,2)," +
            "vendad_total DECIMAL(10,2))";

        private static String SQL_VENDA_D_TEMP = "CREATE TABLE [VENDAD_TEMP] (" +
            "vendad_eanTEMP VARCHAR DEFAULT 50," +
            "vendad_prd_codigoTEMP INTEGER," +
            "vendad_prd_descricaoTEMP VARCHAR DEFAULT 50," +
            "vendad_quantidadeTEMP DECIMAL(10,2), " +
            "vendad_preco_vendaTEMP DECIMAL(10,2)," +
            "vendad_totalTEMP DECIMAL(10,2))";

    private static String SQL_CHEQUE = "CREATE TABLE [CHEQUES] (" +
            "ch_codigo INTEGER PRIMARY KEY AUTOINCREMENT ," +
            "ch_cli_codigo INTEGER," +
            "ch_numero_cheque VARCHAR DEFAULT 20," +
            "ch_contato VARCHAR DEFAULT 20," +
            "ch_cpf_dono VARCHAR DEFAULT 50," +
            "ch_nome_do_dono VARCHAR DEFAULT 50," +
            "ch_nome_banco VARCHAR DEFAULT 50 ," +
            "ch_vencimento DATE," +
            "ch_valor_cheque DECIMAL(10,2)," +
            "ch_proprio CHAR DEFAULT 1 ," +
            "vendac_chave VARCHAR DEFAULT 70 ," +
            "ch_enviado VARCHAR DEFAULT 1 ," +
            "ch_dataCadastro DATE )  ";


    private static String SQL_RECEBER = "CREATE TABLE [CONREC] (" +
            "rec_codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
            "rec_numparcela INTEGER," +
            "rec_cli_codigo INTEGER," +
            "rec_cli_nome VARCHAR DEFAULT 50 ," +
            "vendac_chave VARCHAR DEFAULT 70 ," +
            "rec_datamovimento DATE ," +
            "rec_valor_receber DECIMAL (10,2)," +
            "rec_valorpago DECIMAL (10,2)," +
            "rec_datavencimento DATE ," +
            "rec_data_que_pagou DATE ," +
            "rec_recebeu_com  VARCHAR DEFAULT 20 ," +
            "rec_enviado CHAR DEFAULT 1)";


    private static String SQL_CONFPAGAMENTO = "CREATE TABLE [CONFPAGAMENTO] (" +
            "conf_codigo INTEGER PRIMARY KEY AUTOINCREMENT ," +
            "conf_sementrada_comentrada CHAR DEFAULT 1," +
            "conf_tipo_pagamento VARCHAR DEFAULT 50," +
            "conf_recebeucom_din_chq_car VARCHAR DEFAULT  20," +
            "conf_valor_recebido DECIMAL(10,2)," +
            "conf_vencimento_gerencianet DATE," + // vencimento da primeira parcela
            "conf_valor_parcela_gerencianet DECIMAL(10,2)," + // valor parcela gerencianet
            "conf_parcelas INTEGER," +
            "vendac_chave VARCHAR DEFAULT 70 ," +
            "conf_enviado CHAR DEFAULT 1 )";

    private static String SQL_HISTORICO_PAGAMENTO = "CREATE TABLE [HISTPAGAMENTO] (" +
            "hist_codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
            "hist_numero_parcela INTEGER," +
            "hist_valor_real_parcela DECIMAL(10,2)," +
            "hist_valor_pago_no_dia DECIMAL(10,2)," +
            "hist_restante_a_pagar DECIMAL(10,2)," +
            "hist_data_do_pagamento DATE," +
            "hist_nome_cliente VARCHAR DEFAULT 30," +
            "hist_como_pagou VARCHAR DEFAULT 20 ," +
            "vendac_chave VARCHAR DEFAULT 70 ," +
            "hist_enviado  CHAR DEFAULT 1)  ";


    private static String SQL_PARAMETROS = "CREATE TABLE [PARAMETROS]  (" +
            "p_usu_codigo INTEGER , " +
            "p_importar_todos_clientes CHAR DEFAULT 1," +
          //  "p_qual_endereco_ip CHAR DEFAULT 1," +
            "p_usuario VARCHAR DEFAULT 20," +
            "p_senha VARCHAR DEFAULT 20," +
            "p_url_base VARCHAR DEFAULT 50," +
           // "p_end_ip_remoto VARCHAR DEFAULT 50," +
            "p_trabalhar_com_estoque_negativo CHAR DEFAULT 1," +
            "p_desconto_do_vendedor INTEGER ) ";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CLIENTE);
        db.execSQL(SQL_PRODUTOS);
        db.execSQL(SQL_VENDA_C);
        db.execSQL(SQL_VENDA_D);
        db.execSQL(SQL_VENDA_D_TEMP);
        db.execSQL(SQL_CHEQUE);
        db.execSQL(SQL_RECEBER);
        db.execSQL(SQL_CONFPAGAMENTO);
        db.execSQL(SQL_HISTORICO_PAGAMENTO);
        db.execSQL(SQL_PARAMETROS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int VersaoAntiga, int VersaoNova) {

      // if(VersaoAntiga == 2 && VersaoNova ==3 ){

            // codigo para atualizar qualquer tabela

      //  }

    }


}
