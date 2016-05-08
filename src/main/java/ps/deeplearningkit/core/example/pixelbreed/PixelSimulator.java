package ps.deeplearningkit.core.example.pixelbreed;

import ps.deeplearningkit.core.search.noveltysearch.Behavior;
import ps.deeplearningkit.core.search.noveltysearch.NoveltySearch;
import ps.deeplearningkit.core.search.noveltysearch.RealVectorBehavior;
import ps.deeplearningkit.core.search.uct.Simulator;
import ps.deeplearningkit.core.search.uct.State;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by philipp on 5/6/16.
 */
public class PixelSimulator implements Simulator {

    private PixelState original;
    private State state;
    private NoveltySearch ns;
    public PixelSimulator(PixelState state, NoveltySearch ns){
        this.original=state;
        this.state=state.copy();
        this.ns=ns;
    }
    @Override
    public State getState() {
       return state;
    }

    @Override
    public void setState(State state) {
        this.state=state;
    }

    @Override
    public void takeAction(int a) {
        PixelAction action=((PixelState)this.state).getActions().get(a);
        ((PixelState)this.state).applyPixelAction(action);
    }

    @Override
    public int getNumActions() {
        return ((PixelState)this.state).getActions().size();
    }

    @Override
    public double getDiscountFactor() {
        return 0;
    }

    @Override
    public double getReward() {
        // possible follow up states
        Set<PixelState> nextStates=new HashSet<>();
        for(int i=0;i<getNumActions();i++){
            PixelAction action=((PixelState)this.state).getActions().get(i);
            PixelState next=(PixelState)getState();
            next.applyPixelAction(action);
            nextStates.add(next);
        }
        // evaluate their novelty and sum up
        double sumOfNovelty=0;
        for(PixelState each:nextStates){
            Behavior behavior= new RealVectorBehavior(each.toVector());
            ns.addToCurrentPopulation(behavior);
            sumOfNovelty+=ns.testNovelty(behavior);
        }
        return sumOfNovelty/nextStates.size();
    }

    @Override
    public void initEpisode() {
        this.state=original.copy();
    }
}
