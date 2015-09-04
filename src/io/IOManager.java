package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.google.gson.Gson;

import evolution.Generation;
import evolution.Genome;

public class IOManager {
	private static String fileSeparator = System.getProperty("file.separator");
	
	private static String generationFilePath = "generation";
	private static String genomeFilePath = "genomes";
	
	private static String genomeFileName = "genome";
	private static String generationFileName = "generation";
	private static String fileExtension = ".json";
	private static Gson gson = new Gson();
	
	public static void saveGenerationToFile(Generation generation, int generationNumber) {
		String json = gson.toJson(generation);
		try {
			FileUtils.write(new File(getGenerationFileName(generationNumber)), json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveGenomeToFile(Genome genome, int generationNumber) {
		int genomeNumber = 1;
		String json = gson.toJson(genome);
		try {
			FileUtils.write(new File(getGenomeFileName(generationNumber, genomeNumber)), json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveMultipleGenomesToFile(int generationNumber, List<Genome> genomes) {
		for(Genome g: genomes) {
			saveGenomeToFile(g, generationNumber);
		}
	}
	
	public static List<Genome> readMultipleGenomes(int generationNumber) {
		List<Genome> result = new ArrayList<Genome>();
		
		int lastGenomeNumber = getLatestGenomeNumber(generationNumber);
		for(int i = 1; i < lastGenomeNumber+1; i++) {
			result.add(readGenomeNumber(generationNumber, i));
		}
		
		return result;
	}
	
	public static Genome readGenomeNumber(int generationNumber, int genomeNumber) {
		String jsonAsString = null;
		try {
			jsonAsString = FileUtils.readFileToString(new File(getGenomeFileName(generationNumber, genomeNumber)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return gson.fromJson(jsonAsString, Genome.class);
	}
	
	private static int getLatestGenomeNumber(int generationNumber) {
		IOFileFilter fileFilter = new IOFileFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg0.getName().contains(genomeFileName);
			}

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().contains(genomeFileName);
			}
		};
		
		Collection<File> genomeFiles = FileUtils.listFiles(new File(getGenerationFilePath(generationNumber)), fileFilter, FalseFileFilter.FALSE);
		
		int latestGenomeNumber = 0;
		for(File f: genomeFiles) {
			int genomeNumber = getGenomeNumberFromFileName(f.getName());
			if(genomeNumber > latestGenomeNumber)
				latestGenomeNumber = genomeNumber;
		}
		return latestGenomeNumber;
	}
	
	private static int getGenomeNumberFromFileName(String fileName) {
		char[] chars = fileName.toCharArray();
		return Character.getNumericValue(fileName.toCharArray()[chars.length-1]);
	}
	
	private static String getGenomeFileName(int generationNumber, int genomeNumber) {
		return getGenerationFilePath(generationNumber) + genomeFileName + genomeNumber + fileExtension;
	}
	
	private static String getGenerationFileName(int generationNumber) {
		return getGenerationFilePath(generationNumber) + generationFileName + fileExtension;
	}
	
	private static String getGenerationFilePath(int generationNumber) {
		return genomeFilePath + fileSeparator + generationFilePath + generationNumber + fileSeparator;
	}
	
	
}
