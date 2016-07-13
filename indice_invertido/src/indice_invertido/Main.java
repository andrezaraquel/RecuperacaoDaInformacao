package indice_invertido;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	private static final String OUTPUT_PATH = "/home/andrezarmq/Downloads/outputIndex.txt";
	private static final String INPUT_PATH = "/home/andrezarmq/Downloads/ptwiki-v2.trec";
	private static Map<String, List<Integer>> mapWords = new HashMap<String, List<Integer>>();

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

		File fXmlFile = new File(INPUT_PATH);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(fXmlFile);

		document.getDocumentElement().normalize();

		NodeList docs = document.getElementsByTagName("DOC");

		Integer documentId = new Integer(0);

		for (int i = 0; i < docs.getLength(); i++) {

			Node node = docs.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;

				documentId++;

				String paragraph = eElement.getElementsByTagName("P").item(0).getTextContent();
				paragraph = clean(paragraph);

				String[] words = paragraph.split(" ");

				for (int indexWord = 0; indexWord < words.length; indexWord++) {
					if (!words[indexWord].isEmpty()) {
						insertIntoMap(words[indexWord], documentId);
					}
				}

			}
		}

		PrintWriter writer = new PrintWriter(OUTPUT_PATH, "UTF-8");
		writer.print(mapWords.toString());
		writer.close();

		System.out.println("======================================================");

		// List<Integer> a = new ArrayList<>();
		// List<Integer> b = new ArrayList<>();
		// a.add(1);
		// a.add(2);
		// a.add(3);
		// a.add(4);
		// a.add(5);
		//
		// b.add(1);
		// b.add(2);
		// b.add(5);
		// b.add(6);
		// b.add(7);
		// b.add(10);
		// b.add(11);
		//
		// System.out.println(union(a, b));
		// System.out.println(intersect(b, a));

		searchAnd("nomes", "bíblicos");
		searchOr("nomes", "bíblicos");
		searchAnd("estados", "unidos");
		searchOr("estados", "unidos");
		searchAnd("winston", "churchill");
		searchOr("winston", "churchill");

	}

	private static void searchOr(String par1, String par2) {
		List<Integer> list1 = mapWords.get(par1);
		List<Integer> list2 = mapWords.get(par2);

		List<Integer> result = union(list1, list2);
		System.out.println("The result of union between " + par1 + " and " + par2 + " is " + result);
	}

	private static void searchAnd(String par1, String par2) {
		List<Integer> list1 = mapWords.get(par1);
		List<Integer> list2 = mapWords.get(par2);

		List<Integer> result = intersect(list1, list2);

		System.out.println("The result of intersect between " + par1 + " and " + par2 + " is " + result);
	}

	private static String clean(String str) {
		String aux;
		str = str.toLowerCase();
		str = str.replaceAll("&.{2,4};", " ");
		str = str.replaceAll("\\{\\{!\\}\\}", " ");
		str = str.replace("{{.*?}}", "");
		str = str.replaceAll("[^a-z0-9çáéíóúàãõâêô-]", " ");

		return (str);
	}

	private static void insertIntoMap(String word, Integer documentId) {
		if (mapWords.get(word) != null) {
			List<Integer> list = mapWords.get(word);
			if (!list.contains(documentId)) {
				list.add(documentId);
			}
		} else {
			List<Integer> list = new ArrayList<Integer>();
			list.add(documentId);
			mapWords.put(word, list);
		}

	}

	private static List<Integer> union(List<Integer> listA, List<Integer> listB) {
		List<Integer> result = new ArrayList<Integer>();
		addNoDups(result, listA);
		addNoDups(result, listB);
		return result;
	}

	private static void addNoDups(List<Integer> toAddTo, List<Integer> iterateOver) {
		for (Integer num : iterateOver) {
			if (toAddTo.indexOf(num) == -1) {
				toAddTo.add(num);
			}
		}
	}

	private static List<Integer> intersect(List<Integer> listA, List<Integer> listB) {
		List<Integer> result = new ArrayList<Integer>();

		if (listA != null && listB != null) {
			int sizeA = listA.size();
			int sizeB = listB.size();

			int indexA = 0;
			int indexB = 0;
			while (indexA < sizeA && indexB < sizeB) {

				if (listA.get(indexA).equals(listB.get(indexB))) {
					result.add(listA.get(indexA));
					indexA++;
					indexB++;

				} else if (listA.get(indexA) - listB.get(indexB) < 0) {
					indexA++;
				} else {
					indexB++;
				}
			}
		}
		return result;
	}

}
