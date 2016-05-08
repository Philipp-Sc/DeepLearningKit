package ps.deeplearningkit.core.neurallearning;
import org.encog.ml.MLMethod;
import ps.deeplearningkit.core.simulator.NeuralAction;

import java.util.List;


public abstract class NeuralActor implements Scoring{
	
	protected MLMethod bn;
	protected boolean track;
	public NeuralActor(boolean track){
		this.track=track;
	}
	public void setMLMethod(MLMethod bn){
		this.bn=bn;
	}
	public abstract double scoreActor();
	public void setTrack(boolean track){
		this.track=track;
	}
	public abstract List<NeuralAction> getNeuralActions();
}
