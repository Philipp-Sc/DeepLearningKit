package ps.deeplearningkit.core.example;

import org.encog.ml.CalculateScore;
import ps.deeplearningkit.core.MainController;
import ps.deeplearningkit.core.analysis.heuristic.novelty.search.NoveltySearch;
import ps.deeplearningkit.core.example.maze.Maze;
import ps.deeplearningkit.core.example.maze.MazeMovement;
import ps.deeplearningkit.core.neurallearning.actor.BasicActor;
import ps.deeplearningkit.core.neurallearning.actor.NeuralActor;
import ps.deeplearningkit.core.neurallearning.score.BasicScore;
import ps.deeplearningkit.core.simulator.SmartSimulator;
import ps.deeplearningkit.core.simulator.State;

import java.io.IOException;
import java.util.Random;

/**
 * Created by philipp on 5/19/16.
 */
public class Main {


    public static NoveltySearch ns=new NoveltySearch();
    public static int max=Integer.MIN_VALUE;
    public static Random random=new Random();

    public static void main(String[] args) throws IOException {
        String key="key";
        State<MazeMovement> state=new Maze();
        //NoveltySearch noveltySearch=new NoveltySearch();

        //MainController.getSimpleNetworkController().createART1("art1",1,1,1);

        //MLClassification classification1=MainController.getSimpleNetworkController().getART1Network("art1");
        //UncommonSearch uncommonSearch=new UncommonSearch(classification1);
        //uncommonSearch.setPrecision(10000);

        //ClassificationNetwork classificationNetwork=new ClassificationNetwork((ART1) classification1,1);

        //JunctionSearch junctionSearch=new JunctionSearch();
        //DirectionSearch directionSearch=new DirectionSearch();

        ps.deeplearningkit.core.simulator.Simulator simulator=new SmartSimulator<MazeMovement>(state,
                null,
                null,
                null,
                null,
                null);

        NeuralActor neuralActor = new BasicActor(false, simulator);
        CalculateScore someScore = new BasicScore(neuralActor, false, false);

        MainController.getAdvancedNetworkController().createNEATPopulation(key, 17,4, 500);
        MainController.getAdvancedNetworkController().trainNEATPopulation(key, someScore, 25, 100);
    }
}
