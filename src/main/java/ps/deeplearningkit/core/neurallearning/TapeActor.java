package ps.deeplearningkit.core.neurallearning;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

import ps.deeplearningkit.core.simulator.NeuralAction;
import ps.deeplearningkit.core.simulator.NeuralTape;
import ps.deeplearningkit.core.simulator.TapeSimulator;

public abstract class TapeActor extends NeuralActor{

	private TapeSimulator<NeuralAction> sim;
	
	public TapeActor(boolean track,int maxSize,int iterations){
		super(track);
		this.sim=new TapeSimulator<NeuralAction>(maxSize,iterations) {
			public double getReward(){
				return TapeActor.this.getReward(this.getTape());
			}
			protected NeuralAction getChoice(double[] choice) {
				return TapeActor.this.getChoice(choice);
			}
			public double[] getStateStats(int size) {
				return TapeActor.this.getStateStats(size);
			}
		};
	}
	public double scoreActor(){
		while(!sim.isFinished()){
			if(bn instanceof BasicNetwork){
			MLData input= new BasicMLData(sim.getStateStats(((BasicNetwork) bn).getInputCount()));
			MLData output=((BasicNetwork) this.bn).compute(input);
			sim.improve(output.getData());
		}else if(bn instanceof NEATNetwork){
			MLData input= new BasicMLData(sim.getStateStats(((NEATNetwork) bn).getInputCount()));
			MLData output=((NEATNetwork) this.bn).compute(input);
			sim.improve(output.getData());
		}
		}
		if(track){
			sim.showState();
		}
		return sim.getReward();
	}
	/**
	 * Game logic, here you use the history to evaluate the result.
	 * @return double reward used to archive a score
	 */
	protected abstract double getReward(NeuralTape<NeuralAction> tape);
	/**
	 * 
	 * @param choice made by the neural network
	 * @return different representation of the choice
	 */
	protected abstract NeuralAction getChoice(double[] choice);
	/**
	 * Only double values from -1 to 1 allowed!
	 * @return give the neural network some feedback with different statistics.
	 */
	protected abstract double[] getStateStats(int size);
	
}
