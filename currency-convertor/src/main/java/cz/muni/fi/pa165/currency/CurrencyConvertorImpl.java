package cz.muni.fi.pa165.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        logger.trace("Method convert was called");
        if (sourceCurrency == null) {
            throw new IllegalArgumentException("Source currency cannot be null!");
        }
        if (targetCurrency == null) {
            throw new IllegalArgumentException("Target currency cannot be null!");
        }
        if (sourceAmount == null) {
            throw new IllegalArgumentException("Source amount cannot be null!");
        }

        BigDecimal rate = null;
        try {
            rate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
        } catch (ExternalServiceFailureException e) {
            logger.error(e.getMessage());
            throw new UnknownExchangeRateException("Exchange rate is unknown!", e);
        }

        if (rate == null) {
            logger.warn("Exchange rate is unknown");
            throw new UnknownExchangeRateException("Exchange rate is unknown!");
        }
        return sourceAmount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
    }

}
