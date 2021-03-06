package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import java.math.BigDecimal;

/**
 * Created by JAVA on 07/09/2015.
 */
public class SqliteVendaD_TempBean {


    public static final String TEMP_EAN = "vendad_eanTEMP";
    public static final String TEMP_CODPRODUTO = "vendad_prd_codigoTEMP";
    public static final String TEMP_DESCRICAOPROD = "vendad_prd_descricaoTEMP";
    public static final String TEMP_QUANTVENDIDA = "vendad_quantidadeTEMP";
    public static final String TEMP_PRECOPRODUTO = "vendad_preco_vendaTEMP";
    public static final String TEMP_TOTALPRODUTO = "vendad_totalTEMP";


    private String vendad_eanTEMP;
    private Integer vendad_prd_codigoTEMP;
    private String vendad_prd_descricaoTEMP;
    private BigDecimal vendad_quantidadeTEMP;
    private BigDecimal vendad_preco_vendaTEMP;
    private BigDecimal vendad_totalTEMP;

    public SqliteVendaD_TempBean() {
    }

    public SqliteVendaD_TempBean(String vendad_eanTEMP, Integer vendad_prd_codigoTEMP, String vendad_prd_descricaoTEMP, BigDecimal vendad_quantidadeTEMP, BigDecimal vendad_preco_vendaTEMP, BigDecimal vendad_totalTEMP) {
        this.vendad_eanTEMP = vendad_eanTEMP;
        this.vendad_prd_codigoTEMP = vendad_prd_codigoTEMP;
        this.vendad_prd_descricaoTEMP = vendad_prd_descricaoTEMP;
        this.vendad_quantidadeTEMP = vendad_quantidadeTEMP;
        this.vendad_preco_vendaTEMP = vendad_preco_vendaTEMP;
        this.vendad_totalTEMP = vendad_totalTEMP;
    }

    public String getVendad_eanTEMP() {
        return vendad_eanTEMP;
    }

    public void setVendad_eanTEMP(String vendad_eanTEMP) {
        this.vendad_eanTEMP = vendad_eanTEMP;
    }

    public Integer getVendad_prd_codigoTEMP() {
        return vendad_prd_codigoTEMP;
    }

    public void setVendad_prd_codigoTEMP(Integer vendad_prd_codigoTEMP) {
        this.vendad_prd_codigoTEMP = vendad_prd_codigoTEMP;
    }

    public String getVendad_prd_descricaoTEMP() {
        return vendad_prd_descricaoTEMP;
    }

    public void setVendad_prd_descricaoTEMP(String vendad_prd_descricaoTEMP) {
        this.vendad_prd_descricaoTEMP = vendad_prd_descricaoTEMP;
    }

    public BigDecimal getVendad_quantidadeTEMP() {
        return vendad_quantidadeTEMP;
    }

    public void setVendad_quantidadeTEMP(BigDecimal vendad_quantidadeTEMP) {
        this.vendad_quantidadeTEMP = vendad_quantidadeTEMP;
    }

    public BigDecimal getVendad_preco_vendaTEMP() {
        return vendad_preco_vendaTEMP;
    }

    public void setVendad_preco_vendaTEMP(BigDecimal vendad_preco_vendaTEMP) {
        this.vendad_preco_vendaTEMP = vendad_preco_vendaTEMP;
    }

    public BigDecimal getVendad_totalTEMP() {
        return vendad_totalTEMP;
    }

    public void setVendad_totalTEMP(BigDecimal vendad_totalTEMP) {
        this.vendad_totalTEMP = vendad_totalTEMP;
    }

    public BigDecimal getSubTotal (){
        return  this.vendad_preco_vendaTEMP.multiply(this.vendad_quantidadeTEMP);
    }
}
