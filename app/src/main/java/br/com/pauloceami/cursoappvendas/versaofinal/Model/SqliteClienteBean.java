package br.com.pauloceami.cursoappvendas.versaofinal.Model;

/**
 * Created by JAVA on 24/08/2015.
 */
public class SqliteClienteBean {

    public static final String C_CODIGO_CLIENTE_CURSOR = "_id";
    public static final String C_CODIGO_CLIENTE = "cli_codigo";
    public static final String C_NOME_DO_CLIENTE = "cli_nome";
    public static final String C_NOME_FANTASIA = "cli_fantasia";
    public static final String C_ENDERECO_CLIENTE = "cli_endereco";
    public static final String C_BAIRRO_CLIENTE = "cli_bairro";
    public static final String C_CEP_CLIENTE = "cli_cep";
    public static final String C_CIDADE_CLIENTE = "cid_nome";
    public static final String C_CONTATO_CLIENTE = "cli_contato";
    public static final String C_DATA_NASCIMENTO = "cli_nascimento";
    public static final String C_CNPJCPF = "cli_cpfcnpj";
    public static final String C_RGINSCRICAO_ESTADUAL = "cli_rginscricaoest";
    public static final String C_EMAIL_CLIENTE = "cli_email";
    public static final String C_ENVIADO = "cli_enviado";
    public static final String C_CHAVE_DO_CLIENTE = "cli_chave";


    private Integer cli_codigo;
    private String cli_nome;
    private String cli_fantasia;
    private String cli_endereco;
    private String cli_bairro;
    private String cli_cep;
    private String cid_nome;
    private String cli_contato;
    private String cli_nascimento;
    private String cli_cpfcnpj;
    private String cli_rginscricaoest;
    private String cli_email;
    private String cli_enviado;
    private String cli_chave;


    public Integer getCli_codigo() {
        return cli_codigo;
    }

    public void setCli_codigo(Integer cli_codigo) {
        this.cli_codigo = cli_codigo;
    }

    public String getCli_nome() {
        return cli_nome;
    }

    public void setCli_nome(String cli_nome) {
        this.cli_nome = cli_nome;
    }

    public String getCli_fantasia() {
        return cli_fantasia;
    }

    public void setCli_fantasia(String cli_fantasia) {
        this.cli_fantasia = cli_fantasia;
    }

    public String getCli_endereco() {
        return cli_endereco;
    }

    public void setCli_endereco(String cli_endereco) {
        this.cli_endereco = cli_endereco;
    }

    public String getCli_bairro() {
        return cli_bairro;
    }

    public void setCli_bairro(String cli_bairro) {
        this.cli_bairro = cli_bairro;
    }

    public String getCli_cep() {
        return cli_cep;
    }

    public void setCli_cep(String cli_cep) {
        this.cli_cep = cli_cep;
    }

    public String getCid_nome() {
        return cid_nome;
    }

    public void setCid_nome(String cid_nome) {
        this.cid_nome = cid_nome;
    }

    public String getCli_contato() {
        return cli_contato;
    }

    public void setCli_contato(String cli_contato) {
        this.cli_contato = cli_contato;
    }

    public String getCli_nascimento() {
        return cli_nascimento;
    }

    public void setCli_nascimento(String cli_nascimento) {
        this.cli_nascimento = cli_nascimento;
    }

    public String getCli_cpfcnpj() {
        return cli_cpfcnpj;
    }

    public void setCli_cpfcnpj(String cli_cpfcnpj) {
        this.cli_cpfcnpj = cli_cpfcnpj;
    }

    public String getCli_rginscricaoest() {
        return cli_rginscricaoest;
    }

    public void setCli_rginscricaoest(String cli_rginscricaoest) {
        this.cli_rginscricaoest = cli_rginscricaoest;
    }

    public String getCli_email() {
        return cli_email;
    }

    public void setCli_email(String cli_email) {
        this.cli_email = cli_email;
    }

    public String getCli_enviado() {
        return cli_enviado;
    }

    public void setCli_enviado(String cli_enviado) {
        this.cli_enviado = cli_enviado;
    }

    public String getCli_chave() {
        return cli_chave;
    }

    public void setCli_chave(String cli_chave) {
        this.cli_chave = cli_chave;
    }
}
