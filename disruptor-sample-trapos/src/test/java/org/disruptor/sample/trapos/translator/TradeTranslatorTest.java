package org.disruptor.sample.trapos.translator;

import static org.disruptor.sample.trapos.util.CurrencyPairProvider.EURUSD;
import static org.disruptor.sample.trapos.util.CurrencyPairProvider.USDCAD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.disruptor.sample.trapos.model.Amount;
import org.disruptor.sample.trapos.model.Currency;
import org.disruptor.sample.trapos.model.Rate;
import org.disruptor.sample.trapos.model.Trade;
import org.disruptor.sample.trapos.model.TradeType;
import org.disruptor.sample.trapos.translators.TradeTranslator;
import org.disruptor.sample.trapos.translators.TranslateException;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class TradeTranslatorTest {
    
    Mockery context = new Mockery();
    
    /**
     * Given T|B|5.1t|EURUSD|1.3124
     * 
     * The translated trade should be: Buy 5.1 thousand EUR at @ 1.3124 EURUSD. 
     */
    @Test
    public void translatesATradeMessage() throws Exception {
        String delimitedTrade = "T|B|5.1t|R|EURUSD|1.3124";
        
        Amount fivePointOneThousand = new Amount(5.1 * 1000, new Currency("EUR"));
        Rate atEURUSDRate = new Rate(1.3124, EURUSD);
        final Trade expected = new Trade(TradeType.BUY, fivePointOneThousand, atEURUSDRate);
        
        TradeTranslator t = new TradeTranslator();
        Trade trade = t.translate(delimitedTrade);
        assertThat(trade, equalTo(expected));
    }

    @Test
    public void translatesATradeMessageWithNoMultiplier() throws Exception {
        String delimitedTrade = "T|S|5.1|R|EURUSD|1.3124";
        
        Amount fivePointOne = new Amount(5.1, new Currency("EUR"));
        Rate atEURUSDRate = new Rate(1.3124, EURUSD);
        final Trade expected = new Trade(TradeType.SELL, fivePointOne, atEURUSDRate);
        
        TradeTranslator t = new TradeTranslator();
        Trade trade = t.translate(delimitedTrade);
        assertThat(trade, equalTo(expected));
    }
    
    @Test
    public void translatesATradeMessageMillionsMultiplier() throws Exception {
        String delimitedTrade = "T|S|2m|R|USDCAD|1.0012";
        
        Amount twoMillion = new Amount(2000000.0, new Currency("USD"));
        Rate atUSDCADRate = new Rate(1.0012, USDCAD);
        final Trade expected = new Trade(TradeType.SELL, twoMillion, atUSDCADRate);
        
        TradeTranslator t = new TradeTranslator();
        Trade trade = t.translate(delimitedTrade);
        assertThat(trade, equalTo(expected));
    }    
    
    @Test(expected=TranslateException.class)
    public void translateInvalidTradeMessage() throws Exception {
        String invalidTrade = "T||5.1|R|EURUSD|1.3124";

        TradeTranslator t = new TradeTranslator();
        t.translate(invalidTrade);
    }

    @Test
    public void shouldTranslateTradeMessages(){
        String delimitedTrade = "T";
        
        TradeTranslator tradeTranslator = new TradeTranslator();
        assertThat(tradeTranslator.canHandle(delimitedTrade), equalTo(true));
    }
    
    @Test
    public void shouldNotTranslateAnyOtherMessageTypes(){
        String otherMessageType = "C";
        
        TradeTranslator tradeTranslator = new TradeTranslator();
        assertThat(tradeTranslator.canHandle(otherMessageType), equalTo(false));
    }    
}
