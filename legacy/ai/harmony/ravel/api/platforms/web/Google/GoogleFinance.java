package ai.harmony.ravel.api.platforms.web.Google;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;

/**
 * Created by lauril on 9/21/16
 */
public class GoogleFinance{
    private Gson gson;
    private Retrofit retrofit;
    private GoogleFinanceAPI API;

    public GoogleFinance() {
         this.gson= new GsonBuilder()
                .create();


        this.retrofit = new Retrofit.Builder()
                .baseUrl(GoogleFinanceAPI.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Create an instance of our API interface.
         this.API = retrofit.create(GoogleFinanceAPI.class);
    }

    public StockObject getStock(String market_name, String compony_name) throws  IOException {
        Call<StockObject> call = API.stockValue(market_name+":"+compony_name);
        return call.execute().body();

    }

    public CurrencyObject currencryExchange(String from, String to) throws  IOException{
        Call<CurrencyObject> call = API.currencryExchange("CURRENCY"+":"+from+to);
        return call.execute().body();

    }

    public interface GoogleFinanceAPI {

        public static final String API_URL = "https://finance.google.com/finance/";

        @GET("info")
        Call<StockObject> stockValue(@Query("q") String query);

        @GET("info")
        Call<CurrencyObject> currencryExchange(@Query("q") String query);

    }
}

