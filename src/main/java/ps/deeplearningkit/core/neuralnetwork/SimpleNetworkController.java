package ps.deeplearningkit.core.neuralnetwork;

import java.io.IOException;
import java.util.ArrayList;

import org.encog.ml.CalculateScore;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import ps.deeplearningkit.core.neuralnetwork.utils.Assistant;

public class SimpleNetworkController extends AdvancedNetworkController{

	/**
	 * This controller provides convenience methods.
	 * @param path
	 */
	public SimpleNetworkController(String path) {
		super(path);
	}
	/**
	 * ART1 Networks
	 */
	
	/**
	 * Create ART1 Network with the right configuration for double[] input.
	 * @param key
	 * @param doubleCount length of the double array.
	 * @param precision {10,100,1000,..} number of digits to consider.
	 * @param outputNeurons
	 * @throws IOException
	 */
	public void createART1(String key,int doubleCount,int precision,int outputNeurons) throws IOException{
		this.createART1(key, doubleCount*(((Double) Math.ceil(Math.log(1*precision)/Math.log(2))).intValue()), outputNeurons);
	}
	/**
	 * Clusters the double array on the fly, no training needed.
	 * @param key (unique key used for access to the network)
	 * @param input (must be in range of [0,1])
	 * @param precision 10,100,..(0.x*precision→x) once set should not be changed
	 * @return class
	 * 
	 * @throws IOException 
	 */
	public int clusterART1(String key,double[] input,int precision) throws IOException{
		int maxBinaryDigits=((Double) Math.ceil(Math.log(1*precision)/Math.log(2))).intValue();
		ArrayList<boolean[]> list=new ArrayList<>();
		for(double each:input){
			list.add(Assistant.toBinaryNumber(((Double)(each*precision)).intValue(),maxBinaryDigits));
		}
		if(list.size()==0){
			return -1;
		}
		boolean[] bits=new boolean[list.size()*maxBinaryDigits];
		int index=0;
		for(boolean[] each:list){
			for(int b=0;b<maxBinaryDigits;b++){
				bits[index]=each[b];
				index++;
			}
		}
		return clusterART1(key, bits);
	}
	/**
	 * BasicNeuralNetworks
	 */
	/**
	 * Trains the network with a reward function. 
	 * No training set is needed. 
	 * The network will learn on its own.
	 * This variation uses NeuralSimulatedAnnealing.
	 * @param key
	 * @param someScore
	 * @param startTemp
	 * @param stopTemp
	 * @param cycles
	 * @param iterations
	 * @throws IOException
	 */
	public void trainNewBasicNetwork(String key,int inputCount,int outputCount,CalculateScore someScore,int startTemp,int stopTemp,int cycles,int iterations) throws IOException{
		this.createBasicNetwork(key,inputCount,outputCount);
		BasicNetwork bn=this.getBasicNetwork(key);
		NeuralSimulatedAnnealing train=	new NeuralSimulatedAnnealing(bn, someScore, startTemp,stopTemp ,cycles);
		for(int i=0;i<iterations;i++){
			train.iteration();
		}
		train.finishTraining();
		this.saveBasicNetworks();
	}
}
