package spillprosjekt;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class MenyPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener 
{	
	private static final long serialVersionUID = 1L;
	private int knapp = 0;
	
	//Alle knappene i menyen har hver sin konstant:
	public static final int KNAPP_INGEN=0, KNAPP_NYTTSPILL=1, KNAPP_HIGHSCORE=2, KNAPP_OMSPILLET=3,
							KNAPP_FULLSKJERM=4, KNAPP_AVSLUTT=5, KNAPP_TILBAKE=6, KNAPP_OK=7,
							KNAPP_ALTERNATIVER=8, KNAPP_LYD=9, KNAPP_MUSIKK=10, KNAPP_FX=11, 
							KNAPP_JUKSEKODER=12;
	
	private int x,y; //Musepeker
	
	private TouchBalls game;
	
	private boolean inMenu = true; //Skiller mellom om man er i menyen eller i spillet
	
	public static final int MENY_HOVED=0, MENY_HIGHSCORE=1, MENY_OMSPILLET=2, MENY_SKRIVNAVN=3,
							MENY_ALTERNATIVER=4, MENY_JUKSEKODER=5; //De forskjellige delene av menyen har hver sin konstant
	
	private int menyDel; //Holder på den delen av menyen man er i nå.
	private String name = ""; //Brukes når man skal skrive inn nanvnet sitt
	private boolean focus = false;
	private Highscore hs = null;

	public void setInMenu(boolean inMenu) 
	{
		this.inMenu = inMenu;
	}
	
	public MenyPanel(TouchBalls game) 
	{
		this.game = game;
		
		//Leser highscoren:
		try {
			hs = new Highscore(); //Henter ut highscoren
		} catch (Exception e) {
			//Dette burde ikke skje
		}
		
		menyDel = MENY_HOVED; //Man starter i hovedmenyen!
	}
	
	public MenyPanel (TouchBalls game, int menydel) 
	{
		this.game = game;
		
		//Leser highscoren:
		try {
			hs = new Highscore(); //Henter ut highscoren
		} catch (Exception e) {
			//Dette burde ikke skje
		}
		
		menyDel = menydel; //Man starter i den delen av menyen spesifisert
		setFocusable(true);
		addKeyListener(this);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		tegnMeny(g);
	}
	
	private void tegnMeny(Graphics g) 
	{	
		int xPos = 250;
		int yPos = 230;
		g.drawImage(Bilder.getInstance().getBilde("meny_bakgrunn"), 0, 0, null);

		if (menyDel == MenyPanel.MENY_HOVED) {
			//-------------------------------------------------------------------------------
			// HOVED-DELEN AV MENYEN
			//-------------------------------------------------------------------------------
			
			g.drawImage(Bilder.getInstance().getBilde("meny_head_touchballs"), 185, 85, null);
			
			//Tegner knappene:
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_nyttspill"), xPos, yPos, null);
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_highscore"), xPos, yPos+(50+10)*1, null);
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_omspillet"), xPos, yPos+(50+10)*2, null);
			
			//g.drawImage(Bilder.getInstance().getBilde("meny_knapp_fullskjerm"), xPos, yPos+(50+10)*3, null);	
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_alternativer"), xPos, yPos+(50+10)*3, null);	
			
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_avslutt"), xPos, yPos+(50+10)*4, null);
			
			int dy = 0;
			if (game.isFullscreen()) {
				//Hvis programmet kjører i fullskjerm, må musepilens koordinatsystem flyttes litt oppover.
				dy = -25;
			}
			
			//Sjekker om musepekeren er over noen av knappene. Tegner i så fall en annen knapp.
			if (x > xPos && x < (xPos+300)) {
				if (y > (yPos+25+dy) && y < (yPos+50+25+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_nyttspill_over"), xPos, yPos, null);
					knapp = MenyPanel.KNAPP_NYTTSPILL;
				} else if (y > (yPos+25+(50+10)*1+dy) && y < (yPos+50+25+(50+10)*1+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_highscore_over"), xPos, yPos+(50+10)*1, null);
					knapp = MenyPanel.KNAPP_HIGHSCORE;
				} else if (y > (yPos+25+(50+10)*2+dy) && y < (yPos+50+25+(50+10)*2+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_omspillet_over"), xPos, yPos+(50+10)*2, null);
					knapp = MenyPanel.KNAPP_OMSPILLET;
				} else if (y > (yPos+25+(50+10)*3+dy) && y < (yPos+50+25+(50+10)*3+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_alternativer_over"), xPos, yPos+(50+10)*3, null);
					knapp = MenyPanel.KNAPP_ALTERNATIVER;
				} else if (y > (yPos+25+(50+10)*4+dy) && y < (yPos+50+25+(50+10)*4+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_avslutt_over"), xPos, yPos+(50+10)*4, null);
					knapp = MenyPanel.KNAPP_AVSLUTT;
				}
			}
			
		} else if (menyDel == MenyPanel.MENY_HIGHSCORE) {
			//-------------------------------------------------------------------------------
			// HIGHSCORE-DELEN AV MENYEN
			//-------------------------------------------------------------------------------
			
			yPos = 480;
			int dy = 0;
			if (game.isFullscreen()) {
				//Hvis programmet kjører i fullskjerm, må musepilens koordinatsystem flyttes litt oppover.
				dy = -25;
			}
			
			//Tegner bakgrunnen tittelen og tilbake-knappen
			g.drawImage(Bilder.getInstance().getBilde("meny_head_highscore"), 243, 50, null);
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_tilbake"), xPos, yPos, null);
			
			//Sjekker om musa er over noen av knappene
			if (x > xPos && x < (xPos+300)) {
				if (y > (yPos+25+dy) && y < (yPos+50+25+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_tilbake_over"), xPos, yPos, null);
					knapp = MenyPanel.KNAPP_TILBAKE;
				}
			}
			
			//Posisjonen til highscorelista:
			xPos = 170;
			yPos = 170;
			dy = 30; //Avstanden mellom hver linje
			
			//Setter skrifttypen
			Graphics2D g2d = (Graphics2D) g;
			g2d.setFont(new Font("Courier New", Font.BOLD, 30));
			g2d.setColor(new Color(255,255,255));
			
			ArrayList<String> navn = hs.getNavn();
			ArrayList<Integer> poeng = hs.getPoeng();
			
			for (int i=0; i < 10; i++) {
				//Tegner på skjermen:
				g2d.drawString((i+1) + ".", xPos, yPos+(dy*i));
				if (!navn.get(i).equals("#")) {
					g2d.drawString(navn.get(i) , xPos+60, yPos+(dy*i));
					g2d.drawString(poeng.get(i) + "" , xPos+370, yPos+(dy*i));
				}
			}
			
		} else if (menyDel == MenyPanel.MENY_OMSPILLET) {
			//-------------------------------------------------------------------------------
			// "OM SPILLET"-DELEN AV MENYEN
			//-------------------------------------------------------------------------------
			
			int dy = 0;
			if (game.isFullscreen()) {
				//Hvis programmet kjører i fullskjerm, må musepilens koordinatsystem flyttes litt oppover.
				dy = -25;
			}
			
			Graphics2D g2d = (Graphics2D) g;
			g2d.setFont(new Font("Courier New", Font.BOLD, 30));
			g2d.setColor(new Color(255,255,255));
			String[] linjer;
			
			yPos = 480;
			
			//Tegner bakgrunnen tittelen og tilbake-knappen
			g.drawImage(Bilder.getInstance().getBilde("meny_head_omspillet"), 240, 50, null);
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_tilbake"), xPos, yPos, null);
			
			//Sjekker om musa er over noen av knappene
			if (x > xPos && x < (xPos+300)) {
				if (y > (yPos+25+dy) && y < (yPos+50+25+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_tilbake_over"), xPos, yPos, null);
					knapp = MenyPanel.KNAPP_TILBAKE;
				}
			}
			
			yPos = 230;
			
			//Hvis man har nok poeng til å få tilgang til juksekodene
			if (hs.getTopScore() >= 50000) {
				g.drawImage(Bilder.getInstance().getBilde("meny_knapp_juksekoder"), xPos, yPos+(50+10)*3, null);
				//Sjekker om musepekeren er over noen av knappene. Tegner i så fall en annen knapp.
				if (x > xPos && x < (xPos+300)) {
					if (y > (yPos+25+(50+10)*3+dy) && y < (yPos+50+25+(50+10)*3+dy)) {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_juksekoder_over"), xPos, yPos+(50+10)*3, null);
						knapp = MenyPanel.KNAPP_JUKSEKODER;
					} 
				}
			} else {
				dy = 25; //Avstanden mellom linjene
				yPos = 380;
				g2d.setFont(new Font("Courier New", Font.BOLD, 20));
				
				linjer = new String[2];
				
				linjer[0] = "Hvis du får 50000 eller mer poeng i spillet,";
				linjer[1] = "får du tilgang til noen juksekoder.";
				
				for (int i=0; i < linjer.length; i++) {
					xPos = (int) Math.round(400-(linjer[i].length()/2.0)*12); //Regner ut plasseringen til teksten
					g2d.drawString(linjer[i] , xPos, yPos+(dy*i));
				}
			}
			
			dy = 35; //Avstanden mellom linjene
			yPos = 220;
			xPos = 250;
			g2d.setFont(new Font("Courier New", Font.BOLD, 30));
			
			linjer = new String[3];
			
			linjer[0] = "Touch Balls ble laget av";
			linjer[1] = "Henriette, Kirsti, Kim-Joar";
			linjer[2] = "og Sindre våren 2006.";
			
			for (int i=0; i < linjer.length; i++) {
				xPos = (int) Math.round(400-(linjer[i].length()/2.0)*18); //Regner ut plasseringen til teksten
				g2d.drawString(linjer[i] , xPos, yPos+(dy*i));
			}
			
		} else if (menyDel == MenyPanel.MENY_SKRIVNAVN) {
			//-------------------------------------------------------------------------------
			// "SKRIV NAVN"-DELEN AV MENYEN
			//-------------------------------------------------------------------------------
			
			//Sørger for at panelet har tastaturfokus
			if (!focus) {
				requestFocusInWindow();
				focus = true;
			}
			
			yPos = 480;
			int dy = 0;
			if (game.isFullscreen()) {
				//Hvis programmet kjører i fullskjerm, må musepilens koordinatsystem flyttes litt oppover.
				dy = -25;
			}
			
			//Tegner bakgrunnen tittelen og tilbake-knappen
			g.drawImage(Bilder.getInstance().getBilde("meny_head_gratulerer"), 219, 50, null);
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_ok"), xPos, yPos, null);
			
			//Sjekker om musa er over noen av knappene
			if (x > xPos && x < (xPos+300)) {
				if (y > (yPos+25+dy) && y < (yPos+50+25+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_ok_over"), xPos, yPos, null);
					knapp = MenyPanel.KNAPP_OK;
				}
			}
			
			dy = 35; //Avstanden mellom linjene
			yPos = 200;
			
			Graphics2D g2d = (Graphics2D) g;
			g2d.setFont(new Font("Courier New", Font.BOLD, 30));
			g2d.setColor(new Color(255,255,255));
			
			//Infotekst:
			String linje1 = "Du kom på highscorelista!";
			String linje2 = "Skriv inn navnet ditt:";
			xPos = (int) Math.round(400-(linje1.length()/2.0)*18); //Regner ut plasseringen til teksten
			g2d.drawString(linje1 , xPos, yPos);
			xPos = (int) Math.round(400-(linje2.length()/2.0)*18); //Regner ut plasseringen til teksten
			g2d.drawString(linje2 , xPos, yPos+dy);
			
			yPos = 280;
			//Lager en halvgjennomsiktig bakgrunn den navnet kan skrives:
			g2d.setColor(new Color(134,138,226,50)); //Setter fargen til svart, gjennomsiktig
			g2d.fillRect(125, yPos, 550, 55); //(x,y,dx,dy)
			
			//Skriver ut navnet:
			g2d.setFont(new Font("Courier New", Font.BOLD, 50));
			g2d.setColor(new Color(255,255,255));
			xPos = (int) Math.round(400-(name.length()/2.0)*30); //Regner ut plasseringen til teksten
			g2d.drawString(name , xPos, yPos + 40);
			
			//Antall poeng:
			g2d.setFont(new Font("Courier New", Font.BOLD, 30));
			String linje3 = "Antall poeng: " + game.getPoeng();
			xPos = (int) Math.round(400-(linje3.length()/2.0)*18); //Regner ut plasseringen til teksten
			yPos = 400;
			g2d.drawString(linje3 , xPos, yPos);
			
		} else if (menyDel == MenyPanel.MENY_ALTERNATIVER) {
			//-------------------------------------------------------------------------------
			// ALTERNATIVER-DELEN AV MENYEN
			//-------------------------------------------------------------------------------
			
			int dy = 0;
			if (game.isFullscreen()) {
				//Hvis programmet kjører i fullskjerm, må musepilens koordinatsystem flyttes litt oppover.
				dy = -25;
			}
			
			//Tegner knappene:
			if (game.isSoundOn()) {
				g.drawImage(Bilder.getInstance().getBilde("meny_knapp_lyd_on"), xPos, yPos-(50+10)*1, null);
			} else {
				g.drawImage(Bilder.getInstance().getBilde("meny_knapp_lyd_off"), xPos, yPos-(50+10)*1, null);
			}
			if (game.isMusicOn()) {
				g.drawImage(Bilder.getInstance().getBilde("meny_knapp_musikk_on"), xPos, yPos, null);
			} else {
				g.drawImage(Bilder.getInstance().getBilde("meny_knapp_musikk_off"), xPos, yPos, null);
			}
			if (game.isFxOn()) {
				g.drawImage(Bilder.getInstance().getBilde("meny_knapp_fx_on"), xPos, yPos+(50+10)*1, null);
			} else {
				g.drawImage(Bilder.getInstance().getBilde("meny_knapp_fx_off"), xPos, yPos+(50+10)*1, null);
			}
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_fullskjerm"), xPos, yPos+(50+10)*2, null);
			
			if (game.isFullscreen()) {
				//Hvis programmet kjører i fullskjerm, må musepilens koordinatsystem flyttes litt oppover,
				//og knappen "fullskjerm" byttes ut:
				dy = -25;
				g.drawImage(Bilder.getInstance().getBilde("meny_knapp_visivindu"), xPos, yPos+(50+10)*2, null);	
			}
			
			//Sjekker om musepekeren er over noen av knappene. Tegner i så fall en annen knapp.
			if (x > xPos && x < (xPos+300)) {
				if (y > (yPos+25-(50+10)*1+dy) && y < (yPos+50+25-(50+10)*1+dy)) {
					if (game.isSoundOn()) {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_lyd_on_over"), xPos, yPos-(50+10)*1, null);
					} else {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_lyd_off_over"), xPos, yPos-(50+10)*1, null);
					}
					knapp = MenyPanel.KNAPP_LYD;
				} else if (y > (yPos+25+dy) && y < (yPos+50+25+dy)) {
					if (game.isMusicOn()) {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_musikk_on_over"), xPos, yPos, null);
					} else {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_musikk_off_over"), xPos, yPos, null);
					}
					knapp = MenyPanel.KNAPP_MUSIKK;
				} else if (y > (yPos+25+(50+10)*1+dy) && y < (yPos+50+25+(50+10)*1+dy)) {
					if (game.isFxOn()) {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_fx_on_over"), xPos, yPos+(50+10)*1, null);
					} else {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_fx_off_over"), xPos, yPos+(50+10)*1, null);
					}
					knapp = MenyPanel.KNAPP_FX;
				} else if (y > (yPos+25+(50+10)*2+dy) && y < (yPos+50+25+(50+10)*2+dy)) {
					//Knappen fullskjerm er avhengig av om man er i fullskjerm eller ikke:
					if (!game.isFullscreen()) {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_fullskjerm_over"), xPos, yPos+(50+10)*2, null);
					} else {
						g.drawImage(Bilder.getInstance().getBilde("meny_knapp_visivindu_over"), xPos, yPos+(50+10)*2, null);
					}
					knapp = MenyPanel.KNAPP_FULLSKJERM;
				}
			}
			
			
			yPos = 480;
			//Tegner bakgrunnen tittelen og tilbake-knappen
			g.drawImage(Bilder.getInstance().getBilde("meny_head_alternativer"), 200, 50, null);
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_tilbake"), xPos, yPos, null);
			
			//Sjekker om musa er over noen av knappene
			if (x > xPos && x < (xPos+300)) {
				if (y > (yPos+25+dy) && y < (yPos+50+25+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_tilbake_over"), xPos, yPos, null);
					knapp = MenyPanel.KNAPP_TILBAKE;
				}
			}
			
		} else if (menyDel == MenyPanel.MENY_JUKSEKODER) {
			//-------------------------------------------------------------------------------
			// JUKSEKODER-DELEN AV MENYEN
			//-------------------------------------------------------------------------------
			
			int dy = 0;
			if (game.isFullscreen()) {
				//Hvis programmet kjører i fullskjerm, må musepilens koordinatsystem flyttes litt oppover.
				dy = -25;
			}
			
			yPos = 480;
			
			//Tegner bakgrunnen tittelen og tilbake-knappen
			g.drawImage(Bilder.getInstance().getBilde("meny_head_juksekoder"), 219, 50, null);
			g.drawImage(Bilder.getInstance().getBilde("meny_knapp_tilbake"), xPos, yPos, null);
			
			//Sjekker om musa er over noen av knappene
			if (x > xPos && x < (xPos+300)) {
				if (y > (yPos+25+dy) && y < (yPos+50+25+dy)) {
					g.drawImage(Bilder.getInstance().getBilde("meny_knapp_tilbake_over"), xPos, yPos, null);
					knapp = MenyPanel.KNAPP_TILBAKE;
				}
			}
			
			dy = 25; //Avstanden mellom linjene
			yPos = 150;
			
			Graphics2D g2d = (Graphics2D) g;
			g2d.setFont(new Font("Courier New", Font.BOLD, 18));
			g2d.setColor(new Color(255,255,255));
			
			String[] linjer = new String[13];
			
			linjer[0] = "Juksekodene under kan du skrive mens du spiller:";
			linjer[1] = "";
			linjer[2] = "detervarmther";
			linjer[3] = "nymusikk";
			linjer[4] = "chucknorris";
			linjer[5] = "midtpunktet";
			linjer[6] = "alleveier";
			linjer[7] = "dettevarlett";
			linjer[8] = "dettevarkjipt";
			linjer[9] = "levlenger";
			linjer[10] = "";
			linjer[11] = "Hvis du bruker en eller flere av de seks";
			linjer[12] = "nederste, kommer du ikke på highscorelista.";
			
			for (int i=0; i < linjer.length; i++) {
				xPos = (int) Math.round(400-(linjer[i].length()/2.0)*11); //Regner ut plasseringen til teksten
				g2d.drawString(linjer[i] , xPos, yPos+(dy*i));
			}
			
		}
		
	}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) 
	{
		x = e.getX();
		y = e.getY();
		repaint();
	}
	
	public void mousePressed(MouseEvent e) 
	{	
		//Hva som skjer, hvis noen av knappene trykkes ned:
		if (e.getX() < 250 || e.getX() > 550 || e.getY() < 130) {
			//Var et problem at når vinduet mistet fokus, og man så klikket på det
			//igjen, ble en knapp ufrivillig trykket.
			//Dette er et forsøk på minske, ikke fikse helt, problemet.
			return;
		}

		if (inMenu) {
			switch (knapp) {
				case (MenyPanel.KNAPP_NYTTSPILL):
					inMenu = false;
					game.gotoSpill();
					//new Thread(new SpillMotor(game)).start();
					break;
				case (MenyPanel.KNAPP_HIGHSCORE):
					menyDel = MenyPanel.MENY_HIGHSCORE;
					repaint();
					break;
				case (MenyPanel.KNAPP_OMSPILLET):					
					menyDel = MenyPanel.MENY_OMSPILLET;
					repaint();
					break;
				case (MenyPanel.KNAPP_FULLSKJERM):
					if (!game.isFullscreen()) {
						game.setFullscreen();
					} else {
						game.setWindowed();
					}
					break;
				case (MenyPanel.KNAPP_AVSLUTT):
					game.setWindowed();
					System.exit(0);
					break;
				case (MenyPanel.KNAPP_TILBAKE):
					menyDel = MenyPanel.MENY_HOVED;
					repaint();
					break;
				case (MenyPanel.KNAPP_OK):
					//Hvis det ikke er skrevet noe navn
					if (name.length() == 0) {
						name = "Anonym";
					}
					//Legger til navnet på highscoren
					try {
						hs = new Highscore(); //Henter ut highscoren
						hs.leggTil(name, game.getPoeng());
					} catch (Exception exception) {
						//Dette burde ikke skje
					}
					name = "";
					game.setPoeng(0);
					menyDel = MenyPanel.MENY_HOVED;
					repaint();
					break;
				case (MenyPanel.KNAPP_ALTERNATIVER):
					menyDel = MenyPanel.MENY_ALTERNATIVER;
					repaint();
					break;
				case (MenyPanel.KNAPP_LYD):
					if (game.isSoundOn()) {
						game.setSoundOn(false);
					} else {
						game.setSoundOn(true);
					}
					repaint();
					break;
				case (MenyPanel.KNAPP_MUSIKK):
					if (game.isMusicOn()) {
						game.setMusicOn(false);
					} else {
						game.setMusicOn(true);
					}
					repaint();
					break;
				case (MenyPanel.KNAPP_FX):
					if (game.isFxOn()) {
						game.setFxOn(false);
					} else {
						game.setFxOn(true);
					}
					repaint();
					break;
				case (MenyPanel.KNAPP_JUKSEKODER):
					menyDel = MenyPanel.MENY_JUKSEKODER;
					repaint();
					break;
				default:
					//Gjør ingenting
					break;
			}
		}
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void keyPressed(KeyEvent e) 
	{
		//Brukes når man skal skrive inn navnet sitt, hvis man kommer på highscorelista
		if (menyDel == MENY_SKRIVNAVN) {
			char c = e.getKeyChar();
			if (e.getKeyCode() == 8) {
				if (name.length() > 0) {
					name = name.substring(0, name.length()-1);
				}
			} else {
				if (Character.isLetter(c) || Character.isSpaceChar(c) || Character.isDigit(c)) {
					if (name.length() <= 15) {
						name += e.getKeyChar(); //Legger til den skrevne bokstaven
					}
				}
			}
		}

		repaint();
	}

	public void keyTyped(KeyEvent arg0) {}
	public void keyReleased(KeyEvent arg0) {}
}