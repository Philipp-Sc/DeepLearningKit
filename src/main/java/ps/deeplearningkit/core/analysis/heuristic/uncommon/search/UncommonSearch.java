package ps.deeplearningkit.core.analysis.heuristic.uncommon.search;

import org.encog.ml.MLClassification;
import org.encog.neural.art.ART1;
import ps.deeplearningkit.core.analysis.heuristic.novelty.neuralsearch.ClassifiedBehavior;
import ps.deeplearningkit.core.analysis.heuristic.Behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by philipp on 5/8/16.
 *
 * Searching for the least common classes of behaviors.
 * Knowing how common/uncommon a behavior is might give you very valuable insights.
 */
public class UncommonSearch {

    private int precision=10000;
    private MLClassification classification;
    private HashMap<Integer,Integer> classCount=new HashMap<>();

    public UncommonSearch(MLClassification classification){
        this.classification=classification;
    }
    public void setPrecision(int precision){
        this.precision=precision;
    }
    public double testUncommonnes(Behavior behavior){
        ClassifiedBehavior classifiedBehavior=new ClassifiedBehavior(behavior.getVector().getDataRef());
        int c;
        if(classification instanceof ART1){
            c=classifiedBehavior.getART1Classification(classification,precision);
        }else{
            c=classifiedBehavior.getSOMClassification(classification);
        }
        addClass(c);
        return 1/getDensityOf(c);
    }
    private double getDensityOf(int c){
        ArrayList<Integer> distribution=new ArrayList<>();
        double totalDensity=0;
        for(int densityRadius=0;densityRadius<=1;densityRadius++) {
            // calculates the density of c for the densityRadius.
            for (int i = Math.max(0,c - densityRadius); i <= c + densityRadius; i++) {
               if(classCount.containsKey(i)){
                  distribution.add(classCount.get(i));
               }
            }
            double density = 0;
            for (int n : distribution) {
                density += n;
            }
            distribution.clear();
            // density function could be changed..
            if(densityRadius!=0 && density!=0){
                density =density/(densityRadius + Math.max(c - densityRadius, 0));
            }
            // the higher the densityRadius the less the density is worth.
            if(density!=0 && densityRadius!=0){
                totalDensity+=(density/densityRadius); //densityRadius*densityRadius
            }
        }
        showMaxClass();
        return totalDensity;
    }
    private synchronized void addClass(int c){
        if(classCount.containsKey(c)){
            classCount.put(c,classCount.get(c)+1);
        }else {
            classCount.put(c,1);
        }
    }
    private void showMaxClass(){
        if(Collections.max(classCount.keySet())>=classification.getOutputCount()){
            System.out.println("Error: ART1 no more free classes available..");
        }
    }
}
