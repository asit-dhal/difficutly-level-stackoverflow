package de.tu_darmstadt.kom.stackoverflow.ml;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class LoadData {
	
	public Instances load(String filename) throws IOException {
		File file= new File(filename);
		ArffLoader loader = new ArffLoader();
		loader.setFile(file);
		Instances data;
		data= loader.getDataSet();
		return data;
	}

}
