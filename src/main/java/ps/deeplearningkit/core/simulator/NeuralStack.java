package ps.deeplearningkit.core.simulator;

import java.util.List;

public interface NeuralStack<NeuralElement> {
	
	public abstract List<NeuralElement> getList();
	public abstract void add(NeuralElement obj);
	public abstract boolean isFull();
	public abstract int getSize();
	public abstract NeuralStack<NeuralElement> copy();

}
