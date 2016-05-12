package ps.deeplearningkit.core.simulator;

public interface Simulator {

	/**
	 * This (re-)initializes the simulator.
	 */
	void init();

	/**
	 * This is useful for using multiple threads.
	 * @return a exact copy of this simulator.
     */
	Simulator copy();
	/**
	 * This applies an action, output produced by a neural network.
	 * @param action array with values mostly between 0 and 1.
     */
	void applyAction(double[] action);
	/**
	 *
	 * @return whether the simulation has finished or not.
     */
	boolean isAbsorbing();
	/**
	 * @return reward, can be based on the final result or a (fitness) heuristic.
     */
	double getReward();

	/**
	 * @return input for the neuronal network
	 */
	double[] getInput();

	void printState();
}
