package cz.muni.fi.pa165.currency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("cz.muni.fi.pa165")
public class JavaConfig {

    @Bean
    public ExchangeRateTable exchangeRateTable() {
        return new ExchangeRateTableImpl();
    }

    @Bean
    public CurrencyConvertor currencyConvertor() {
        return new CurrencyConvertorImpl(exchangeRateTable());
    }
}
