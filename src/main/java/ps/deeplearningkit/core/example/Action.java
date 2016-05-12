package ps.deeplearningkit.core.example;

import ps.deeplearningkit.core.simulator.debug.BasicNeuralAction;

/**
 * Example implementation of BasicNeuralAction
 * This class is important for the whole implementation.
 * @author Philipp Schlültermann
 *
 */
public class Action extends BasicNeuralAction{

	public Action(double[] choice){
		super(choice);
		/*
		 * deNormalize all values and save them here.
		 */
	}
	public <T> T[] deNormalize() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> double[] normalize(T[] obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> double normalize(T obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	public <T> T deNormalize(double value) {
		// TODO Auto-generated method stub
		return null;
	}
}
