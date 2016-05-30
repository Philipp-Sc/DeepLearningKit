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
        maze[0]=new Obj[]{Obj.WALL,Obj.WALL,Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[1]=new Obj[]{Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[2]=new Obj[]{Obj.WALL,Obj.SPACE,Obj.WALL,Obj.SPACE,Obj.WALL,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[3]=new Obj[]{Obj.WALL,Obj.EXIT,Obj.WALL,Obj.WALL,Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.WALL};
        maze[4]=new Obj[]{Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.SPACE,Obj.WALL};
        maze[5]=new Obj[]{Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.SPACE,Obj.WALL};
        maze[6]=new Obj[]{Obj.SPACE,Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.SPACE,Obj.WALL};
        maze[7]=new Obj[]{Obj.SPACE,Obj.SPACE,Obj.WALL,Obj.WALL,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.SPACE,Obj.WALL};
        maze[8]=new Obj[]{Obj.WALL,Obj.START,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.WALL,Obj.SPACE,Obj.WALL,Obj.WALL};
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

        Obj[][] view=new Obj[3][3];
        if(x.get(x.size()-1)>0) {
            if(y.get(y.size() - 1)>0){
                view[0][0] = maze[x.get(x.size() - 1) - 1][y.get(y.size() - 1) - 1];
            }else{
                view[0][0] = Obj.WALL;
            }
            view[0][1] = maze[x.get(x.size() - 1) - 1][y.get(y.size() - 1)];
            if(y.get(y.size() - 1)<maze[0].length) {
                view[0][2] = maze[x.get(x.size() - 1) - 1][y.get(y.size() - 1) + 1];
            }else{
                view[0][2] = Obj.WALL;
            }
        }else{
            view[0][0] = Obj.WALL;
            view[0][1] = Obj.WALL;
            view[0][2] = Obj.WALL;
        }

        if(y.get(y.size() - 1)>0){
            view[1][0]=maze[x.get(x.size()-1)][y.get(y.size()-1)-1];
        }else{
            view[1][0] = Obj.WALL;
        }
        view[1][1]=maze[x.get(x.size()-1)][y.get(y.size()-1)];
        if(y.get(y.size() - 1)<maze[0].length){
            view[1][2]=maze[x.get(x.size()-1)][y.get(y.size()-1)+1];
        }else{
            view[1][2] = Obj.WALL;
        }

        if(x.get(x.size()-1)<maze.length) {
            if(y.get(y.size() - 1)>0){
                view[2][0] = maze[x.get(x.size() - 1) + 1][y.get(y.size() - 1) - 1];
            }else{
                view[2][0] = Obj.WALL;
            }
            view[2][1] = maze[x.get(x.size() - 1) + 1][y.get(y.size() - 1)];
            if(y.get(y.size() - 1)<maze[0].length){
                view[2][2] = maze[x.get(x.size() - 1) + 1][y.get(y.size() - 1) + 1];
            }else{
                view[2][2] = Obj.WALL;
            }
        }else{
            view[2][0] = Obj.WALL;
            view[2][1] = Obj.WALL;
            view[2][2] = Obj.WALL;

        }
        maze=view;

        double[][] agentMaze=new double[maze.length][maze[0].length];
        double val=1.0/3;
        for(int x=0;x<agentMaze.length;x++){
            for(int y=0;y<agentMaze[x].length;y++){
                if(maze[x][y].equals(Obj.UNKNOWN)){
                    agentMaze[x][y]=val;
                }else if(maze[x][y].equals(Obj.WALL)){
                    agentMaze[x][y]=2*val;
                }else if(maze[x][y].equals(Obj.SPACE)){
                    agentMaze[x][y]=3*val;
                }else if(maze[x][y].equals(Obj.VISITED)){
                    agentMaze[x][y]=-1*val;
                }else if(maze[x][y].equals(Obj.START)){
                    agentMaze[x][y]=-2*val;
                }else if(maze[x][y].equals(Obj.EXIT)){
                    agentMaze[x][y]=-3*val;
                }
            }
        }
        // maze, dead, position x,y, time, last action.

        double[] vector=new double[(maze.length*maze[0].length)+1+1+1+1+2];
        int i=0;
        for(double[] each:agentMaze){
            for(double e:each){
             vector[i]=e;
                i++;
            }
        }
        if(isDead){
            vector[i]=-1;
        }else{
            vector[i]=1;
        }
        i++;
        double xVal;
        if(x.get(x.size()-1)!=0){
            xVal=1.0/x.get(x.size()-1);
        }else {
            xVal=-1;
        }
        vector[i]=xVal;
        i++;
        double yVal;
        if(y.get(y.size()-1)!=0){
            yVal=1.0/y.get(y.size()-1);
        }else {
            yVal=-1;
        }
        vector[i]=yVal;
        i++;

        double time=1.0/x.size();
        vector[i]=time;
        i++;
        if(x.size()-2>=0){
            if(x.get(x.size()-2)>0){
                vector[i]=1.0/x.get(x.size()-2);
            }else{
                vector[i]=0;
            }
            i++;
            if(y.get(y.size()-2)>0){
                vector[i]=1.0/y.get(y.size()-2);
            }else{
                vector[i]=0;
            }
        }else{
            vector[i]=-1;
            i++;
            vector[i]=-1;
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
            int x = action.transformX(this.x.get(this.x.size() - 1));
            int y = action.transformY(this.y.get(this.y.size() - 1));

            if(this.x.get(this.x.size()-1)==x && this.y.get(this.y.size()-1)==y){
                isDead=true;
                return;
            }

            if(this.x.size()>=2){
                if(this.x.get(this.x.size()-2)==x && this.y.get(this.y.size()-2)==y){
                    isDead=true;
                    return;
                }
            }
            if (x >= 0 && x < maze.length) {
                if(y >= 0 && y < maze[x].length) {
                    if (maze[x][y].equals(Obj.WALL)) {
                        isDead = true;
                    } else {
                        // System.out.println("x:"+x+"y:"+y);
                        this.x.add(x);
                        this.y.add(y);
                    }
                }else {
                    isDead=true;
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

            if(this.x.get(this.x.size()-1)==x && this.y.get(this.y.size()-1)==y){
                return false;
            }

            if(this.x.size()>=2){
                if(this.x.get(this.x.size()-2)==x && this.y.get(this.y.size()-2)==y){
                    return false;
                }
            }
            if (x >= 0 && x < maze.length) {
                if(y >= 0 && y < maze[x].length) {
                    if (maze[x][y].equals(Obj.WALL)) {
                        return false;
                    } else {
                        return true;
                    }
                }else {
                    return false;
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
        int temp;
        if(action[0]>0){
            if(action[1]>0){
                temp=3;
            }else {
                temp=2;
            }
        }else{
            if(action[1]<0){
                temp=1;
            }else {
                temp=0;
            }
        }
        MazeMovement mm=null;
        if(temp==0){
            mm= new MazeMovement(MazeMovement.Horizontal.RIGHT, MazeMovement.Vertical.NONE);
        }else if(temp==1){
            mm= new MazeMovement(MazeMovement.Horizontal.LEFT, MazeMovement.Vertical.NONE);
        }else if(temp==2){
            mm= new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.UP);
        }else if(temp==3){
            mm= new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.DOWN);
        }
        return mm;
    }
    @Override
    public List<MazeMovement> getActions(){
        MazeMovement m2=new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.UP);
        MazeMovement m3=new MazeMovement(MazeMovement.Horizontal.NONE, MazeMovement.Vertical.DOWN);
        MazeMovement m4=new MazeMovement(MazeMovement.Horizontal.LEFT, MazeMovement.Vertical.NONE);
        MazeMovement m7=new MazeMovement(MazeMovement.Horizontal.RIGHT, MazeMovement.Vertical.NONE);
        List<MazeMovement> actions=new ArrayList<>();
        if(isLegal(m2)){
            actions.add(m2);
        }
        if(isLegal(m3)){
            actions.add(m3);
        }
        if(isLegal(m4)){
            actions.add(m4);
        }
        if(isLegal(m7)){
            actions.add(m7);
        }
        return actions;
    }

    @Override
    public boolean isAbsorbing() {
       return  isDead || (x.get(x.size()-1)==xExit && y.get(y.size()-1)==yExit);
    }

    private boolean isSteppingStone(){
        // if novelty is high
        return true;
    }

    @Override
    public double getFitnessReward() {
        if(isDead && false){
            return 0;
        }
        // high reward for archiving the goal
        else if((x.get(x.size()-1)==xExit && y.get(y.size()-1)==yExit)){
            printState();
            return 2*x.size();
        }else{
           // System.out.println(x.size());
            if(x.size()>Application.max){
                Application.max=x.size();
                printState();
            }
            return  x.size();//1.0/x.size();
        }
    }

    @Override
    public void printState() {
        Obj[][] maze=getAgentMazeView();
        for(int x=0;x<maze.length;x++){
            for(int y=0;y<maze[x].length;y++){
                if(maze[x][y].equals(Obj.WALL)){
                    System.out.print(" "+maze[x][y].toString() + "  ");
                }else {
                    System.out.print(maze[x][y].toString() + " ");
                }
            }
            System.out.println();
        }
        for (int i=0;i<x.size();i++){
            System.out.println("x: "+x.get(i)+" y: "+y.get(i));
        }
    }
}
