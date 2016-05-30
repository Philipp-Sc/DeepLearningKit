package ps.deeplearningkit.core.neurallearning.actor;

import org.encog.ml.MLMethod;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;
import ps.deeplearningkit.core.example.Application;
import ps.deeplearningkit.core.simulator.Simulator;
import ps.deeplearningkit.core.simulator.debug.NeuralAction;

import java.util.List;

/**
 * Created by philipp on 5/29/16.
 * Trains the network to choose a network to compute the action.
 */
public class ChoosingActor extends NeuralActor {

    Simulator simulator;
    List<MLMethod> mlMethods;

    public ChoosingActor(boolean track, Simulator simulator) {
        super(track);
        this.simulator=simulator;
    }
    public void setMLMethods(List<MLMethod> methods){
        this.mlMethods=methods;
    }

    public synchronized double scoreActor() {
        simulator.initEpisode();
        while(!simulator.isAbsorbing()){
            if(bn instanceof NEATNetwork){
                MLData input= new BasicMLData(simulator.getInput());
                MLData output=((NEATNetwork) this.bn).compute(input);
                simulator.applyAction(computeAction(output,input).getData());
            }
        }
        double score=simulator.getReward();
        if(score>Application.maxScore){
            track();
            Application.maxScore=score;
        }
        return score;
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

    private MLData computeAction(MLData choice,MLData input) {
        double[] data = choice.getData();
        int number = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > 0) {
                number += Math.pow(2, i);
            }
        }
        MLMethod selected = mlMethods.get(number);
        if(selected instanceof NEATNetwork){
            MLData output=((NEATNetwork) selected).compute(input);
            return output;
        }else {
            return null;
        }
    }
}
