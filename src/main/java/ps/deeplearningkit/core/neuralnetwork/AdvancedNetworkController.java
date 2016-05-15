package ps.deeplearningkit.core.neuralnetwork;

import java.io.IOException;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationLOG;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.CalculateScore;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.specific.BiPolarNeuralData;
import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.art.ART1;
import org.encog.neural.hyperneat.HyperNEATCODEC;
import org.encog.neural.hyperneat.substrate.Substrate;
import org.encog.neural.hyperneat.substrate.SubstrateFactory;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;
import org.encog.neural.neat.training.species.OriginalNEATSpeciation;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.pnn.BasicPNN;
import org.encog.neural.pnn.PNNKernelType;
import org.encog.neural.pnn.PNNOutputMode;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;

/**
 * This controller is able to test,train and create networks.
 * They are all identified by (unique) keys.
 * @author Philipp Schlütermann
 *
 */
public class AdvancedNetworkController extends NetworkController{
	
	public AdvancedNetworkController(String path){
		super(path);
	}
	/**
	 * SOM Networks:
	 */
	public void createSOM(String key,int inputNeurons,int outputNeurons) throws IOException{
		this.addSomNetwork(key, new SOM(inputNeurons, outputNeurons));
	}
	public int testSOM(String key,double[] data){
		return this.testSOM(key, new BasicMLData(data));
	}
	private int testSOM(String key,BasicMLData data){
		return this.getSomNetwork(key).classify(data);
	}
	public void trainSOM(String key,BasicMLDataSet data,int iterations){
		trainSOM(key, data, iterations, 0.01,5,30,0.8,0.003,500,500);
	}
	public void trainSOM(String key,BasicMLDataSet data,int iterations,double learningRate,
			int endRadius,int startRadius,double startRate,double endRate, int xDim,int yDim){
		System.out.print("<");
		// other neighborhood functions can be used.
		NeighborhoodRBF gaussian=new NeighborhoodRBF(RBFEnum.Gaussian, xDim, yDim);
		BasicTrainSOM train=new BasicTrainSOM(this.getSomNetwork(key), learningRate, data, gaussian);
		train.setAutoDecay(iterations, startRate, endRate, startRadius, endRadius);
		for(int i=0;i<iterations;i++){
			System.out.print(".");
			train.iteration();
			train.autoDecay();
		}
		train.finishTraining();
		System.out.println("/>");
	}
	/**
	 * Basic Networks
	 * @throws IOException 
	 */
	/**
	 * Creates a basic neuralsearch network.
	 * @param key
	 * @param inputSize
	 * @param outputSize
	 * @throws IOException 
	 */
	public void createBasicNetwork(String key,int inputSize,int outputSize) throws IOException{
		BasicNetwork bn=new BasicNetwork();
		bn.addLayer(new BasicLayer(null,true, inputSize));
		bn.addLayer(new BasicLayer(new ActivationLOG(),true,inputSize*2));
		bn.addLayer(new BasicLayer(new ActivationLOG(), false, outputSize));
		bn.getStructure().finalizeStructure();
		bn.reset();
		addBasicNetwork(key, bn);
	}
	/**
	 * Trains the network with a reward function. 
	 * No training set is needed. 
	 * The network will learn on its own.
	 * This variation uses NeuralSimulatedAnnealing.
	 * @param key
	 * @param someScore
	 * @param startTemp
	 * @param stopTemp
	 * @param cycles
	 * @param iterations
	 * @throws IOException
	 */
	public void trainBasicNetwork(String key,CalculateScore someScore,int startTemp,int stopTemp,int cycles,int iterations) throws IOException{
		BasicNetwork bn=this.getBasicNetwork(key);
		NeuralSimulatedAnnealing train=	new NeuralSimulatedAnnealing(bn, someScore, startTemp,stopTemp ,cycles);
		for(int i=0;i<iterations;i++){
			train.iteration();
		}
		train.finishTraining();
		this.saveBasicNetworks();
	}
	/**
	 * Computes the result based on the test input.
	 * @param key
	 * @param test
	 * @return result
	 */
	public MLData testBasicNetwork(String key,MLData test){
		return this.getBasicNetwork(key).compute(test);
	}

