package ps.deeplearningkit.core.analysis.heuristic;

import java.util.List;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 * A behavior is basically a result of actions:
 * - most importantly you can calculate the distance between behaviors
 * @author Philipp Schlütermann
 *
 */
public interface Behavior {
	// determine the distance in behavior space in the range of [0,1].
	public abstract double distanceFrom(Behavior b);
	// Provide a default threshold in the range of [0,1].
	// This threshold tells when a behavior is new and different.
	public abstract double defaultThreshold();
	// can be overridden.
	public abstract ArrayRealVector getVector();
	// may want to override this method for logging/visualization.
	public abstract void renderArchive(List<Behavior> archive);

}
