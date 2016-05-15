package ps.deeplearningkit.core.analysis.heuristic.novelty.neuralsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.encog.ml.MLClassification;
import org.encog.neural.som.SOM;

import ps.deeplearningkit.core.MainController;
import ps.deeplearningkit.core.neuralnetwork.utils.Assistant;
import ps.deeplearningkit.core.analysis.heuristic.Behavior;
/**
 * 
 * This AbstractNoveltySearch is different to NoveltySearch in the following aspect:
 * - The behavior is classified using different NeuralNetworks.
 * - These classifications are then used to determine the distance in behavior space.
 */
public class AbstractNoveltySearch {
	/**
	 * The number of nearest neighbors to consider when determining the sparseness
	 *  in behavior space to decide if the new behavior should be added to the archive.  
	 */
	private int neighborsToConsider=31;
	/**
	 * Threshold to decide whether the behavior is novel enough to be added to the archive.
	 * By default the noveltyThreshold is taken from the behavior.
	 */
	private double noveltyThreshold=0;
	/**
	 * Minimal value to decrease the noveltyThreshold to. Default is 0.05*noveltyThreshold.
	 */
	private double minNoveltyThreshold=0;
	/**
	 * If a new induvidual's novelty is high, it is typically added to the archive.
	 */
	public List<ClassifiedBehavior> archive=Collections.synchronizedList(new ArrayList<ClassifiedBehavior>());
	private List<ClassifiedBehavior> toArchive=new ArrayList<>();
	/**
	 * Current behaviors in the current population. Similar to actions used by MCS.
	 * These behaviors may be initialized for the current state.
	 * Run testNovelty(Behavior behavior) for all individuals.
	 */
	private List<ClassifiedBehavior> population=new ArrayList<>();
	
	/*
	 * Count number of generations in a row for which no behavior is added to the archive.
	 */
	private int noNewArchiveCount=0;
	/*
	 * The noveltyThreshold is adjusted if no new behavior was found.
	 */
	private int noNewArchiveCountThreshold=10;
	/*
	 * Factor to reduce the noveltyThreshold if necessary.
	 */
	private double noveltyThresholdChangeFactor=1;
	
	/*
	 * Iterations for the som
	 */
	private int iterations;
	/*
	 * The precision value describes the number of decimal places of the behavior values to consider.
	 * (1.x*precision)
	 */
	private int precision;
	
	private List<MLClassification> networks;
	/*
	 * Needed to tell this class which MLClassification to use.
	 */
	public AbstractNoveltySearch(String som,int iterations,String art1,int precision){
		this.iterations=iterations;
		this.precision=precision;
		networks=new ArrayList<>();
		networks.add(MainController.getSimpleNetworkController().getART1Network(art1));
		networks.add(MainController.getSimpleNetworkController().getSomNetwork(som));
	}
	public AbstractNoveltySearch(String som,int iterations,String art1,int precision,int doubleCount,int somClasses) throws IOException{
		this.iterations=iterations;
		this.precision=precision;
		networks=new ArrayList<>();
		MainController.getSimpleNetworkController().createART1(art1, doubleCount, precision, 10000);
		MainController.getSimpleNetworkController().createSOM(som, doubleCount, somClasses);
		networks.add(MainController.getSimpleNetworkController().getART1Network(art1));
		networks.add(MainController.getSimpleNetworkController().getSomNetwork(som));
	}
	
