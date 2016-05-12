package ps.deeplearningkit.core.simulator;

/**
 * Created by philipp on 5/12/16.
 */
public abstract class SmartSimulator implements Simulator{

    public double[] getInput(){
        double[] state=getState();
        double[] c=getClassification();
        double[] u=getUncommonnes();
        double[] n=getNovelty();
        double r=getReward();
        double[] data=new double[state.length+c.length+u.length+n.length+1];
        int i=0;
        for(double each:state){
            data[i]=each;
            i++;
        }
        for(double each:c){
            data[i]=each;
            i++;
        }
        for(double each:u){
            data[i]=each;
            i++;
        }
        for(double each:n){
            data[i]=each;
            i++;
        }
        data[i]=r;
        return data;
    }
    /**
     * This is useful for using multiple threads.
     * @return a exact copy of this simulator.
     */
    @Override
    public abstract SmartSimulator copy();

    /**
     * @return a representation of the state with values between 0 and 1.
     */
    public abstract double[] getState();
    /**
     * @return a classification of the state as a double values between 0 and 1.
     */
    public abstract double[] getClassification();
    /**
     * @return testNovelty by NoveltySearch, values should be between 0 and 1.
     */
    public abstract double[] getNovelty();
    /**
     * @return testUncommones by UncommonSearch, values should be between 0 and 1.
     */
    public abstract double[] getUncommonnes();
}
