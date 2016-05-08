package ps.deeplearningkit.core.example.pixelbreed;

import ps.deeplearningkit.core.search.noveltysearch.Behavior;
import ps.deeplearningkit.core.search.noveltysearch.RealVectorBehavior;
import ps.deeplearningkit.core.simulator.NeuralAction;

import java.util.List;
import ps.deeplearningkit.core.simulator.EndPoint;
/**
 * Created by philipp on 5/5/16.
 */
public class PixelEndPoint extends EndPoint<PixelState> {

    private final int value=5;
    public PixelEndPoint(List<NeuralAction> actions){
        super(actions);
    }
    public boolean isLegal(NeuralAction action){
        return this.getState().isValid(new PixelAction(action.getNeuronValues(),value,value));
    }
    public Behavior getBehavior(){
        return new RealVectorBehavior(this.getState().toVector());
    }
    public String showState() {
        return this.getState().toString();
    }
    public PixelState initState() {
        return new PixelState(value,value);
    }
    public void applyAction(NeuralAction action) {
        this.getState().applyPixelAction(new PixelAction(action.getNeuronValues(),value,value));
    }
}
