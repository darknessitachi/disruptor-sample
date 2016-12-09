package org.disruptor.sample.trapos.disruptor;

import org.disruptor.sample.trapos.disruptor.MarketEventPublisher.MarketEventTranslator;
import org.disruptor.sample.trapos.gateway.TextMessageSubscriber;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class MarketEventPublisherTest {
 
    Mockery context = new Mockery() {{
      setImposteriser(ClassImposteriser.INSTANCE);  
    }};
    
    @Test
    public void publishEvent() {
        final String delimitedMessage = "T|B|5.1t|R|EURUSD|1.3124";
        final MarketEvent emptyEvent = context.mock(MarketEvent.class);
        @SuppressWarnings("unchecked")
        final RingBufferAdapter<MarketEvent> ringBuffer = (RingBufferAdapter<MarketEvent>) context.mock(RingBufferAdapter.class);

        TextMessageSubscriber publisher = new MarketEventPublisher(ringBuffer);
        context.checking(new Expectations(){{
//        	final long SEQUENCE = 1;
//            
//            oneOf(ringBuffer).next();
//            will(returnValue(SEQUENCE));
//            
//            oneOf(ringBuffer).get(SEQUENCE);
//            will(returnValue(emptyEvent));
//            
//            oneOf(emptyEvent).setMessage(delimitedMessage);
//            
//            oneOf(ringBuffer).publish(SEQUENCE);
            
            oneOf(ringBuffer).publishEvent(new MarketEventTranslator(delimitedMessage));
        }});
        
        publisher.accept(delimitedMessage);
    }
}
