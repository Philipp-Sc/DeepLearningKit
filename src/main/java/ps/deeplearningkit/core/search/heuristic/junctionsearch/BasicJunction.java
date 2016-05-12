package ps.deeplearningkit.core.search.heuristic.junctionsearch;

import java.util.Collection;
import java.util.List;

/**
 * Created by philipp on 5/12/16.
 */
public abstract class BasicJunction implements Junction{

    protected abstract List<Junction> getBranches();

    /**
     * @param known Junctions to discard from the branches.
     * @return if known.size() was 0, the return value would be all branches.
     */
    @Override
    public List<Junction> getUniqueJunctions(Collection<Junction> known) {
        List<Junction> branches=getBranches();
        branches.removeAll(known);
        return branches;
    }
}
