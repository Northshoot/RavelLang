package org.stanford.ravel.api.platforms.web;

/**
 * Created by lauril on 9/21/16.
 */
import java.io.IOException;

import org.stanford.ravel.api.platforms.web.Google.GoogleFinance;


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


