package org.disruptor.sample.trapos.disruptor;

import org.disruptor.sample.trapos.disruptor.MarketEvent;
import org.disruptor.sample.trapos.disruptor.MarketRateEventHandler;
import org.disruptor.sample.trapos.model.Rate;
import org.disruptor.sample.trapos.translators.RateTranslator;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class MarketRateEventHandlerTest {
    
    Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    @Test
    public void consumesRateEvent() throws Exception {
        
        final String delimitedRate = "R|GBPUSD|1.6324";
        final RateTranslator rateTranslator = context.mock(RateTranslator.class);
        final MarketEvent marketEvent = context.mock(MarketEvent.class);
        final Rate rate = context.mock(Rate.class);
        
        final MarketRateEventHandler h = new MarketRateEventHandler(rateTranslator);
        context.checking(new Expectations(){{
            oneOf(marketEvent).getMessage();
            will(returnValue(delimitedRate));
            
            oneOf(rateTranslator).canHandle(delimitedRate);
            will(returnValue(true));
            oneOf(rateTranslator).translate(delimitedRate);
            will(returnValue(rate));

            oneOf(marketEvent).accept(with(any(Rate.class)));
        }});
        
        h.onEvent(marketEvent, 1, true);
    }
    
    @Test
    public void shouldNotConsumeTradeEvents() throws Exception {
        final String delimitedTrade = "T|S|2.3m|R|GBPUSD|1.6324";
        
        final RateTranslator rateTranslator = context.mock(RateTranslator.class);
        final MarketEvent marketEvent = context.mock(MarketEvent.class);
        
        final MarketRateEventHandler h = new MarketRateEventHandler(rateTranslator);
        context.checking(new Expectations(){{
            oneOf(marketEvent).getMessage();
            will(returnValue(delimitedTrade));
            
            oneOf(rateTranslator).canHandle(delimitedTrade);
            will(returnValue(false));
        }});
        
        h.onEvent(marketEvent, 1, true);
    }    
}
