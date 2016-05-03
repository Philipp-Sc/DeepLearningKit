package ps.deeplearningkit.core.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.hyperneat.HyperNEATCODEC;
import org.encog.neural.hyperneat.substrate.Substrate;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
import org.encog.util.simple.EncogUtility;

import ps.deeplearningkit.core.MainController;
import ps.deeplearningkit.core.example.music.MusicAction;
import ps.deeplearningkit.core.example.music.MusicEndPoint;
import ps.deeplearningkit.core.neurallearning.BasicScore;
import ps.deeplearningkit.core.neurallearning.LinearActor;
import ps.deeplearningkit.core.neurallearning.NeuralActor;
import ps.deeplearningkit.core.search.abstractnoveltysearch.AbstractNoveltySearch;
import ps.deeplearningkit.core.search.abstractnoveltysearch.ClassifiedBehavior;
import ps.deeplearningkit.core.search.noveltysearch.Behavior;
import ps.deeplearningkit.core.search.noveltysearch.NoveltySearch;
import ps.deeplearningkit.core.search.noveltysearch.RealVectorBehavior;
import ps.deeplearningkit.core.simulator.BasicNeuralAction;
import ps.deeplearningkit.core.simulator.NeuralAction;
import ps.deeplearningkit.core.simulator.NeuralStack;

public class Application {

	public static void main1(String[] args) throws IOException{
		NeuralActor neuralActor=new LinearActor(false, 1) {
			
			@Override
			protected double[] getInput(int size) {
				return new double[]{};
			}
			
			@Override
			protected double getReward(NeuralStack<NeuralAction> stack) {
				/*
				 * stack.getStack().getNeuronValues()
				 */
				List<NeuralAction> list=stack.getList();
				if(list==null){
					return -1;
				}
				if(list.size()==0){
					return -1;
				}
				if(list.get(0)==null){
					return -1;
				}
				if(list.get(0).getNeuronValues().length<=0){
					return -1;
				}
				if(list.get(0).getNeuronValues()[0]==0.42){
					return 1;
				}else if(list.get(0).getNeuronValues()[0]<0.0){
					return -1;
				}else{
					return -1*Math.abs(list.get(0).getNeuronValues()[0]-0.42);
				}
			}
			@Override
			protected NeuralAction toActionObject(double[] choice) {
				return new Action(choice);
			}
		};
		CalculateScore someScore= new BasicScore(neuralActor, false, false);
		//MainController.getAdvancedNetworkController().createNEATPopulation("testpop", 1, 1, 1000);
		//MainController.getAdvancedNetworkController().trainNEATPopulation("testpop", someScore, 0.0001);
		NEATPopulation nn=(NEATPopulation) MainController.getAdvancedNetworkController().getBestMethod("testpop", someScore);
		System.out.println(nn.compute(null));
	}
	public static void main2(String[] args) throws IOException{
		NeuralActor neuralActor=new LinearActor(true, 1) {
			
			@Override
			protected double[] getInput(int size) {
				return new double[]{};
			}
			
			@Override
			protected double getReward(NeuralStack<NeuralAction> stack) {
				/*
				 * stack.getStack().getNeuronValues()
				 */
				List<NeuralAction> list=stack.getList();
				if(list.get(0).getNeuronValues()[0]==0.42){
					return 1;
				}else if(list.get(0).getNeuronValues()[0]<0.0){
					return -1;
				}else{
					return -1*Math.abs(list.get(0).getNeuronValues()[0]-0.42);
				}
			}
			@Override
			protected NeuralAction toActionObject(double[] choice) {
				return new Action(choice);
			}
		};
		CalculateScore someScore= new BasicScore(neuralActor, false, false);
		MainController.getAdvancedNetworkController().trainNewBasicNetwork("test",0,1 ,someScore, 20, 2, 100, 500);
	}
	public static void main(String[] args) throws IOException, InvalidMidiDataException{
		int stackSize=4;
		//AbstractNoveltySearch ans=new AbstractNoveltySearch("som", 10, "art1", 10000, stackSize, 30);
		NoveltySearch ans=new NoveltySearch();
		NeuralActor neuralActor=new LinearActor(true, stackSize) {
			
			@Override
			protected double[] getInput(int size) {
				return new double[]{};
			}
			
			@Override
			protected double getReward(NeuralStack<NeuralAction> stack) {
				/*
				 * stack.getStack().getNeuronValues()
				 */
				List<NeuralAction> list=stack.getList();
				/*
				 * Initialize State
				 */
				int reward=0;
				double val=0.00001;
				int i=1;
				for(NeuralAction each:list){
					if(each==null){
						return -1;
					}
					if(each.getNeuronValues()==null){
						return -1;
					}
					
					if(each.getNeuronValues()[0]<val){
						reward++;
					}
				}
				//System.out.println(reward+""+list.size());
				return reward;
				/*
				double novelty;
				try{
				MusicEndPoint perf=new MusicEndPoint(list);
				Behavior current= perf.getBehavior();
				ans.addToCurrentPopulation(new RealVectorBehavior(current.getVector().getDataRef()));
				novelty=ans.testNovelty(new RealVectorBehavior(current.getVector().getDataRef()));
				System.out.println(novelty);
					//ans.finishedEvaluation();
				}catch(Exception e){
					e.printStackTrace();
					throw new RuntimeException();
				}
				return novelty;
				*/
			}
			@Override
			protected NeuralAction toActionObject(double[] choice) {
				return new BasicNeuralAction(choice);
			}
		};
		CalculateScore someScore= new BasicScore(neuralActor, false, false);
		MainController.getAdvancedNetworkController().createNEATPopulation("neatpop", 1, 1, 1000);
		MainController.getAdvancedNetworkController().trainNEATPopulation("neatpop", someScore, 999,100);
		NEATNetwork nnn=MainController.getAdvancedNetworkController().getBestNEATNetwork("neatpop", someScore);
		NormalizedField n=new NormalizedField(NormalizationAction.Normalize, "index", 4, 0, 0.9, -0.9);
		System.out.println(nnn.compute(new BasicMLData(new double[]{n.normalize(0)})));
		System.out.println(nnn.compute(new BasicMLData(new double[]{n.normalize(1)})));
		System.out.println(nnn.compute(new BasicMLData(new double[]{n.normalize(2)})));
		System.out.println(nnn.compute(new BasicMLData(new double[]{n.normalize(3)})));
		//MainController.getAdvancedNetworkController().trainNewBasicNetwork("test",1,1 ,someScore, 20, 4, 300, 7000);
		ans.finishedEvaluation();
		List<NeuralAction> list=new ArrayList<>();
		for(int i=1;i<stackSize;i++){
			list.add(new MusicAction(MainController.getAdvancedNetworkController().testBasicNetwork("test", new BasicMLData(new double[]{1/i})).getData()));
		}
		//MusicEndPoint mp=new MusicEndPoint(list);
		//System.out.println(mp.getString());
	}
	public static void main4(String[] args)throws IOException{
		double[] input1=new double[]{0.1,0.2,0.3};
		double[] input2=new double[]{0.1,0.7,0.3};
		double[] input3=new double[]{0.1,0.5,0.5};
		double[] input4=new double[]{0.7,0.2,0.3};
		double[] input5=new double[]{0.9,0.2,0.1};
		
		MainController.getSimpleNetworkController().createART1("f", 12, 10000);
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input1, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input2, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input3, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input1, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input2, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input3, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input4, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input5, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input4, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input5, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input1, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input2, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input3, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input4, 10));
		System.out.println(MainController.getSimpleNetworkController().clusterART1("f", input5, 10));
	}

}
