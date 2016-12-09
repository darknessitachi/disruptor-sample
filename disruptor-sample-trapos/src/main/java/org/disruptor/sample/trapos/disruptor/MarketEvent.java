package org.disruptor.sample.trapos.disruptor;

import org.disruptor.sample.trapos.model.Rate;
import org.disruptor.sample.trapos.model.Trade;

import com.lmax.disruptor.EventFactory;

/**
 * This holds the translated trade or rate events as they are received from the
 * Netty server.
 * 
 * @author ewhite
 */
public class MarketEvent {
	
	public static EventFactory<MarketEvent> FACTORY = new EventFactory<MarketEvent>() {
		@Override
        public MarketEvent newInstance() {
            return new MarketEvent();
        }
    };
    
    /** The delimited message from the gateway. */
    private String delimitedMessage = "";

    /** Will be non null if a trade event was received. */
    private Trade trade;

    /** Will be non null if a rate event was received. */
    private Rate rate;

    /**
     * Returns the delimited string message.
     */
    public String getMessage() {
        return delimitedMessage;
    }

    public void setMessage(String delimitedMessage) {
        this.delimitedMessage = delimitedMessage;
        reset();
    }

    private void reset() {
        this.trade = null;
        this.rate = null;
    }

    public void accept(Trade trade) {
        this.trade = trade;
    }

    public void accept(Rate rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "MarketEvent [delimitedMessage=" + delimitedMessage + ", trade=" + trade + ", rate=" + rate + "]";
    }

    /**
     * @return if there is a trade attached to the event.
     */
    public boolean isTradeEvent() {
        return trade != null;
    }

    /**
     * @return the trade populated on the event. May be null, check with
     *         isTradeEvent.
     */
    public Trade getTrade() {
        return trade;
    }
}
