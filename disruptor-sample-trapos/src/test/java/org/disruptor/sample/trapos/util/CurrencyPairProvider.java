package org.disruptor.sample.trapos.util;

import static org.disruptor.sample.trapos.util.CurrencyProvider.CAD;
import static org.disruptor.sample.trapos.util.CurrencyProvider.EUR;
import static org.disruptor.sample.trapos.util.CurrencyProvider.JPY;
import static org.disruptor.sample.trapos.util.CurrencyProvider.USD;

import org.disruptor.sample.trapos.model.CurrencyPair;

/**
 * Sample currency pairs for test cases.
 * 
 * @author ewhite
 */
public class CurrencyPairProvider {
    public static final CurrencyPair EURUSD = new CurrencyPair(EUR, USD);
    public static final CurrencyPair USDCAD = new CurrencyPair(USD, CAD);
    public static final CurrencyPair USDJPY = new CurrencyPair(USD, JPY);
}
