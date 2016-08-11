package lucene;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.xml.sax.SAXException;

public class Main {

	private static final String CAMINHO_PASTA = "/home/andrezarmq/Área de Trabalho/DOCRI/";
	private static final String INPUT_PATH = "data/ptwiki-v2.trec";
	private static final int QUANTIDADE_ELEMENTOS = 2;
	private static String consulta = "gênero musical";

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		Util.geraDocs(INPUT_PATH, CAMINHO_PASTA);

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
		
//		double relevantRetrieved = 6;
//		double irrelevantRetrieved = 4;
//		double relevant = 10;
//		
//		double precision = Util.precision(relevantRetrieved, irrelevantRetrieved);
//		double recall =  Util.recall(relevantRetrieved, relevant);
//		
//		System.out.println("Precision: "+ precision);
//		System.out.println("Recall: "+ recall);
//		System.out.println("F-Measure"+ Util.f_measure(precision, recall));
		
	}
	
}
