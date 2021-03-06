package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import java.math.BigDecimal;

/**
 * Created by JAVA on 09/09/2015.
 */
public class SqliteVendaDBean {

    public static final String CHAVE_DA_VENDA = "vendac_chave";
    public static final String NUMERO_ITEM = "vendad_nro_item";
    public static final String EAN = "vendad_ean";
    public static final String CODPRODUTO = "vendad_prd_codigo";
    public static final String DESCRICAOPROD = "vendad_prd_descricao";
    public static final String QUANTVENDIDA = "vendad_quantidade";
    public static final String PRECOPRODUTO = "vendad_preco_venda";
    public static final String TOTALPRODUTO = "vendad_total";

    private String vendac_chave;
    private Integer vendad_nro_item ;
    private String vendad_ean ;
    private Integer vendad_prd_codigo ;
    private String vendad_prd_descricao ;
    private BigDecimal vendad_quantidade ;
    private BigDecimal vendad_preco_venda ;
    private BigDecimal vendad_total ;

    public String getVendac_chave() {
        return vendac_chave;
    }

    public void setVendac_chave(String vendac_chave) {
        this.vendac_chave = vendac_chave;
    }

    public Integer getVendad_nro_item() {
        return vendad_nro_item;
    }

    public void setVendad_nro_item(Integer vendad_nro_item) {
        this.vendad_nro_item = vendad_nro_item;
    }

    public String getVendad_ean() {
        return vendad_ean;
    }

    public void setVendad_ean(String vendad_ean) {
        this.vendad_ean = vendad_ean;
    }

    public Integer getVendad_prd_codigo() {
        return vendad_prd_codigo;
    }

    public void setVendad_prd_codigo(Integer vendad_prd_codigo) {
        this.vendad_prd_codigo = vendad_prd_codigo;
    }

    public String getVendad_prd_descricao() {
        return vendad_prd_descricao;
    }

    public void setVendad_prd_descricao(String vendad_prd_descricao) {
        this.vendad_prd_descricao = vendad_prd_descricao;
    }

    public BigDecimal getVendad_quantidade() {
        return vendad_quantidade;
    }

    public void setVendad_quantidade(BigDecimal vendad_quantidade) {
        this.vendad_quantidade = vendad_quantidade;
    }

    public BigDecimal getVendad_preco_venda() {
        return vendad_preco_venda;
    }

    public void setVendad_preco_venda(BigDecimal vendad_preco_venda) {
        this.vendad_preco_venda = vendad_preco_venda;
    }

    public BigDecimal getVendad_total() {
        return vendad_total;
    }

    public void setVendad_total(BigDecimal vendad_total) {
        this.vendad_total = vendad_total;
    }

    public BigDecimal getSubTotal (){
        return  this.vendad_quantidade.multiply(this.vendad_preco_venda);
    }
}
