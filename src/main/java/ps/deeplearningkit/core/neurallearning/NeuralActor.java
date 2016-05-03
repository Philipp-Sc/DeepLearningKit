package ps.deeplearningkit.core.neurallearning;
import org.encog.ml.MLMethod;


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
}
