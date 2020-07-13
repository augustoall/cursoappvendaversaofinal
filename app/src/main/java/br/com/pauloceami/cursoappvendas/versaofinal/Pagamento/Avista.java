package br.com.pauloceami.cursoappvendas.versaofinal.Pagamento;

import android.content.Context;
import android.util.Log;

import java.math.BigDecimal;

import br.com.pauloceami.cursoappvendas.versaofinal.Interfaces.iPagamento;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteParametroDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaCBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;


public class Avista implements iPagamento {


    @Override
    public void gerar_parcela(SqliteConfPagamentoBean pagamento, SqliteVendaCBean vendaCBean, Context ctx) {


        double PERCENTUAL_DESCONTO = new SqliteParametroDao(ctx).busca_parametros().getP_desconto_do_vendedor();
        double VALOR_DESCONTO = PERCENTUAL_DESCONTO * vendaCBean.getTotal().doubleValue() / 100;
        double VALOR_COM_DESCONTO = vendaCBean.getTotal().doubleValue() - VALOR_DESCONTO;

        new SqliteConRecDao(ctx).gravar_parcela(
                new SqliteConRecBean(
                        1,
                        vendaCBean.getVendac_cli_codigo(),
                        vendaCBean.getVendac_cli_nome(),
                        vendaCBean.getVendac_chave(),
                        Util.DataHojeSemHorasUSA(),
                        vendaCBean.getTotal().doubleValue(),
                        VALOR_COM_DESCONTO,
                        Util.DataHojeSemHorasUSA(),
                        Util.DataHojeSemHorasUSA(),
                        pagamento.getConf_recebeucom_din_chq_car(),
                        "N"
                ));


        Log.i("script", "+++++++++++++++++++++++++++++");
        Log.i("script", "+++++++++++++++++++++++++++++");
        Log.i("script", "Numero da Parcela : " + 1);
        Log.i("script", "Codigo do cliente : " + vendaCBean.getVendac_cli_codigo());
        Log.i("script", "Nome do cliente : " + vendaCBean.getVendac_cli_nome());
        Log.i("script", "Chave da venda : " + vendaCBean.getVendac_chave());
        Log.i("script", "Data do movimento : " + Util.DataHojeSemHorasUSA());
        Log.i("script", "Valor a receber : " + vendaCBean.getTotal().setScale(2, BigDecimal.ROUND_UP).toString());
        Log.i("script", "Data de vencimento : " + Util.DataHojeSemHorasUSA());
        Log.i("script", "Data do pagamento : " + Util.DataHojeSemHorasUSA());
        Log.i("script", "Valor Pago : " + String.valueOf(VALOR_COM_DESCONTO));
        Log.i("script", "Como recebeu : " + "Venda Avista recebida em " + pagamento.getConf_recebeucom_din_chq_car() + " com desc de " + PERCENTUAL_DESCONTO + "%");
        Log.i("script", "Enviado : " + "N");
        Log.i("script", "+++++++ REGISTRO GRAVADO COM SUCESSO ++++++");


    }


}
