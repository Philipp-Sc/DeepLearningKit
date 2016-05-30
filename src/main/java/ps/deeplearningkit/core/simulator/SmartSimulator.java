package ps.deeplearningkit.core.simulator;

import org.encog.ml.MLClassification;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import ps.deeplearningkit.core.analysis.heuristic.Behavior;
import ps.deeplearningkit.core.analysis.heuristic.RewardStrategy;
import ps.deeplearningkit.core.analysis.heuristic.direction.search.DirectionSearch;
import ps.deeplearningkit.core.analysis.heuristic.junction.Junction;
import ps.deeplearningkit.core.analysis.heuristic.junction.search.JunctionSearch;
import ps.deeplearningkit.core.analysis.heuristic.novelty.search.NoveltySearch;
import ps.deeplearningkit.core.analysis.heuristic.uncommon.search.UncommonSearch;

/**
 * Created by philipp on 5/12/16.
 */
public class SmartSimulator<T> implements Simulator{

    private RewardStrategy rewardStrategy;

    private State<T> original;
    private State<T> state;

    public SmartSimulator(State<T> state){
        super();
        this.original=state;
        this.state=original.copy();

    }
    public void setRewardStrategy(RewardStrategy rewardStrategy){
        this.rewardStrategy=rewardStrategy;
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
        double[] data=new double[state.length+1];
        int i=0;
        for(double each:state){
            data[i]=each;
            i++;
        }
        data[i]=getReward();
        return data;
    }

    public double getStrategyReward(){
        Behavior behavior=this.getState().getBehavior();
      //  this.rewardStrategy.addToCurrentPopulation(behavior);
        double testBehavior=this.rewardStrategy.testBehavior(behavior);
        return testBehavior;
    }
    /**
     * @return testUncommones by UncommonSearch, values should be between 0 and 1.
     *
    public double[] getUncommonnes(){
        Behavior behavior=this.getState().getBehavior();
        double uncommonnes=this.uncommonSearch.testUncommonnes(behavior);
        return new double[]{uncommonnes};
    }
    /**
     * @return testDirection by DirectionSearch, values should be between 0 and 1.
     *
    public double[] getDirection(){
        Junction junction=this.getState().getJunction();
        double destiny=this.directionSearch.testDirection(junction);
        return new double[]{destiny};
    }
    /**
     * @return testJunction by JunctionSearch, values should be between 0 and 1.

    public double[] getJunction(){
        Junction junction=this.getState().getJunction();
        double value=this.junctionSearch.testJunction(junction);
        return new double[]{value};
    } */
    @Override
    public double getReward(){
        return state.getFitnessReward();//getStrategyReward();
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
