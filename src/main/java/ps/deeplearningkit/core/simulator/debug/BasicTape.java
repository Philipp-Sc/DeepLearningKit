package ps.deeplearningkit.core.simulator.debug;

import java.util.ArrayList;

public class BasicTape<NeuralElement> implements NeuralTape<NeuralElement> {
	
	private ArrayList<NeuralElement> tape;
	private int maxTapeSize;
	private int pointerIndex;
	
	public BasicTape(int size){
		tape=new ArrayList<>(size);
		for(int i=0;i<size;i++){
				tape.add(null);
		}
		this.maxTapeSize=size;
		this.pointerIndex=0;
	}

	public int getPointer(){
		return pointerIndex;
	}
	public void movePointer(int index){
		if(index<maxTapeSize && index>0){
			this.pointerIndex=index;
		}
	}
	/**
	 * 
	 * @return the current string the pointer points to
	 */
	public NeuralElement getCurrent() {
		return tape.get(pointerIndex);
	}
	/**
	 * Sets the string the pointer points to
	 */
	public void setCurrent(NeuralElement obj) {
		tape.set(pointerIndex, obj);
	}
	public String toString(){
		String out="";
		for(NeuralElement each:tape){
			out+=" "+each.toString();
		}
		return out;
	}
}
