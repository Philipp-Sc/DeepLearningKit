package ps.deeplearningkit.core.simulator;

import ps.deeplearningkit.core.analysis.heuristic.Behavior;
import ps.deeplearningkit.core.analysis.heuristic.junction.Junction;

/**
 * Created by philipp on 5/15/16.
 */
public interface State<T> {

    State<T> copy();

    Behavior getBehavior();
    Junction getJunction();

    void applyAction(T action);
    T toAction(double[] action);

    boolean isAbsorbing();
    double getReward();

    void printState();
}
