package ps.deeplearningkit.core.example.maze;

/**
 * Created by philipp on 5/16/16.
 */
public class MazeMovement {

    public enum Horizontal { LEFT,RIGHT,NONE}
    public enum Vertical { UP,DOWN,NONE}

    private Horizontal horizontal;
    private Vertical vertical;

    public MazeMovement(int action){
        switch (action){
            case 1:
                this.horizontal=Horizontal.NONE;
                this.vertical=Vertical.NONE;
                break;
            case 2:
                this.horizontal=Horizontal.NONE;
                this.vertical=Vertical.DOWN;
                break;
            case 3:
                this.horizontal=Horizontal.NONE;
                this.vertical=Vertical.UP;
                break;
            case 4:
                this.horizontal=Horizontal.LEFT;
                this.vertical=Vertical.NONE;
                break;
            case 5:
                this.horizontal=Horizontal.LEFT;
                this.vertical=Vertical.DOWN;
                break;
            case 6:
                this.horizontal=Horizontal.LEFT;
                this.vertical=Vertical.UP;
                break;
            case 7:
                this.horizontal=Horizontal.RIGHT;
                this.vertical=Vertical.NONE;
                break;
            case 8:
                this.horizontal=Horizontal.RIGHT;
                this.vertical=Vertical.DOWN;
                break;
            case 9:
                this.horizontal=Horizontal.RIGHT;
                this.vertical=Vertical.UP;
                break;
        }
    }
    public MazeMovement(Horizontal horizontal, Vertical vertical){
        this.horizontal=horizontal;
        this.vertical=vertical;
       // printAction();
    }
    public int transformX(int x){
        if(vertical==Vertical.NONE){
            //System.out.println("x:None");
            return x;
        }else if(vertical==Vertical.DOWN){
            //System.out.println("x:Down");
            return x-1;
        }else{
            //System.out.println("x:Up");
            return x+1;
        }
    }
    public int transformY(int y){
        if(horizontal==Horizontal.NONE){
            //System.out.println("y:None");
            return y;
        }else if(horizontal==Horizontal.LEFT) {
            //System.out.println("y:Left");
            return y - 1;
        }else {
           // System.out.println("y:Right");
            return y+1;
        }
    }
    private void printAction(){
        System.out.print("< ");
        System.out.print(horizontal.toString()+"  ");
        System.out.print(vertical.toString());
        System.out.println(" >");
    }
}
