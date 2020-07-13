package br.com.pauloceami.cursoappvendas.versaofinal.Pagamento;

import android.content.Context;
import android.util.Log;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.pauloceami.cursoappvendas.versaofinal.Interfaces.iPagamento;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaCBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;


public class Quinzenal implements iPagamento {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public void gerar_parcela(SqliteConfPagamentoBean pagamento, SqliteVendaCBean vendaCBean, Context ctx) {

        Calendar calendar_default = Calendar.getInstance(new Locale("pt", "BR"));
        calendar_default.set(Calendar.YEAR, 2000);
        calendar_default.set(Calendar.MONTH, Calendar.JANUARY);
        calendar_default.set(Calendar.DAY_OF_MONTH, 01);
        Date data_padrao = calendar_default.getTime();


        double DIVISOR = pagamento.getConf_parcelas();
        double VALOR_PARCELA_DIVIDIDA = vendaCBean.getTotal().doubleValue() / DIVISOR;


        Calendar calendar_1 = Calendar.getInstance(new Locale("pt", "BR"));

        for (int parcela = 1; parcela < pagamento.getConf_parcelas() + 1; parcela++) {

            calendar_1.add(Calendar.DATE, 15);

            if (calendar_1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                calendar_1.add(Calendar.DATE, 2);
            }

            if (calendar_1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                calendar_1.add(Calendar.DATE, 1);
            }

            Date data_de_vencimento = calendar_1.getTime();


            new SqliteConRecDao(ctx).gravar_parcela(
                    new SqliteConRecBean(
                            parcela,
                            vendaCBean.getVendac_cli_codigo(),
                            vendaCBean.getVendac_cli_nome(),
                            vendaCBean.getVendac_chave(),
                            Util.DataHojeSemHorasUSA(),
                            VALOR_PARCELA_DIVIDIDA,
                            0,
                            dateFormat.format(data_de_vencimento),
                            dateFormat.format(data_padrao),
                            "",
                            "N"
                    ));

            Log.i("script", "+++++++++++++++++++++++++++++");
            Log.i("script", "+++++++++++++++++++++++++++++");
            Log.i("script", "Numero da Parcela : " + parcela);
            Log.i("script", "Codigo do cliente : " + vendaCBean.getVendac_cli_codigo());
            Log.i("script", "Nome do cliente : " + vendaCBean.getVendac_cli_nome());
            Log.i("script", "Chave da venda : " + vendaCBean.getVendac_chave());
            Log.i("script", "Data do movimento : " + Util.DataHojeSemHorasUSA());
            Log.i("script", "Valor a receber : " + String.valueOf(VALOR_PARCELA_DIVIDIDA));
            Log.i("script", "Data de vencimento : " + dateFormat.format(data_de_vencimento));
            Log.i("script", "Data do pagamento : " + dateFormat.format(data_padrao));
            Log.i("script", "Valor Pago : " + BigDecimal.ZERO);
            Log.i("script", "Como recebeu : " + "");
            Log.i("script", "Enviado : " + "N");
            Log.i("script", "+++++++ REGISTRO GRAVADO COM SUCESSO ++++++");

        }


        if (pagamento.isComEntrada()) {
            BaixaParcelaEntrada.baixar_parcela(pagamento, vendaCBean, ctx);
        }

    }


}
