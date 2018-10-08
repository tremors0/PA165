package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertorImplTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency CZK = Currency.getInstance("CZK");

    @Mock
    private ExchangeRateTable exchangeRateTable;
    private CurrencyConvertor currencyConvertor;

    @Before
    public void setUp() {
        this.currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);
    }

    @Test
    public void testConvert() throws ExternalServiceFailureException {

        // Don't forget to test border values and proper rounding.
        when(exchangeRateTable.getExchangeRate(USD, CZK)).thenReturn(new BigDecimal("22.31"));

        CurrencyConvertor currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);
        assertThat(currencyConvertor.convert(USD, CZK, new BigDecimal("2.00"))).isEqualTo(new BigDecimal("44.62"));

        // border values
        assertThat(currencyConvertor.convert(USD, CZK, new BigDecimal("0.00"))).isEqualTo(new BigDecimal("0.00"));

        // rounding test
        assertThat(currencyConvertor.convert(USD, CZK, new BigDecimal("1.45"))).isEqualTo(new BigDecimal("32.35"));
    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(null, CZK, BigDecimal.ONE))
                .withMessage("Source currency cannot be null!");
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(USD, null, BigDecimal.TEN))
                .withMessage("Target currency cannot be null!");
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(USD, CZK, null))
                .withMessage("Source amount cannot be null!");
    }

    @Test
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(CZK, USD)).thenReturn(null);

        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> currencyConvertor.convert(CZK, USD, BigDecimal.ONE))
                .withMessage("Exchange rate is unknown!");
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        ExternalServiceFailureException externalServiceFailureException = new ExternalServiceFailureException("External error");

        when(exchangeRateTable.getExchangeRate(USD, CZK)).thenThrow(externalServiceFailureException);
        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> currencyConvertor.convert(USD, CZK, BigDecimal.ONE))
                .withMessage("Exchange rate is unknown!")
                .withCause(externalServiceFailureException);
    }

}
