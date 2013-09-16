package info.hexin.statemachine.factory;

import info.hexin.statemachine.transition.StateMachine;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 状态机工场类，来创建多个状态机。
 * <p>
 * 1. 存在多个当前状态对应同一个事物去处理 <br>
 * 2. 对于增加的状态机来说处理每个事物返回状态只能是唯一确定的<br>
 * 3. STATE必须要是枚举，因为后面使用EnumMap
 * </p>
 * 
 * @author hexin
 * 
 * @param <OPERAND>
 * @param <STATE>
 * @param <EVENTTYPE>
 * @param <EVENT>
 */
public class StateMachineFactory<STATE extends Enum<STATE>, EVENTTYPE, EVENT> {
    protected static final Logger LOG = LoggerFactory.getLogger(StateMachineFactory.class);

    private STATE defaultInitialState;

    private Map<STATE, Map<EVENTTYPE, Transition<STATE, EVENTTYPE, EVENT>>> stateMachineTable;

    /**
     * 初始化状态
     * 
     * @param defaultState
     */
    public StateMachineFactory(STATE defaultState) {
        LOG.info("int StateMachineFactory 1 param that:" + this);
        this.stateMachineTable = null;
        this.defaultInitialState = defaultState;
    }

    // -----------------增加状态机
    public StateMachineFactory<STATE, EVENTTYPE, EVENT> addTransition(STATE preState, STATE postState,
            EVENTTYPE eventType, SingleArcTransition<EVENT> hook) {
        LOG.info(" add  preState :" + preState.toString() + "  postState: " + postState);

        Map<EVENTTYPE, Transition<STATE, EVENTTYPE, EVENT>> map = inspectStateMachineTable(preState);
        map.put(eventType, new SingleInternalArc(postState, hook));
        return this;
    }

    public StateMachineFactory<STATE, EVENTTYPE, EVENT> addTransition(Set<STATE> preStateSet, STATE postState,
            EVENTTYPE eventType, SingleArcTransition<EVENT> hook) {

        for (STATE preState : preStateSet) {
            LOG.info(" add  preState :" + preState.toString() + "  postState: " + postState);
            this.addTransition(preState, postState, eventType, hook);
        }
        return this;
    }

    /**
     * 检查路由表是否已经初始化
     * 
     * @param preState
     * @return
     */
    private Map<EVENTTYPE, Transition<STATE, EVENTTYPE, EVENT>> inspectStateMachineTable(STATE preState) {
        if (this.stateMachineTable == null) {
            Map<STATE, Map<EVENTTYPE, Transition<STATE, EVENTTYPE, EVENT>>> prototype = new HashMap<STATE, Map<EVENTTYPE, Transition<STATE, EVENTTYPE, EVENT>>>();
            prototype.put(this.defaultInitialState, null);
            stateMachineTable = new EnumMap<STATE, Map<EVENTTYPE, Transition<STATE, EVENTTYPE, EVENT>>>(prototype);
        }

        Map<EVENTTYPE, Transition<STATE, EVENTTYPE, EVENT>> transitionRouteMap = stateMachineTable.get(preState);

        if (transitionRouteMap == null) {
            transitionRouteMap = new HashMap<EVENTTYPE, StateMachineFactory.Transition<STATE, EVENTTYPE, EVENT>>();
            stateMachineTable.put(preState, transitionRouteMap);
        }
        return transitionRouteMap;
    }

    private STATE doTransition(STATE oldState, EVENTTYPE eventType, EVENT event) {
        Map<EVENTTYPE, Transition<STATE, EVENTTYPE, EVENT>> transitionMap = stateMachineTable.get(oldState);
        if (transitionMap != null) {
            Transition<STATE, EVENTTYPE, EVENT> transition = transitionMap.get(eventType);
            if (transition != null) {
                return transition.doTransition(oldState, eventType, event);
            }
        }
        throw new RuntimeException();
    }

    /**
     * 根据出事状态，构造状态机
     * 
     * @param operand
     * @return
     */
    public StateMachine<STATE, EVENTTYPE, EVENT> make() {
        return new StateMachineImpl(defaultInitialState);
    }

    /**
     * 状态机实现， 只保留状态和事物
     * 
     * @author hexin
     * 
     * @param <STATE>
     * @param <EVENTTYPE>
     * @param <EVENT>
     */
    private class StateMachineImpl implements StateMachine<STATE, EVENTTYPE, EVENT> {

        private STATE currentState;

        public StateMachineImpl(STATE initialState) {
            this.currentState = initialState;
            LOG.debug(" 初始化StateMachineImpl  currentState : " + currentState);
        }

        /**
         * 修改当前的状态
         */
        @Override
        public STATE doTransition(EVENTTYPE eventType, EVENT event) throws Exception {
            LOG.info("StateMachineFactory.this" + StateMachineFactory.this);
            currentState = StateMachineFactory.this.doTransition(currentState, eventType, event);
            return currentState;
        }

        @Override
        public STATE getCurrentState() {
            return currentState;
        }
    }

    /**
     * 
     * 
     * @param <OPERAND>
     * @param <EVENT>
     * @param <STATE>
     * @param <EVENTTYPE>
     */
    private class SingleInternalArc implements Transition<STATE, EVENTTYPE, EVENT> {
        private STATE postState;
        private SingleArcTransition<EVENT> hook; // transition hook

        SingleInternalArc(STATE postState, SingleArcTransition<EVENT> hook) {
            this.postState = postState;
            this.hook = hook;
        }

        @Override
        public STATE doTransition(STATE oldState, EVENTTYPE eventType, EVENT event) {
            if (hook != null) {
                hook.transition(event);
            }
            return postState;
        }
    }

    /**
     * 事物
     * 
     * @param <STATE>
     * @param <EVENTTYPE>
     */
    // TODO 需要使用一种算法去掉这个接口让程序更加通俗
    interface Transition<STATE, EVENT, EVENTTYPE> {

        STATE doTransition(STATE oldState, EVENT event, EVENTTYPE eventType);
    }
}
