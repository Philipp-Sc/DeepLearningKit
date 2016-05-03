package ps.deeplearningkit.core.neurallearning;

import org.encog.engine.network.activation.ActivationLOG;
import org.encog.ml.CalculateScore;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
public class Ai {
	private final int inputSize,outputSize,startTemp,stopTemp,cycles;
	private final CalculateScore someScore;
	
	BasicNetwork bn;
	MLTrain train;
	
	public Ai(BasicNetwork bn,int inputSize,int outputSize,CalculateScore someScore,int startTemp,int stopTemp,int cycles){
		this.startTemp=startTemp;
		this.stopTemp=stopTemp;
		this.cycles=cycles;
		this.someScore=someScore;
		if(bn==null){
			this.inputSize=inputSize;
			this.outputSize=outputSize;
			this.bn=new BasicNetwork();
			setBasicNetwork();
		}else{
			this.bn=bn;
			this.inputSize=bn.getInputCount();
			this.outputSize=bn.getOutputCount();
		}
		setTrain();
	}
	public BasicNetwork getBasicNetwork(){
		return bn;
	}
	public void setBasicNetwork(){
		bn.addLayer(new BasicLayer(null,true, inputSize));
		bn.addLayer(new BasicLayer(new ActivationLOG(),true,inputSize*2));
		bn.addLayer(new BasicLayer(new ActivationLOG(), false, outputSize));
		bn.getStructure().finalizeStructure();
		bn.reset();
	}
	
	public void setTrain(){
		train=new NeuralSimulatedAnnealing(bn, someScore, startTemp,stopTemp ,cycles);
	}
	public void train(int i){
		while(i>0){
			train.iteration();
			i--;
		}
		train.finishTraining();
	}

}
