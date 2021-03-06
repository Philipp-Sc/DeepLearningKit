package ps.deeplearningkit.core.simulator;

import ps.deeplearningkit.core.analysis.heuristic.Behavior;
import ps.deeplearningkit.core.analysis.heuristic.junction.Junction;

import java.util.List;

/**
 * Created by philipp on 5/15/16.
 */
public interface State<T> {

    State<T> copy();

    Behavior getBehavior();
    Junction getJunction();

    List<T> getActions();

    void applyAction(T action);
    T toAction(double[] action);

    boolean isAbsorbing();
    double getFitnessReward();

    void printState();
}
