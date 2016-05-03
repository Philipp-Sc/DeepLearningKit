package ps.deeplearningkit.core.simulator;

/**
 * Basically a action taken by a neural network, might not be legal.
 * You should be able to construct an end state from a list of neural actions.
 * That end state can then be used for further evaluations.
 * For invalid end states you should reserve a common representation.
 * @author Philipp Schlütermann
 *
 */
public interface NeuralAction {
	public abstract String toString();
	public abstract int getNeuronCount();
	public abstract double[] getNeuronValues();
}
