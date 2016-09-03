package lab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CleanBdMovies {

	private final static String OUTPUT_DIR = "C:/Users/Andreza/Documents/RI/OUTPUT/";
	private final static String INPUT_DIR = "C:/Users/Andreza/Documents/RI/INPUT";
	private final static String TRAIN_DIR = "C:/Users/Andreza/Documents/RI/TRAIN";
	private final static String TEST_DIR = "C:/Users/Andreza/Documents/RI/TEST";
	private final static double PERCENTAGE = 0.7;

	public static void main(String[] args) throws IOException {
		File folder = new File(INPUT_DIR);
		List<File> movies = listFilesForFolder(folder);
		for (File file : movies) {
			cleanFile(file);
		}

		splitSets();

		System.out.println("FINISHED!");

	}

	private static void splitSets() throws IOException {
		File outputDir = new File(OUTPUT_DIR);
		File trainDir = new File(TRAIN_DIR);
		File testDir = new File(TEST_DIR);

		if (!trainDir.exists()) {
			trainDir.mkdir();
		}
		if (!testDir.exists()) {
			testDir.mkdir();
		}

		int lenTrainSet = (int) (outputDir.listFiles().length * PERCENTAGE);

		for (int i = 0; i < outputDir.listFiles().length; i++) {
			File inputFile = outputDir.listFiles()[i];
			File outputFile = null;
			
			if (i <= lenTrainSet) {
				outputFile = new File(trainDir.getAbsoluteFile() + File.separator + inputFile.getName());
			} else {
				outputFile = new File(testDir.getAbsoluteFile() + File.separator + inputFile.getName());
			}

			InputStream inStream = new FileInputStream(inputFile);
			OutputStream outStream = new FileOutputStream(outputFile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();
		}
	}

	public static void cleanFile(File file) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(OUTPUT_DIR + file.getName());

		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);

			String row = br.readLine();
			row = br.readLine();
			row = br.readLine();
			while (row != null) {
				if (row.equals("9999")) {
					break;
				}
				if (row.equals("")) {
					row = br.readLine();
					row = br.readLine();
				} else if (row.contains("-->") || row.contains("</font>")) {
					row = br.readLine();
				} else {
					if (row.contains("<i>")) {
						row = row.replace("<i>", "");
					}
					if (row.contains("</i>")) {
						row = row.replace("</i>", "");
					}
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