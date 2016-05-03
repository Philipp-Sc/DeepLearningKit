package ps.deeplearningkit.core.neuralnetwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Preserving {
	
	protected void save(String path,Object obj) throws IOException{
		// Write to disk with FileOutputStream
		FileOutputStream f_out = new 
		FileOutputStream(path);
		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new
		ObjectOutputStream (f_out);
		// Write object out to disk
		obj_out.writeObject (obj);
		obj_out.close();
		f_out.close();
	}
	protected Object load(String path) throws ClassNotFoundException, IOException{
		// Read from disk using FileInputStream
		FileInputStream f_in = new FileInputStream(path);
		// Read object using ObjectInputStream
		ObjectInputStream obj_in = new ObjectInputStream (f_in);
		// Read an object
		Object obj = obj_in.readObject();
		obj_in.close();
		f_in.close();
		return obj;
	}

}
