package ps.deeplearningkit.core.search.modulsearch;

import ps.deeplearningkit.core.example.pixelbreed.PixelSimulator;
import ps.deeplearningkit.core.search.noveltysearch.Behavior;
import ps.deeplearningkit.core.search.uct.Simulator;
import ps.deeplearningkit.core.search.uct.UCT;

import java.util.Random;

/**
 * Created by philipp on 5/8/16.
 * Searching for the most expandable behavior.
 */
public class ModularitySearch {

    private Random random=new Random();

    public double testModularity(Behavior behavior){
        // needs domain knowledge:
        // state (UTC)
        // behavior is modular if it is basis for many offspring..
        return 0;
       }
}
