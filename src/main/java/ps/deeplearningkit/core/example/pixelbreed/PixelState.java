package ps.deeplearningkit.core.example.pixelbreed;

import ps.deeplearningkit.core.search.uct.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philipp on 5/5/16.
 */
public class PixelState implements State{

    private boolean[][] plane;
    private int count=0;
    public PixelState(boolean[][] plane,int count){
        this.plane=plane;
    }
    public PixelState(int width,int height){
        plane=new boolean[width][height];
    }
    public int getWidth(){
        return plane.length;
    }
    public int getHeight(){
        return plane[0].length;
    }
    /*
     * It is assumed that the action is legal.
     */
    public void applyPixelAction(PixelAction action){
        int x=action.getX();
        int y=action.getY();
        boolean pixel=action.getPixel();
        plane[x][y] = pixel;
        count++;
    }
    public boolean isValid(PixelAction action){
        // the state still remains valid but it is not considered valid anymore
        // since actions were discarded.
        int x=action.getX();
        int y=action.getY();
        if(x<plane.length && x>=0 && y<plane[0].length && y>=0){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public String toString(){
        String result="";
        for(boolean[] line:plane){
            for(boolean item:line){
                if(item){
                    result+="X";
                }else{
                    result+="O";
                }
            }
            result+="\n";
        }
        return result;
    }
    public double[] toVector() {
        double[] vector = new double[plane.length * plane[0].length];
        int index = 0;
        for (boolean[] line : plane) {
            for (boolean item : line) {
                if (item) {
                    vector[index] = 1;
                } else {
                    vector[index] = 0;
                }
                index++;
            }
        }
        return vector;
    }
    public List<PixelAction> getActions(){
        List<PixelAction> list=new ArrayList<>();
        for(int i=0;i<plane.length;i++){
            for(int e=0;e<plane[i].length;e++){
                if(!plane[i][e]){
                    list.add(new PixelAction(new double[]{PixelAction.normalizeKoordinate1(i,getWidth()),PixelAction.normalizeKoordinate2(e,getHeight())},getWidth(),getHeight()));
                }
            }
        }
        return list;
    }

    @Override
    public State copy() {
       return new PixelState(this.plane.clone(),this.count);
    }

    @Override
    public boolean isAbsorbing() {
        return count>plane.length;
    }
}
