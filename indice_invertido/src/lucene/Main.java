package lucene;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.xml.sax.SAXException;

import indice_invertido.Input;

public class Main {

	private static final String CAMINHO_PASTA = "C:/Users/Andreza/Desktop/DOCS_RI/";
	private static final String INPUT_PATH = "data/ptwiki-v2.trec";
	private static final int QUANTIDADE_ELEMENTOS = 10;
	private static String consulta = "sistema operacional";

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//
//		Input input = new Input();
//		input.geraDocs(INPUT_PATH, CAMINHO_PASTA);
				
		try {
			Lucene lucene = new Lucene();
			lucene.lerDocs(CAMINHO_PASTA);			
			lucene.luceneBusca(consulta, QUANTIDADE_ELEMENTOS);

		} catch (IOException e) {
			System.err.println("Erro ao criar index!");
			e.printStackTrace();

		} catch (ParseException e) {
			System.err.println("Busca inválida!");
			e.printStackTrace();
		}
	}
}
