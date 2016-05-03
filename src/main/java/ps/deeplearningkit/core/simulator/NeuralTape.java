package ps.deeplearningkit.core.simulator;

public interface NeuralTape<NeuralElement> {
	
	public abstract void movePointer(int index);
	public abstract int getPointer();
	public abstract NeuralElement getCurrent();
	public abstract void setCurrent(NeuralElement obj);
	
	public abstract String toString();

}
