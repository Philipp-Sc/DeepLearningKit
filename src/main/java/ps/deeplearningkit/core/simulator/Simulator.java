package ps.deeplearningkit.core.simulator;

public interface Simulator {

	/**
	 * This (re-)initializes the simulator.
	 */
	void initEpisode();

	void setState(State state);
	State getState();

	/**
	 * This applies an action, output produced by a neuralsearch network.
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

	void printStatus();
}
