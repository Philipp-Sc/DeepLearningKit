package ps.deeplearningkit.core.simulator.debug;

import ps.deeplearningkit.core.simulator.Simulator;
import ps.deeplearningkit.core.simulator.debug.BasicStack;
import ps.deeplearningkit.core.simulator.debug.NeuralStack;

/**
 * Actions of Type <T> can be added until the stack is full.
 * @author Philipp Schlütermann
 *
 * @param <T>
 */
public abstract class StackSimulator<T> implements Simulator {
	
	private NeuralStack<T> stack;
	
	public StackSimulator(int maxSize){
		stack=new BasicStack<>(maxSize);
	}
	public void applyAction(double[] choice) {
		stack.add(toAction(choice));
	}
	public void printState() {
		String result="";
		for(T each:stack.getList()){
			if(each!=null){
				result+=" "+each.toString();
			}else{
				result+=" null";
			}
		}
		System.out.println("stack:"+result);
	}
	public boolean isAbsorbing() {
		return stack.isFull();
	}
	public NeuralStack<T> getStack(){
		return stack;
	}
	
	public abstract double getReward();
	/**
	 * Used to add T (the action) to the stack.
	 * @param choice
	 * @return
	 */
	protected abstract T toAction(double[] choice);
	public abstract double[] getInput(int size);
}