	public void reset(){
		archive=Collections.synchronizedList(new ArrayList<ClassifiedBehavior>());
		toArchive=new ArrayList<>(neighborsToConsider);
		population=new ArrayList<>();
		noNewArchiveCount=0;
	}
	/**
	 * This method determines the novelty of the behavior.
	 * Because of the use of toArchive and archive this method can be called by multiple threads asynchronously.
	 * @param behavior
	 * @return novelty in the range from [0,1].
	 * @throws IOException 
	 */
	public double testNovelty(ClassifiedBehavior behavior) throws IOException{
		int behaviorsCount=toArchive.size()+population.size();
		double[] distanceOfBehavior=new double[behaviorsCount];
		int index=0;
		int inArchiveCount=0;
		/*
		 * Get distances to all behaviors for the passed behavior.
		 */
		for(ClassifiedBehavior novelBehavior:toArchive){
			distanceOfBehavior[index]=behavior.distanceFrom(novelBehavior,networks,precision);
			/*
			 * If the behaviors are approximately equal increment inArchiveCount.
			 */
			if(distanceOfBehavior[index]<0.0000001){
				inArchiveCount++;
			}
			index++;
		}
		for(ClassifiedBehavior normalBehavior:population){
			distanceOfBehavior[index]=behavior.distanceFrom(normalBehavior,networks,precision);
			index++;
		}
		/*
		 * Compute the average distance of the behaviors to the passed behavior considering short distances first
		 * in case the neighbors to consider are filtered. 
		 */
		int neighborsToConsiderTemp=Math.min(behaviorsCount, neighborsToConsider);
		Arrays.sort(distanceOfBehavior);
		double avgDistanceOfBehavior=0;
		for(index=0;index<neighborsToConsiderTemp;index++){
			avgDistanceOfBehavior+=distanceOfBehavior[index];
		}
		/*
		 * Decide to add the behavior to the archive.
		 */
		// do not add the behavior to the archive if it was already enough times added.
		if(inArchiveCount<neighborsToConsider){
			if(noveltyThreshold==0){
				noveltyThreshold=behavior.defaultThreshold();
				minNoveltyThreshold=noveltyThreshold*0.05;
			}
			// if archive and toArchive do not contain similar behaviors add it.
			if(!containsSimilar(toArchive, behavior, noveltyThreshold) && !containsSimilar(archive, behavior, noveltyThreshold)){
				/*
				 * Add the behavior to the temporal archive.
				 */
				toArchive.add(behavior);
			}
		}
		return avgDistanceOfBehavior;
	}
	private boolean containsSimilar(List<ClassifiedBehavior> behaviors,ClassifiedBehavior b,double threshold){
		if(behaviors.isEmpty()){
			return false;
		}
		synchronized (behaviors) {
			for(Behavior b2:behaviors){
				if(b.distanceFrom(b2)<threshold){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * This may be called before evaluating behaviors from a population to allow determining the novelty.
	 * @param behaviors
	 */
	public synchronized void setCurrentPopulation(List<ClassifiedBehavior> behaviors){
		this.population=behaviors;
		//updateSOM(population); // maybe better not to update som to often with the normal population?
	}
	public synchronized void addToCurrentPopulation(ClassifiedBehavior b){
		population.add(b);
	}
	/**
	 * This method must be called when the population has been evaluated.
	 */
	public void finishedEvaluation(){
		if(toArchive.isEmpty()){
			noNewArchiveCount++;
			if(noNewArchiveCount==noNewArchiveCountThreshold){
				noveltyThreshold/=noveltyThresholdChangeFactor; // defaults to 1.
				if(noveltyThreshold<minNoveltyThreshold){
					noveltyThreshold=minNoveltyThreshold;
				}
				noNewArchiveCount=0;
			}
		}
		archive.addAll(toArchive);
		updateSOM(archive);
		toArchive.clear();
		population=new ArrayList<>();
	}
	private void updateSOM(List<ClassifiedBehavior> b){
		for(MLClassification network:networks){
			if(network instanceof SOM){
				Assistant.trainSOM(((SOM)network), behaviorDataSet(b), iterations);
			}
		}
	}
	private double[][] behaviorDataSet(List<ClassifiedBehavior> b){
		if(b.size()==0){
			return new double[][]{};
		}
		double[][] data=new double[b.size()][];
		for(int i=0;i<b.size();i++){
			data[i]=b.get(i).getVector().getDataRef();
		}
		return data;
	}
}
