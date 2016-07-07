package indice_invertido;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;

import javax.sql.rowset.spi.TransactionalWriter;

import estruturas.BTree;

public class Main {

	private static BTree<String, List<Integer>> bTree = new BTree<String, List<Integer>>();
	
	public static void main(String[] args) throws IOException {
		FileReader file = new FileReader(new File("C:\\Users\\Andreza\\Downloads\\ptwiki.trec"));
		BufferedReader reader = new BufferedReader(file);

		String linha = reader.readLine();
		
		Integer documentId = 0;
		String[] palavras;
		while (linha != null ) {

			if (!(linha.contains("<DOC>") || linha.contains("</DOC>") || linha.contains("<P>")
					|| linha.contains("</P>"))) {
				
				
				if (linha.contains("<DOCNO>")) {
					documentId++;
				} else if (linha.contains("<HEADLINE>")) {
					linha = linha.substring("<HEADLINE>".length(), linha.length() - "</HEADLINE>".length());
					palavras = linha.split(" ");
					
					String palavra;
					for (int i = 0; i < palavras.length; i++) {
						palavra = palavras[i];
						palavra = trataPalavra(palavra);
						insertIntoTree(palavra, documentId);
					}	
				}
				
			}
			linha = reader.readLine();

		}
		System.out.println(bTree);

	}

	private static String trataPalavra(String palavra) {
		if (palavra.contains("[")) {
			palavra = palavra.replaceAll("[", "");
		}
		
		if (palavra.contains("]")) {
			palavra = palavra.replaceAll("]", "");
		}
		
		if (palavra.contains("{")) {
			palavra = palavra.replaceAll("{", "");
		}
		
		if (palavra.contains("}")) {
			palavra = palavra.replaceAll("}", "");
		}
		return palavra.toLowerCase();
		
	}

	private static void insertIntoTree(String word, int documentId) {
		if (bTree.get(word) != null) {
			List<Integer> list = bTree.get(word);
			list.add(documentId);
		} else {
			List<Integer> list = new ArrayList<Integer>();
			list.add(documentId);
			bTree.put(word, list);
		}
		
		
	}

}
