package org.distuptor.sample.getting.started;

import com.lmax.disruptor.EventFactory;

/**
 * 
 * @author Darkness
 * @date 2016年12月9日 下午3:01:30
 * @version V1.0
 */
public class LongEventFactory implements EventFactory<LongEvent> {
	
	@Override
	public LongEvent newInstance() {
		return new LongEvent();
	}
	
}
