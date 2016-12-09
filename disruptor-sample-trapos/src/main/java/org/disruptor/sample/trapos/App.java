package org.disruptor.sample.trapos;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.disruptor.sample.trapos.disruptor.MarketEvent;
import org.disruptor.sample.trapos.disruptor.MarketEventPublisher;
import org.disruptor.sample.trapos.disruptor.MarketRateEventHandler;
import org.disruptor.sample.trapos.disruptor.MarketTradeEventHandler;
import org.disruptor.sample.trapos.disruptor.PortfolioPositionEventHandler;
import org.disruptor.sample.trapos.disruptor.RingBufferAdapter;
import org.disruptor.sample.trapos.gateway.TextMessageGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Starts the gateway and configures the disruptor to handle messages.
 * 
 * Message can be sent to the gateway using Netcat.
 * 
 * <pre>
 * Examples of sending messages:
 * 
 * cat SAMPLE-DATA.txt | nc localhost 7000
 * echo 'C|STOP' | nc 127.0.0.1 7000
 * </pre>
 * 
 * See: README.md for more details about how to start and interact with with the
 * server.
 */
public class App implements ShutdownListener {
	
    private static final Logger logger = LoggerFactory.getLogger(TextMessageGateway.class.getName());

    /** This is the number of event processors + 1 thread for the gateway */
    private static final int THREAD_POOL_SIZE = 4;

    private static final int RINGBUFFER_SIZE = 16;
    
    /**
     * Thread pool for disruptor threads.
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    Future<?>[] tasks = new Future<?>[THREAD_POOL_SIZE];
    private EventProcessor[] eventProcessors = new EventProcessor[THREAD_POOL_SIZE - 1];

    private CountDownLatch shutdown = new CountDownLatch(1);

    private void run() throws InterruptedException {

        // This is to keep my MBA from catching on fire...
        WaitStrategy waitStrategy = new BlockingWaitStrategy();

        Disruptor<MarketEvent> disruptor = new Disruptor<>(MarketEvent.FACTORY, RINGBUFFER_SIZE, threadPool, getClaimStrategy(), waitStrategy);

        // TT in the README.md
        MarketTradeEventHandler tradeHandler = new MarketTradeEventHandler();
        // RT in the README.md
        MarketRateEventHandler rateHandler = new MarketRateEventHandler();
        
        // PP in the README.md
        PortfolioPositionEventHandler portfolioPositionHandler = new PortfolioPositionEventHandler();
        
        // Add the portfolio position aggregator with a barrier after both
        // processors.
        disruptor.handleEventsWith(tradeHandler, rateHandler).then(portfolioPositionHandler);
        
        RingBuffer<MarketEvent> ringBuffer = disruptor.start();
        
		// Netty Event Publisher
        TextMessageGateway gateway = createGatewayEventPublisher(ringBuffer);

        // The producer can't move past this barrier.
//		ringBuffer.setGatingSequences(tradeProcessor.getSequence(), rateProcessor.getSequence(), portfolioPositionProcessor.getSequence());

        // Start the threads
        tasks[0] = threadPool.submit(gateway);
//        tasks[1] = threadPool.submit(tradeProcessor);
//        tasks[2] = threadPool.submit(rateProcessor);
//        tasks[3] = threadPool.submit(portfolioPositionProcessor);

        shutdown.await();
        logger.info("Shutting down the app.");
    }

    /**
     * G* in the README.md
     * 
     * @param ringBuffer
     * @return
     */
    private TextMessageGateway createGatewayEventPublisher(RingBuffer<MarketEvent> ringBuffer) {
        MarketEventPublisher eventPublisher = new MarketEventPublisher(new RingBufferAdapter<MarketEvent>(ringBuffer));
        TextMessageGateway gateway = new TextMessageGateway(eventPublisher, this);
        return gateway;
    }

    /**
     * The sequence claim strategy for the producer is dependent on the number
     * of threads in the gateway.
     */
    private ProducerType getClaimStrategy() {
        if (TextMessageGateway.PUBLISHING_THREADS == 1)
            return ProducerType.SINGLE;

        return ProducerType.MULTI;
    }

    @Override
    public void notifyShutdown() {
        shutdownDisruptor();
        shutdownThreadPool();

        // This is the final step.
        shutdown.countDown();
    }

    private void shutdownDisruptor() {
        for (EventProcessor p : eventProcessors) {
            p.halt();
        }
        for (Future<?> task : tasks) {
            task.cancel(true);
        }
    }

    private void shutdownThreadPool() {
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore as we are shutting down anyway.
        }
    }
    
    public static void main(String[] args) throws Exception {
        new App().run();
    }
}
