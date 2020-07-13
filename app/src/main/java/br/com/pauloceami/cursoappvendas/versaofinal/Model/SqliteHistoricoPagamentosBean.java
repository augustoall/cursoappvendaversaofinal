package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import java.math.BigDecimal;

public class SqliteHistoricoPagamentosBean {

    private String hist_codigo;
    private Integer hist_numero_parcela;
    private BigDecimal hist_valor_real_parcela;
    private BigDecimal hist_valor_pago_no_dia;
    private BigDecimal hist_restante_a_pagar;
    private String hist_data_do_pagamento;
    private String hist_nome_cliente;
    private String hist_como_pagou;
    private String vendac_chave;
    private String hist_enviado;


    public SqliteHistoricoPagamentosBean() {
    }

    public String getHist_codigo() {
        return hist_codigo;
    }

    public void setHist_codigo(String hist_codigo) {
        this.hist_codigo = hist_codigo;
    }

    public Integer getHist_numero_parcela() {
        return hist_numero_parcela;
    }

    public void setHist_numero_parcela(Integer hist_numero_parcela) {
        this.hist_numero_parcela = hist_numero_parcela;
    }

    public BigDecimal getHist_valor_real_parcela() {
        return hist_valor_real_parcela;
    }

    public void setHist_valor_real_parcela(BigDecimal hist_valor_real_parcela) {
        this.hist_valor_real_parcela = hist_valor_real_parcela;
    }

    public BigDecimal getHist_valor_pago_no_dia() {
        return hist_valor_pago_no_dia;
    }

    public void setHist_valor_pago_no_dia(BigDecimal hist_valor_pago_no_dia) {
        this.hist_valor_pago_no_dia = hist_valor_pago_no_dia;
    }

    public BigDecimal getHist_restante_a_pagar() {
        return hist_restante_a_pagar;
    }

    public void setHist_restante_a_pagar(BigDecimal hist_restante_a_pagar) {
        this.hist_restante_a_pagar = hist_restante_a_pagar;
    }

    public String getHist_data_do_pagamento() {
        return hist_data_do_pagamento;
    }

    public void setHist_data_do_pagamento(String hist_data_do_pagamento) {
        this.hist_data_do_pagamento = hist_data_do_pagamento;
    }

    public String getHist_nome_cliente() {
        return hist_nome_cliente;
    }

    public void setHist_nome_cliente(String hist_nome_cliente) {
        this.hist_nome_cliente = hist_nome_cliente;
    }

    public String getHist_como_pagou() {
        return hist_como_pagou;
    }

    public void setHist_como_pagou(String hist_como_pagou) {
        this.hist_como_pagou = hist_como_pagou;
    }

    public String getVendac_chave() {
        return vendac_chave;
    }

    public void setVendac_chave(String vendac_chave) {
        this.vendac_chave = vendac_chave;
    }

    public String getHist_enviado() {
        return hist_enviado;
    }

    public void setHist_enviado(String hist_enviado) {
        this.hist_enviado = hist_enviado;
    }
}
