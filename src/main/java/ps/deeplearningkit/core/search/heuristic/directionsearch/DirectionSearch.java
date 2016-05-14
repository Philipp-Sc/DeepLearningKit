package ps.deeplearningkit.core.search.heuristic.directionsearch;

import ps.deeplearningkit.core.search.heuristic.junctionsearch.Junction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by philipp on 5/13/16.
 * Search for Junctions that lead to similar Junctions the most Junctions lead to.
 * (waterfall,destiny,fate,..)
 */
public class DirectionSearch {

    /**
     * count of junction equivalence classes on occurrence.
     */
    private HashMap<Junction,Integer> occurrences=new HashMap<>();
    /**
     * @param junction
     * @return a high value if the junction leads to (similar) junctions that are leaded to by other junctions.
     */
    public double testDirection(Junction junction){
        // get all branches that are leaded to by the passed junction
        List<Junction> junctionList=junction.getBranches();
        // each branch is added to its equivalence class
        for(Junction each:junctionList){
            addOccurrence(each);
        }
        double sumOfOccurrences=0;
        for(Junction each:junctionList) {
            sumOfOccurrences += getOccurrenceCount(each);
        }
        return 1/sumOfOccurrences;
    }
    private double getOccurrenceCount(Junction junction){
        List<Junction> occurrences=getOccurrences(junction);
        double sum=0;
        for(Junction each:occurrences){
            sum+=this.occurrences.get(each);
        }
        return sum/occurrences.size();
    }
    private List<Junction> getOccurrences(Junction junction){
        List<Junction> all=new ArrayList<>();
        // for all equivalence classes of occurrences
        for(Junction occurrence:occurrences.keySet()){
            // if the junction belongs to this occurrence
            if(occurrence.distanceFrom(junction)<junction.defaultThreshold()){
               all.add(occurrence);
            }
        }
        return all;
    }
    private synchronized void addOccurrence(Junction junction){
        boolean isNew=true;
        // for all equivalence classes of occurrences
        for(Junction occurrence:occurrences.keySet()){
            // if the junction belongs to this occurrence
            if(occurrence.distanceFrom(junction)<junction.defaultThreshold()){
                // increment count of occurrence
                occurrences.put(occurrence,occurrences.get(occurrence)+1);
                // junction is not new.
                isNew=false;
            }
        }
        if(isNew){
            occurrences.put(junction,1);
        }
    }
}
