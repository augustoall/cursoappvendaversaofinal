package br.com.pauloceami.cursoappvendas.versaofinal.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "http://192.168.1.67/CursoAppVendasSistemaWEB/json/importar/";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient() {
        if (retrofit == null) {

            // com esta implementacao do interceptor voce pode obter uma
            // apuracao de erros melhor no logcat.. portanto a classe
            //HttpLoggingInterceptor foi implementada apos as aulas de retrofit,
            // sao apenas 6 linhas de codigo + a introducao da lib no gradle.
            // USE O INTERCEPTOR EM TODOS OS SEUS MODELOS DE REQUESTS RETROFIT.
            // BONS ESTUDOS
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


}
