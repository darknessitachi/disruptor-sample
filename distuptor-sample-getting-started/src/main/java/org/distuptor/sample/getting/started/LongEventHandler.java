package org.distuptor.sample.getting.started;

import com.lmax.disruptor.EventHandler;

/**
 * 
 * @author Darkness
 * @date 2016年12月9日 下午3:04:05
 * @version V1.0
 */
public class LongEventHandler implements EventHandler<LongEvent> {
	
	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		System.out.println("Event: " + event.getValue());
	}
	
}
