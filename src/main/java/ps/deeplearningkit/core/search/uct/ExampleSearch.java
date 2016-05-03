package ps.deeplearningkit.core.search.uct;


import java.util.Random;

/*
 * Example Search
 */
public class ExampleSearch {
	public static void main(String[] args) throws Exception {
			
			// "real" world
			Simulator simReal = null;
			// simulator for planning
			Simulator simPlan = null;
			int trajectories = 50;		
			int depth = 10;
			UCT planner = new UCT(simPlan, trajectories, depth,
					simPlan.getDiscountFactor(), new Random());
			planner.ucb_scaler = 1;
			State currState;		
			for (int timestep = 0; timestep < 20; timestep++) {
				System.out.println(timestep);
				currState = simReal.getState();
				int a = planner.planAndAct(currState);
				System.out.print("Q: ");
				for(int i = 0; i<4;i++)
					System.out.printf("%.4f ",planner.getQ(i));
				System.out.println();
				simReal.takeAction(a);
				Thread.sleep(100);
			}
	}
}
