package org.disruptor.sample.trapos.disruptor;


import org.disruptor.sample.trapos.model.Trade;
import org.disruptor.sample.trapos.translators.TradeTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

/**
 * EventHandler that converts raw trade messages into {@link Trade} objects.
 * 
 * @author ewhite
 */
public class MarketTradeEventHandler implements EventHandler<MarketEvent> {
	
    private static final Logger logger = LoggerFactory.getLogger(MarketTradeEventHandler.class.getName());
    
    private TradeTranslator translator;
    
    public MarketTradeEventHandler(TradeTranslator translator) {
        this.translator = translator;
    }
    
    public MarketTradeEventHandler() {
        this(new TradeTranslator());
    }

    @Override
    public void onEvent(MarketEvent marketEvent, long sequence, boolean endOfBatch) throws Exception {
        String delmitedTrade = marketEvent.getMessage();
        
        if(!translator.canHandle(delmitedTrade))
            return;
        
        Trade trade = translator.translate(delmitedTrade);

        marketEvent.accept(trade);
        
        if(logger.isInfoEnabled())
            logger.info("onEvent: seq:"+sequence + "/" + endOfBatch + " event: "+ marketEvent);
    }
}
