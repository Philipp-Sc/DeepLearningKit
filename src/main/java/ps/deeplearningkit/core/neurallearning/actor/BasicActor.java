package ps.deeplearningkit.core.neurallearning.actor;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
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
    public synchronized double scoreActor() {
        simulator.initEpisode();
        while(!simulator.isAbsorbing()){
            if(bn instanceof NEATNetwork){
                MLData input= new BasicMLData(simulator.getInput());
                MLData output=((NEATNetwork) this.bn).compute(input);
                simulator.applyAction(output.getData());
            }
        }
        track();
        return simulator.getReward();
    }
    private synchronized void track(){
        if(track){
            simulator.printStatus();
        }
    }


    @Override
    public List<NeuralAction> getNeuralActions() {
        return null;
    }
}
