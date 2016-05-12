package ps.deeplearningkit.core.example.music;
import java.io.IOException;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;

import ps.deeplearningkit.core.search.noveltysearch.Behavior;
import ps.deeplearningkit.core.simulator.debug.EndPoint;
import ps.deeplearningkit.core.simulator.debug.NeuralAction;

public class MusicEndPoint extends EndPoint<MusicState>{
	
	public MusicEndPoint(List<NeuralAction> actions) throws IOException, InvalidMidiDataException{
		super(actions);
	}
	public boolean isLegal(NeuralAction action){
		return false;
	}
	public Behavior getBehavior(){
		return null;
	}
	@Override
	public String showState() {
		//TODO
		return null;
	}
	@Override
	public MusicState initState() {
		// TODO 
		return null;
	}
	@Override
	public void applyAction(NeuralAction action) {
		try{
			MusicAction ma=new MusicAction(action.getNeuronValues());
			//TODO
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
