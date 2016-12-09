package org.disruptor.sample.trapos.disruptor;

import org.disruptor.sample.trapos.model.PortfolioPosition;
import org.disruptor.sample.trapos.model.Trade;
import org.disruptor.sample.trapos.model.event.DomainEvents;
import org.disruptor.sample.trapos.model.event.PositionChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

/**
 * This event processor holds a position in memory and receives position updates
 * via {@link DomainEvents}.
 * 
 * @author ewhite
 */
public class PortfolioPositionEventHandler implements EventHandler<MarketEvent>,
        org.disruptor.sample.trapos.model.event.EventHandler<PositionChangeEvent> {
	
    private static Logger logger = LoggerFactory.getLogger(PortfolioPositionEventHandler.class.getName());

    /**
     * Cached portfolio position.
     */
    private PortfolioPosition portfolioPosition;

    private long currentSequence;

    public PortfolioPositionEventHandler(PortfolioPosition position) {
        this.portfolioPosition = position;
    }

    public PortfolioPositionEventHandler() {
        this(new PortfolioPosition());
    }

    @Override
    public void onEvent(MarketEvent marketEvent, long sequence, boolean endOfBatch) throws Exception {
        if (logger.isInfoEnabled())
            logger.info("onEvent: seq:" + sequence + "/" + endOfBatch + " event: " + marketEvent);

        currentSequence = sequence;
        try {
            DomainEvents.registerFor(PositionChangeEvent.class, this);

            if (!marketEvent.isTradeEvent())
                return;

            Trade t = marketEvent.getTrade();
            portfolioPosition.add(t);

        } finally {
            DomainEvents.unregisterFor(PositionChangeEvent.class);
        }
    }

    /**
     * Log the positions as they are changed, in the real world this might
     * notify something else.
     */
    public void handle(PositionChangeEvent event) {
        logger.info("Position change. seq:" + currentSequence + " pos:" + event.getPosition());
    }
}
