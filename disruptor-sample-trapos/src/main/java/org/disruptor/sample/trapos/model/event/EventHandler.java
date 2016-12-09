package org.disruptor.sample.trapos.model.event;

public interface EventHandler<T extends Event> {
	
	void handle(T event);
}
