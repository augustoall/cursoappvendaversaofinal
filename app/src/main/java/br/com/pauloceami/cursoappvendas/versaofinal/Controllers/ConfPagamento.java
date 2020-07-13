package br.com.pauloceami.cursoappvendas.versaofinal.Controllers;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteChequeBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteChequeDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteClienteDao;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoBean;
import br.com.pauloceami.cursoappvendas.versaofinal.Model.SqliteConfPagamentoDao;
import br.com.pauloceami.cursoappvendas.versaofinal.R;
import br.com.pauloceami.cursoappvendas.versaofinal.Util.Util;


public class ConfPagamento extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, Spinner.OnItemSelectedListener {

    private SimpleDateFormat dateFormatterBR;
    private SimpleDateFormat dateFormatterUSA;
    private DatePickerDialog datePicker;
    private RadioGroup conf_rgentrada;
    private RadioGroup conf_rgPagamentos;
    private EditText conf_txtqtdparcelas, conf_txtvalorrecebido;
    private TextView conf_txvvalorvenda, conf_valorparcela, conf_txvlabelvalorrecebido, conf_txvlabelparcelas;
    private Spinner conf_spfpgto;
    private List<String> array_forma_pagamento = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    private String RECEBIMENTO_DIN_CAR_CHQ = "";
    private String COMENTRADA_SEMENTRADA = "";
    private String TIPO_PAGAMENTO = "";
    private Double SUBTOTAL_VENDA;
    private Intent INTENT_SOBTOTAL_VENDA, INTENT_CLI_CODIGO;
    private Integer CLI_CODIGO;
    private RadioButton conf_rbsementrada, conf_rbcomentrada, conf_rbdinheiro, conf_rbcartao, conf_rbcheques;

    // componentes da entidade cheques
    private Button cheque_btngravacheque, cheque_btnfechar, cheque_btninformadatavencimento;
    private Spinner cheque_spterceiro;
    private EditText cheque_txtnomedobanco, cheque_txtcpfdono, cheque_txtnomedono, cheque_txtnumerocheque, cheque_txtcontato, cheque_txtvalorcheque;
    private TextView cheque_txvdatavencimento, cheque_txvcpfdono, cheque_txvcontato, cheque_txvnomedono;
    private String CHEQUE_PROPRIO_TERCEIRO = "";
    private SqliteClienteBean cliente;
    private LayoutInflater inflater;
    private Builder alerta;
    private AlertDialog alertdialog;


    private EditText txt_venc_prim_parcela_gerencianet;
    private EditText txt_numero_parcelas_gerencianet;
    private TextView txv_valor_parcela_gerencianet;

    private boolean ger_data_venc_valida;
    private boolean ger_num_parcela_valida;

