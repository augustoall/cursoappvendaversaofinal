package br.com.pauloceami.cursoappvendas.versaofinal.Model;


public class SqliteParametroBean {

    public static final String P_CODIGO_USUARIO = "p_usu_codigo";
    public static final String P_IMPORTAR_TODOS_CLIENTES = "p_importar_todos_clientes";
    public static final String P_URL_BASE = "p_url_base";
    public static final String P_ESTOQUE_NEGATIVO = "p_trabalhar_com_estoque_negativo";
    public static final String P_DESCONTO_VENDEDOR = "p_desconto_do_vendedor";
    public static final String P_USUARIO = "p_usuario";
    public static final String P_SENHA = "p_senha";


    private Integer p_usu_codigo;
    private String p_importar_todos_clientes;
    private String p_url_base;
    private String p_trabalhar_com_estoque_negativo;
    private Integer p_desconto_do_vendedor;
    private String p_senha;
    private String p_usuario;

    public SqliteParametroBean() {
    }

    public Integer getP_usu_codigo() {
        return p_usu_codigo;
    }

    public void setP_usu_codigo(Integer p_usu_codigo) {
        this.p_usu_codigo = p_usu_codigo;
    }

    public String getP_importar_todos_clientes() {
        return p_importar_todos_clientes;
    }

    public void setP_importar_todos_clientes(String p_importar_todos_clientes) {
        this.p_importar_todos_clientes = p_importar_todos_clientes;
    }

    public String getP_url_base() {
        return p_url_base;
    }

    public void setP_url_base(String p_url_base) {
        this.p_url_base = p_url_base;
    }

    public String getP_trabalhar_com_estoque_negativo() {
        return p_trabalhar_com_estoque_negativo;
    }

    public void setP_trabalhar_com_estoque_negativo(String p_trabalhar_com_estoque_negativo) {
        this.p_trabalhar_com_estoque_negativo = p_trabalhar_com_estoque_negativo;
    }

    public Integer getP_desconto_do_vendedor() {
        return p_desconto_do_vendedor;
    }

    public void setP_desconto_do_vendedor(Integer p_desconto_do_vendedor) {
        this.p_desconto_do_vendedor = p_desconto_do_vendedor;
    }

    public String getP_senha() {
        return p_senha;
    }

    public void setP_senha(String p_senha) {
        this.p_senha = p_senha;
    }

    public String getP_usuario() {
        return p_usuario;
    }

    public void setP_usuario(String p_usuario) {
        this.p_usuario = p_usuario;
    }
}
