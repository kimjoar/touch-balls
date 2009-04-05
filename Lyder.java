package spillprosjekt;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;

public class Lyder 
{
	private HashMap<String, AudioClip> lydmappe;
	private static Lyder instance;
	private HashMap<String, Long> spilleliste; //Passer på hvilke lyder som spilles nå
	public static final String LYD_TREFFER_BALL="treffer_ball",LYD_MISTER_LIV="mister_liv",
							   LYD_FAR_LIV="far_liv",LYD_UDODELIG="udodelig";
	
	
	//**************************************************************************
	// OBS! OBS! OM LYDKLIPP!!!
	//**************************************************************************
	// HAR FUNNET UT AV LYDKLIPPENE MÅ VÆRE AV EN VISS LENGDE FOR
	// AT FLERE LYDER SKAL KUNNE SPILLES AV SAMTIDIG.
	// HVIS LYDKLIPPET ER FOR KORT, SKAPER DET STORE PROBLEMER, MEN HVIS
	// LYDKLIPPET ER OVER ETT SEKUND GÅR DETTE HELT FINT.
	// HAR LAGT PÅ STILLHET PÅ DE KORTE LYDENE, SÅ DE BLIR OVER ETT SEKUND!
	//**************************************************************************
	
	private Lyder() 
	{
		// Lagrer lydene i en HashMap slik at de blir
		// enkle å hente ut igjen
		lydmappe = new HashMap <String, AudioClip>();
		spilleliste = new HashMap<String,Long>();

		// Legge til lyder
		leggTilLydserie(LYD_TREFFER_BALL, "lyd_bloop.wav",     10);
		leggTilLydserie(LYD_MISTER_LIV,   "lyd_explosion.wav",  5);
		leggTilLydserie(LYD_FAR_LIV,      "lyd_gunreload.wav",  5);
		leggTilLydserie(LYD_UDODELIG,     "lyd_moo.wav",        2);
	}
	
	public AudioClip getLyd (String lydnavn) 
	{
		if (lydnavn.equals(LYD_TREFFER_BALL)) {
			return getFreeSound(LYD_TREFFER_BALL, 10, 1000);
		} else if (lydnavn.equals(LYD_MISTER_LIV)) {
			return getFreeSound(LYD_MISTER_LIV, 5, 1000);
		} else if (lydnavn.equals(LYD_FAR_LIV)) {
			return getFreeSound(LYD_FAR_LIV, 3, 1000);
		} else if (lydnavn.equals(LYD_UDODELIG)) {
			return getFreeSound(LYD_UDODELIG, 2, 1000);
		}
		
		return null;
	}
	
	//Legger til mange av hver lyd, der antallet er spisifisert av "antall".
	//Dette gjøres fordi et AudioClip kan ikke spille to lyder oppå hverandre,
	//så flere AudioClip som representerer samme lydfilen må til for å
	//få simultan lyd
	private void leggTilLydserie (String lydnavn, String filnavn, int antall) 
	{
		for (int i=1; i <= antall; i++) {
			leggTilLyd(lydnavn + "_" + i,filnavn);			
		}
	}
	
	private void leggTilLyd (String lydnavn, String filnavn) 
	{
	  filnavn = "lyder/" + filnavn;
	  
		AudioClip lyden = Applet.newAudioClip(getClass().getResource(filnavn));
		
		// Legger til lyden
		lydmappe.put(lydnavn, lyden);
	}
	
	public static Lyder getInstance() 
	{
		if (instance == null) {
			// Lager bildeinstans
			instance = new Lyder();
		}

		return instance;
	}
	
	//Returnerer en ledig lyd av typen lydnavn. Returnerer null hvis det ikke
	//finnes noen ledige lyder.
	private AudioClip getFreeSound (String lydnavn, int antall, int tid) 
	{
		for (int i=1; i <= antall; i++) {
			String key = lydnavn + "_" + i;

			if (spilleliste.containsKey(key)) {
				if ((System.currentTimeMillis()-spilleliste.get(key).longValue()) > tid) {
					spilleliste.remove(key);
					spilleliste.put(key, System.currentTimeMillis());
					return lydmappe.get(key);
				}
			} else {
				spilleliste.put(key, System.currentTimeMillis());
				return lydmappe.get(key);
			}
		}

		return null; //Hvis det ikke er noen ledige klipp, returneres null.
	}
	
	//Denne skal kalles fra SpillMotor. Denne sørger for at lyd blir spilt.
	public void spillLyd (String lydnavn, SpillMotor motor) 
	{
		AudioClip lyd = Lyder.getInstance().getLyd(lydnavn);

		if (motor.isRunning() && lyd != null) { 
			//motor.isRunning() sørger for at lyder kun blir spillt mens SpillMotoren kjører
			//Det var et problem at noen lyder "hang igjen" etter game over, så de
			//ble spilt først når man var tilbake i menyen
			lyd.play(); //Spiller lyden hvis det er noen ledige lyder.
		}
	}	
}