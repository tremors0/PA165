package cz.muni.fi.pa165.currency;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainJavaConfig {

    public static void main(String[] args) {
        Currency CZK = Currency.getInstance("CZK");
        Currency EUR = Currency.getInstance("EUR");

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("cz.muni.fi.pa165.currency");
        CurrencyConvertor currencyConvertor = applicationContext.getBean("currencyConvertor", CurrencyConvertor.class);
        System.out.println(currencyConvertor.convert(EUR, CZK, BigDecimal.ONE));
    }

}
