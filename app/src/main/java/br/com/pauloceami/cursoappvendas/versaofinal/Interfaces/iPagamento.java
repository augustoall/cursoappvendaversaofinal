package br.com.pauloceami.cursoappvendas.versaofinal.Interfaces;


import android.content.Context;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteVendaCBean;

public interface iPagamento {
    void gerar_parcela(SqliteConfPagamentoBean pagamento, SqliteVendaCBean vendaCBean, Context ctx);
}
