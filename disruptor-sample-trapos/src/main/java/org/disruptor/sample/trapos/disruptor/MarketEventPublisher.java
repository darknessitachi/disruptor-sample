package org.disruptor.sample.trapos.disruptor;

import org.disruptor.sample.trapos.gateway.TextMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventTranslator;

/** 
 * Publishes events from the gateway to the ring buffer.
 * 
 * This is done through an adapter to support testing via JMock
 * 
 * @author ewhite
 */
public class MarketEventPublisher implements TextMessageSubscriber {
	
    private static final Logger logger = LoggerFactory.getLogger(MarketEventPublisher.class.getName());
    
    private RingBufferAdapter<MarketEvent> ringBuffer;
    
    public MarketEventPublisher(RingBufferAdapter<MarketEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void accept(String delimitedMessage) {
//        if(logger.isInfoEnabled())
//            logger.info("publishEvent: seq:"+sequence+" event:"+ event);
//        
        ringBuffer.publishEvent(new MarketEventTranslator(delimitedMessage));
    }
    
    static class MarketEventTranslator implements EventTranslator<MarketEvent> {
        private String delimitedMessage;
        
        public MarketEventTranslator(String delimitedMessage) {
            this.delimitedMessage = delimitedMessage;
        }

        @Override
		public void translateTo(MarketEvent event, long sequence) {
			event.setMessage(delimitedMessage);
		}
    }
}