	/**
	 * BasicPNN
	 */
	public void createBasicPNN(String key,int inputNeurons,int outputNeurons){
		BasicPNN pnn= new BasicPNN(PNNKernelType.Gaussian, PNNOutputMode.Regression,inputNeurons,outputNeurons);
	}
	/**
	 * ART1 Networks
	 */
	/**
	 * Create an ART1 network and saves it.
	 * @param key (unique key used for access to the network)
	 * @param inputNeurons (how many boolean values the network accepts)
	 * @param outputNeurons (how many classes the network can create)
	 * @throws IOException 
	 */
	public void createART1(String key,int inputNeurons,int outputNeurons) throws IOException{
		ART1 logic=new ART1(inputNeurons, outputNeurons);
		this.addART1Network(key, logic);
	}
	/**
	 * Clusters the boolean array on the fly, no training needed.
	 * @param key (unique key used for access to the network)
	 * @param input (its length must be less than the inputNeuronCount)
	 * @return class
	 * @throws IOException 
	 */
	public int classifyART1(String key, boolean[] input) throws IOException{
		ART1 logic=this.getART1Network(key);
		int outputCount=logic.getOutputCount();
		BiPolarNeuralData in=new BiPolarNeuralData(input);
		BiPolarNeuralData out=new BiPolarNeuralData(outputCount);
		logic.compute(in, out);
		this.saveART1Networks();
		if(logic.hasWinner()){
			return logic.getWinner();
		}{
			return logic.getNoWinner();
		}
	}
	/**
	 * NEATNetworks
	 */
	/**
	 * Creates a NEATPopulation which can be used to create a best NEATNetwork.
	 * @param key for the NEATPopulation
	 * @param inputCount
	 * @param outputCount
	 * @param populationSize
	 * @param //error
	 * @throws IOException
	 */
	public void createNEATPopulation(String key,int inputCount,int outputCount,int populationSize) throws IOException{
		NEATPopulation pop=new NEATPopulation(inputCount, outputCount, populationSize);
		pop.reset();
		addNEATPopulation(key, pop);
	}
	/**
	 * Train an existing NEATPopulation.
	 * @param key for a NEATPopulation.
	 * @param score
	 * @param scoreValue
	 * @throws IOException 
	 */
	public void trainNEATPopulation(String key,CalculateScore score,double scoreValue,int iterations) throws IOException{
		NEATPopulation pop=this.getNEATPopulation(key);
		final EvolutionaryAlgorithm train=NEATUtil.constructNEATTrainer(pop, score);
		do{
			train.iteration();
			System.out.println("Epoch #"+train.getIteration()+" score:"+train.getError()+", Species:"+pop.getSpecies().size());
		}while(train.getIteration()<=iterations && train.getError()<scoreValue);
		train.finishTraining();
		Encog.getInstance().shutdown();
		this.addNEATPopulation(key, (NEATPopulation)train.getPopulation());
	}
	/**
	 * NEATNetworks are always retrieved from their populations.
	 * @return NEATNetwork
	 */
	public NEATNetwork getBestNEATNetwork(String key,CalculateScore score){
		NEATPopulation pop=this.getNEATPopulation(key);
		final EvolutionaryAlgorithm train=NEATUtil.constructNEATTrainer(pop, score);
		train.finishTraining();
		return (NEATNetwork)train.getCODEC().decode(train.getBestGenome());
	}
	/**
	 * HyperNeat Networks
	 */
	/**
	 * Create a HyperNEATPopulation with a SandwichSubstrate.
	 * @param key
	 * @param populationSize
	 * @param inputEdgeSize
	 * @param outputEdgeSize
	 * @throws IOException 
	 */
	public void createHyperNEATPopulation(String key,int populationSize,int inputEdgeSize,int outputEdgeSize) throws IOException{
		Substrate theSubstrate=SubstrateFactory.factorSandwichSubstrate(inputEdgeSize, outputEdgeSize);
		NEATPopulation pop=new NEATPopulation(theSubstrate, populationSize);
		pop.reset();
		pop.setActivationCycles(4);
		this.addNEATPopulation(key,pop);
	}
	public void trainHyperNEATPopulation(String key,CalculateScore score,int scoreValue,int iterations) throws IOException{
		NEATPopulation pop=this.getNEATPopulation(key);
		if(pop.isHyperNEAT()){
			final EvolutionaryAlgorithm train=NEATUtil.constructNEATTrainer(pop, score);
			train.setSpeciation(new OriginalNEATSpeciation());
			do{
				train.iteration();
				System.out.println("Epoch #"+train.getIteration()+" score:"+train.getError()+", Species:"+pop.getSpecies().size());
			}while(train.getIteration()<=iterations && train.getError()<scoreValue);
			train.finishTraining();
			Encog.getInstance().shutdown();
			this.addNEATPopulation(key, (NEATPopulation)train.getPopulation());
		}
	}
	public NEATNetwork getBestHyperNEATNetwork(String key,CalculateScore score){
		NEATPopulation pop=this.getNEATPopulation(key);
		if(pop.isHyperNEAT()){
			final EvolutionaryAlgorithm train=NEATUtil.constructNEATTrainer(pop, score);
			train.finishTraining();
			return (NEATNetwork)((HyperNEATCODEC)train.getCODEC()).decode(train.getBestGenome());
		}
		return null;
	}
}
