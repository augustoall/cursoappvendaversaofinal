package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import java.math.BigDecimal;

public class SqliteChequeBean {

    public static final String CODIGO_DO_CHEQUE = "ch_codigo";
    public static final String CODIGO_DO_CLIENTE = "ch_cli_codigo";
    public static final String NUMERO_DO_CHEQUE = "ch_numero_cheque";
    public static final String CONTATO_DO_CHEQUE = "ch_contato";
    public static final String CPF_DONO_CHEQUE = "ch_cpf_dono";
    public static final String NOME_DONO_CHEQUE = "ch_nome_do_dono";
    public static final String NOME_DO_BANCO = "ch_nome_banco";
    public static final String VENCIMENTO_DO_CHEQUE = "ch_vencimento";
    public static final String VALOR_DO_CHEQUE = "ch_valor_cheque";
    public static final String CHEQUE_PROPRIO = "ch_proprio";
    public static final String CHAVE_DA_VENDA = "vendac_chave";
    public static final String CHEQUE_ENVIADO = "ch_enviado";
    public static final String DATA_CADASTRO_CHEQUE = "ch_dataCadastro";

    private Integer ch_codigo;
    private Integer ch_cli_codigo;
    private String ch_numero_cheque;

    private String ch_contato;
    private String ch_cpf_dono;
    private String ch_nome_do_dono;

    private String ch_nome_banco;
    private String ch_vencimento;
    private BigDecimal ch_valor_cheque;

    private String ch_proprio;
    private String vendac_chave;
    private String ch_enviado;

    private String ch_dataCadastro;

    public SqliteChequeBean() {
    }

    public SqliteChequeBean(Integer ch_cli_codigo, String ch_numero_cheque, String ch_contato, String ch_cpf_dono, String ch_nome_do_dono, String ch_nome_banco, String ch_vencimento, BigDecimal ch_valor_cheque, String ch_proprio, String vendac_chave, String ch_enviado, String ch_dataCadastro) {
        this.ch_cli_codigo = ch_cli_codigo;
        this.ch_numero_cheque = ch_numero_cheque;
        this.ch_contato = ch_contato;
        this.ch_cpf_dono = ch_cpf_dono;
        this.ch_nome_do_dono = ch_nome_do_dono;
        this.ch_nome_banco = ch_nome_banco;
        this.ch_vencimento = ch_vencimento;
        this.ch_valor_cheque = ch_valor_cheque;
        this.ch_proprio = ch_proprio;
        this.vendac_chave = vendac_chave;
        this.ch_enviado = ch_enviado;
        this.ch_dataCadastro = ch_dataCadastro;
    }

    public Integer getCh_codigo() {
        return ch_codigo;
    }

    public void setCh_codigo(Integer ch_codigo) {
        this.ch_codigo = ch_codigo;
    }

    public Integer getCh_cli_codigo() {
        return ch_cli_codigo;
    }

    public void setCh_cli_codigo(Integer ch_cli_codigo) {
        this.ch_cli_codigo = ch_cli_codigo;
    }

    public String getCh_numero_cheque() {
        return ch_numero_cheque;
    }

    public void setCh_numero_cheque(String ch_numero_cheque) {
        this.ch_numero_cheque = ch_numero_cheque;
    }

    public String getCh_contato() {
        return ch_contato;
    }

    public void setCh_contato(String ch_contato) {
        this.ch_contato = ch_contato;
    }

    public String getCh_cpf_dono() {
        return ch_cpf_dono;
    }

    public void setCh_cpf_dono(String ch_cpf_dono) {
        this.ch_cpf_dono = ch_cpf_dono;
    }

    public String getCh_nome_do_dono() {
        return ch_nome_do_dono;
    }

    public void setCh_nome_do_dono(String ch_nome_do_dono) {
        this.ch_nome_do_dono = ch_nome_do_dono;
    }

    public String getCh_nome_banco() {
        return ch_nome_banco;
    }

    public void setCh_nome_banco(String ch_nome_banco) {
        this.ch_nome_banco = ch_nome_banco;
    }

    public String getCh_vencimento() {
        return ch_vencimento;
    }

    public void setCh_vencimento(String ch_vencimento) {
        this.ch_vencimento = ch_vencimento;
    }

    public BigDecimal getCh_valor_cheque() {
        return ch_valor_cheque;
    }

    public void setCh_valor_cheque(BigDecimal ch_valor_cheque) {
        this.ch_valor_cheque = ch_valor_cheque;
    }

    public String getCh_proprio() {
        return ch_proprio;
    }

    public void setCh_proprio(String ch_proprio) {
        this.ch_proprio = ch_proprio;
    }

    public String getVendac_chave() {
        return vendac_chave;
    }

    public void setVendac_chave(String vendac_chave) {
        this.vendac_chave = vendac_chave;
    }

    public String getCh_enviado() {
        return ch_enviado;
    }

    public void setCh_enviado(String ch_enviado) {
        this.ch_enviado = ch_enviado;
    }

    public String getCh_dataCadastro() {
        return ch_dataCadastro;
    }

    public void setCh_dataCadastro(String ch_dataCadastro) {
        this.ch_dataCadastro = ch_dataCadastro;
    }
}
