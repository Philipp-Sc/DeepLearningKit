package ps.deeplearningkit.core.analysis.heuristic;

import java.util.List;

/**
 * Created by philipp on 5/29/16.
 */
public interface RewardStrategy {

    double testBehavior(Behavior behavior);
    void setCurrentPopulation(List<Behavior> behaviors);
    void addToCurrentPopulation(Behavior behavior);
    void finishedEvaluation();
}
