/*
package ps.deeplearningkit.core.neurallearning.debug;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import ps.deeplearningkit.core.neurallearning.actor.NeuralActor;
import ps.deeplearningkit.core.simulator.debug.NeuralAction;
import ps.deeplearningkit.core.simulator.debug.NeuralStack;
import ps.deeplearningkit.core.simulator.debug.StackSimulator;

import java.util.List;

public abstract class LinearActor extends NeuralActor {

	private StackSimulator<NeuralAction> sim;
	private int maxSize;
	
	public LinearActor(boolean track,int maxSize){
		super(track);
		this.maxSize=maxSize;
	}
	private void initSimulator(){
		this.sim=new StackSimulator<NeuralAction>(maxSize) {
			private NormalizedField index=new NormalizedField(NormalizationAction.Normalize, "index", maxSize, 0, 1, -1);
			public double getReward(){
				return LinearActor.this.getReward(this.getStack().copy());
			}
			protected NeuralAction toAction(double[] choice) {
				return LinearActor.this.toActionObject(choice);
			}
			public double[] getInput(int size){
				double[] input= LinearActor.this.getInput(this.getStack().getList());
				double[] result=new double[size];
				result[0]=index.normalize(this.getStack().getSize());
				for(int i=0;i<size-1;i++){
					result[i+1]=input[i];
				}
				return result;
			}
		};
	}
	public synchronized double scoreActor(){
		initSimulator();
		while(!sim.isAbsorbing()){
			if(bn instanceof BasicNetwork){
				MLData input= new BasicMLData(sim.getInput(((BasicNetwork) bn).getInputCount()));
				MLData output=((BasicNetwork) this.bn).compute(input);
				sim.applyAction(output.getData());
			}else if(bn instanceof NEATNetwork){
				//this.track=false;
				MLData input= new BasicMLData(sim.getInput(((NEATNetwork) bn).getInputCount()));
				MLData output=((NEATNetwork) this.bn).compute(input);
				sim.applyAction(output.getData());
			}
		}
		if(track){
			sim.printState();
		}
		return sim.getReward();
	}
	public List<NeuralAction> getNeuralActions(){
		return sim.getStack().getList();
	}
	/**
	 * Game logic, here you use the history to evaluate the result.
	 * @param stack
	 * @return double reward used to archive a score

	protected abstract double getReward(NeuralStack<NeuralAction> stack);
	/**
	 * Used to add T (the action) to the stack.
	 * @param choice made by the neuralsearch network
	 * @return different representation of the choice

	protected abstract NeuralAction toActionObject(double[] choice);
	/**
	 * Only double values from -1 to 1 allowed!
	 * @param list
	 * @return give the neuralsearch network some feedback.

	protected abstract double[] getInput(List<NeuralAction> list);
	
}
*/