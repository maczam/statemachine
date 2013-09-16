package info.hexin.statemachine;

import info.hexin.statemachine.event.Event;
import info.hexin.statemachine.handler.Handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件分发器
 * 
 * @author hexin
 * 
 */
public class Dispatcher {
    private static Logger LOG = LoggerFactory.getLogger(Dispatcher.class);

    private Map<String, Handler> map = new HashMap<String, Handler>();

    /**
     * 分发事件
     * 
     * @param event
     */
    public void dispatch(Event event) {
        map.get(event.getClass().getName()).handler(event);
    }

    public void register(Class<? extends Event> clazz, Handler handler) {
        LOG.debug(" 为" + clazz.getName() + " 配置handler" + handler.getClass().toString());
        map.put(clazz.getName(), handler);
    }
}
