package org.disruptor.sample.trapos.util;

import org.disruptor.sample.trapos.model.Currency;

/**
 * Static currencies instances to be used in test cases.
 * 
 * @author ewhite
 */
public class CurrencyProvider {
    
    public static Currency USD = new Currency("USD");
    public static Currency EUR = new Currency("EUR");
    public static Currency CAD = new Currency("CAD");
    public static Currency JPY = new Currency("JPY");

}
