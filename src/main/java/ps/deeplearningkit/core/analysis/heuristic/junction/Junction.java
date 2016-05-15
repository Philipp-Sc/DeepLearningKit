package ps.deeplearningkit.core.analysis.heuristic.junction;

import ps.deeplearningkit.core.analysis.heuristic.Behavior;

import java.util.Collection;
import java.util.List;

/**
 * Created by philipp on 5/12/16.
 */
public interface Junction extends Behavior{

    List<Junction> getBranches();
    /**
     * @param known Junctions to discard from the branches.
     * @return if known.size() was 0, the return value would be all branches.
     */
    List<Junction> getUniqueJunctions(Collection<Junction> known);
}
