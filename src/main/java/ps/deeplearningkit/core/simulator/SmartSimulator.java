package ps.deeplearningkit.core.simulator;

import org.encog.ml.MLClassification;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import ps.deeplearningkit.core.analysis.heuristic.Behavior;
import ps.deeplearningkit.core.analysis.heuristic.direction.search.DirectionSearch;
import ps.deeplearningkit.core.analysis.heuristic.junction.Junction;
import ps.deeplearningkit.core.analysis.heuristic.junction.search.JunctionSearch;
import ps.deeplearningkit.core.analysis.heuristic.novelty.search.NoveltySearch;
import ps.deeplearningkit.core.analysis.heuristic.uncommon.search.UncommonSearch;

/**
 * Created by philipp on 5/12/16.
 */
public class SmartSimulator<T> implements Simulator{

    private MLClassification classification;

    private NoveltySearch noveltySearch;
    private UncommonSearch uncommonSearch;
    private JunctionSearch junctionSearch;
    private DirectionSearch directionSearch;

    private State<T> original;
    private State<T> state;

    public SmartSimulator(State<T> state,
                          MLClassification classification,
                          NoveltySearch noveltySearch,
                          UncommonSearch uncommonSearch,
                          JunctionSearch junctionSearch,
                          DirectionSearch directionSearch){
        super();
        this.original=state;
        this.state=original.copy();
        this.classification=classification;
        this.noveltySearch=noveltySearch;
        this.uncommonSearch=uncommonSearch;
        this.junctionSearch=junctionSearch;
        this.directionSearch=directionSearch;
    }
    public void initEpisode(){
        this.state=original.copy();
        // one might want to reset more.
    }
    public void setState(State state){
        this.state=state;
    }
    public State getState(){
        return this.state;
    }

    public double[] getInput(){
        double[] state=getState().getBehavior().getVector().getDataRef();
        double[] c=new double[]{};//getClassification();
        double[] u=new double[]{};//getUncommonnes();
        double[] n=getNovelty();
        double[] d=getDirection();
        double[] j=getJunction();
        double r=getReward();
        double[] data=new double[state.length+c.length+u.length+n.length+d.length+j.length+1];
        int i=0;
        for(double each:state){
            data[i]=each;
            i++;
        }
        for(double each:c){
            data[i]=each;
            i++;
        }
        for(double each:u){
            data[i]=each;
            i++;
        }
        for(double each:n){
            data[i]=each;
            i++;
        }
        for(double each:d){
            data[i]=each;
            i++;
        }
        for(double each:j){
            data[i]=each;
            i++;
        }
        data[i]=r;
        return data;
    }

    /**
     * @return a classification of the state as a double values between 0 and 1.
     */
    public double[] getClassification(){
        Behavior behavior=this.getState().getBehavior();
        MLData input=new BasicMLData(behavior.getVector().getDataRef());
        return new double[]{this.classification.classify(input)};
    }
    /**
     * @return testNovelty by NoveltySearch, values should be between 0 and 1.
     */
    public double[] getNovelty(){
        Behavior behavior=this.getState().getBehavior();
        this.noveltySearch.addToCurrentPopulation(behavior);
        double novelty=this.noveltySearch.testNovelty(behavior);
        return new double[]{novelty};
    }
    /**
     * @return testUncommones by UncommonSearch, values should be between 0 and 1.
     */
    public double[] getUncommonnes(){
        Behavior behavior=this.getState().getBehavior();
        double uncommonnes=this.uncommonSearch.testUncommonnes(behavior);
        return new double[]{uncommonnes};
    }
    /**
     * @return testDirection by DirectionSearch, values should be between 0 and 1.
     */
    public double[] getDirection(){
        Junction junction=this.getState().getJunction();
        double destiny=this.directionSearch.testDirection(junction);
        return new double[]{destiny};
    }
    /**
     * @return testJunction by JunctionSearch, values should be between 0 and 1.
     */
    public double[] getJunction(){
        Junction junction=this.getState().getJunction();
        double value=this.junctionSearch.testJunction(junction);
        return new double[]{value};
    }
    public double getReward(){
        return state.getReward();
    }
    public boolean isAbsorbing(){
        return this.state.isAbsorbing();
    }
    public void applyAction(double[] action){
        this.state.applyAction(state.toAction(action));
    }
    public void printStatus(){
        this.state.printState();
    }
}
