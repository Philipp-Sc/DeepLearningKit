package ps.deeplearningkit.core.simulator;

/**
 * Actions of Type <T> can be added until the stack is full.
 * @author Philipp Schlütermann
 *
 * @param <T>
 */
public abstract class StackSimulator<T> implements Simulator{
	
	private NeuralStack<T> stack;
	
	public StackSimulator(int maxSize){
		stack=new BasicStack<>(maxSize);
	}
	public void improve(double[] choice) {
		stack.add(toAction(choice));
	}
	public void showState() {
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
	public boolean isFinished() {
		return stack.isFull();
	}
	protected NeuralStack<T> getStack(){
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
