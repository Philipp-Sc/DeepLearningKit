package ps.deeplearningkit.core.analysis.heuristic.novelty.neuralsearch;

import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.encog.ml.MLClassification;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.art.ART1;

import ps.deeplearningkit.core.neuralnetwork.utils.Assistant;
import ps.deeplearningkit.core.analysis.heuristic.RealVectorBehavior;

/**
 * Classification and clustering is used to provide additional values for the distance calculation.
 * @author Philipp Schlütermann
 *
 */
public class ClassifiedBehavior extends RealVectorBehavior{

	/**
	 * 
	 * @param values 
	 */
	public ClassifiedBehavior(double[] values) {
		super(values);
	}
	/**
	 * characterizes the values with neuralsearch networks.
	 * @param networks implementing MLMethods.
	 * @return an array of classifications for the behavior.
	 * @throws IOException
	 */
	public double[] getClassification(List<MLClassification> networks,int precision) throws IOException{
		double[] classifications=new double[networks.size()];
		for(int index=0;index<networks.size();index++){
			if(networks.get(index)instanceof ART1){
				classifications[index]=Assistant.clusterART1((ART1) networks.get(index),getVector().getDataRef(),precision);
			}
			else{
				classifications[index]=networks.get(index).classify(new BasicMLData(getVector().getDataRef()));
			}
		}
		return classifications;
	}
	public int getART1Classification(MLClassification classification,int precision){
		return Assistant.clusterART1((ART1) classification,getVector().getDataRef(),precision);
	}
	public int getSOMClassification(MLClassification classification){
		return classification.classify(new BasicMLData(getVector().getDataRef()));
	}
	/**
	 * The distanceFrom is extended to include the characterization
	 * @param classifiedBehavior characterized behavior.
	 * @return distance
	 * @throws IOException
	 */
	public double distanceFrom(ClassifiedBehavior classifiedBehavior,List<MLClassification> networks,int precision) throws IOException {
		double[] myClassification=getClassification(networks,precision);
		double[] theirClassification=classifiedBehavior.getClassification(networks,precision);
		return new ArrayRealVector(myClassification).getDistance(new ArrayRealVector(theirClassification))/myClassification.length;
	}
}
