package io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import evolution.Genome;

public class IOManager {
	private static String genomeFileName = "genome";
	private static String genomeFileExtension = ".json";
	private static Gson gson = new Gson();
	private static int genomeNumber = 0;
	
	public static void saveGenomeToFile(Genome genome) {
		String json = gson.toJson(genome);
		try {
			FileUtils.write(new File(getFileName(genomeNumber)), json);
			genomeNumber++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveMultipleGenomesToFile(List<Genome> genomes) {
		for(Genome g: genomes) {
			saveGenomeToFile(g);
		}
	}
	
	public static Genome readGenomeNumber(int number) {
		String jsonAsString = null;
		try {
			jsonAsString = FileUtils.readFileToString(new File(getFileName(number)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return gson.fromJson(jsonAsString, Genome.class);
	}
	
	private static String getFileName(int number) {
		return genomeFileName + number + genomeFileExtension;
	}
}
