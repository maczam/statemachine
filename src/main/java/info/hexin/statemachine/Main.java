package info.hexin.statemachine;

import info.hexin.statemachine.event.RecourceEvent;
import info.hexin.statemachine.event.ResourceEventType;
import info.hexin.statemachine.handler.impl.ResourceImpl;

public class Main {
	public static void main(String[] args) {
		
		Dispatcher dispatcher = new Dispatcher();
		
		ResourceImpl resourceImpl = new ResourceImpl();
		dispatcher.register(RecourceEvent.class, resourceImpl);
		
		RecourceEvent event = new RecourceEvent(ResourceEventType.ACCEPT); // new -> post
		RecourceEvent running = new RecourceEvent(ResourceEventType.RUNING); // post -> process
		RecourceEvent udpate = new RecourceEvent(ResourceEventType.UPDATE);  // xxx -> xxx
		RecourceEvent complete = new RecourceEvent(ResourceEventType.COMPLETE); // xx -> done

		dispatcher.dispatch(event);
		dispatcher.dispatch(running);
		dispatcher.dispatch(complete);
		dispatcher.dispatch(complete);
	}
}
