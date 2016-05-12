package ps.deeplearningkit.core.search.heuristic.junctionsearch;

import java.util.HashSet;
import java.util.List;

/**
 * Created by philipp on 5/8/16.
 * Searching for unknown junctions.
 */
public class JunctionSearch {

    private HashSet<Junction> knownJunctions=new HashSet<>();
    // TODO: second metric: junctions everything tend to lead to (waterfall)
    /**
     * @param junction
     * @return a high value if the junction leads to many unknown junctions.
     */
    public double testJunction(Junction junction){
        List<Junction> junctionList=junction.getUniqueJunctions(knownJunctions);
        int junctions=junctionList.size();
        knownJunctions.addAll(junctionList);
        return junctions;
       }
}
