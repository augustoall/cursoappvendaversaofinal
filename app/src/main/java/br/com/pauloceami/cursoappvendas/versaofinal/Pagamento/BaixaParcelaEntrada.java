package br.com.pauloceami.cursoappvendas.versaofinal.Pagamento;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConRecDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaCBean;


public class BaixaParcelaEntrada {

    public static void baixar_parcela(SqliteConfPagamentoBean pagamento, SqliteVendaCBean vendaCBean, Context ctx) {

        double DIVISOR = pagamento.getConf_parcelas();
        double VALOR_DA_PARCELA = vendaCBean.getTotal().doubleValue() / DIVISOR;
        double VALOR_DA_ENTRADA = pagamento.getConf_valor_recebido().doubleValue();
        SqliteConRecBean primeiraParcela = new SqliteConRecDao(ctx).busca_menor_parcela(vendaCBean.getVendac_chave());

        Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
        Date data_pagamento = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // VERIFICA VAOR ENTRADA IGUAL VALOR PARCELA
        if (VALOR_DA_ENTRADA == VALOR_DA_PARCELA) {

            new SqliteConRecDao(ctx).baixar_parcela_cliente_integral(new SqliteConRecBean(
                    VALOR_DA_PARCELA,
                    dateFormat.format(data_pagamento),
                    pagamento.getConf_recebeucom_din_chq_car(),
                    "N",
                    primeiraParcela.getVendac_chave(),
                    primeiraParcela.getRec_numparcela()
            ));
        }


        // VERIFICA VALOR ENTRADA MENOR DO QUE O VALOR PARCELA
        if (VALOR_DA_ENTRADA < VALOR_DA_PARCELA) {
            SqliteConRecBean parcela_parcial = new SqliteConRecBean();
            parcela_parcial.setRec_valor_receber(primeiraParcela.getRec_valor_receber() - VALOR_DA_ENTRADA);
            parcela_parcial.setRec_enviado("N");
            parcela_parcial.setVendac_chave(primeiraParcela.getVendac_chave());
            parcela_parcial.setRec_numparcela(primeiraParcela.getRec_numparcela());
            new SqliteConRecDao(ctx).atualiza_valorparcela(parcela_parcial);
        }

        //VERIFICA VALOR ENTRADA MAIOR QUE O VALOR PARCELA
        if (VALOR_DA_ENTRADA > VALOR_DA_PARCELA) {

            List<SqliteConRecBean> lista_de_parcela = new SqliteConRecDao(ctx).busca_parcelas_geradas_na_compra(vendaCBean.getVendac_chave());
            int posicao = 0;

            while (VALOR_DA_ENTRADA >= VALOR_DA_PARCELA) {
                double VALOR_A_RECEBER = lista_de_parcela.get(posicao).getRec_valor_receber();

                if (VALOR_DA_ENTRADA >= VALOR_A_RECEBER) {

                    new SqliteConRecDao(ctx).baixar_parcela_cliente_integral(new SqliteConRecBean(
                            VALOR_DA_PARCELA,
                            dateFormat.format(data_pagamento),
                            pagamento.getConf_recebeucom_din_chq_car(),
                            "N",
                            lista_de_parcela.get(posicao).getVendac_chave(),
                            lista_de_parcela.get(posicao).getRec_numparcela()
                    ));
                    VALOR_DA_ENTRADA = VALOR_DA_ENTRADA- VALOR_A_RECEBER;

                }

                if (VALOR_DA_ENTRADA < VALOR_A_RECEBER) {

                    SqliteConRecBean parcela_menor = new SqliteConRecDao(ctx).busca_menor_parcela(vendaCBean.getVendac_chave());
                    SqliteConRecBean parcela_parcial = new SqliteConRecBean();
                    parcela_parcial.setRec_valor_receber(parcela_menor.getRec_valor_receber()-VALOR_DA_ENTRADA);
                    parcela_parcial.setRec_enviado("N");
                    parcela_parcial.setVendac_chave(parcela_menor.getVendac_chave());
                    parcela_parcial.setRec_numparcela(parcela_menor.getRec_numparcela());
                    new SqliteConRecDao(ctx).atualiza_valorparcela(parcela_parcial);
                    VALOR_DA_ENTRADA = 0;

                }

                posicao++;
            }


        }

    }

}
