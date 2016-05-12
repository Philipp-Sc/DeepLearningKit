package ps.deeplearningkit.core.simulator.debug;

import java.util.ArrayList;
import java.util.List;

public class BasicStack<NeuralElement> implements NeuralStack<NeuralElement> {
	
	private ArrayList<NeuralElement> stack;
	private int limit;

	public BasicStack(int maxSize){
		limit=maxSize;
		stack=new ArrayList<>();
	}
	public List<NeuralElement> getList(){
		return stack;
	}
	public void add(NeuralElement obj) {
		stack.add(obj);
		limit--;
	}
	public boolean isFull() {
		return limit<=0;
	}
	public int getSize() {
		return this.stack.size();
	}
	public NeuralStack<NeuralElement> copy() {
		BasicStack<NeuralElement> copy=new BasicStack<>(limit);
		for(NeuralElement each:(ArrayList<NeuralElement>)stack.clone()){
			copy.add(each);
		}
		return copy;
	}

}
