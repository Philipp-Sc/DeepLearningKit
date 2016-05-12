package ps.deeplearningkit.core.simulator.debug;

import ps.deeplearningkit.core.simulator.Simulator;
import ps.deeplearningkit.core.simulator.debug.BasicTape;
import ps.deeplearningkit.core.simulator.debug.NeuralTape;

public abstract class TapeSimulator<NeuralElement> implements Simulator {

	private NeuralTape<NeuralElement> tape;
	private int iterations;
	
	public TapeSimulator(int maxTapeSize,int iterations) {
		tape=new BasicTape<>(maxTapeSize);
		this.iterations=iterations;
	}
	public void applyAction(double[] choice) {
		tape.movePointer((int)choice[0]);
		tape.setCurrent(getChoice(choice));
		iterations--;
	}
	public void printState() {
		System.out.println(tape.toString());
	}
	public boolean isAbsorbing() {
		if(iterations>0){
				return false;
			}else{
				return true;
		}
	}
	public int getPointer(){
		return tape.getPointer();
	}
	public NeuralElement getCurrent(){
		return tape.getCurrent();
	}
	public NeuralTape<NeuralElement> getTape(){
		return tape;
	}

	public abstract double getReward();
	protected abstract NeuralElement getChoice(double[] choice);
	public abstract double[] getStateStats(int size);
}
