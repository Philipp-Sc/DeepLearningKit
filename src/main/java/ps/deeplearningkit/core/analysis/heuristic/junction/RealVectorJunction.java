package ps.deeplearningkit.core.analysis.heuristic.junction;

import org.apache.commons.math3.linear.ArrayRealVector;
import ps.deeplearningkit.core.analysis.heuristic.Behavior;

import java.util.List;

/**
 * Created by philipp on 5/16/16.
 */
public abstract class RealVectorJunction extends BasicJunction{

    private ArrayRealVector vector;
    private double maxDistance;
    // all values in the vector should in the range of [0,1].
    public RealVectorJunction(double[] values) {
        this.vector=new ArrayRealVector(values);
        this.maxDistance=vector.getDimension();
    }
    @Override
    public double distanceFrom(Behavior b) {
        //return  vector.getL1Distance(b.getVector())/maxDistance;
        return vector.getDistance(b.getVector())/maxDistance;
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
