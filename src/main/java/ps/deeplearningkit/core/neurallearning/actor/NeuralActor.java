package ps.deeplearningkit.core.neurallearning.actor;
import org.encog.ml.MLMethod;
import ps.deeplearningkit.core.simulator.debug.NeuralAction;

import java.util.List;


public abstract class NeuralActor implements Scoring {
	
	protected MLMethod bn;
	protected boolean track;
	public NeuralActor(boolean track){
		this.track=track;
	}
	public void setMLMethod(MLMethod bn){
		this.bn=bn;
	}
	public void setTrack(boolean track){
		this.track=track;
	}
	public abstract double scoreActor();
	public abstract List<NeuralAction> getNeuralActions();
}
