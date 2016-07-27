package ranking_modelo_vetorial;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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

	private static final String OUTPUT_PATH = "data/outputRMV.txt";
	private static final String INPUT_PATH = "data/ptwiki-v2.trec";
	private static Map<Map<String, Double>, Map<Integer, Integer>> mapVMR = new HashMap<Map<String, Double>, Map<Integer, Integer>>();

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

		File fXmlFile = new File(INPUT_PATH);

		// Biblioteca para ler xml
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(fXmlFile);

		document.getDocumentElement().normalize();

		// recuperando todos os elementos dentro da tag DOC
		NodeList docs = document.getElementsByTagName("DOC");

		Integer documentId = new Integer(0);

		// percorrendo todos os elementos
		for (int i = 0; i < docs.getLength(); i++) {

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
						// inserindo cada palavra do paragrafo no mapa
						insertIntoMap(words[indexWord], documentId);
					}
				}
			}
		}
		
		setIDF();
		
		// escrevendo o mapa no arquivo
		PrintWriter writer = new PrintWriter(OUTPUT_PATH, "UTF-8");
		writer.print(mapVMR.toString());
		writer.close();
		System.out.println(mapVMR);
		
		

	}

	private static void setIDF() {
				
		// Percorrendo o mapa do modelo vetorial inteiro
		for (Entry<Map<String, Double>, Map<Integer, Integer>> entry : mapVMR.entrySet()) {
									
			// Vejo a quantidade de documentos em que cada palavra apareceu
			Double numOccurrences = (double)entry.getValue().size();
			
			// Calculo o idf da palavra
			Double idf = (Double)1.0 / numOccurrences;
			
			// Percorro as chaves o mapaKey<palavra, idf>
			for (String keyWord : entry.getKey().keySet()) {
				entry.getKey().put(keyWord, idf);
			}
		}
	}

	private static String clean(String str) {
		str = str.toLowerCase();
		str = str.replaceAll("&.{2,4};", " ");
		str = str.replaceAll("\\{\\{!\\}\\}", " ");
		str = str.replaceAll("\\{\\{.*?\\}\\}", "");
		str = str.replaceAll("[^a-z0-9çáéíóúàãõâêô-]", " ");

		return (str);
	}

	private static void insertIntoMap(String word, Integer documentId) {
		// Mapa inicialmente com o idf null
		Map<String, Double> mapKey = new HashMap<>();
		mapKey.put(word, null);
		
		// Se a palavra jah existe no mapa
		if (mapVMR.get(mapKey) != null) {
			// Recupero o mapa da palavra e verifico se o documento atual jah
			// foi inserido
			Map<Integer, Integer> map = mapVMR.get(mapKey);
			if (map.containsKey(documentId)) { // se o documento jah foi
												// inserido, incrementa o tf
				Integer actualtf = map.get(documentId);
				map.put(documentId, actualtf + 1);

			} else { // caso contrario adiciona o documento com tf igual a 1
				map.put(documentId, 1);
			}

		} else { // Se a palavra nao exite no mapa, adiciona ela com seu tf no
				// documento atual igual a 1
			Map<Integer, Integer> mapvalue = new HashMap<Integer, Integer>();
			mapvalue.put(documentId, 1);
			mapVMR.put(mapKey, mapvalue);
		}
	}

}
