package info.hexin.statemachine.factory;

/**
 * 事物钩子
 * @author hexin
 *
 */
public interface TransitionHook<STATE> {
	STATE doTransition();
}
