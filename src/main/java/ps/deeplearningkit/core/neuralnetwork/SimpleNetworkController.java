package ps.deeplearningkit.core.neuralnetwork;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;

public class SimpleNetworkController extends AdvancedNetworkController{

	public SimpleNetworkController(String path) {
		super(path);
	}
	
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
		return clusterART1(key, bits);
	}
	private boolean[] toBinaryNumber(int number,int digits){
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
