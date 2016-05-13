package ps.deeplearningkit.core.search.heuristic.directionsearch;

import ps.deeplearningkit.core.search.heuristic.junctionsearch.Junction;

import java.util.HashMap;
import java.util.List;

/**
 * Created by philipp on 5/13/16.
 * Search for Junctions that lead to Junctions the most Junctions lead to.
 * (waterfall,destiny,fate,..)
 * TODO: Could be extended with novelty search.
 */
public class DirectionSearch {

    /**
     * Need to rate junctions on occurrence.
     */
    private HashMap<Junction,Integer> occurrences=new HashMap<>();
    /**
     * @param junction
     * @return a high value if the junction leads to junctions that are leaded to by other junctions.
     */
    public double testDirection(Junction junction){
        List<Junction> junctionList=junction.getBranches();
        for(Junction each:junctionList){
            addOccurrence(each);
        }
        int sumOfOccurrences=0;
        for(Junction each:junctionList) {
            sumOfOccurrences += occurrences.get(each);
        }
        return 1/sumOfOccurrences;
    }
    private synchronized void addOccurrence(Junction junction){
        if(occurrences.containsKey(junction)){
            occurrences.put(junction,occurrences.get(junction)+1);
        }else{
            occurrences.put(junction,1);
        }
    }
}
