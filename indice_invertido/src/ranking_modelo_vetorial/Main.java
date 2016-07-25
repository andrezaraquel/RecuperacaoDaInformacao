package ranking_modelo_vetorial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	private static final String OUTPUT_PATH = "data/outputIndex.txt";
	private static final String INPUT_PATH = "data/ptwiki-v2.trec";
	private static Map<String, Map<Integer, Integer>> mapWords = new HashMap<String, Map<Integer, Integer>>();

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
		// Se a palavra jah existe no mapa
		if (mapWords.get(word) != null) {
			// Recupero o mapa da palavra e verifico se o documento atual jah foi inserido
			Map<Integer, Integer> map = mapWords.get(word);
			if (map.containsKey(documentId)) { // se o documento jah foi inserido, incrementa o tf
				Integer actualtf = map.get(documentId);
				actualtf++;
				
			} else { // caso contrario adiciona o documento com tf igual a 1
				map.put(documentId, 1);
			}
			
		} else { // Se a palavra não exite no mapa, adiciona ela com seu tf no documento atual igual a 1
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			map.put(documentId, 1);
			mapWords.put(word, map);
		}
	}

}
