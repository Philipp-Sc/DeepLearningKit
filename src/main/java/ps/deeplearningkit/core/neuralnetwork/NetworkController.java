package ps.deeplearningkit.core.neuralnetwork;

import java.io.IOException;
import java.util.HashMap;

import org.encog.neural.art.ART1;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.som.SOM;

/**
 * Master Network Controller is used by the other controller to save/load and edit/create networks
 * (If you need to store settings create an extra controller)
 * @author Philpp Schlütermann
 *
 */
public class NetworkController extends Preserving{

	private final String path;
	private final String somNetworksName="som.networks",
			basicNetworksName="basic.networks",
			art1NetworksName="art1.networks",
			neatPopulationsName="neat.populations";
	
	private HashMap<String,SOM> somNetworks;
	private HashMap<String,BasicNetwork> basicNetworks;
	private HashMap<String,ART1> art1Networks;
	private HashMap<String, NEATPopulation> neatPopulations;
	
	public NetworkController(String path){
		this.path=path;
		try{
			this.neatPopulations=(HashMap<String, NEATPopulation>)load(path+neatPopulationsName);
			this.art1Networks=(HashMap<String,ART1>)load(path+art1NetworksName);
			this.somNetworks=(HashMap<String, SOM>) load(path+somNetworksName);
			this.basicNetworks=(HashMap<String, BasicNetwork>) load(path+basicNetworksName);
			}catch(IOException | ClassNotFoundException e){
				this.art1Networks=new HashMap<>();
				this.neatPopulations=new HashMap<>();
				this.somNetworks=new HashMap<>();
				this.basicNetworks=new HashMap<>();	
			}
	}
	public void resetNetworks() throws IOException{
		resetBasicNetworks();
		resetSomNetworks();
	}
	public void resetBasicNetworks() throws IOException{
		this.basicNetworks=new HashMap<>();	
		saveBasicNetworks();
	}
	public void resetSomNetworks() throws IOException{
		this.somNetworks=new HashMap<>();
		saveSomNetworks();
	}
	protected void removeSomNetwork(String key) throws IOException{
		somNetworks.remove(key);
		saveSomNetworks();
	}
	public void removeBasicNetwork(String key) throws IOException{
		basicNetworks.remove(key);
		saveBasicNetworks();
	}
	public SOM getSomNetwork(String key){
		return somNetworks.get(key);
	}
	public BasicNetwork getBasicNetwork(String key){
		return basicNetworks.get(key);
	}
	public ART1 getART1Network(String key){
		return art1Networks.get(key);
	}
	public NEATPopulation getNEATPopulation(String key){
		return neatPopulations.get(key);
	}
	public void addSomNetwork(String key,SOM tn) throws IOException{
		somNetworks.put(key, tn);
		saveSomNetworks();
	}
	public void addBasicNetwork(String key,BasicNetwork bn) throws IOException{
		basicNetworks.put(key, bn);
		saveBasicNetworks();
	}
	public void addART1Network(String key,ART1 art1) throws IOException{
		art1Networks.put(key, art1);
		saveART1Networks();
	}
	public void addNEATPopulation(String key,NEATPopulation pop) throws IOException{
		neatPopulations.put(key, pop);
		saveNEATPopulations();
	}
	public void saveAll() throws IOException{
		saveBasicNetworks();
		saveSomNetworks();
		saveART1Networks();
		saveNEATPopulations();
	}
	protected void saveNEATPopulations() throws IOException{
		this.save(path+neatPopulationsName, neatPopulations);
	}
	protected void saveART1Networks() throws IOException{
		this.save(path+art1NetworksName, art1Networks);
	}
	protected void saveBasicNetworks() throws IOException{
		this.save(path+basicNetworksName, basicNetworks);
	}
	protected void saveSomNetworks() throws IOException{
		this.save(path+somNetworksName, somNetworks);
	}

}
