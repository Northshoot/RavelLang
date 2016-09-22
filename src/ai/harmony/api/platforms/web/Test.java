package ai.harmony.api.platforms.web;

/**
 * Created by lauril on 9/21/16.
 */
import java.io.IOException;

import ai.harmony.api.platforms.web.Google.GoogleFinance;
import ai.harmony.api.platforms.web.Google.StockObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.tools.javac.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;


public class Test {


    public static void main(String... args) throws IOException {

        GoogleFinance gf = new GoogleFinance();
        System.out.println(gf.getStock("NASDAQ", "GOOG"));
        System.out.println(gf.getStock("NASDAQ", "AAPL"));
        System.out.println(gf.getStock("NASDAQ", "MSFT"));
        System.out.println(gf.getStock("NASDAQ", "INTC"));
        System.out.println(gf.currencryExchange("EUR", "USD"));
        System.out.println(gf.currencryExchange("USD", "EUR"));
        System.out.println(gf.currencryExchange("USD", "SEK"));
        System.out.println(gf.currencryExchange("SEK", "USD"));
        System.out.println(gf.currencryExchange("SEK", "EUR"));
    }
}


