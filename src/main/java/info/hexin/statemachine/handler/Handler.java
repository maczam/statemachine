package info.hexin.statemachine.handler;

import info.hexin.statemachine.event.Event;

/**
 * 事件处理器
 * 
 * @author RandySuh
 * 
 */
public interface Handler {
	/** 处理事件接口方法 */
	public void handler(Event es);
}
