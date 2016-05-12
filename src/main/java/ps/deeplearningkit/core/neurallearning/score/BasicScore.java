package ps.deeplearningkit.core.neurallearning.score;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import ps.deeplearningkit.core.neurallearning.actor.NeuralActor;

public class BasicScore implements CalculateScore{
	
	private NeuralActor scoring;
	private boolean shouldMinimize;
	private boolean singelThreaded;
	public BasicScore(NeuralActor neuralActor,boolean shouldMinimize,boolean singleThreaded){
		this.scoring=neuralActor;
		this.shouldMinimize=shouldMinimize;
		this.singelThreaded=singleThreaded;
	}

	public double calculateScore(MLMethod method) {
		scoring.setMLMethod(method);
		return scoring.scoreActor();
	}
	public boolean shouldMinimize() {
		return shouldMinimize;
	}
	public boolean requireSingleThreaded() {
		return singelThreaded;
	}
}
