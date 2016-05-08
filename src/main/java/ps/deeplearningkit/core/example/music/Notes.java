package ps.deeplearningkit.core.example.music;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;

import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
//import org.jfugue.midi.MidiFileManager;

public class Notes {

	private static boolean initialized=false;
	private static List<String> musicStrings;
	private static NormalizedField musicField;
	private static void initialize() throws IOException, InvalidMidiDataException{
		List<String> midis=new ArrayList<>();
		String dir="/home/user-p/Documents/Project/Piano/midi/beeth/";
		midis.add(dir+"waldstein_1.mid");
		midis.add(dir+"waldstein_2.mid");
		midis.add(dir+"waldstein_3.mid");
		midis.add(dir+"pathetique_1.mid");
		midis.add(dir+"pathetique_2.mid");
		midis.add(dir+"pathetique_3.mid");
		midis.add(dir+"mond_1.mid");
		midis.add(dir+"mond_2.mid");
		midis.add(dir+"mond_3.mid");
		midis.add(dir+"beethoven_opus90_1.mid");
		midis.add(dir+"beethoven_opus90_2.mid");
		midis.add(dir+"beethoven_opus22_1.mid");
		midis.add(dir+"beethoven_opus22_2.mid");
		midis.add(dir+"beethoven_opus22_3.mid");
		midis.add(dir+"beethoven_opus22_4.mid");
		midis.add(dir+"beethoven_opus10_1.mid");
		midis.add(dir+"beethoven_opus10_2.mid");
		midis.add(dir+"beethoven_opus10_3.mid");
		midis.add(dir+"beethoven_les_adieux_1.mid");
		midis.add(dir+"beethoven_les_adieux_2.mid");
		midis.add(dir+"beethoven_les_adieux_3.mid");
		midis.add(dir+"appass_1.mid");
		midis.add(dir+"appass_2.mid");
		midis.add(dir+"appass_3.mid");
		midis.add(dir+"beethoven_hammerklavier_1.mid");
		midis.add(dir+"beethoven_hammerklavier_2.mid");
		midis.add(dir+"beethoven_hammerklavier_3.mid");
		midis.add(dir+"beethoven_hammerklavier_4.mid");
		midis.add(dir+"elise.mid");
		initMusicStrings(getMusicPieces(midis));
		initialized=true;
		musicField=new NormalizedField(NormalizationAction.Normalize, "music", getAHigh(), 0, 0.9, -0.9);
	}
	public static int deNormalize(double value) throws IOException, InvalidMidiDataException{
		if(!initialized){
			initialize();
		}
		return (int)musicField.deNormalize(value);
	}
	public static String getString(int musicString) throws IOException, InvalidMidiDataException{
		if(!initialized){
			initialize();
		}
		return musicStrings.get(musicString);
	}
	public static int getAHigh() throws IOException, InvalidMidiDataException{
		if(!initialized){
			initialize();
		}
		return musicStrings.size()-1;
	}
	public static boolean isValid(int index){
		if(index>=0 && index<musicStrings.size()){
			return true;
		}
		return false;
	}
	private static List<String> getMusicPieces(List<String> midi) throws IOException, InvalidMidiDataException{
		List<String> list=new ArrayList<>();
		for(String e:midi){
			list.add(getMusicPiece(e));
		}
		return list;
	}
	private static String getMusicPiece(String midi) throws IOException, InvalidMidiDataException{
		//String musicString = MidiFileManager.loadPatternFromMidi(new File(midi)).toString();
		return "";
	}
	private static void initMusicStrings(List<String> completePiece){
		Set<String> atomSet=new HashSet<>();
		for(String p:completePiece){
			for(String pp:p.split(" ")){
				if(pp.startsWith("@")){
					try{
						double e=Double.parseDouble(pp.replace("@", ""));
						if(e<=1.5){
							atomSet.add(pp);
						}
					}catch(NumberFormatException nfe){
						// do nothing
					}
				}else if(pp.contains("(") || pp.contains(")")){
					int count=0;
					for(int i=0;i<pp.length();i++){
						if(pp.charAt(i)=='('){
							count++;
						}else if(pp.charAt(i)==')'){
							count--;
						}
						if(count==0){
							atomSet.add(pp);
						}
					}
				}else{
					atomSet.add(pp);
				}
			}
		}
		musicStrings=new ArrayList<>(atomSet);	
	}
	
}
