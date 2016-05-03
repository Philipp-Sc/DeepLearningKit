package ps.deeplearningkit.core.simulator;

public interface Simulator {
	
	public abstract double getReward();
	public abstract void improve(double[] action);
	public abstract void showState();
	public abstract boolean isFinished();
}
