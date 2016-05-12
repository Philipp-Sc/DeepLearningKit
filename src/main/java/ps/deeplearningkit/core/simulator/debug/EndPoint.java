package ps.deeplearningkit.core.simulator.debug;

import java.util.List;

/**
 *  This class constructs a state T from a number of neural actions.
 * @author Philipp Schlütermann
 *
 * @param <T>
 */
public abstract class EndPoint<T> implements BranchEnd<T>{

	protected T state;
	protected List<NeuralAction> actions;
	private boolean isValid=true;
	public EndPoint(List<NeuralAction> actions){
		this.actions=actions;
		state=initState();
		for(NeuralAction action:actions){
			checkAndApply(action);
		}
	}
	/**
	 * If no illegal actions were applied the state is valid.
	 */
	public boolean isValid(){
		return isValid;
	}
	 /**
	  * Return the state.
	  */
	public T getState(){
		return state;
	}
	/**
	 * Initialize the starting state.
	 * @return
	 */
	public abstract T initState();
	/**
	 * Apply the action if state is valid and if the action is legal.
	 * @param action
	 */
	private void checkAndApply(NeuralAction action){
		if(isValid){
			if(this.isLegal(action)){
				applyAction(action);
			}else{
				isValid=false;
			}
		}
	}
	/**
	 * Check if the action is legal for the current state.
	 * @param action
	 * @return isLegal
	 */
	public abstract boolean isLegal(NeuralAction action);
	/**
	 * Apply the action to the state
	 * @param legal action
	 */
	public abstract void applyAction(NeuralAction action);
}
