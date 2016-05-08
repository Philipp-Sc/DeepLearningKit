package ps.deeplearningkit.core.example.pixelbreed;

import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
import ps.deeplearningkit.core.simulator.BasicNeuralAction;

/**
 * Created by philipp on 5/5/16.
 * DeNormalizes the NeuralAction, so it can be applied to a state.
 */
public class PixelAction extends BasicNeuralAction{
    private final NormalizedField k1;
    private final NormalizedField k2;
    private int[] koordinates=new int[2];
    private boolean pixel;
    public PixelAction(double[] action,int width,int height){
        super(action);
        k1=new NormalizedField(NormalizationAction.Normalize,
                "k1",
                width,
                0,
                1,
                -1);
        k2=new NormalizedField(NormalizationAction.Normalize,
                "k2",
               height,
                0,
                1,
                -1);
        init();
    }
    private void init(){
        if(this.getNeuronCount()==3) {
            deNormalizeKoordinate1();
            deNormalizeKoordinate2();
            deNormalizePixel();
        }else{
            koordinates[0]=0;
            koordinates[0]=0;
            pixel=false;
        }
    }
    private void deNormalizeKoordinate1(){
        koordinates[0]=(int)k1.deNormalize(this.getNeuronValues()[0]);
    }
    private void deNormalizeKoordinate2(){
        koordinates[1]=(int)k2.deNormalize(this.getNeuronValues()[1]);
    }
    private void deNormalizePixel(){
        pixel=this.getNeuronValues()[2]>0;
    }
    public int getX(){
        return koordinates[0];
    }
    public int getY(){
        return koordinates[1];
    }
    public boolean getPixel(){
        return pixel;
    }

    public static double normalizeKoordinate1(int x,int width){
        return new NormalizedField(NormalizationAction.Normalize,
                "k1",
                width,
                0,
                1,
                -1).normalize(x);
    }
    public static double normalizeKoordinate2(int y,int height){
        return new NormalizedField(NormalizationAction.Normalize,
                "k1",
                height,
                0,
                1,
                -1).normalize(y);
    }
}
