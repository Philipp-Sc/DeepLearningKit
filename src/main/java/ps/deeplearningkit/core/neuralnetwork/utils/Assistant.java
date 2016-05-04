package ps.deeplearningkit.core.neuralnetwork.utils;

import java.io.IOException;
import java.util.ArrayList;

import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.specific.BiPolarNeuralData;
import org.encog.neural.art.ART1;
import org.encog.neural.hyperneat.substrate.Substrate;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;

public class Assistant {
		
	public static int testSOM(SOM network,double[] data){
		return network.classify(new BasicMLData(data));
	}
	public static void trainSOM(SOM network,double[][] data,int iterations){
		trainSOM(network, new BasicMLDataSet(data,null), iterations, 0.01,5,30,0.8,0.003,500,500);
	}
	public static void trainSOM(SOM network,BasicMLDataSet data,int iterations){
		trainSOM(network, data, iterations, 0.01,5,30,0.8,0.003,500,500);
	}
	public static void trainSOM(SOM network,BasicMLDataSet data,int iterations,double learningRate,
			int endRadius,int startRadius,double startRate,double endRate, int xDim,int yDim){
		System.out.print("<");
		// other neighborhood functions can be used.
		NeighborhoodRBF gaussian=new NeighborhoodRBF(RBFEnum.Gaussian, xDim, yDim);
		BasicTrainSOM train=new BasicTrainSOM(network, learningRate, data, gaussian);
		train.setAutoDecay(iterations, startRate, endRate, startRadius, endRadius);
		for(int i=0;i<iterations;i++){
			System.out.print(".");
			train.iteration();
			train.autoDecay();
		}
		train.finishTraining();
		System.out.println("/>");
	}
	/**
	 * Clusters the boolean array on the fly, no training needed.
	 * @param the network
	 * @param input (its length must be less than the inputNeuronCount)
	 * @return class
	 * @throws IOException 
	 */
	public static int clusterART1(ART1 network,boolean[] input) throws IOException{
		ART1 logic=network;
		int outputCount=logic.getOutputCount();
		BiPolarNeuralData in=new BiPolarNeuralData(input);
		BiPolarNeuralData out=new BiPolarNeuralData(outputCount);
		logic.compute(in, out);
		if(logic.hasWinner()){
			return logic.getWinner();
		}{
			return logic.getNoWinner();
		}
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
	public static int clusterART1(ART1 network,double[] input,int precision) throws IOException{
		int maxBinaryDigits=((Double) Math.ceil(Math.log(1*precision)/Math.log(2))).intValue();
		ArrayList<boolean[]> list=new ArrayList<>();
		for(double each:input){
			list.add(toBinaryNumber(((Double)(each*precision)).intValue(),maxBinaryDigits));
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
		return clusterART1(network, bits);
	}
	public static boolean[] toBinaryNumber(int number,int digits){
		String binary=Integer.toBinaryString(number);
		boolean[] binaryNumber=new boolean[Math.max(digits,binary.length())];
		for(int i=0;i<binaryNumber.length;i++){
			if(i<binary.length()){
				if(binary.charAt(i)=='0'){
					binaryNumber[i]=false;
				}else{
					binaryNumber[i]=true;
				}
			}else{
				binaryNumber[i]=false;
			}
		}
		return binaryNumber;
	}

}
