package cz.muni.fi.pa165.currency;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainXml {
    public static void main(String[] args) {
        Currency CZK = Currency.getInstance("CZK");
        Currency EUR = Currency.getInstance("EUR");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        CurrencyConvertor currencyConvertor = applicationContext.getBean(CurrencyConvertorImpl.class);
        System.out.println(currencyConvertor.convert(EUR, CZK, BigDecimal.ONE));
    }
}
