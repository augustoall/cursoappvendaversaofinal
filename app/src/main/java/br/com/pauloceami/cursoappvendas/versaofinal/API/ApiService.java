package br.com.pauloceami.cursoappvendas.versaofinal.API;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST(SqliteConfPagamentoBean.ENDPOINT_CONF_PAGAMENTO)
    Call<RetrofitConfPagamentoRetorno> postConfPagamento(
            @Field(SqliteConfPagamentoBean.CONF_SEMENTADA_COMENTRADA) String CONF_SEMENTADA_COMENTRADA,
            @Field(SqliteConfPagamentoBean.CONF_TIPO_DO_PAGAMENTO) String CONF_TIPO_DO_PAGAMENTO,
            @Field(SqliteConfPagamentoBean.CONF_DINHEIRO_CARTAO_CHEQUE) String CONF_DINHEIRO_CARTAO_CHEQUE,
            @Field(SqliteConfPagamentoBean.CONF_VALOR_RECEBIDO) String CONF_VALOR_RECEBIDO,
            @Field(SqliteConfPagamentoBean.CONF_QUANTIDADE_PARCELAS) String CONF_QUANTIDADE_PARCELAS,
            @Field(SqliteConfPagamentoBean.CONF_VENDAC_CHAVE) String CONF_VENDAC_CHAVE,
            @Field(SqliteConfPagamentoBean.CONF_VENCIMENTO_GERENCIANET) String CONF_VENCIMENTO_GERENCIANET,
            @Field(SqliteConfPagamentoBean.CONF_VALOR_PARCELA_GERENCIANET) String CONF_VALOR_PARCELA_GERENCIANET

    );


}
