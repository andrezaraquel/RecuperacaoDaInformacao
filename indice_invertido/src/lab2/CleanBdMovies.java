package lab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CleanBdMovies {

	private final static String OUTPUT_DIR = "C:/Users/Andreza/Desktop/output_movies/";
	private final static String INPUT_DIR = "C:/Users/Andreza/Desktop/movies";
	
	public static void main(String[] args) {
		File folder = new File(INPUT_DIR);
		List<File> movies = listFilesForFolder(folder);
		for (File file: movies) {
			cleanFile(file);
		}
		System.out.println("FINISHED!");

	}

	public static void cleanFile(File file) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(OUTPUT_DIR + file.getName(), "UTF-8");
		} catch (FileNotFoundException e1) {		
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {			
			e1.printStackTrace();
		}
		

		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);

			String row = br.readLine();
			row = br.readLine();
			row = br.readLine();
			while (row != null) {
				if (row.equals("")) {
					row = br.readLine();	
					row = br.readLine();	
				} else if (row.contains("-->") || row.contains("</font>")) {
					row = br.readLine();	
				} else {
					writer.println(row);
					row = br.readLine();
				}				
			}
			fileReader.close();
		} catch (IOException e) {
			System.err.printf("File reader exception: %s.\n", e.getMessage());
		}

		writer.close();

	}

	public static List<File> listFilesForFolder(File folder) {
		List<File> movies = new ArrayList<>();
		for (File fileEntry : folder.listFiles()) {
			movies.add(fileEntry);
		}
		return movies;
	}
}