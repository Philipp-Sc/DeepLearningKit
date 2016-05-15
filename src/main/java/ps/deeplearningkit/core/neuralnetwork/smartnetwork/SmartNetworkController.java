package ps.deeplearningkit.core.neuralnetwork.smartnetwork;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLClassification;
import org.encog.neural.neat.NEATNetwork;
import ps.deeplearningkit.core.MainController;
import ps.deeplearningkit.core.analysis.heuristic.direction.search.DirectionSearch;
import ps.deeplearningkit.core.analysis.heuristic.junction.search.JunctionSearch;
import ps.deeplearningkit.core.example.pixelbreed.PixelEndPoint;
import ps.deeplearningkit.core.neurallearning.actor.BasicActor;
import ps.deeplearningkit.core.neurallearning.actor.NeuralActor;
import ps.deeplearningkit.core.neuralnetwork.SimpleNetworkController;
import ps.deeplearningkit.core.analysis.heuristic.novelty.search.NoveltySearch;
import ps.deeplearningkit.core.analysis.heuristic.uncommon.search.UncommonSearch;
import ps.deeplearningkit.core.simulator.Simulator;
import ps.deeplearningkit.core.simulator.SmartSimulator;
import ps.deeplearningkit.core.simulator.State;
import ps.deeplearningkit.core.simulator.debug.NeuralAction;

import java.io.IOException;
import java.util.List;

/**
 * Created by philipp on 5/12/16.
 * Here complex network setups are managed
 */
public class SmartNetworkController extends SimpleNetworkController{

    public SmartNetworkController(String path){
        super(path);
    }

    /**
     *
     * @param key
     * @param noveltySearch The learning agent is provided a novelty rating for the current state.
     * @param uncommonSearch The learning agent is provided a rating how uncommon the classification
     * of the current state is.
     * @param someScore The learning agent is provided a CalculateScore for fitness evaluation.
     */
    public void createBasicMLAgent(String key,
                                   State state,
                                   MLClassification classification,
                                   NoveltySearch noveltySearch,
                                   UncommonSearch uncommonSearch,
                                   DirectionSearch directionSearch,
                                   JunctionSearch junctionSearch,
                                   CalculateScore someScore) throws IOException {
        Simulator simulator=new SmartSimulator<Object>(state,
                classification,
                noveltySearch,
                uncommonSearch,
                junctionSearch,
                directionSearch);

        NeuralActor neuralActor = new BasicActor(true, simulator);
        MainController.getAdvancedNetworkController().createNEATPopulation(key, 26, 4, 1500);
        MainController.getAdvancedNetworkController().trainNEATPopulation(key, someScore, 999, 1);
        NEATNetwork nnn = MainController.getAdvancedNetworkController().getBestNEATNetwork(key, someScore);

        neuralActor.setTrack(true);
        neuralActor.setMLMethod(nnn);
        neuralActor.scoreActor();
    }
}
