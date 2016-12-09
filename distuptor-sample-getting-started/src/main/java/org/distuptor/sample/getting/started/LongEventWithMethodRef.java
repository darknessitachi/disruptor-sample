package org.distuptor.sample.getting.started;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 
 * @author Darkness
 * @date 2016年12月9日 下午3:25:35
 * @version V1.0
 */
public class LongEventWithMethodRef {
	
	public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
		System.out.println(event.getValue());
	}

	public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
		event.setValue(buffer.getLong(0));
	}

	public static void main(String[] args) throws Exception {
		// Executor that will be used to construct new threads for consumers
		Executor executor = Executors.newCachedThreadPool();
		// Specify the size of the ring buffer, must be power of 2.
		int bufferSize = 1024;
		// Construct the Disruptor
		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, executor);
		// Connect the handler
		disruptor.handleEventsWith(LongEventWithMethodRef::handleEvent);
		// Start the Disruptor, starts all threads running
		disruptor.start();
		
		// Get the ring buffer from the Disruptor to be used for publishing.
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; true; l++) {
			bb.putLong(0, l);
			ringBuffer.publishEvent(LongEventWithMethodRef::translate, bb);
			Thread.sleep(1000);
		}
	}
	
}
