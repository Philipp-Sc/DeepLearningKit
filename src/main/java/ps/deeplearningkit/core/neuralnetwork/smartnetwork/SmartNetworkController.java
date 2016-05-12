package ps.deeplearningkit.core.neuralnetwork.smartnetwork;

import org.encog.ml.CalculateScore;
import ps.deeplearningkit.core.neuralnetwork.SimpleNetworkController;
import ps.deeplearningkit.core.search.noveltysearch.NoveltySearch;
import ps.deeplearningkit.core.search.uncommonsearch.UncommonSearch;

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
    public void createBasicMLAgent(String key, NoveltySearch noveltySearch, UncommonSearch uncommonSearch,
                                   CalculateScore someScore){

    }

}
