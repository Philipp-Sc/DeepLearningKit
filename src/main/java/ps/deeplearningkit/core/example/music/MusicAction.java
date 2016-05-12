package ps.deeplearningkit.core.example.music;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import ps.deeplearningkit.core.simulator.debug.BasicNeuralAction;

/**
 * Example implementation of BasicNeuralAction
 * This class is important for the whole implementation.
 * @author Philipp Schlültermann
 *
 */
public class MusicAction extends BasicNeuralAction{

	private int musicString;
	
	public MusicAction(double[] choice) throws IOException, InvalidMidiDataException{
		super(choice);
		/*
		 * deNormalize all values and save them here.
		 */
		musicString=(int) Notes.deNormalize(choice[0]);
	}
	public double getChoice(){
		return this.getNeuronValues()[0];
	}
	public int getMusicIndex(){
		return musicString;
	}
	public String getMusicString() throws IOException, InvalidMidiDataException{
		return Notes.getString(musicString);
	}
}
