package org.disruptor.sample.trapos.disruptor;


import org.disruptor.sample.trapos.model.Rate;
import org.disruptor.sample.trapos.translators.RateTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

/**
 * EventHandler that converts raw rate messages into {@link Rate} objects.
 * 
 * @author ewhite
 */
public class MarketRateEventHandler implements EventHandler<MarketEvent> {
	
    private static final Logger logger = LoggerFactory.getLogger(MarketRateEventHandler.class.getName());
    
    private RateTranslator translator;
    
    public MarketRateEventHandler(RateTranslator translator) {
        this.translator = translator;
    }
    
    public MarketRateEventHandler() {
        this(new RateTranslator());
    }

    @Override
    public void onEvent(MarketEvent marketEvent, long sequence, boolean endOfBatch) throws Exception {
        String delmitedRate = marketEvent.getMessage();
        
        if(!translator.canHandle(delmitedRate))
            return;
        
        Rate rate = translator.translate(delmitedRate);

        marketEvent.accept(rate);
        
        if(logger.isInfoEnabled())
            logger.info("onEvent: seq:"+sequence + "/" + endOfBatch + " event: "+ marketEvent);
    }
}
