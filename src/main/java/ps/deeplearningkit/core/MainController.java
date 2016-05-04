package ps.deeplearningkit.core;

import ps.deeplearningkit.core.neuralnetwork.AdvancedNetworkController;
import ps.deeplearningkit.core.neuralnetwork.SimpleNetworkController;

/**
 * This controller controls everything needed for the DeepLearningKit.
 * @author Philipp Schlütermann
 *
 */
public class MainController {

	private static String pathToSaveNeuralNetworks="/home/philipp/Documents/";
	private static SimpleNetworkController networkController;
	
	public static void setPath(String path){
		pathToSaveNeuralNetworks=path;
	}
	
	private static void initAdvancedNetworkController(){
		networkController=new SimpleNetworkController(pathToSaveNeuralNetworks);
	}
	public static AdvancedNetworkController getAdvancedNetworkController(){
		if(networkController==null){
			initAdvancedNetworkController();
		}
		return (AdvancedNetworkController)networkController;
	}
	public static SimpleNetworkController getSimpleNetworkController(){
		if(networkController==null){
			initAdvancedNetworkController();
		}
		return networkController;
	}
	
}
