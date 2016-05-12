package ps.deeplearningkit.core.simulator.debug;

import java.util.List;

public interface NeuralStack<T> {
	
	public abstract List<T> getList();
	public abstract void add(T obj);
	public abstract boolean isFull();
	public abstract int getSize();
	public abstract NeuralStack<T> copy();

}
