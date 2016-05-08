package ps.deeplearningkit.core.example;

import java.io.IOException;
import java.util.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLClassification;
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
import ps.deeplearningkit.core.example.pixelbreed.PixelAction;
import ps.deeplearningkit.core.example.pixelbreed.PixelEndPoint;
import ps.deeplearningkit.core.example.pixelbreed.PixelSimulator;
import ps.deeplearningkit.core.example.pixelbreed.PixelState;
import ps.deeplearningkit.core.neurallearning.BasicScore;
import ps.deeplearningkit.core.neurallearning.LinearActor;
import ps.deeplearningkit.core.neurallearning.NeuralActor;
import ps.deeplearningkit.core.search.abstractnoveltysearch.AbstractNoveltySearch;
import ps.deeplearningkit.core.search.abstractnoveltysearch.ClassifiedBehavior;
import ps.deeplearningkit.core.search.disparitysearch.DisparitySearch;
import ps.deeplearningkit.core.search.noveltysearch.Behavior;
import ps.deeplearningkit.core.search.noveltysearch.NoveltySearch;
import ps.deeplearningkit.core.search.noveltysearch.RealVectorBehavior;
import ps.deeplearningkit.core.search.uct.Simulator;
import ps.deeplearningkit.core.search.uct.State;
import ps.deeplearningkit.core.search.uct.UCT;
import ps.deeplearningkit.core.simulator.BasicNeuralAction;
import ps.deeplearningkit.core.simulator.NeuralAction;
import ps.deeplearningkit.core.simulator.NeuralStack;
import sun.applet.Main;

public class Application {

