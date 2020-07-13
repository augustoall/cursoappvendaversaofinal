package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import java.math.BigDecimal;

/**
 * Created by JAVA on 28/08/2015.
 */
public class SqliteProdutoBean {


    public static final String P_PRODUTO_SIMPLECURSOR = "_id";

    public static final String P_CODIGO_PRODUTO = "prd_codigo";
    public static final String P_CODIGO_BARRAS = "prd_EAN13";
    public static final String P_DESCRICAO_PRODUTO = "prd_descricao";
    public static final String P_UNIDADE_MEDIDA = "prd_unmedida";
    public static final String P_CUSTO_PRODUTO = "prd_custo";
    public static final String P_QUANTIDADE_PRODUTO = "prd_quantidade";
    public static final String P_PRECO_PRODUTO = "prd_preco";
    public static final String P_CATEGORIA_PRODUTO = "prd_categoria";


    private Integer prd_codigo;
    private String prd_EAN13;
    private String prd_descricao;
    private String prd_unmedida;
    private BigDecimal prd_custo;
    private BigDecimal prd_quantidade;
    private BigDecimal prd_preco;
    private String prd_categoria;

    public SqliteProdutoBean() {
    }

    public SqliteProdutoBean(Integer prd_codigo, BigDecimal prd_quantidade) {
        this.prd_codigo = prd_codigo;
        this.prd_quantidade = prd_quantidade;
    }



    public Integer getPrd_codigo() {
        return prd_codigo;
    }

    public void setPrd_codigo(Integer prd_codigo) {
        this.prd_codigo = prd_codigo;
    }

    public String getPrd_EAN13() {
        return prd_EAN13;
    }

    public void setPrd_EAN13(String prd_EAN13) {
        this.prd_EAN13 = prd_EAN13;
    }

    public String getPrd_descricao() {
        return prd_descricao;
    }

    public void setPrd_descricao(String prd_descricao) {
        this.prd_descricao = prd_descricao;
    }

    public String getPrd_unmedida() {
        return prd_unmedida;
    }

    public void setPrd_unmedida(String prd_unmedida) {
        this.prd_unmedida = prd_unmedida;
    }

    public BigDecimal getPrd_custo() {
        return prd_custo;
    }

    public void setPrd_custo(BigDecimal prd_custo) {
        this.prd_custo = prd_custo;
    }

    public BigDecimal getPrd_quantidade() {
        return prd_quantidade;
    }

    public void setPrd_quantidade(BigDecimal prd_quantidade) {
        this.prd_quantidade = prd_quantidade;
    }

    public BigDecimal getPrd_preco() {
        return prd_preco;
    }

    public void setPrd_preco(BigDecimal prd_preco) {
        this.prd_preco = prd_preco;
    }

    public String getPrd_categoria() {
        return prd_categoria;
    }

    public void setPrd_categoria(String prd_categoria) {
        this.prd_categoria = prd_categoria;
    }
}
