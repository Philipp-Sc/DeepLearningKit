package ps.deeplearningkit.core.example;

import ps.deeplearningkit.core.simulator.BasicNeuralAction;

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
	@Override
	public <T> T[] deNormalize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> double[] normalize(T[] obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> double normalize(T obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T deNormalize(double value) {
		// TODO Auto-generated method stub
		return null;
	}
}
