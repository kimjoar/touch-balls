package spillprosjekt;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;

public class Musikk 
{
	private HashMap<Integer, AudioClip> musikkmappe;
	private static Musikk instance;
	private AudioClip musikk;
	
	private Musikk () 
	{
		musikkmappe = new HashMap <Integer, AudioClip>();

		// Legge til musikk
		leggTilLyd(1,   "musikk_Gunsmoke_Level_1.mid");
		leggTilLyd(2,   "musikk_Adventure_Island_2_Overworld_1.mid");
		leggTilLyd(2,   "musikk_Adventure_Island_2_Overworld_2.mid");
		leggTilLyd(2,   "musikk_Adventure_Island_2_Overworld_3.mid");
		leggTilLyd(3,   "musikk_Adventure_Island_2_Underworld_1.mid");
		leggTilLyd(4,   "musikk_Chip_n_Dales_Rescue_Rangers_Beginning_Stage_2.mid");
		leggTilLyd(5,   "musikk_Bubble_Bobble_Main.mid");
		leggTilLyd(100, "musikk_Adventure_Island_2_Underworld_2.mid");
	}
		
	private void leggTilLyd (int i, String filnavn) 
	{
	  filnavn = "musikk/" + filnavn;
	  
		AudioClip lyden = Applet.newAudioClip(getClass().getResource(filnavn));
		
		// Legger til lyden
		musikkmappe.put(i, lyden);
	}
	
	public static Musikk getInstance() 
	{
		if (instance == null) {
			// Lager bildeinstans
			instance = new Musikk();
		}
	
		return instance;
	}
	
	//Denne skal kalles fra SpillMotor. Denne sørger for at lyd blir spilt.
	public void startMusikk() 
	{
		musikk = getRandomTune();
		musikk.loop();
	}
	
	public void startMusikk(int nummer) 
	{
		musikk = musikkmappe.get(nummer);
		musikk.loop();
	}
	
	public void stoppMusikk() 
	{
		musikk.stop();
	}
	
	private AudioClip getRandomTune() {
		int number = (int)Math.ceil(1.0*Math.random()*(musikkmappe.size()-1));
		return musikkmappe.get(number);
	}
}