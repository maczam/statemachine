package info.hexin.statemachine.factory;

/**
 * 对外提供的接口，主要负责处理事件对应的业务。开发者不用去考虑当前状态和返回状态
 * 
 * @author hexin
 * 
 * @param <OPERAND>
 * @param <EVENT>
 */
public interface SingleArcTransition<EVENT> {
	/**
	 * 
	 * @param operand
	 * @param event
	 */
	public void transition(EVENT event);

}
