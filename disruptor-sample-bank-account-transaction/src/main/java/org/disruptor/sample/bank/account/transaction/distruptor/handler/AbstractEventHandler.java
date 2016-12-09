package org.disruptor.sample.bank.account.transaction.distruptor.handler;

import org.disruptor.sample.bank.account.transaction.distruptor.event.TransactionEvent;

import com.lmax.disruptor.EventHandler;

/**
 * In a real implementation, we might have all sorts of common functionality in here.
 */
public abstract class AbstractEventHandler implements EventHandler<TransactionEvent> {

    public abstract void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws Exception;
    
}
