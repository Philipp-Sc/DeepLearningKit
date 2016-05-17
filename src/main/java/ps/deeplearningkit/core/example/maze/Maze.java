package ps.deeplearningkit.core.example.maze;

import ps.deeplearningkit.core.analysis.heuristic.Behavior;
import ps.deeplearningkit.core.analysis.heuristic.RealVectorBehavior;
import ps.deeplearningkit.core.analysis.heuristic.junction.Junction;
import ps.deeplearningkit.core.analysis.heuristic.junction.RealVectorJunction;
import ps.deeplearningkit.core.example.Application;
import ps.deeplearningkit.core.simulator.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philipp on 5/16/16.
 */
public class Maze implements State<MazeMovement>{

    private final int maxActions=20;

    private enum Obj {WALL,SPACE,START,EXIT,UNKNOWN,VISITED}
    private Obj[][] maze;
    // history of positions
    private ArrayList<Integer> x=new ArrayList<>();
    private ArrayList<Integer> y=new ArrayList<>();

    private int xExit;
    private int yExit;

    private boolean isDead=false;

    public Maze(){
        initMaze();
    }
    public Maze(Obj[][] maze,ArrayList<Integer> x,ArrayList<Integer> y,boolean isDead){
        this.isDead=isDead;
        this.maze=maze;
        this.x=new ArrayList<>();
        for(Integer each:x){
            this.x.add(each);
        }
        this.y=new ArrayList<>();
        for(Integer each:y){
            this.y.add((each));
        }
        initExitPosition();
    }
    private void initMaze(){
        maze=new Obj[10][10];
        maze[0]=new Obj[]{Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[1]=new Obj[]{Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[2]=new Obj[]{Obj.WALL,Obj.SPACE,Obj.WALL,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[3]=new Obj[]{Obj.WALL,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[4]=new Obj[]{Obj.WALL,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[5]=new Obj[]{Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[6]=new Obj[]{Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.EXIT,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[7]=new Obj[]{Obj.WALL,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[8]=new Obj[]{Obj.WALL,Obj.START,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[9]=new Obj[]{Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        initStartingPosition();
        initExitPosition();
    }
    private void initStartingPosition(){
        for(int x=0;x<maze.length;x++){
            for(int y=0;y<maze[x].length;y++){
                if(maze[x][y].equals(Obj.START)){
                    this.x.add(x);
                    this.y.add(y);
                    return;
                }
            }
        }
    }
    private void initExitPosition(){
        for(int x=0;x<maze.length;x++){
            for(int y=0;y<maze[x].length;y++){
                if(maze[x][y].equals(Obj.EXIT)){
                    xExit=x;
                    yExit=y;
                    return;
                }
            }
        }
    }
    private Obj[][] getAgentMazeView(){
        Obj[][] agentMaze=new Obj[maze.length][maze[0].length];
        for(int i=0;i<agentMaze.length;i++){
            for(int ii=0;ii<agentMaze[i].length;ii++){
                agentMaze[i][ii]=Obj.UNKNOWN;
            }
        }
        for(int i=0;i<x.size();i++){
            if(y.get(i)+1<maze[0].length && y.get(i)+1>=0){
                agentMaze[x.get(i)][y.get(i)+1]=maze[x.get(i)][y.get(i)+1];
            }
            if(y.get(i)-1<maze[0].length && y.get(i)-1>=0){
                agentMaze[x.get(i)][y.get(i)-1]=maze[x.get(i)][y.get(i)-1];
            }
            if(x.get(i)-1<maze.length && x.get(i)-1>=0) {
                if(y.get(i)-1<maze[0].length && y.get(i)-1>=0){
                    agentMaze[x.get(i) - 1][y.get(i) - 1] = maze[x.get(i) - 1][y.get(i) - 1];
                }
                agentMaze[x.get(i) - 1][y.get(i)] = maze[x.get(i) - 1][y.get(i)];
                if(y.get(i)+1<maze[0].length && y.get(i)+1>=0){
                    agentMaze[x.get(i) - 1][y.get(i) + 1] = maze[x.get(i) - 1][y.get(i) + 1];
                }
            }
            if(x.get(i)+1<maze.length && x.get(i)+1>=0) {
                if(y.get(i)-1<maze.length && y.get(i)-1>=0) {
                    agentMaze[x.get(i) + 1][y.get(i) - 1] = maze[x.get(i) + 1][y.get(i) - 1];
                }
                agentMaze[x.get(i) + 1][y.get(i)] = maze[x.get(i) + 1][y.get(i)];
                if(y.get(i)+1<maze.length && y.get(i)+1>=0) {
                    agentMaze[x.get(i) + 1][y.get(i) + 1] = maze[x.get(i) + 1][y.get(i) + 1];
                }
            }
        }
        for(int i=0;i<x.size();i++){
            agentMaze[x.get(i)][y.get(i)]=Obj.VISITED;
        }
        return agentMaze;
    }

    @Override
    public State<MazeMovement> copy() {
       return new Maze(maze.clone(),(ArrayList<Integer>) x,(ArrayList<Integer>)y,this.isDead);
    }

    @Override
    public Behavior getBehavior() {
        Obj[][] maze=getAgentMazeView();
        double[][] agentMaze=new double[maze.length][maze[0].length];
        double val=1/6;
        for(int x=0;x<agentMaze.length;x++){
            for(int y=0;y<agentMaze[x].length;y++){
                if(maze[x][y].equals(Obj.UNKNOWN)){
                    agentMaze[x][y]=val;
                }else if(maze[x][y].equals(Obj.WALL)){
                    agentMaze[x][y]=2*val;
                }else if(maze[x][y].equals(Obj.SPACE)){
                    agentMaze[x][y]=3*val;
                }else if(maze[x][y].equals(Obj.VISITED)){
                    agentMaze[x][y]=4*val;
                }else if(maze[x][y].equals(Obj.START)){
                    agentMaze[x][y]=5*val;
                }else if(maze[x][y].equals(Obj.EXIT)){
                    agentMaze[x][y]=6*val;
                }
            }
        }
        double[] vector=new double[(maze.length*maze[0].length)+1];
        int i=0;
        for(double[] each:agentMaze){
            for(double e:each){
             vector[i]=e;
                i++;
            }
        }
        if(isDead){
            vector[i]=0;
        }else{
            vector[i]=1;
        }
        return new RealVectorBehavior(vector);
    }

    @Override
    public Junction getJunction() {
        Junction junction=new RealVectorJunction(this.getBehavior().getVector().getDataRef()) {
            private State<MazeMovement> state=Maze.this.copy();
            @Override
            public List<Junction> getBranches() {
                if(state.isAbsorbing()){
                    return new ArrayList<Junction>();
                }
                List<Junction> junctionList=new ArrayList<Junction>();
                for(MazeMovement action:state.getActions()){
                    State<MazeMovement> state1=state.copy();
                    state1.applyAction(action);
                    junctionList.add(state1.getJunction());
                }
                return junctionList;
            }
        };
        return junction;
    }

    @Override
    public void applyAction(MazeMovement action) {
        if(!isDead) {
            if(action.transformX(2)==0 && action.transformY(2)==0){
                isDead=true;
                return;
            }
            int x = action.transformX(this.x.get(this.x.size() - 1));
            int y = action.transformY(this.y.get(this.y.size() - 1));
            if (x >= 0 && x < maze.length && y >= 0 && y < maze[0].length) {
                if (maze[x][y].equals(Obj.WALL)) {
                    isDead = true;
                }else{
                   // System.out.println("x:"+x+"y:"+y);
                    this.x.add(x);
                    this.y.add(y);
                }
            } else {
                // index out of bounds
                isDead=true;
            }
        }
    }
    private boolean isLegal(MazeMovement action){
        if(!isDead) {
            int x = action.transformX(this.x.get(this.x.size() - 1));
            int y = action.transformY(this.y.get(this.y.size() - 1));
            if (x >= 0 && x < maze.length && y >= 0 && y < maze[0].length) {
                if (maze[x][y].equals(Obj.WALL)) {
                   return false;
                }else{
                    return true;
                }
            } else {
                // index out of bounds
               return false;
            }
        }
        return false;
    }

    @Override
    public MazeMovement toAction(double[] action) {
        boolean hNone=false;
        boolean vNone=false;
        boolean left=false;
        boolean down=false;
        if(action[0]>0.5){
            hNone=true;
        }
        if(action[3]<0.5 && action[3]>0){
            vNone=true;
        }
        if(action[2]>0.5){
            left=true;
        }
        if(action[2]>0.5){
            down=true;
        }
        if(hNone && vNone){
            return new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.NONE);
        }else if(hNone){
            if(down){
                return new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.DOWN);
            }else{
                return new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.UP);
            }
        }else if(vNone){
            if(left){
                return new MazeMovement(MazeMovement.Horizontal.LEFT, MazeMovement.Vertical.NONE);
            }else{
                return new MazeMovement(MazeMovement.Horizontal.RIGHT, MazeMovement.Vertical.NONE);
            }
        }else {
            if(down && left){
                return new MazeMovement(MazeMovement.Horizontal.LEFT, MazeMovement.Vertical.DOWN);
            }else if(down){
                return new MazeMovement(MazeMovement.Horizontal.RIGHT, MazeMovement.Vertical.DOWN);
            }else  if(left){
                return new MazeMovement(MazeMovement.Horizontal.LEFT, MazeMovement.Vertical.UP);
            }else{
                return new MazeMovement(MazeMovement.Horizontal.RIGHT, MazeMovement.Vertical.UP);
            }
        }

        /*NormalizedField normalizedField=new NormalizedField(NormalizationAction.Normalize,"action",9,1,0.9,-0.9);
        int a=(int)normalizedField.deNormalize(action[0]);
        //System.out.println(a);
        return new MazeMovement(a);*/
       /* double val=1.0/8.0;
        for(int i=1;i<=8;i++){
            if(action[0]<=i*val){
                return new MazeMovement(i+1);
            }
        }*/
       // System.out.println("Possible Error!!"+action[0]);
       // return new MazeMovement(9);
    }
    @Override
    public List<MazeMovement> getActions(){
        MazeMovement m1=new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.NONE);
        MazeMovement m2=new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.UP);
        MazeMovement m3=new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.DOWN);
        MazeMovement m4=new MazeMovement(MazeMovement.Horizontal.LEFT, MazeMovement.Vertical.NONE);
        MazeMovement m5=new MazeMovement(MazeMovement.Horizontal.LEFT, MazeMovement.Vertical.UP);
        MazeMovement m6=new MazeMovement(MazeMovement.Horizontal.LEFT, MazeMovement.Vertical.DOWN);
        MazeMovement m7=new MazeMovement(MazeMovement.Horizontal.RIGHT, MazeMovement.Vertical.NONE);
        MazeMovement m8=new MazeMovement(MazeMovement.Horizontal.RIGHT, MazeMovement.Vertical.UP);
        MazeMovement m9=new MazeMovement(MazeMovement.Horizontal.RIGHT, MazeMovement.Vertical.DOWN);
        List<MazeMovement> actions=new ArrayList<>();
        if(isLegal(m1)){
           actions.add(m1);
        }
        if(isLegal(m2)){
            actions.add(m2);
        }
        if(isLegal(m3)){
            actions.add(m3);
        }
        if(isLegal(m4)){
            actions.add(m4);
        }
        if(isLegal(m5)){
            actions.add(m5);
        }
        if(isLegal(m6)){
            actions.add(m6);
        }
        if(isLegal(m7)){
            actions.add(m7);
        }
        if(isLegal(m8)){
            actions.add(m8);
        }
        if(isLegal(m9)){
            actions.add(m9);
        }
        return actions;
    }

    @Override
    public boolean isAbsorbing() {
       return  isDead ||x.size()>maxActions|| (x.get(x.size()-1)==xExit && y.get(y.size()-1)==yExit);
    }

    @Override
    public double getReward() {
        // no reward for illegal actions
        if(isDead){

            return -1.0/x.size();
        }
        // high reward for archiving the goal
        if((x.get(x.size()-1)==xExit && y.get(y.size()-1)==yExit)){
            return x.size()*1000;
        }else{
            /*double val1=Math.abs(x.get(x.size()-1)-xExit);
            val1*=val1;
            double val2=Math.abs(y.get(y.size()-1)-yExit);
            val2*=val2;
            double reward=Math.sqrt(val1+val2);*/
            return  x.size();
        }
    }

    @Override
    public void printState() {
        Obj[][] maze=getAgentMazeView();
        for(int x=0;x<maze.length;x++){
            for(int y=0;y<maze[x].length;y++){
                System.out.print(maze[x][y].toString()+" ");
            }
            System.out.println();
        }
        for (int i=0;i<x.size();i++){
            System.out.println("x: "+x.get(i)+" y: "+y.get(i));
        }
    }
}
