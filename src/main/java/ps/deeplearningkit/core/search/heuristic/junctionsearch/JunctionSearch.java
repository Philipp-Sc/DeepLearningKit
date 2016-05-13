package ps.deeplearningkit.core.search.heuristic.junctionsearch;

import java.util.HashSet;
import java.util.List;

/**
 * Created by philipp on 5/8/16.
 * Searching for unknown junctions.
 * TODO: Could be extended with novelty search. (Would not lose much, but gain a wider space.)
 */
public class JunctionSearch {

    private HashSet<Junction> knownJunctions=new HashSet<>();
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
