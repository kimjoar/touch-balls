package spillprosjekt;

import java.io.*;
import java.util.*;

public class Highscore 
{
	private final String filename = "highscore.tb";
	private ArrayList<String> navn;
	private ArrayList<Integer> poeng;
	
	public Highscore () throws IOException 
	{
		checkFile();
		readFile();
	}

	// Sjekker om highscorefila eksisterer, og lager en ny fil
	// hvis den ikke gjør det.
	private void checkFile() throws IOException
	{
		try {
			File fil = new File(filename);
			new FileInputStream(fil);
		} catch(FileNotFoundException ex) {
			//Filen finnes ikke, så en ny fil lages:
			createNewFile();
		}
	}
	
	// Lager en ny highscorefil
	private void createNewFile() throws IOException 
	{	
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(filename));			
			for (int i=0; i < 10; i++){	
				bw.write("#:-1");
				bw.newLine();
			}
		} catch(IOException ex) {
		}	finally {
			bw.close();
		}
	}
	
	// Henter ut highscoren fra fila, og lager den i navn- og poengtabellene.
	// 1.plass tilsvarer tabellposisjon 0, osv.
	public void readFile() throws IOException 
	{		
		navn = new ArrayList<String>();
		poeng = new ArrayList<Integer>();
		BufferedReader reader = null;
		
		try {
			File fil = new File("highscore.tb");
			FileInputStream in = new FileInputStream(fil);
			reader = new BufferedReader(new InputStreamReader(in));
			
			//Legger til alle navn og poengsummer i tabellene:
			while (reader.ready()) {
				String linje = reader.readLine();
				String[] tabell = linje.split(":");
				navn.add(tabell[0]);
				poeng.add(Integer.parseInt(tabell[1]));
			}
		}	catch(FileNotFoundException ex){
			System.err.println("Fil ikke funnet!");
		}	catch(IOException ex){
			System.err.println("Feil ved lesing av fil!");
		}	finally {
			reader.close();
		}
	}
	
	// Sjekker om argumentet er nok poeng til å komme på lista.
	// Returnerer true hvis det er det.
	public boolean erNokPoeng (int poeng) 
	{
		return (this.poeng.get(this.poeng.size()-1) < poeng);
	}
	
	// Oppdaterer highscorefila med et nytt navn
	public void leggTil (String navn, int poeng) throws IOException 
	{
		if (erNokPoeng(poeng)) {
			BufferedWriter bw = null;

			try {
				bw = new BufferedWriter (new FileWriter(filename));			
				boolean written = false;

				for (int i=0; i < 9; i++) {
					if (written == false && poeng > this.poeng.get(i)) {
						bw.write(navn + ":" +  poeng);
						i--;
						written = true;
					} else {
						bw.write(this.navn.get(i) + ":" +  this.poeng.get(i));
					}
					
					if (i < 8) {
						bw.newLine();
					}
				}

				if (!written) {
					bw.newLine();
					bw.write(navn + ":" +  poeng);
				}
			}	catch(IOException ex){				
			}	finally{
				bw.close();
			}
		}
		
		this.readFile();
	}
	
	public ArrayList<String> getNavn() 
	{
		return navn;
	}
	
	public ArrayList<Integer> getPoeng() 
	{
		return poeng;
	}
	
	public int getTopScore() 
	{
		return poeng.get(0);
	}
}