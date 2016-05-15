package ps.deeplearningkit.core.example;

import java.util.*;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLClassification;
import org.encog.neural.neat.NEATNetwork;

import ps.deeplearningkit.core.MainController;
import ps.deeplearningkit.core.example.pixelbreed.PixelEndPoint;
import ps.deeplearningkit.core.example.pixelbreed.PixelSimulator;
import ps.deeplearningkit.core.neurallearning.score.BasicScore;
import ps.deeplearningkit.core.neurallearning.debug.LinearActor;
import ps.deeplearningkit.core.neurallearning.actor.NeuralActor;
import ps.deeplearningkit.core.analysis.heuristic.uncommon.search.UncommonSearch;
import ps.deeplearningkit.core.analysis.heuristic.Behavior;
import ps.deeplearningkit.core.analysis.heuristic.novelty.search.NoveltySearch;
import ps.deeplearningkit.core.analysis.uct.Simulator;
import ps.deeplearningkit.core.analysis.uct.UCT;
import ps.deeplearningkit.core.simulator.debug.BasicNeuralAction;
import ps.deeplearningkit.core.simulator.debug.NeuralAction;
import ps.deeplearningkit.core.simulator.debug.NeuralStack;

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
				System.out.println(branch.printState());
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
	public static void main(String[] args) throws Exception {
		Random random=new Random();
		int stackSize = 25;
		int precision=10;
		MainController.getSimpleNetworkController().createART1("art1",25,precision,1000);
		MLClassification art1= MainController.getAdvancedNetworkController().getART1Network("art1");
		UncommonSearch ds=new UncommonSearch(art1);
		//NoveltySearch ns=new NoveltySearch();
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
				Behavior behavior=new PixelEndPoint(list).getBehavior();
				double uncommonnes=ds.testUncommonnes(behavior);
				//	System.out.println("disparity: "+d);
				//ns.finishedEvaluation();
				//System.out.println("value: "+value);
				// return metric like sums/count
				//Behavior behavior = branch.getBehavior();
				//double novelty;
				//ns.addToCurrentPopulation(behavior);
				//novelty = ns.testNovelty(behavior);
				//System.out.println(novelty);
				//ans.finishedEvaluation();
				return (uncommonnes);

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
	/**
	 * Here we aim to use every state on the way to the final state.
	 * In this attempt we use only the difference between states,
	 * in future attempts it might be better to use the difference between comparable actions.
	 * @param args
	 * @throws Exception
     */
	public static void mainUncommon(String[] args) throws Exception {
		Random random=new Random();
		int stackSize = 25;
		int precision=10;
		MainController.getSimpleNetworkController().createART1("art1",25,precision,2000);
		MLClassification art1= MainController.getAdvancedNetworkController().getART1Network("art1");
		UncommonSearch ds=new UncommonSearch(art1);
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
				double d=ds.testUncommonnes(endPoints.get(0).getBehavior());
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
