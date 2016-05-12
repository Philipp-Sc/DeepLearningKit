package ps.deeplearningkit.core.neurallearning.actor;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;
import ps.deeplearningkit.core.simulator.debug.NeuralAction;
import ps.deeplearningkit.core.simulator.Simulator;

import java.util.List;

/**
 * Created by philipp on 5/12/16.
 * Most basic actor simply runs a simulation using the MLMethod to progress.
 */
public class BasicActor extends NeuralActor {

    private Simulator simulator;
    public BasicActor(boolean track, Simulator simulator){
        super(track);
        this.simulator=simulator;
    }

    @Override
    public double scoreActor() {
        Simulator copiedSimulator=this.simulator.copy();
        copiedSimulator.init();
        while(!copiedSimulator.isAbsorbing()){
            if(bn instanceof NEATNetwork){
                MLData input= new BasicMLData(copiedSimulator.getInput());
                MLData output=((NEATNetwork) this.bn).compute(input);
                copiedSimulator.applyAction(output.getData());
            }
        }
        track();
        return 0;
    }
    private synchronized void track(){
        if(track){
            simulator.printState();
        }
    }


    @Override
    public List<NeuralAction> getNeuralActions() {
        return null;
    }
}
