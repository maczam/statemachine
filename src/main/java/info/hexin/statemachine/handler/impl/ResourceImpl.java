package info.hexin.statemachine.handler.impl;

import info.hexin.statemachine.event.Event;
import info.hexin.statemachine.event.RecourceEvent;
import info.hexin.statemachine.event.ResourceEventType;
import info.hexin.statemachine.event.ResourceState;
import info.hexin.statemachine.factory.SingleArcTransition;
import info.hexin.statemachine.factory.StateMachineFactory;
import info.hexin.statemachine.handler.Handler;
import info.hexin.statemachine.transition.StateMachine;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 资源状态机
 * 
 * @author hexin
 * 
 */
public class ResourceImpl implements Handler {

	private static final Logger LOG = LoggerFactory
			.getLogger(ResourceImpl.class);

	private StateMachineFactory<ResourceState, ResourceEventType, RecourceEvent> stateFactory = new StateMachineFactory<ResourceState, ResourceEventType, RecourceEvent>(
			ResourceState.NEW)
			.addTransition(ResourceState.NEW, ResourceState.POST,
					ResourceEventType.ACCEPT, new InitResourceTransition())
			.addTransition(ResourceState.POST, ResourceState.POST,
					ResourceEventType.UPDATE, new UpdateResourceTransition())
			.addTransition(ResourceState.POST, ResourceState.PROCESS,
					ResourceEventType.RUNING, new RunningResourceTransition())
			.addTransition(ResourceState.PROCESS, ResourceState.PROCESS,
					ResourceEventType.UPDATE, new UpdateResourceTransition())

			//
			.addTransition(EnumSet.of(ResourceState.NEW, ResourceState.POST, ResourceState.PROCESS,ResourceState.DONE),
					ResourceState.DONE, ResourceEventType.COMPLETE,
					new CompleteResourceTransition());

	private StateMachine<ResourceState, ResourceEventType, RecourceEvent> stateMachine = stateFactory
			.make();

	@Override
	public void handler(Event event) {
		ResourceState oldState = stateMachine.getCurrentState();
		RecourceEvent recourceEvent = (RecourceEvent) event;
		try {
			stateMachine.doTransition(recourceEvent.getType(), recourceEvent);
			LOG.info("  状态改变   " + oldState.toString()
					+ "  ->  " + stateMachine.getCurrentState().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class InitResourceTransition implements
			SingleArcTransition<RecourceEvent> {
		@Override
		public void transition(RecourceEvent event) {
			LOG.debug(" 进入 InitResourceTransition 中。。。。");
		}
	}

	private class RunningResourceTransition implements
			SingleArcTransition<RecourceEvent> {
		@Override
		public void transition(RecourceEvent event) {
			LOG.debug(" 进入 RunningResourceTransition 中。。。。");
		}
	}

	private class UpdateResourceTransition implements
			SingleArcTransition<RecourceEvent> {

		@Override
		public void transition(RecourceEvent event) {
			LOG.debug(" 进入 UpdateResourceTransition 中。。。。");

		}
	}

	public class CompleteResourceTransition implements
			SingleArcTransition<RecourceEvent> {

		@Override
		public void transition(RecourceEvent event) {
			LOG.debug(" 进入 CompleteResourceTransition 中。。。。");

		}

	}
}
