package br.com.pauloceami.cursoappvendas.versaofinal.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAVA on 09/09/2015.
 */
public class SqliteVendaCBean {


    public static String CODIGO_DA_VENDA = "vendac_id";
    public static String CHAVE_DA_VENDA = "vendac_chave";
    public static String DATA_HORA_DA_VENDA = "vendac_datahoravenda";
    public static String PREVISAO_ENTREGA = "vendac_previsaoentrega";
    public static String CODIGO_DO_CLIENTE = "vendac_cli_codigo";
    public static String NOME_DO_CLIENTE = "vendac_cli_nome";
    public static String CODIGO_DO_USUARIO_VENDEDOR = "vendac_usu_codigo";
    public static String NOME_DO_USUARIO_VENDEDOR = "vendac_usu_nome";
    public static String FORMA_DE_PAGAMENTO = "vendac_formapgto";
    public static String VALOR_DA_VENDA = "vendac_valor";
    public static String DESCONTO = "vendac_desconto";
    public static String PESO_TOTAL_DOS_PRODUTOS = "vendac_pesototal";
    public static String VENDA_ENVIADA_SERVIDOR = "vendac_enviada";
    public static String LATITUDE = "vendac_latitude";
    public static String LONGITUDE = "vendac_longitude";

    private Integer vendac_id;
    private String vendac_chave;
    private String vendac_datahoravenda;
    private String vendac_previsaoentrega;
    private Integer vendac_cli_codigo;
    private String vendac_cli_nome;
    private Integer vendac_usu_codigo;
    private String vendac_usu_nome;
    private String vendac_formapgto;
    private BigDecimal vendac_valor;
    private BigDecimal vendac_desconto;
    private BigDecimal vendac_pesototal;
    private String vendac_enviada;
    private double vendac_latitude;
    private double vendac_longitude;

    public List<SqliteVendaDBean> itens_da_venda;

    public SqliteVendaCBean() {
        this.itens_da_venda = new ArrayList<SqliteVendaDBean>();
    }


    public List<SqliteVendaDBean> getItens_da_venda() {
        return itens_da_venda;
    }


    public Integer getVendac_id() {
        return vendac_id;
    }

    public void setVendac_id(Integer vendac_id) {
        this.vendac_id = vendac_id;
    }

    public String getVendac_chave() {
        return vendac_chave;
    }

    public void setVendac_chave(String vendac_chave) {
        this.vendac_chave = vendac_chave;
    }

    public String getVendac_datahoravenda() {
        return vendac_datahoravenda;
    }

    public void setVendac_datahoravenda(String vendac_datahoravenda) {
        this.vendac_datahoravenda = vendac_datahoravenda;
    }

    public String getVendac_previsaoentrega() {
        return vendac_previsaoentrega;
    }

    public void setVendac_previsaoentrega(String vendac_previsaoentrega) {
        this.vendac_previsaoentrega = vendac_previsaoentrega;
    }

    public Integer getVendac_cli_codigo() {
        return vendac_cli_codigo;
    }

    public void setVendac_cli_codigo(Integer vendac_cli_codigo) {
        this.vendac_cli_codigo = vendac_cli_codigo;
    }

    public String getVendac_cli_nome() {
        return vendac_cli_nome;
    }

    public void setVendac_cli_nome(String vendac_cli_nome) {
        this.vendac_cli_nome = vendac_cli_nome;
    }

    public Integer getVendac_usu_codigo() {
        return vendac_usu_codigo;
    }

    public void setVendac_usu_codigo(Integer vendac_usu_codigo) {
        this.vendac_usu_codigo = vendac_usu_codigo;
    }

    public String getVendac_usu_nome() {
        return vendac_usu_nome;
    }

    public void setVendac_usu_nome(String vendac_usu_nome) {
        this.vendac_usu_nome = vendac_usu_nome;
    }

    public String getVendac_formapgto() {
        return vendac_formapgto;
    }

    public void setVendac_formapgto(String vendac_formapgto) {
        this.vendac_formapgto = vendac_formapgto;
    }

    public BigDecimal getVendac_valor() {
        return vendac_valor;
    }

    public void setVendac_valor(BigDecimal vendac_valor) {
        this.vendac_valor = vendac_valor;
    }

    public BigDecimal getVendac_desconto() {
        return vendac_desconto;
    }

    public void setVendac_desconto(BigDecimal vendac_desconto) {
        this.vendac_desconto = vendac_desconto;
    }

    public BigDecimal getVendac_pesototal() {
        return vendac_pesototal;
    }

    public void setVendac_pesototal(BigDecimal vendac_pesototal) {
        this.vendac_pesototal = vendac_pesototal;
    }

    public String getVendac_enviada() {
        return vendac_enviada;
    }

    public void setVendac_enviada(String vendac_enviada) {
        this.vendac_enviada = vendac_enviada;
    }

    public double getVendac_latitude() {
        return vendac_latitude;
    }

    public void setVendac_latitude(double vendac_latitude) {
        this.vendac_latitude = vendac_latitude;
    }

    public double getVendac_longitude() {
        return vendac_longitude;
    }

    public void setVendac_longitude(double vendac_longitude) {
        this.vendac_longitude = vendac_longitude;
    }

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;
        for (SqliteVendaDBean item : itens_da_venda) {
            total = total.add(item.getSubTotal());
        }
        return total;
    }


}
