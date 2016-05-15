package ps.deeplearningkit.core.simulator.debug;

/**
 * For the communication between the neuralsearch network and the domain specific actions/behaviors.
 * @author Philipp Schlütermann
 */
public class BasicNeuralAction implements NeuralAction {

	private double[] choice;
	public BasicNeuralAction(double[] choice){
		this.choice=choice;
	}
	@Override
	public String toString(){
		String result="[";
		for(double each:choice){
			result+=" "+each;
		}
		return result+"]";
	}
	public int getNeuronCount() {
		return choice.length;
	}
	public double[] getNeuronValues() {
		return choice;
	}
	/*
	public abstract <T> T deNormalize(double value);
	public abstract <T> T[] deNormalize();
	public abstract <T> double[] normalize(T[] obj);
	public abstract <T> double normalize(T obj);*/
}
