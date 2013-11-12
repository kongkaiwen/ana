package kb;

import java.util.ArrayList;

public class CallbackBuffer {
	
	ArrayList<Callback> callbacks;
	
	public CallbackBuffer() {
		callbacks = new ArrayList<Callback>();
	}
	
	public void add( Callback callback ) {
		callbacks.add(callback);
	}
	
	public Callback get() {
		if (this.callbacks.size() == 0)
			return null;
		return this.callbacks.get(0);
	}
}
