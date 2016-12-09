package org.disruptor.sample.trapos.model;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

import org.disruptor.sample.trapos.model.Amount;
import org.disruptor.sample.trapos.model.Rate;

import static org.disruptor.sample.trapos.util.CurrencyPairProvider.EURUSD;
import static org.disruptor.sample.trapos.util.CurrencyProvider.EUR;
import static org.disruptor.sample.trapos.util.CurrencyProvider.USD;
import static org.hamcrest.MatcherAssert.assertThat;

public class RateTest {

    /**
     * Foreign to USD, e.g. EURUSD.
     */
    @Test
    public void shouldConverntToQuoteAmountWhenTheRateIsForeign() {

        final Amount baseAmount = new Amount(2000000, EUR);
        Rate rateEURUSD = new Rate(1.3124, EURUSD);

        final Amount expectedQuoteAmount = new Amount(2624800, USD);
        assertThat(rateEURUSD.convert(baseAmount), equalTo(expectedQuoteAmount));
    }
    
}
