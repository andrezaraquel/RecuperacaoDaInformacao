package lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Input {

	public void geraDocs(String inputPath, String folderDestinationPath) throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(inputPath);

		// Biblioteca para ler xml
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(fXmlFile);

		document.getDocumentElement().normalize();

		// Recuperando todos os elementos dentro da tag DOC
		NodeList docs = document.getElementsByTagName("DOC");

		String documentId;
		int collectionSize = docs.getLength();
		// Percorrendo todos os elementos
		for (int i = 0; i < collectionSize; i++) {

			Node node = docs.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;

				documentId = eElement.getElementsByTagName("DOCNO").item(0).getTextContent();

				// Recuperando o conteudo dentro da tag P
				String paragraph = eElement.getElementsByTagName("P").item(0).getTextContent();
				paragraph = clean(paragraph);

				// Criando os arquivos com os documentos
				try {
					PrintWriter writer = new PrintWriter(folderDestinationPath + documentId, "UTF-8");
					writer.print(paragraph);
					writer.close();

				} catch (FileNotFoundException | UnsupportedEncodingException e) {					
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String clean(String str) {
		str = str.toLowerCase();
		str = str.replaceAll("&.{2,4};", " ");
		str = str.replaceAll("\\{\\{!\\}\\}", " ");
		str = str.replaceAll("\\{\\{.*?\\}\\}", " ");
		str = str.replaceAll("[^a-z0-9çáéíóúàãõâêô-]", " ");
		str = str.replaceAll("           ", " ");
		str = str.replaceAll("          ", " ");
		str = str.replaceAll("         ", " ");
		str = str.replaceAll("        ", " ");
		str = str.replaceAll("       ", " ");
		str = str.replaceAll("      ", " ");
		str = str.replaceAll("     ", " ");
		str = str.replaceAll("    ", " ");
		str = str.replaceAll("   ", " ");
		str = str.replaceAll("  ", " ");

		return (str);
	}
}
