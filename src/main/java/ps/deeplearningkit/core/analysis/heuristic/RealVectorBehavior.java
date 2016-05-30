package ps.deeplearningkit.core.analysis.heuristic;

import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;

/**
 * Implementation of Behavior.
 * Getting started quickly.
 */
public class RealVectorBehavior implements Behavior {

	private ArrayRealVector vector;
	private double maxDistance;
	// all values in the vector should in the range of [0,1].
	public RealVectorBehavior(double[] values) {
		this.vector=new ArrayRealVector(values);
		this.maxDistance=vector.getDimension();
	}
	@Override
	public double distanceFrom(Behavior b) {
		//return  vector.getL1Distance(b.getVector())/maxDistance;
		double distance=vector.getDistance(b.getVector());
		if(distance==0){
			return 0;
		}
		return distance/maxDistance;
	}
	@Override
	public double defaultThreshold() {
		return 1.0/vector.getDimension();
	}
	@Override
	public ArrayRealVector getVector(){
		return vector;
	}
	@Override
	public void renderArchive(List<Behavior> archive) {
		// TODO Auto-generated method stub
		
	}
}