	/**
	 * Here the final state is used for evaluation of novelty.
	 * @param args
	 * @throws Exception
     */
	public static void main1(String[] args) throws Exception {
		int stackSize = 25;
		NoveltySearch ans = new NoveltySearch();
		NeuralActor neuralActor = new LinearActor(true, stackSize) {

			@Override
			protected double[] getInput(List<NeuralAction> list) {
				PixelEndPoint branch = new PixelEndPoint(list);
				return branch.getBehavior().getVector().getDataRef();
			}

			@Override
			protected double getReward(NeuralStack<NeuralAction> stack) {
				/*
				 * stack.getStack().getNeuronValues()
				 */
				List<NeuralAction> list = stack.getList();
				/*
				 * Initialize State
				 */
				PixelEndPoint branch = new PixelEndPoint(list);
				/*
				System.out.println(branch.showState());
				try {
					wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				Behavior behavior = branch.getBehavior();
				double novelty;
				ans.addToCurrentPopulation(behavior);
				novelty = ans.testNovelty(behavior);
				//System.out.println(novelty);
				//ans.finishedEvaluation();
				return novelty;

			}

			@Override
			protected NeuralAction toActionObject(double[] choice) {
				return new BasicNeuralAction(choice);
			}
		};
		CalculateScore someScore = new BasicScore(neuralActor, false, false);
		MainController.getAdvancedNetworkController().createNEATPopulation("neatpop", 26, 4, 1500);
		MainController.getAdvancedNetworkController().trainNEATPopulation("neatpop", someScore, 999, 1);
		NEATNetwork nnn = MainController.getAdvancedNetworkController().getBestNEATNetwork("neatpop", someScore);

		//ans.finishedEvaluation();
		neuralActor.setTrack(true);
		neuralActor.setMLMethod(nnn);
		neuralActor.scoreActor();
		System.out.println(new PixelEndPoint(neuralActor.getNeuralActions()).showState());
	}

	/**
	 * Here we aim to use every state on the way to the final state.
	 * In this attempt we use only the difference between states,
	 * in future attempts it might be better to use the difference between comparable actions.
	 * @param args
	 * @throws Exception
     */
	public static void main(String[] args) throws Exception {
		Random random=new Random();
		int stackSize = 25;
		int precision=10;
		MainController.getSimpleNetworkController().createART1("art1",100,precision,2000);
		MLClassification art1= MainController.getAdvancedNetworkController().getART1Network("art1");
		DisparitySearch ds=new DisparitySearch(art1);
		ds.setPrecision(precision);
		NeuralActor neuralActor = new LinearActor(false, stackSize) {

			@Override
			protected double[] getInput(List<NeuralAction> list) {
				PixelEndPoint branch = new PixelEndPoint(list);
				return branch.getBehavior().getVector().getDataRef();
			}

			@Override
			protected double getReward(NeuralStack<NeuralAction> stack) {
				/*
				 * stack.getStack().getNeuronValues()
				 */
				List<NeuralAction> list = stack.getList();
				/*
				 * Initialize States
				 */
				List<PixelEndPoint> endPoints=new ArrayList<>();
				//for(int i=0;i<list.size();i++){
				//	endPoints.add(new PixelEndPoint(list.subList(0,i)));
				//}
				endPoints.add(new PixelEndPoint(list));
				double d=ds.testDisparity(endPoints.get(0).getBehavior());
			//	System.out.println("disparity: "+d);
				//ns.finishedEvaluation();
				//System.out.println("value: "+value);
				// return metric like sums/count
				//Behavior behavior = branch.getBehavior();
				//double novelty;
				//ans.addToCurrentPopulation(behavior);
				//novelty = ans.testNovelty(behavior);
				//System.out.println(novelty);
				//ans.finishedEvaluation();
				return d;

			}

			@Override
			protected NeuralAction toActionObject(double[] choice) {
				return new BasicNeuralAction(choice);
			}
		};
		CalculateScore someScore = new BasicScore(neuralActor, false, false);
		MainController.getAdvancedNetworkController().createNEATPopulation("neatpop", 25+1, 3, 1000);
		MainController.getAdvancedNetworkController().trainNEATPopulation("neatpop", someScore, 100000, 200);
		NEATNetwork nnn = MainController.getAdvancedNetworkController().getBestNEATNetwork("neatpop", someScore);

		//ans.finishedEvaluation();
		neuralActor.setTrack(true);
		neuralActor.setMLMethod(nnn);
		neuralActor.scoreActor();
		System.out.println(new PixelEndPoint(neuralActor.getNeuralActions()).showState());
	}
	public static void mainOld(String[] args) throws Exception {
		Random random=new Random();
		int stackSize = 1;
		NoveltySearch ns = new NoveltySearch();
		NeuralActor neuralActor = new LinearActor(false, stackSize) {

			@Override
			protected double[] getInput(List<NeuralAction> list) {
				PixelEndPoint branch = new PixelEndPoint(list);
				return branch.getBehavior().getVector().getDataRef();
			}

			@Override
			protected double getReward(NeuralStack<NeuralAction> stack) {
				/*
				 * stack.getStack().getNeuronValues()
				 */
				List<NeuralAction> list = stack.getList();
				/*
				 * Initialize States
				 */
				List<PixelEndPoint> endPoints=new ArrayList<>();
				//for(int i=0;i<list.size();i++){
				//	endPoints.add(new PixelEndPoint(list.subList(0,i)));
				//}
				endPoints.add(new PixelEndPoint(list));
				double value=0;
				for(PixelEndPoint endPoint:endPoints){
					// Calculate sum of future novelty for each node.
					// "real" world
					Simulator simReal = new PixelSimulator(endPoint.getState(),ns);
					// simulator for planning
					Simulator simPlan = new PixelSimulator(endPoint.getState(),ns);
					int trajectories = 1;
					int depth = 1;
					UCT planner = new UCT(simPlan, trajectories, depth,
							simPlan.getDiscountFactor(), random);
					value+=planner.getQ(planner.planAndAct(simReal.getState()));
				}
				//ns.finishedEvaluation();
				//System.out.println("value: "+value);
				// return metric like sums/count
				//Behavior behavior = branch.getBehavior();
				//double novelty;
				//ans.addToCurrentPopulation(behavior);
				//novelty = ans.testNovelty(behavior);
				//System.out.println(novelty);
				//ans.finishedEvaluation();
				return value;

			}

			@Override
			protected NeuralAction toActionObject(double[] choice) {
				return new BasicNeuralAction(choice);
			}
		};
		CalculateScore someScore = new BasicScore(neuralActor, false, false);
		MainController.getAdvancedNetworkController().createNEATPopulation("neatpop", 26, 3, 200);
		MainController.getAdvancedNetworkController().trainNEATPopulation("neatpop", someScore, 999, 500);
		NEATNetwork nnn = MainController.getAdvancedNetworkController().getBestNEATNetwork("neatpop", someScore);

		//ans.finishedEvaluation();
		neuralActor.setTrack(true);
		neuralActor.setMLMethod(nnn);
		neuralActor.scoreActor();
		System.out.println(new PixelEndPoint(neuralActor.getNeuralActions()).showState());
	}
}