    private BigDecimal ger_valor_parcela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conf_pagamento);

        declaraObjetosListeners();

        new SqliteConfPagamentoDao(this).excluir_CONFPAGAMENTO();

        INTENT_SOBTOTAL_VENDA = getIntent();
        SUBTOTAL_VENDA = INTENT_SOBTOTAL_VENDA.getDoubleExtra("SUBTOTAL_VENDA", 0);
        INTENT_CLI_CODIGO = getIntent();
        CLI_CODIGO = INTENT_CLI_CODIGO.getIntExtra("CLI_CODIGO", 0);

        // pegando os dados do cliente para cheque proprio
        if (CLI_CODIGO != null || CLI_CODIGO != 0) {
            cliente = new SqliteClienteBean();
            cliente = new SqliteClienteDao(this).buscar_cliente_pelo_codigo(String.valueOf(CLI_CODIGO));
        }

        conf_txvvalorvenda.setText("Valor Venda : " + new BigDecimal(SUBTOTAL_VENDA.toString()).setScale(2, BigDecimal.ROUND_UP).toString());
        conf_txtvalorrecebido.setText(new BigDecimal(SUBTOTAL_VENDA.toString()).setScale(2, BigDecimal.ROUND_UP).toString());
        array_forma_pagamento.add("AVISTA");
        array_forma_pagamento.add("MENSAL");
        array_forma_pagamento.add("QUINZENAL");
        array_forma_pagamento.add("SEMANAL");
        array_forma_pagamento.add("MENSAL BOLETOS");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array_forma_pagamento);
        conf_spfpgto.setAdapter(arrayAdapter);

    }


    public void salvar_fpgto(View v) {

        if (validar_forma_de_pagamento()) {

            Util.log("TIPO_PAGAMENTO :" + TIPO_PAGAMENTO);
            Util.log("PARCELAS :" + conf_txtqtdparcelas.getText().toString());
            Util.log("COM_ENTRADA :" + COMENTRADA_SEMENTRADA);
            Util.log("VALOR_RECEBIDO :" + conf_txtvalorrecebido.getText().toString());
            Util.log("COMO_RECEBEU :" + RECEBIMENTO_DIN_CAR_CHQ);

            new SqliteConfPagamentoDao(this).gravar_CONFPAGAMENTO(

                    new SqliteConfPagamentoBean(
                            COMENTRADA_SEMENTRADA,
                            TIPO_PAGAMENTO,
                            RECEBIMENTO_DIN_CAR_CHQ,
                            new BigDecimal(conf_txtvalorrecebido.getText().toString().trim()).setScale(2, BigDecimal.ROUND_UP),
                            Integer.parseInt(conf_txtqtdparcelas.getText().toString()),
                            "",
                            "N",
                            "",
                            BigDecimal.ZERO
                    )
            );
            Util.msg_toast_personal(getBaseContext(), "SALVO COM SUCESSO", Util.SUCESSO);
            finish();
        }


    }


    private boolean validar_forma_de_pagamento() {

        boolean fechar = true;

        // venda avista
        if (TIPO_PAGAMENTO.equals("AVISTA")) {
            if (conf_txtvalorrecebido.getText().toString().trim().length() > 0) {
                BigDecimal valor_recebido = new BigDecimal(conf_txtvalorrecebido.getText().toString());
                if (RECEBIMENTO_DIN_CAR_CHQ.equals("DINHEIRO") && valor_recebido.doubleValue() > SUBTOTAL_VENDA) {
                    fechar = false;
                    Util.msg_toast_personal(getBaseContext(), "Valor avista maior que valor da venda", Util.ALERTA);
                } else if (RECEBIMENTO_DIN_CAR_CHQ.equals("DINHEIRO") && valor_recebido.doubleValue() < SUBTOTAL_VENDA) {
                    fechar = false;
                    Util.msg_toast_personal(getBaseContext(), "Valor avista menor que valor da venda", Util.ALERTA);
                }
            } else {
                Util.msg_toast_personal(getBaseContext(), "informe o valor recebido", Util.ALERTA);
            }
        }


        if (TIPO_PAGAMENTO.equals("MENSAL") || TIPO_PAGAMENTO.equals("QUINZENAL") || TIPO_PAGAMENTO.equals("SEMANAL")) {

            // condicao sem entrada
            if (COMENTRADA_SEMENTRADA.equals("N")) {
                if (conf_txtqtdparcelas.getText().toString().trim().length() <= 0) {
                    fechar = false;
                    Util.msg_toast_personal(getBaseContext(), "informe a quantidade de parcelas", Util.ALERTA);
                }
            }

            // condicao com entrada
            if (COMENTRADA_SEMENTRADA.equals("S")) {
                if (conf_txtqtdparcelas.getText().toString().trim().length() <= 0) {
                    fechar = false;
                    Util.msg_toast_personal(getBaseContext(), "informe a quantidade de parcelas", Util.ALERTA);
                } else if (conf_txtvalorrecebido.getText().toString().trim().length() <= 0) {
                    fechar = false;
                    Util.msg_toast_personal(getBaseContext(), "informe o valor a ser recebido", Util.ALERTA);
                } else if (new BigDecimal(conf_txtvalorrecebido.getText().toString()).doubleValue() <= 0) {
                    fechar = false;
                    Util.msg_toast_personal(getBaseContext(), "valor recebido nao pode ser (0) zero ", Util.ALERTA);
                }
            }
        }

        return fechar;
    }


    @Override
    public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {

        TIPO_PAGAMENTO = spinner.getItemAtPosition(position).toString();

        if (TIPO_PAGAMENTO.equals("AVISTA")) {

            conf_txvlabelparcelas.setVisibility(View.GONE);
            conf_txtqtdparcelas.setVisibility(View.GONE);
            conf_rgentrada.setVisibility(View.GONE);

            conf_rgPagamentos.setVisibility(View.VISIBLE);
            conf_txvlabelvalorrecebido.setVisibility(View.VISIBLE);
            conf_txtvalorrecebido.setVisibility(View.VISIBLE);
            conf_txtvalorrecebido.setText(new BigDecimal(SUBTOTAL_VENDA.toString()).setScale(2, BigDecimal.ROUND_UP).toString());
        }


        if (TIPO_PAGAMENTO.equals("MENSAL") || TIPO_PAGAMENTO.equals("QUINZENAL") || TIPO_PAGAMENTO.equals("SEMANAL")) {

            conf_txvlabelparcelas.setVisibility(View.VISIBLE);
            conf_txtqtdparcelas.setVisibility(View.VISIBLE);
            conf_rgentrada.setVisibility(View.VISIBLE);
            conf_txtvalorrecebido.setText("0");
            conf_txtqtdparcelas.setText("1");

            Integer opcao_comentrada_sementrada = conf_rgentrada.getCheckedRadioButtonId();

            if (opcao_comentrada_sementrada == conf_rbsementrada.getId()) {
                RECEBIMENTO_DIN_CAR_CHQ = "";
                conf_rgPagamentos.setVisibility(View.GONE);
                conf_txvlabelvalorrecebido.setVisibility(View.GONE);
                conf_txtvalorrecebido.setVisibility(View.GONE);
                conf_txtvalorrecebido.setText("0");
            }


            if (opcao_comentrada_sementrada == conf_rbcomentrada.getId()) {
                conf_rgPagamentos.setVisibility(View.VISIBLE);
                conf_txvlabelvalorrecebido.setVisibility(View.VISIBLE);
                conf_txtvalorrecebido.setVisibility(View.VISIBLE);
                conf_txtvalorrecebido.setText(new BigDecimal(SUBTOTAL_VENDA.toString()).setScale(2, BigDecimal.ROUND_UP).toString());
            }

        }


        if (TIPO_PAGAMENTO.equals("MENSAL BOLETOS")) {

            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    //.iconRes(R.drawable.gerencianet)
                    //.maxIconSize(250)
                    //.title("BOLETOS GERENCIANET")
                    //.content("INFORME OS DADOS DO CARNÊ")
                    .cancelable(false)
                    .customView(R.layout.gerencianet_carnes_boletos, true)
                    .positiveText("OK")
                    .negativeText("CANCELAR")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            COMENTRADA_SEMENTRADA = "";
                            TIPO_PAGAMENTO = "MENSAL-BOLETOS-CARNES-GERENCIANET";
                            RECEBIMENTO_DIN_CAR_CHQ = "";
                            BigDecimal VALOR_RECEBIDO = BigDecimal.ZERO;
                            int parcela = Integer.parseInt(txt_numero_parcelas_gerencianet.getText().toString());
                            String vencimento = Util.FormataDataAAAAMMDD(txt_venc_prim_parcela_gerencianet.getText().toString());
                            BigDecimal valor_parcela = ger_valor_parcela;


                            new SqliteConfPagamentoDao(getApplicationContext()).gravar_CONFPAGAMENTO(

                                    new SqliteConfPagamentoBean(
                                            COMENTRADA_SEMENTRADA,
                                            TIPO_PAGAMENTO,
                                            RECEBIMENTO_DIN_CAR_CHQ,
                                            VALOR_RECEBIDO,
                                            parcela,
                                            "",
                                            "N",
                                            vencimento,
                                            valor_parcela
                                    )
                            );
                            Util.msg_toast_personal(getBaseContext(), "CONFIGURAÇÕES DE PAGAMENTOS EM BOLETOS SALVA COM SUCESSO", Util.SUCESSO);
                            finish();


                        }
                    })
                    .build();


            final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
            txt_venc_prim_parcela_gerencianet = dialog.getCustomView().findViewById(R.id.txt_venc_prim_parcela_gerencianet);
            txt_numero_parcelas_gerencianet = dialog.getCustomView().findViewById(R.id.txt_numero_parcelas_gerencianet);
            txv_valor_parcela_gerencianet = dialog.getCustomView().findViewById(R.id.txv_valor_parcela_gerencianet);

            txt_venc_prim_parcela_gerencianet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence data_digitada, int i, int i1, int i2) {


                    if (data_digitada.toString().trim().length() == 10) {
                        if (Util.validate_data_BR(data_digitada.toString())) {
                            ger_data_venc_valida = true;
                        } else {
                            ger_data_venc_valida = false;
                        }
                    }

                    if (ger_data_venc_valida && ger_num_parcela_valida) {
                        positiveAction.setEnabled(true);
                    } else {
                        positiveAction.setEnabled(false);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            txt_numero_parcelas_gerencianet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence parcela_digitada, int i, int i1, int i2) {


                    if (parcela_digitada.toString().length() > 0) {

                        String QUANTIDADE_PARCELAS = txt_numero_parcelas_gerencianet.getText().toString();
                        if (Integer.parseInt(QUANTIDADE_PARCELAS) > 0) {
                            ger_num_parcela_valida = true;

                            BigDecimal divisor = new BigDecimal(Integer.parseInt(QUANTIDADE_PARCELAS));
                            BigDecimal valor_venda = new BigDecimal(SUBTOTAL_VENDA.toString());
                            ger_valor_parcela = valor_venda.divide(divisor, 2, RoundingMode.HALF_UP);
                            txv_valor_parcela_gerencianet.setText("Valor parcela : " + ger_valor_parcela.toString());
                        } else {
                            ger_num_parcela_valida = false;

                            BigDecimal divisor = new BigDecimal("1");
                            BigDecimal valor_venda = new BigDecimal(SUBTOTAL_VENDA.toString());
                            ger_valor_parcela = valor_venda.divide(divisor, 2, RoundingMode.HALF_UP);
                            conf_valorparcela.setText("Valor parcela : " + ger_valor_parcela.toString());
                        }

                    } else {
                        ger_num_parcela_valida = false;
                        txv_valor_parcela_gerencianet.setText("Valor parcela : 0.00");
                    }

                    if (ger_data_venc_valida && ger_num_parcela_valida) {
                        positiveAction.setEnabled(true);
                    } else {
                        positiveAction.setEnabled(false);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            dialog.show();
            positiveAction.setEnabled(false);

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.conf_rbsementrada:
                COMENTRADA_SEMENTRADA = "N";
                RECEBIMENTO_DIN_CAR_CHQ = "";

                conf_rgPagamentos.setVisibility(View.GONE);
                conf_txvlabelvalorrecebido.setVisibility(View.GONE);
                conf_txtvalorrecebido.setVisibility(View.GONE);
                conf_txtvalorrecebido.setText("0");

                Util.msg_toast_personal(getBaseContext(), "sem entrada", Util.ALERTA);
                break;

            case R.id.conf_rbcomentrada:

                COMENTRADA_SEMENTRADA = "S";
                conf_txtqtdparcelas.setText("1");
                conf_rgPagamentos.setVisibility(View.VISIBLE);
                conf_txvlabelvalorrecebido.setVisibility(View.VISIBLE);
                conf_txtvalorrecebido.setVisibility(View.VISIBLE);
                conf_txtvalorrecebido.setText(new BigDecimal(SUBTOTAL_VENDA.toString()).setScale(2, BigDecimal.ROUND_UP).toString());

                Integer opcao_cartao_dinheiro_cheque = conf_rgPagamentos.getCheckedRadioButtonId();
                if (opcao_cartao_dinheiro_cheque == conf_rbdinheiro.getId()) {
                    RECEBIMENTO_DIN_CAR_CHQ = "DINHEIRO";
                }

                if (opcao_cartao_dinheiro_cheque == conf_rbcartao.getId()) {
                    RECEBIMENTO_DIN_CAR_CHQ = "CARTAO";
                }

                if (opcao_cartao_dinheiro_cheque == conf_rbcheques.getId()) {
                    RECEBIMENTO_DIN_CAR_CHQ = "CHEQUE";
                }

                Util.msg_toast_personal(getBaseContext(), "com entrada", Util.ALERTA);

                break;

            case R.id.conf_rbdinheiro:
                RECEBIMENTO_DIN_CAR_CHQ = "DINHEIRO";
                conf_txtvalorrecebido.setText(new BigDecimal(SUBTOTAL_VENDA.toString()).setScale(2, BigDecimal.ROUND_UP).toString());
                Util.msg_toast_personal(getBaseContext(), "dinheiro", Util.ALERTA);
                break;

            case R.id.conf_rbcartao:
                RECEBIMENTO_DIN_CAR_CHQ = "CARTAO";
                conf_txtvalorrecebido.setText(new BigDecimal(SUBTOTAL_VENDA.toString()).setScale(2, BigDecimal.ROUND_UP).toString());
                Util.msg_toast_personal(getBaseContext(), "cartao", Util.ALERTA);
                break;

            case R.id.conf_rbcheques:
                receber_com_cheque();
                RECEBIMENTO_DIN_CAR_CHQ = "CHEQUE";
                conf_txtvalorrecebido.setText(new BigDecimal(SUBTOTAL_VENDA.toString()).setScale(2, BigDecimal.ROUND_UP).toString());
                Util.msg_toast_personal(getBaseContext(), "cheques", Util.ALERTA);
                break;
        }


    }

    private void receber_com_cheque() {

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.info_cheques, null);
        alerta = new Builder(this);
        alerta.setCancelable(false);
        alerta.setView(view);
        cheque_txtnomedobanco = (EditText) view.findViewById(R.id.cheque_txtnomedobanco);
        cheque_txtcpfdono = (EditText) view.findViewById(R.id.cheque_txtcpfdono);
        cheque_txtnomedono = (EditText) view.findViewById(R.id.cheque_txtnomedono);
        cheque_txtnumerocheque = (EditText) view.findViewById(R.id.cheque_txtnumerocheque);
        cheque_txtcontato = (EditText) view.findViewById(R.id.cheque_txtcontato);
        cheque_txtvalorcheque = (EditText) view.findViewById(R.id.cheque_txtvalorcheque);
        cheque_txvcpfdono = (TextView) view.findViewById(R.id.cheque_txvcpfdono);
        cheque_txvcontato = (TextView) view.findViewById(R.id.cheque_txvcontato);
        cheque_txvnomedono = (TextView) view.findViewById(R.id.cheque_txvnomedono);
        cheque_txvdatavencimento = (TextView) view.findViewById(R.id.cheque_txvdatavencimento);


        List<String> items_spinner = new ArrayList<>();
        items_spinner.add("CHEQUE PRÓPRIO");
        items_spinner.add("CHEQUE DE TERCEIRO");

        cheque_spterceiro = (Spinner) view.findViewById(R.id.cheque_spterceiro);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_spinner);
        cheque_spterceiro.setAdapter(ad);

        cheque_spterceiro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posicao_selecionada, long id) {

                if (posicao_selecionada == 0) {
                    CHEQUE_PROPRIO_TERCEIRO = "S";
                    cheque_txtcpfdono.setVisibility(View.GONE);
                    cheque_txvcpfdono.setVisibility(View.GONE);
                    cheque_txtnomedono.setVisibility(View.GONE);
                    cheque_txvnomedono.setVisibility(View.GONE);
                    cheque_txtcontato.setVisibility(View.GONE);
                    cheque_txvcontato.setVisibility(View.GONE);

                }

                if (posicao_selecionada == 1) {
                    CHEQUE_PROPRIO_TERCEIRO = "N";
                    cheque_txtcpfdono.setVisibility(View.VISIBLE);
                    cheque_txvcpfdono.setVisibility(View.VISIBLE);
                    cheque_txtnomedono.setVisibility(View.VISIBLE);
                    cheque_txvnomedono.setVisibility(View.VISIBLE);
                    cheque_txtcontato.setVisibility(View.VISIBLE);
                    cheque_txvcontato.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        view.findViewById(R.id.cheque_btngravacheque).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (CHEQUE_PROPRIO_TERCEIRO) {

                    case "S":

                        new SqliteChequeDao(getApplicationContext()).gravar_cheque(
                                new SqliteChequeBean(
                                        CLI_CODIGO,
                                        cheque_txtnumerocheque.getText().toString(),
                                        cliente.getCli_contato(),
                                        cliente.getCli_cpfcnpj(),
                                        cliente.getCli_nome(),
                                        cheque_txtnomedobanco.getText().toString(),
                                        Util.FormataDataAAAAMMDD(cheque_txvdatavencimento.getText().toString()),
                                        new BigDecimal(cheque_txtvalorcheque.getText().toString().trim()).setScale(2, BigDecimal.ROUND_HALF_UP),
                                        CHEQUE_PROPRIO_TERCEIRO,
                                        "",
                                        "N",
                                        Util.DataHojeSemHorasUSA())
                        );
                        limpa_campos();
                        Util.msg_toast_personal(getBaseContext(), "CHEQUE ADICIONADO", Util.SUCESSO);

                        break;

                    case "N":

                        new SqliteChequeDao(getApplicationContext()).gravar_cheque(
                                new SqliteChequeBean(
                                        CLI_CODIGO,
                                        cheque_txtnumerocheque.getText().toString(),
                                        cheque_txtcontato.getText().toString(),
                                        cheque_txtcpfdono.getText().toString(),
                                        cheque_txtnomedono.getText().toString(),
                                        cheque_txtnomedobanco.getText().toString(),
                                        Util.FormataDataAAAAMMDD(cheque_txvdatavencimento.getText().toString()),
                                        new BigDecimal(cheque_txtvalorcheque.getText().toString().trim()).setScale(2, BigDecimal.ROUND_HALF_UP),
                                        CHEQUE_PROPRIO_TERCEIRO,
                                        "",
                                        "N",
                                        Util.DataHojeSemHorasUSA())
                        );
                        limpa_campos();
                        Util.msg_toast_personal(getBaseContext(), "CHEQUE ADICIONADO", Util.SUCESSO);


                        break;

                }


            }
        });

        view.findViewById(R.id.cheque_btnfechar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.dismiss();
            }
        });


        view.findViewById(R.id.cheque_btninformadatavencimento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        alertdialog = alerta.create();
        alertdialog.show();

    }

    private void limpa_campos() {
        cheque_txtnomedobanco.setText("");
        cheque_txtcpfdono.setText("");
        cheque_txtnomedono.setText("");
        cheque_txtnumerocheque.setText("");
        cheque_txtcontato.setText("");
        cheque_txtvalorcheque.setText("");
        cheque_txvdatavencimento.setText("00/00/0000");
        cheque_txtnomedobanco.requestFocus();
    }

    public void calcular_valor_parcela(CharSequence valor_digitado) {
        if (valor_digitado.length() > 0) {
            String QUANTIDADE_PARCELAS = conf_txtqtdparcelas.getText().toString();
            if (Integer.parseInt(QUANTIDADE_PARCELAS) > 0) {
                BigDecimal divisor = new BigDecimal(Integer.parseInt(QUANTIDADE_PARCELAS));
                BigDecimal valor_venda = new BigDecimal(SUBTOTAL_VENDA.toString());
                BigDecimal valor_parcela = valor_venda.divide(divisor, 2, RoundingMode.HALF_UP);
                conf_valorparcela.setText("Valor parcela : " + valor_parcela.toString());
            } else {
                BigDecimal divisor = new BigDecimal("1");
                BigDecimal valor_venda = new BigDecimal(SUBTOTAL_VENDA.toString());
                BigDecimal valor_parcela = valor_venda.divide(divisor, 2, RoundingMode.HALF_UP);
                conf_valorparcela.setText("Valor parcela : " + valor_parcela.toString());
            }

        } else {
            conf_valorparcela.setText("Valor parcela : 0.00");
        }

    }


    private void declaraObjetosListeners() {

        mostraCalendario();

        conf_txtvalorrecebido = (EditText) findViewById(R.id.conf_txtvalorrecebido);
        conf_txvvalorvenda = (TextView) findViewById(R.id.conf_txvvalorvenda);
        conf_txvlabelparcelas = (TextView) findViewById(R.id.conf_txvlabelparcelas);
        conf_txvlabelvalorrecebido = (TextView) findViewById(R.id.conf_txvlabelvalorrecebido);
        conf_valorparcela = (TextView) findViewById(R.id.conf_valorparcela);
        conf_spfpgto = (Spinner) findViewById(R.id.conf_spfpgto);
        conf_rgentrada = (RadioGroup) findViewById(R.id.conf_rgentrada);
        conf_rgPagamentos = (RadioGroup) findViewById(R.id.conf_rgPagamentos);
        conf_txtqtdparcelas = (EditText) findViewById(R.id.conf_txtqtdparcelas);

        conf_spfpgto.setOnItemSelectedListener(this);
        conf_rgentrada.setOnCheckedChangeListener(this);
        conf_rgPagamentos.setOnCheckedChangeListener(this);

        conf_rbcomentrada = (RadioButton) findViewById(R.id.conf_rbcomentrada);
        conf_rbsementrada = (RadioButton) findViewById(R.id.conf_rbsementrada);
        conf_rbdinheiro = (RadioButton) findViewById(R.id.conf_rbdinheiro);
        conf_rbcartao = (RadioButton) findViewById(R.id.conf_rbcartao);
        conf_rbcheques = (RadioButton) findViewById(R.id.conf_rbcheques);

        Integer opcao_cartao_dinheiro_cheque = conf_rgPagamentos.getCheckedRadioButtonId();
        if (opcao_cartao_dinheiro_cheque == conf_rbdinheiro.getId()) {
            RECEBIMENTO_DIN_CAR_CHQ = "DINHEIRO";
            conf_txtqtdparcelas.setText("0");
        }


        Integer opcao_comentrada_sementrada = conf_rgentrada.getCheckedRadioButtonId();
        if (opcao_comentrada_sementrada == conf_rbcomentrada.getId()) {
            COMENTRADA_SEMENTRADA = "S";
        }

        if (opcao_comentrada_sementrada == conf_rbsementrada.getId()) {
            COMENTRADA_SEMENTRADA = "N";
        }

        conf_txtqtdparcelas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence char_digitado, int start, int before, int count) {
                calcular_valor_parcela(char_digitado);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void mostraCalendario() {
        dateFormatterBR = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateFormatterUSA = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                cheque_txvdatavencimento.setText(dateFormatterBR.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new SqliteConfPagamentoDao(this).excluir_CONFPAGAMENTO();
    }
}
