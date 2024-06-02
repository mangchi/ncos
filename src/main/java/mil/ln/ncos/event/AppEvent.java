package mil.ln.ncos.event;

public class AppEvent {
	
	private Object obj;

    public AppEvent(){
		
	}
	public AppEvent(Object obj){
		this.obj = obj;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
