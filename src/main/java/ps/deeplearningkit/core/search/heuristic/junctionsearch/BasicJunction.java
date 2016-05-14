package ps.deeplearningkit.core.search.heuristic.junctionsearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by philipp on 5/12/16.
 */
public abstract class BasicJunction implements Junction{

    public abstract List<Junction> getBranches();

    /** Here unique means the junctions are different,
     * disparity is defined by the defaultThreshold and distanceFrom.
     *
     * @param known (to similar to known) junctions to discard from the branches.
     * @return if known.size() was 0, the return value would be all branches.
     */
    @Override
    public List<Junction> getUniqueJunctions(Collection<Junction> known) {
        List<Junction> uniqueJunctions=new ArrayList<>();
        // check whether the branch is new.
        for(Junction branch:getBranches()){
            boolean same=false;
            // for all known junctions check
            for(Junction k:known){
                // if the branch is different
                if(branch.distanceFrom(k)<branch.defaultThreshold()){
                    same=true;
                    break;
                }
            }
            if(!same){
                uniqueJunctions.add(branch);
            }
        }
        return uniqueJunctions;
    }

}
