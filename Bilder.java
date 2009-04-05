package spillprosjekt;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Bilder 
{
	private Image bilde;
	private HashMap<String, Image> bildemappe;
	private static Bilder instance;

	private Bilder() 
	{
		// Lagrer bildene i en HashMap slik at de blir
		// enkle å hente ut igjen
		bildemappe = new HashMap<String, Image>();

		// Legge til bilder
		
		//Baller:
		leggTilBilde("ball_ond","ball_ond.gif");
		leggTilBilde("ball_liv","ball_liv.gif");
		leggTilBilde("ball_udodelig","ball_udodelig.gif");
		leggTilBilde("ball_50","ball_50.gif");
		leggTilBilde("ball_100","ball_100.gif");
		leggTilBilde("ball_200","ball_200.gif");
		leggTilBilde("ball_500","ball_500.gif");
		leggTilBilde("ball_1000","ball_1000.gif");
		
		//Spiller:
		leggTilBilde("spiller","spiller.gif");
		leggTilBilde("spiller_vondt","spiller_vondt.gif");
		leggTilBilde("spiller_udodelig","spiller_udodelig.gif");
		leggTilBilde("spiller_chucknorris","spiller_chucknorris.gif");
		
		//Spillbilder:
		leggTilBilde("spill_gameover","spill_gameover.gif");		
		
		//Menybilder:
		leggTilBilde("meny_bakgrunn","meny_bakgrunn.jpg");
		leggTilBilde("meny_head_touchballs","meny_head_touchballs.gif");
		leggTilBilde("meny_head_highscore","meny_head_highscore.gif");
		leggTilBilde("meny_head_gratulerer","meny_head_gratulerer.gif");
		leggTilBilde("meny_head_omspillet","meny_head_omspillet.gif");
		leggTilBilde("meny_head_alternativer","meny_head_alternativer.gif");
		leggTilBilde("meny_head_juksekoder","meny_head_juksekoder.gif");
		
		//Knapper:
		leggTilBilde("meny_knapp_nyttspill","meny_knapp_nyttspill.gif");
		leggTilBilde("meny_knapp_nyttspill_over","meny_knapp_nyttspill_over.gif");
		leggTilBilde("meny_knapp_highscore","meny_knapp_highscore.gif");
		leggTilBilde("meny_knapp_highscore_over","meny_knapp_highscore_over.gif");
		leggTilBilde("meny_knapp_omspillet","meny_knapp_omspillet.gif");
		leggTilBilde("meny_knapp_omspillet_over","meny_knapp_omspillet_over.gif");
		leggTilBilde("meny_knapp_fullskjerm","meny_knapp_fullskjerm.gif");
		leggTilBilde("meny_knapp_fullskjerm_over","meny_knapp_fullskjerm_over.gif");
		leggTilBilde("meny_knapp_avslutt","meny_knapp_avslutt.gif");
		leggTilBilde("meny_knapp_avslutt_over","meny_knapp_avslutt_over.gif");
		leggTilBilde("meny_knapp_visivindu","meny_knapp_visivindu.gif");
		leggTilBilde("meny_knapp_visivindu_over","meny_knapp_visivindu_over.gif");
		leggTilBilde("meny_knapp_tilbake","meny_knapp_tilbake.gif");
		leggTilBilde("meny_knapp_tilbake_over","meny_knapp_tilbake_over.gif");
		leggTilBilde("meny_knapp_ok","meny_knapp_ok.gif");
		leggTilBilde("meny_knapp_ok_over","meny_knapp_ok_over.gif");
		leggTilBilde("meny_knapp_alternativer","meny_knapp_alternativer.gif");
		leggTilBilde("meny_knapp_alternativer_over","meny_knapp_alternativer_over.gif");
		leggTilBilde("meny_knapp_juksekoder","meny_knapp_juksekoder.gif");
		leggTilBilde("meny_knapp_juksekoder_over","meny_knapp_juksekoder_over.gif");
		leggTilBilde("meny_knapp_lyd_on","meny_knapp_lyd_on.gif");
		leggTilBilde("meny_knapp_lyd_on_over","meny_knapp_lyd_on_over.gif");
		leggTilBilde("meny_knapp_lyd_off","meny_knapp_lyd_off.gif");
		leggTilBilde("meny_knapp_lyd_off_over","meny_knapp_lyd_off_over.gif");
		leggTilBilde("meny_knapp_musikk_on","meny_knapp_musikk_on.gif");
		leggTilBilde("meny_knapp_musikk_on_over","meny_knapp_musikk_on_over.gif");
		leggTilBilde("meny_knapp_musikk_off","meny_knapp_musikk_off.gif");
		leggTilBilde("meny_knapp_musikk_off_over","meny_knapp_musikk_off_over.gif");
		leggTilBilde("meny_knapp_fx_on","meny_knapp_fx_on.gif");
		leggTilBilde("meny_knapp_fx_on_over","meny_knapp_fx_on_over.gif");
		leggTilBilde("meny_knapp_fx_off","meny_knapp_fx_off.gif");
		leggTilBilde("meny_knapp_fx_off_over","meny_knapp_fx_off_over.gif");

		
		//Hell:
		leggTilBilde("hell_bakgrunn","hell_bakgrunn.jpg");
		leggTilBilde("hell_spiller","hell_spiller.gif");
		leggTilBilde("hell_spiller_vondt","hell_spiller_vondt.gif");
		leggTilBilde("hell_spiller_udodelig","hell_spiller_udodelig.gif");
		
		//Visuelle effekter:
		for ( int i=1; i<=9; i++ ) {
			leggTilBilde("fx_explosion_0" + i,"fx_explosion_0" + i + ".gif");
		}
		for ( int i=0; i<=7; i++ ) {
			leggTilBilde("fx_explosion_1" + i,"fx_explosion_1" + i + ".gif");
		}
		
	}

	private void leggTilBilde(String bildenavn, String bildefil) 
	{
	  bildefil = "bilder/" + bildefil;
	  
		// lager bildeinstans
		ImageIcon ic = new ImageIcon(getClass().getResource(bildefil));
		
		// Henter bilde
		bilde = ic.getImage();
		
		// Legger til bilder
		bildemappe.put(bildenavn, bilde);
	}

	public static Bilder getInstance() 
	{
		if (instance == null) {
			// Lager bildeinstans
			instance = new Bilder();
		}

		return instance;
	}

	public Image getBilde (String bildetittel) {
		return bildemappe.get(bildetittel);
	}	
}