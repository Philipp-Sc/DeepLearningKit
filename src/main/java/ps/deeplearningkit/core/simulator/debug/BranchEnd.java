package ps.deeplearningkit.core.simulator.debug;

import ps.deeplearningkit.core.search.noveltysearch.Behavior;

public interface BranchEnd<T> {
	public abstract String showState();
	public abstract boolean isValid();
	public abstract Behavior getBehavior();
	public abstract T getState();
}
