package lab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CleanBdMovies {

	private final static String OUTPUT_DIR = "C:/Users/Andreza/Desktop/output_movies/";
	private final static String INPUT_DIR = "C:/Users/Andreza/Desktop/movies/";

	public static void main(String[] args) throws IOException {
		File folder = new File(INPUT_DIR);
		List<File> movies = listFilesForFolder(folder);
		for (File file : movies) {
			cleanFile(file);
		}

		System.out.println("FINISHED!");

	}

	public static void cleanFile(File file) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(OUTPUT_DIR + file.getName());

		try {
			BufferedReader br = null;
			
			if (file.getName().contains("Chico Xavier.DVDRip.Lis.br")) {
				br = new BufferedReader(
						   new InputStreamReader(
				                      new FileInputStream(file), "UTF-16"));
			} else {
				br = new BufferedReader(
						   new InputStreamReader(
				                      new FileInputStream(file)));
			}
			
			String row = br.readLine();
			row = br.readLine();
			row = br.readLine();
			while (row != null) {
				if (row.equals("9999")) {
					break;
				} 
				if (row.contains("037es")){
					System.out.println("Lascou!");
				}
				if (row.equals("")) {
					row = br.readLine();
					row = br.readLine();
				} else if (row.contains("-->") || row.contains("</font>")) {
					row = br.readLine();
				} else if (isTrashData(row)) {
					row = br.readLine();
				} else {
					row = cleanRow(row);

					writer.println(row);
					row = br.readLine();
				}
			}
			br.close();
		} catch (IOException e) {
			System.err.printf("File reader exception: %s.\n", e.getMessage());
		}

		writer.close();

	}

	private static boolean isTrashData(String row) {
		if (row.contains("Tradução") || row.contains("Resinc por:") || row.contains("BlizZardBr")
				|| row.contains("Bliz") || row.contains("www.") || row.contains("http://") || row.contains(".org")
				|| row.contains(".com") || row.contains(".br") || row.contains("Revisão final:")
				|| row.contains("Legendas por:") || row.contains("LEGENDAS") || row.contains("Subtitles")) {
			return true;
		}
		return false;
	}

	private static String cleanRow(String row) {
		row = row.replace("<i>", "");
		row = row.replace("</i>", "");
		row = row.replace("-", "");
		row = row.replaceAll("\"", "");
		row = row.replaceAll("\\”", "");
		row = row.replaceAll("\\“", "");
		row = row.replaceAll("\\’", "");
		row = row.replaceAll("\\–", "");
		return row;
	}

	public static List<File> listFilesForFolder(File folder) {
		List<File> movies = new ArrayList<>();
		for (File fileEntry : folder.listFiles()) {
			movies.add(fileEntry);
		}
		return movies;
	}
}