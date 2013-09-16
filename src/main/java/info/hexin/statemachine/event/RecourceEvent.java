package info.hexin.statemachine.event;

/**
 * 资源事件
 * 
 * @author hexin
 * 
 */
public class RecourceEvent implements Event {
	private ResourceEventType type;

	public RecourceEvent(ResourceEventType type) {
		this.type = type;
	}

	public ResourceEventType getType() {
		return type;
	}

	public void setType(ResourceEventType type) {
		this.type = type;
	}
}
