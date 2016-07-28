package ranking_modelo_vetorial;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	// Termo de normalizacao - penalizar palavras muito frequentes
	private static final Integer K = 10;
	private static final String OUTPUT_PATH = "data/outputRMV.txt";
	private static final String INPUT_PATH = "data/ptwiki-v2.trec";
	private static Map<String, Map<Integer, Integer>> mapVMR = new HashMap<String, Map<Integer, Integer>>();

	// Numero de documentos presentes na colecao
	private static Integer collectionSize;

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

		File fXmlFile = new File(INPUT_PATH);

		// Biblioteca para ler xml
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(fXmlFile);

		document.getDocumentElement().normalize();

		// Recuperando todos os elementos dentro da tag DOC
		NodeList docs = document.getElementsByTagName("DOC");

		collectionSize = docs.getLength();

		Integer documentId = new Integer(0);
		// Percorrendo todos os elementos
		for (int i = 0; i < collectionSize; i++) {

			Node node = docs.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;

				documentId++;

				// Recuperando o conteudo dentro da tag P
				String paragraph = eElement.getElementsByTagName("P").item(0).getTextContent();
				paragraph = clean(paragraph);

				String[] words = paragraph.split(" ");

				for (int indexWord = 0; indexWord < words.length; indexWord++) {
					if (!words[indexWord].isEmpty()) {
						// Inserindo cada palavra do paragrafo no mapa
						insertIntoMap(words[indexWord], documentId);
					}
				}
			}
		}

		// Escrevendo o mapa no arquivo
		PrintWriter writer = new PrintWriter(OUTPUT_PATH, "UTF-8");
		writer.print(mapVMR.toString());
		writer.close();

		// Consultas
		printTopFive("primeira guerra mundial");
		printTopFive("espa�o e tempo");
		printTopFive("minha terra tem palmeiras onde canta o sabi�");
		printTopFive("grupo ra�a negra");

	}

	/**
	 * Imprime os cinco documentos mais bem posicionados
	 * 
	 * @param query
	 *  A consulta desejada
	 *  
	 * @return
	 */
	private static void printTopFive(String query) {
		List<Integer> tops = getTops(query);
		System.out.println("Consulta: " + query);
		for (int i = 0; i < 5; i++) {
			System.out.println("Top" + (i + 1) + ": " + tops.get(i));
		}
		System.out.println("============================");
	}

	/**
	 * Metodo que recupera os documentos ordenados pelo score obtido na consulta
	 * 
	 * @param query
	 * 	A consulta desejada
	 * 
	 * @return
	 * 	A lista de documentos ordenados pelo score obtido na consulta
	 * 
	 * */
	private static List<Integer> getTops(String query) {
		List<Integer> tops = new ArrayList<Integer>();
		Map<Integer, Double> accumulator = new HashMap<Integer, Double>();

		String[] queryWords = query.toLowerCase().split(" ");

		for (int i = 0; i < queryWords.length; i++) {
			// Aqui recupero o mapa<documento, tf> de cada palavra da consulta
			Map<Integer, Integer> docs = mapVMR.get(queryWords[i]);

			if (docs != null) { // Se encontrar algum documento para aquela palavra
				
				// Calcula o idf da palavra
				Double idf = (double) 1 / docs.size();
				
				// Percorre os documentos da palavra
				for (Integer document : docs.keySet()) {
					Double frequency = BM25(docs.get(document), idf); // Calcula o score da palavra no documento
					Double oldFrequency = accumulator.get(document); // Recupera o score anterior naquele documento
					
					// Se existe outra palavra da consulta que esta no documento
					// E se o score dessa palavra ja foi calculado
					// oldFrequency vai ser diferente de null
					if (oldFrequency != null) {
						accumulator.put(document, oldFrequency + frequency);
						
					} else {
						accumulator.put(document, frequency);
					}
				}
			}
		}

		// Recupero um mapa ordenado pelos valores de score
		LinkedHashMap<Integer, Double> sortedMap = sortHashMapByValues(accumulator);
		for (Entry<Integer, Double> entry : sortedMap.entrySet()) {
			tops.add(entry.getKey());
		}

		return tops;
	}

	/**
	 * Metodo para ordenar o mapa pelos valores
	 * 
	 * @param passedMap
	 * 	O mapa que deseja-se ordenar
	 *   
	 * @return 
	 * 	Um LinkedHashMap ordenado pelos valores de forma decrescente
	 */
	public static LinkedHashMap<Integer, Double> sortHashMapByValues(Map<Integer, Double> passedMap) {
		List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
		List<Double> mapValues = new ArrayList<>(passedMap.values());
		Collections.sort(mapValues);

		LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

		for (int i = mapValues.size() - 1; i >= 0; i--) {
			Double val = mapValues.get(i);
			Iterator<Integer> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Integer key = keyIt.next();
				Double comp1 = passedMap.get(key);
				Double comp2 = val;

				if (comp1.equals(comp2)) {
					keyIt.remove();
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;
	}

	/**
	 *  Metodo que calcula a frequencia do tempo, ponderando sua frequencia
	 *   @param termFrequency
	 * 	O valor do tf do termo no documento
	 *   @param inverseTF
	 * 	O IDF do termo
	 * 
	 *  @return A frequencia do termo
	 * */
	private static Double BM25(Integer termFrequency, Double inverseTF) {
		Double frequency = (((K + 1) * termFrequency) / termFrequency + K) * (Math.log(collectionSize + 1) * inverseTF);
		return frequency;
	}

	/*
	 * private static void setIDF() {
	 * 
	 * // Percorrendo o mapa do modelo vetorial inteiro for (Entry<Map<String,
	 * Double>, Map<Integer, Integer>> entry : mapVMR.entrySet()) {
	 * 
	 * // Vejo a quantidade de documentos em que cada palavra apareceu Double
	 * numOccurrences = (double)entry.getValue().size();
	 * 
	 * // Calculo o idf da palavra Double idf = (Double)1.0 / numOccurrences;
	 * 
	 * // Percorro as chaves o mapaKey<palavra, idf> for (String keyWord :
	 * entry.getKey().keySet()) { entry.getKey().put(keyWord, idf); } } }
	 */

	private static String clean(String str) {
		str = str.toLowerCase();
		str = str.replaceAll("&.{2,4};", " ");
		str = str.replaceAll("\\{\\{!\\}\\}", " ");
		str = str.replaceAll("\\{\\{.*?\\}\\}", "");
		str = str.replaceAll("[^a-z0-9çáéíóúàãõâêô-]", " ");

		return (str);
	}

	private static void insertIntoMap(String word, Integer documentId) {
		// Se a palavra jah existe no mapa
		if (mapVMR.get(word) != null) {
			// Recupero os documentos da palavra e verifico se o documento
			// passado por parametro jah foi inserido
			Map<Integer, Integer> map = mapVMR.get(word);
			if (map.containsKey(documentId)) { // se o documento jah foi
												// inserido, incrementa o tf
				Integer actualtf = map.get(documentId);
				map.put(documentId, actualtf + 1);

			} else { // caso contrario, adiciona o documento com tf igual a 1
				map.put(documentId, 1);
			}

		} else { // Se a palavra nao exite no mapa, adiciona ela com seu tf no
					// documento atual igual a 1
			Map<Integer, Integer> mapvalue = new HashMap<Integer, Integer>();
			mapvalue.put(documentId, 1);
			mapVMR.put(word, mapvalue);
		}
	}
}