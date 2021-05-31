package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * @param txtInp	Pfad Briefvorlage
 * @param csvInp	Pfad Empf�ngerdaten
 */

public class Serienbrief {
	static Path txtInp = Paths.get("Serienbrief/input/briefvorlage.txt");
	static Path csvInp =Paths.get("Serienbrief/input/empfaenger.csv");
	
	/*
	 *  Die Methoden zur Erstellung der Serienbriefe werden durchlaufen
	 */
	public static void main(String[] args) throws IOException {
		String[][] csvArray= getCSV(csvInp);
		writeFiles(csvArray, txtInp);
	}
	
	/*
	 * Das CSV File mit den Empf�ngerdaten wird durchlaufen, gestreamt die Anreden der Empf�nger eingef�gt und diese als Array zur�ckgegeben
	 * @param csvInp	�bergabe Pfad Briefvorlage
	 * @param csvString	Stream der CSV Datei
	 * @param csvArray	Array zur R�ckgabe der eingelesenen Empf�ngerdaten
	 */
	public static String[][] getCSV(Path csvInp) throws IOException {
		Stream <String> csvString = Files.lines(csvInp);
		String[][] csvArray = csvString
				.map(g -> g.replaceAll("\\bM\\b","Sehr geehrter Herr"))
				.map(g -> g.replaceAll("\\bF\\b","Sehr geehrte Frau"))
				.map(s -> s.split(";"))
				.toArray(String[][]::new);
		return csvArray; 
	}
	
	/*
	 * Die Eingelesenen Empf�ngerdaten werden in den Musterbrief eingef�gt und die generierten Serienbriefe werden gespeichert
	 * @param casArray	  Array zur R�ckgabe der eingelesenen Empf�ngerdaten
	 * @param csvInp	  �bergabe Pfad Musterbrief
	 * @param i			  Z�hler for Loop
	 * @param c			  Variable zum auslesen von Daten
	 * @param txtOut	  String Pfad Serienbrief
	 * @param outputPath  Pfad Serienbrief
	 * @param lines		  Stream der CSV Datei
	 * @param replaced	  Liste mit bearbeiteten Textbestandteilen
	 */
	public static void writeFiles(String[][] csvArray, Path txtInp) throws IOException {
		for (int i = 1 ; i <(csvArray.length);i++ ) {
			int c = i;
			String txtOut = "Serienbrief/output/quittung_"+csvArray[c][1]+".txt";

			Path outputPath = Paths.get(txtOut);
			Stream <String> lines = Files.lines(txtInp);
			List <String> replaced = lines
					.map(line -> line.replaceAll("\\{Anrede\\}", csvArray[c][2]))
					.map(line -> line.replaceAll("\\{Vorname\\}", csvArray[c][0]))
					.map(line ->line.replaceAll("\\{Name\\}", csvArray[c][1]))
					.map(line ->line.replaceAll("\\{EMail\\}", csvArray[c][3]))
					.collect(Collectors.toList());
			Files.write(outputPath, replaced);
			lines.close();
			System.out.println("Serienbrief "+i+" erstellt: "+ csvArray[c][0]+" "+csvArray[c][1]);
		}
	}
}



