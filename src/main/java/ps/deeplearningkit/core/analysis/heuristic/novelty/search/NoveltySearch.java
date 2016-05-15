package ps.deeplearningkit.core.analysis.heuristic.novelty.search;

import ps.deeplearningkit.core.analysis.heuristic.Behavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simplified version of novelty analysis.
 * Implement Behavior and you are ready to use this.
 * Replace your fitness function with the use of this class. 
 * 1. set population
 * 2. calculate novelty
 * 3. finish evaluation
 * 
 * @author Most of the code was taken from OliverColeman @see at github: /ahni/src/com/ojcoleman/ahni/evaluation/novelty/
 */
public class NoveltySearch {

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
	public List<Behavior> archive=Collections.synchronizedList(new ArrayList<Behavior>());
	private List<Behavior> toArchive=new ArrayList<>();
	/**
	 * Current behaviors in the current population. Similar to actions used by MCS.
	 * These behaviors may be initialized for the current state.
	 * Run testNovelty(Behavior behavior) for all individuals.
	 */
	private List<Behavior> population=new ArrayList<>();
	
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
	
	public NoveltySearch(){
	}
	public void reset(){
		archive=Collections.synchronizedList(new ArrayList<Behavior>());
		toArchive=new ArrayList<>(neighborsToConsider);
		population=new ArrayList<>();
		noNewArchiveCount=0;
	}
	/**
	 * This method determines the novelty of the behavior.
	 * Because of the use of toArchive and archive this method can be called by multiple threads asynchronously.
	 * @param behavior
	 * @return novelty in the range from [0,1].
	 */
	public double testNovelty(Behavior behavior){
		int behaviorsCount=toArchive.size()+population.size();
		double[] distanceOfBehavior=new double[behaviorsCount];
		int index=0;
		int inArchiveCount=0;
		/*
		 * Get distances to all behaviors for the passed behavior.
		 */
		for(Behavior novelBehavior:toArchive){
			distanceOfBehavior[index]=behavior.distanceFrom(novelBehavior);
			/*
			 * If the behaviors are approximately equal increment inArchiveCount.
			 */
			if(distanceOfBehavior[index]<0.0000001){
				inArchiveCount++;
			}
			index++;
		}
		for(Behavior normalBehavior:population){
			distanceOfBehavior[index]=behavior.distanceFrom(normalBehavior);
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
	private boolean containsSimilar(List<Behavior> behaviors,Behavior b,double threshold){
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
	public synchronized void setCurrentPopulation(List<Behavior> behaviors){
		this.population=behaviors;
	}
	public synchronized void addToCurrentPopulation(Behavior b){
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
		toArchive.clear();
		population=new ArrayList<>();
	}
}
