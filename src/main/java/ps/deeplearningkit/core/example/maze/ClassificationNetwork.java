package ps.deeplearningkit.core.example.maze;

import org.encog.ml.MLClassification;
import org.encog.ml.data.MLData;
import org.encog.neural.art.ART1;
import ps.deeplearningkit.core.neuralnetwork.utils.Assistant;

/**
 * Created by philipp on 5/16/16.
 */
public class ClassificationNetwork implements MLClassification {

    private ART1 network;
    private int precision;
    public ClassificationNetwork(ART1 network,int precision){
        this.network=network;
        this.precision=precision;
    }

    /**
     * Classify the input into a group.
     *
     * @param input The input data to classify.
     * @return The group that the data was classified into.
     */
    @Override
    public int classify(MLData input) {
        return Assistant.clusterART1(network,input.getData(),precision);
    }

    /**
     * @return The input.
     */
    @Override
    public int getInputCount() {
        return network.getInputCount();
    }

    /**
     * @return The output count.
     */
    @Override
    public int getOutputCount() {
        return network.getOutputCount();
    }
}
