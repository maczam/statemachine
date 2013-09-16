package info.hexin.statemachine.transition;

/**
 * 状态机描述
 * 
 * @author hexin
 * 
 * @param <STATE>
 * @param <EVENTTYPE>
 * @param <EVENT>
 */
public interface StateMachine<STATE, EVENTTYPE, EVENT> {
    /**
     * 返回当前状态，即 没有做事物方法
     * 
     * @return
     */
    public STATE getCurrentState();

    /**
     * 
     * 根据当前的事件类型，事件类和事件内容来和当前的状态，相应的事物类
     * 
     * @param eventType
     * @param event
     * @return
     * @throws Exception
     */
    public STATE doTransition(EVENTTYPE eventType, EVENT event) throws Exception;
}