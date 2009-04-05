package spillprosjekt;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import javax.swing.JPanel;

public class SpillMotor extends JPanel implements MouseMotionListener, KeyListener, Runnable 
{
	private static final long serialVersionUID = 1L;
	
	private final Color BGCOLORSPILL = new Color(102,102,204); //Bakgrunnsfarge spill
	private final int WIDTH=800, HEIGHT=600;
	
	private TouchBalls game;
	
	private Spiller spiller;
	private ArrayList<Aktor> aktorer;
	private Poeng poeng;
	private Liv liv;
	private long startTid;
	private long startTidSpillerVondt = -1; //Tidspunktet når spilleren mister et liv 

	private boolean running = true;
	
	private boolean soundOn; //Om lyden er på eller ikke
	private boolean musicOn; //Om musikk er på eller ikke
	private boolean fxOn; //Om visuelle effekter er på eller ikke
	
	// Visuelle effekter:
	private ArrayList<FX> fx;
	
	// Udødelighet er mulig:
	private long udodeligStart = -1;
	
	// Alle følgende variable har med cheatcodes å gjøre
	private String tekst = "";
	private long tekstTid = -1;
	private long tidCheatSkrevet = -1;
	private String lastCheat = "";
	private ArrayList<Integer> cheats = new ArrayList<Integer>(); //Liste over de cheatsene som er skrevet inn
	private boolean hasCheated = false;

	// Innlagte cheatcodes:
	private final int CHEAT_DETTEVARKJIPT=0, CHEAT_DETTEVARLETT=1, CHEAT_LEVLENGER=2,
					  CHEAT_MIDTPUNKTET=4, CHEAT_ALLEVEIER=5,
					  CHEAT_CHUCKNORRIS=7, CHEAT_STATISTIKK=8, CHEAT_DETERVARMTHER=9, CHEAT_NYMUSIKK=10;
	
	public SpillMotor(TouchBalls game) 
	{
		this.game = game;
		
		// Setter innstillinger
		soundOn = game.isSoundOn();
		musicOn = game.isMusicOn();
		fxOn = game.isFxOn();
		
		// Laster bilder
		Bilder.getInstance();
		
		// Laster lyder og musikk

		if (soundOn) {
			Lyder.getInstance();
		}

		if (musicOn) {
			Musikk.getInstance();
		}
		
		// Instansiere spiller og aktører
		spiller = new Spiller();
		aktorer = new ArrayList<Aktor>();
		
		// Type teller hvor mange som lages og "tas" av de forskjellige typene.
		// Dette på nullstilles ved start:
		Type.resetCounters();
		
		// Instansierer visuelle effekter
		if (fxOn) {
			fx = new ArrayList<FX>();
		}
		
		// Instansiere poeng og liv
		poeng = new Poeng();
		poeng.setPoeng(0);
		liv = new Liv();
		liv.setLiv(3);

		// Laste GUIet
		//gui = new SpillGUI(this);
		
		setFocusable(true);
		addKeyListener(this);
	}
	
	public TouchBalls getGame () 
	{
		return game;
	}

	private void kollisjonstesting () 
	{
		// Går gjennom alle aktører og sjekker om de 
		// kræsjer med spilleren
		for (int i = 0; i < aktorer.size(); i++) {
			Aktor a = aktorer.get(i);

			//Sjekke kollisjon
			if (a.intersects(spiller)) {
				//Legger til i statistikken:
				a.getType().setAntTatt(a.getType().getAntTatt()+1);
				
				if (a.getPoeng() > 0) {
					// Dersom poeng > 0 vil man ha truffet en
					// "god" aktør, altså man får poeng og aktøren
					// blir borte
					poeng.leggTilPoeng(a.getPoeng());
					
					//Visuell effekt
					if (fxOn) {
						fx.add(new FX(FX.FX_RING, a.x, a.y, ""));
						if (a.getPoeng() == 50) {
							fx.add(new FX(FX.FX_TEKST, a.x+10, a.y, a.getPoeng() + ""));
						} else {
							fx.add(new FX(FX.FX_TEKST, a.x, a.y, a.getPoeng() + ""));
						}
						
						if (isCheat(CHEAT_DETERVARMTHER)) {
							//Tegner fem eksploasjoner oppå hverandre:
							fx.add(new FX(FX.FX_EXPLOSION, a.x-10, a.y+10, ""));
							fx.add(new FX(FX.FX_EXPLOSION, a.x-10, a.y-10, ""));
							fx.add(new FX(FX.FX_EXPLOSION, a.x+10, a.y+10, ""));
							fx.add(new FX(FX.FX_EXPLOSION, a.x+10, a.y-10, ""));
							fx.add(new FX(FX.FX_EXPLOSION, a.x, a.y, "")); //Midt i
						}
					}
					
					//Fjerner aktoren
					aktorer.remove(a);
					
					//Lyd som spilles når man treffer en ball:
					if (soundOn) {
						if (isCheat(CHEAT_DETERVARMTHER)) {
							Lyder.getInstance().spillLyd(Lyder.LYD_MISTER_LIV,this);
						} else {
							Lyder.getInstance().spillLyd(Lyder.LYD_TREFFER_BALL, this);							
						}
					}
					
					continue;
				} else if (a.getPoeng() == -1) {
					// Dersom poeng = -1 vil man ha truffet en
					// spesiell aktør. Liv kan da endres, og aktøren 
					// blir borte. 
					if (a.getLiv() < 0 && udodeligStart == -1) {
						//Spilleren endrer utseende en viss tid hvis den mister liv:
						startTidSpillerVondt = System.currentTimeMillis();
						
						//Visuell effekt
						if (fxOn) {
							if (isCheat(CHEAT_DETERVARMTHER)) {
								//Tegner eksploasjoner oppå hverandre:
								fx.add(new FX(FX.FX_EXPLOSION, a.x-20, a.y+20, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x-20, a.y-20, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x+20, a.y+20, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x+20, a.y-20, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x-30, a.y, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x+30, a.y, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x, a.y+30, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x, a.y-30, ""));
							} else {
								//Tegner eksploasjoner oppå hverandre:
								fx.add(new FX(FX.FX_EXPLOSION, a.x-10, a.y+10, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x-10, a.y-10, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x+10, a.y+10, ""));
								fx.add(new FX(FX.FX_EXPLOSION, a.x+10, a.y-10, ""));
							}
							fx.add(new FX(FX.FX_EXPLOSION, a.x, a.y, "")); //Midt i
							fx.add(new FX(FX.FX_TEKST, a.x, a.y, "-LIV"));
						}
						
						//Lyd som spilles når man mister et liv:
						if (soundOn) {
							Lyder.getInstance().spillLyd(Lyder.LYD_MISTER_LIV,this);
						}
					} else if (a.getLiv() > 0) {						
						//Lyd som spilles når man får et liv:
						if (soundOn) {
							Lyder.getInstance().spillLyd(Lyder.LYD_FAR_LIV,this);
						}
						
						//Visuelle effekter
						if (fxOn) {
							fx.add(new FX(FX.FX_TEKST, a.x, a.y, "+LIV"));
						}
					} else {
						//Andre baller som verken tar eller gir liv:
						if (a.getType() == Type.UDODELIG) {
							//Udødelig-ball!
							udodeligStart = System.currentTimeMillis();
							
							//Lydeffekt:
							if (soundOn) {
								Lyder.getInstance().spillLyd(Lyder.LYD_UDODELIG,this);
							}
						}
					}
					
					//Hvis man er udødelig, skal bare de farlige ballene går "gjennom"
					//spilleren, og ingenting skjer...
					//Ellers skal man oppdatere livene, og fjerne aktoren:
					if (!(a.getLiv() < 0 && udodeligStart != -1)) {
						//Fjerner aktoren:
						aktorer.remove(a);
						
						//Legger til liv:
						if (isCheat(CHEAT_CHUCKNORRIS) && a.getLiv() < 0) {
							//CHEATCODE:
							liv.leggTilLiv(-a.getLiv());
						} else {
							liv.leggTilLiv(a.getLiv());
						}
					}

					// Dersom man har null liv igjen
					// stopper spillet og meny vil vises
					if (liv.getLiv() == 0) {
						stoppSpill();
						continue;
					}
				}
			}
			
			// sjekke om aktør er utenfor spillområdet og evt. drepe aktører
			if (a.x > (WIDTH+5) || a.x < -(a.getSize()+5) || 
				 a.y > (HEIGHT+5) || a.y < -(a.getSize()+5)) {
				aktorer.remove(a);
			}			
		}

		// Hvis det har gått litt tid etter spilleren mistet et liv,
		// går bildet tilbake til originalen:
		if (startTidSpillerVondt != -1 && startTidSpillerVondt < (System.currentTimeMillis()-500)) {
			startTidSpillerVondt = -1;
		}
	}
	
	private void addAktorer() 
	{
		// Ønsker ikke at antall aktører skal være
		// konstant hele tiden
		int ant_aktorer = (int) (Math.random() * 5) + 3;

		// Hyppigheten endrer seg utover i spillet:
		if (Type.POENG_500.getHyppighet() == 0 && getTid() > 60) {
			Type.POENG_500.setHyppighet(0.019); //500-baller kommer først etter en stund
		}
		if (Type.POENG_1000.getHyppighet() == 0 && getTid() > 120) {
			Type.POENG_1000.setHyppighet(0.007); //1000-baller kommer først etter en stund
		}
		if (Type.UDODELIG.getHyppighet() == 0 && getTid() > 180) {
			Type.LIV.setHyppighet(0.025); //Liv blir mer sjeldent
			Type.UDODELIG.setHyppighet(0.002); //Udødelig-baller kan komme
		}
		
		// Cheatcodes
		if (isCheat(CHEAT_DETTEVARKJIPT)) {
			ant_aktorer = 20;
		} else if (isCheat(CHEAT_DETTEVARLETT)) {
			ant_aktorer = 50;
		}
		
		while ((System.currentTimeMillis()-startTid) > 3000 && aktorer.size() < ant_aktorer) {
			double tidsfaktor = getTid()/15; //Styrer hvor for farten går oppover
			// Hastigheten består av ett konstantledd pluss et variabelt
			// tidsfaktorledd som følge av at vi ønsker mer dynamikk
			// i spillet
			
			int dx = 5 + (int)(tidsfaktor*Math.random());
			int dy = 5 + (int)(tidsfaktor*Math.random());
			int xpos;
			int ypos;

			Type type = Type.getRandomType(); 

			// Ønsker at halvparten av elementene skal komme horisontalt
			// og den andre halvparten vertikalt
			if (Math.random() > 0.5) {
				ypos = ((int)(Math.random() * (HEIGHT-2*type.getSize()))) + type.getSize(); //;)
				if (Math.random() > 0.5) {
					xpos = -type.getSize();
					dx += Math.random()*3 - 1;
				} else {
					xpos = WIDTH;
					dx = - dx - (int)(Math.random()*3 - 1);
				}
				dy = 0;
			} else {
				xpos = ((int)(Math.random() * (WIDTH-2*type.getSize()))) + type.getSize(); //;)
				if (Math.random() > 0.5) {
					ypos = -type.getSize();
					dy += Math.random()*3 - 1;
				} else {
					ypos = HEIGHT;
					dy = -dy - (int)(Math.random()*3 - 1);
				}
				dx = 0;
			}
			
			// Cheatcodes
			if (isCheat(CHEAT_DETTEVARKJIPT)) {
				type = Type.OND;
			} else if (isCheat(CHEAT_DETTEVARLETT)) {
				type = Type.POENG_1000;
			}
			if (isCheat(CHEAT_MIDTPUNKTET)) {
				xpos = 400-25;
				ypos = 300-25;
			}
			if (isCheat(CHEAT_ALLEVEIER)) {
				if (dy == 0) {
					if (Math.random() > 0.5) {
						dy = 5 + (int)(tidsfaktor*Math.random());
					}
					if (Math.random() > 0.5) {
						dy += Math.random()*3 - 1;
					} else {
						dy = -dy - (int)(Math.random()*3 - 1);
					}
				} else if (dx == 0) {
					if (Math.random() > 0.5) {
						dx = 5 + (int)(tidsfaktor*Math.random());
					}
					if (Math.random() > 0.5) {
						dx += Math.random()*3 - 1;
					} else {
						dx = -dx - (int)(Math.random()*3 - 1);
					}
				}
			}
			
			// Setter maks hastighet
			int maks = 20;
			
			if (dx > maks) {
				dx = 20;
			} else if (dx < -maks) {
				dx = -20;
			}
			if (dy > maks) {
				dy = 20;
			} else if (dy < -maks) {
				dy = -20;
			}
			
			Aktor nyAktor = new Aktor(type, type.getSize(), xpos, ypos, dx, dy);
			aktorer.add(nyAktor);
			
			// Oppdaterer statistikken
			type.setAntLaget(type.getAntLaget() + 1);
		}
	}
	
	public Spiller getSpiller() 
	{
		// returnerer spiller
		return spiller;
	}

	public ArrayList<Aktor> getAktorer() 
	{
		// returnerer aktører
		return aktorer;
	}

	public void mouseDragged(MouseEvent me) 
	{
		// gjør det samme som mousemoved
		mouseMoved(me);
	}

	public void mouseMoved(MouseEvent me) 
	{
		spiller.setPosisjon(me.getX(), me.getY());
	}
	
	public int getPoeng () 
	{
		return poeng.getPoeng();
	}

	public int getLiv() 
	{
		return liv.getLiv();
	}

	private int getTid () 
	{
		int tidsDiff = (int)(System.currentTimeMillis() - startTid);
		double antallSek = Math.round(tidsDiff/1000);
		
		return (int)antallSek;
	}
	
	public int getSek() 
	{
		return getTid() % 60;
	}
	
	public int getMin() 
	{
		return (int)Math.floor(getTid()/60);
	}
	
	public void stoppSpill() 
	{
		// Stopper spillet
		running = false;
		
		//Skriver ut game over:
		repaint();
		
		//Stopper musikken:
		if (musicOn) {
			Musikk.getInstance().stoppMusikk();
		}
		
		//Stopper spillet litt før menyen vises:
		try {
			Thread.sleep(2000); //Hvor lenge "Game over" skal vises på skjermen i millisekunder
		} catch (InterruptedException e) {
			// skjer aldri
		}
		
		if (!hasCheated) {
			//Sier så ifra til spillet hvis man har nok poeng til highscoren:
			Highscore hs = null;
			try {
				hs = new Highscore(); //Henter ut highscoren
			} catch (Exception e) {
				//Dette burde ikke skje
			}
			
			if (hs.erNokPoeng(poeng.getPoeng())) {
				game.setPoeng(poeng.getPoeng());
			} else {
				game.setPoeng(-1);
			}		
		} else {
			//Hvis man har jukset, kommer man ikke på highscore-lista:
			game.setPoeng(-1);
		}

		// Viser meny
		game.gotoMeny();
	}

	public void keyTyped(KeyEvent ke) {}
	public void keyReleased(KeyEvent ke) {}

	public void keyPressed(KeyEvent ke) 
	{		
		tekstTid = System.currentTimeMillis();
		
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			// Stopper spillet og går ut til menyen
			running = false;
			repaint();
			
			//Stopper musikken:
			if (musicOn) {
				Musikk.getInstance().stoppMusikk();
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// skjer aldri
			}
			game.gotoMeny();
			
		} else {
			//Mulighet for å skrive inn "cheatcodes"
			if (Character.isLetter(ke.getKeyChar())) {
				tekst += ke.getKeyChar();
			}
		}
		
		int cheatsize = cheats.size();
		
		//Bestemmer og juksekoden regnes som juksing
		//Hvis den ikke regners som juksing, kan man komme på highscore
		boolean isCheating = false;
		
		//Iverksetter eventuelle cheatcodes
		if (tekst.equals("dettevarkjipt")) {
			isCheating = true;
			lastCheat = "dettevarkjipt";
			cheats.add(CHEAT_DETTEVARKJIPT);
			removeCheat(CHEAT_DETTEVARLETT); //Tar bort denne
		} else if (tekst.equals("dettevarlett")) {
			isCheating = true;
			lastCheat = "dettevarlett";
			cheats.add(CHEAT_DETTEVARLETT);
			removeCheat(CHEAT_DETTEVARKJIPT); //Tar bort denne
			aktorer = new ArrayList<Aktor>(); //Fjerner alle aktører
		} else if (tekst.equals("detvanlige")) {
			isCheating = false;
			lastCheat = "detvanlige";
			if (isCheat(CHEAT_DETERVARMTHER)) {
				Musikk.getInstance().stoppMusikk();
				Musikk.getInstance().startMusikk();
			}
			//Fjerner alle cheats hvis man har jukset
			cheats = new ArrayList<Integer>();
			tidCheatSkrevet = System.currentTimeMillis();
		} else if (tekst.equals("levlenger")) {
			isCheating = true;
			lastCheat = "levlenger";
			cheats.add(CHEAT_LEVLENGER);
			liv.leggTilLiv(1000);
		} else if (tekst.equals("midtpunktet")) {
			isCheating = true;
			lastCheat = "midtpunktet";
			cheats.add(CHEAT_MIDTPUNKTET);
			aktorer = new ArrayList<Aktor>(); //Fjerner alle aktører
		} else if (tekst.equals("alleveier")) {
			isCheating = true;
			lastCheat = "alleveier";
			cheats.add(CHEAT_ALLEVEIER);
		} else if (tekst.equals("chucknorris")) {
			isCheating = true;
			lastCheat = "chucknorris";
			cheats.add(CHEAT_CHUCKNORRIS);
		} else if (tekst.equals("statistikk")) {
			isCheating = false;
			lastCheat = "statistikk";
			cheats.add(CHEAT_STATISTIKK);
		} else if (tekst.equals("detervarmther")) {
			isCheating = false;
			lastCheat = "detervarmther";
			Musikk.getInstance().stoppMusikk();
			Musikk.getInstance().startMusikk(100);
			cheats.add(CHEAT_DETERVARMTHER);
		} else if (tekst.equals("nymusikk")) {
			isCheating = false;
			lastCheat = "nymusikk";
			Musikk.getInstance().stoppMusikk();
			Musikk.getInstance().startMusikk();
			cheats.add(CHEAT_NYMUSIKK);
		}
		
		if (cheats.size() > cheatsize) {
			if (!hasCheated && !isCheating) {
				hasCheated = false;
			} else {
				hasCheated = true;				
			}
			tidCheatSkrevet = System.currentTimeMillis();
			tekst = "";
		}
	}

	public void run() 
	{	
		//Henter starttiden
		startTid = System.currentTimeMillis();
		
		//Setter tastaturfokus til vinduet
		requestFocusInWindow();
		
		//Spiller musikk:
		if (musicOn) {
			Musikk.getInstance().startMusikk();
		}
		
		//Nullstiller Type, som er STATISK objekt:
		//Hyppigheten endrer seg utover i spillet:
		Type.POENG_500.setHyppighet(0);
		Type.POENG_1000.setHyppighet(0);
		Type.UDODELIG.setHyppighet(0);
		Type.LIV.setHyppighet(0.05);
		
		// Kjører spillet
		while (running) {
			// flytt alt
			for (Aktor a : aktorer) {
				a.flytt(); //Vanlig
			}
			
			//Endrer visuelle effekter:
			if (fxOn) {
				for (FX x : fx) {
					x.flytt();
				}
			}
				
			// kollisjonstesting
			kollisjonstesting();
		
			// legge til aktører
			addAktorer();
		
			// tegn alt
			repaint();
			
			//Man har ett sekund mellom hver bokstav når man skal skrive cheatcodes
			if (tekstTid != -1 && (System.currentTimeMillis()-tekstTid) > 1000) {
				tekst = "";
				tekstTid = -1;
			}
		
			// vent litt
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// skjer aldri
			}
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//Tegner samme bakgrunn som i menyen:
		if (fxOn) {
			if (isCheat(CHEAT_DETERVARMTHER)) {
				g.drawImage(Bilder.getInstance().getBilde("hell_bakgrunn"), 0, 0, null);
			} else {
				g.drawImage(Bilder.getInstance().getBilde("meny_bakgrunn"), 0, 0, null);
			}
		}
		
		visMusepeker(false);

		// Setter bakgrunnsfargen
		setBackground (BGCOLORSPILL);
		
		//CHEATCODE:
		if (isCheat(CHEAT_STATISTIKK)) {
			tegnStatistikk(g2d);
		}
		
		//Tenger nedtelling før start:
		int runtime = (int)Math.floor((System.currentTimeMillis()-startTid)/1000);
		if (runtime < 4) {
			g2d.setFont(new Font("Courier New", Font.BOLD, 50));
			g2d.setColor(new Color(0,0,0,100));
			if (runtime == 3) {
				g2d.drawString("START!",310,190);
			} else {
				g2d.drawString("Starter om " + (3-runtime)+ "...",180,190);
			}
		}
		
		//Tegn spiller:
		//Tegner spilleren vanlig eller "sur"
		if (fxOn && startTidSpillerVondt != -1) {
			if (isCheat(CHEAT_CHUCKNORRIS)) {
				getSpiller().tegn(g,"spiller_chucknorris",0); //Tegner Chuck Norris som spiller
			} else if(isCheat(CHEAT_DETERVARMTHER)) {
				getSpiller().tegn(g,"hell_spiller_vondt",-7); //djevel
			} else {
				getSpiller().tegn(g,"spiller_vondt",0); //vondt
			}
		} else if (fxOn && udodeligStart != -1) {
			if (isCheat(CHEAT_CHUCKNORRIS)) {
				getSpiller().tegn(g,"spiller_chucknorris",0); //Tegner Chuck Norris som spiller
			} else if (isCheat(CHEAT_DETERVARMTHER)) {
				getSpiller().tegn(g,"hell_spiller_udodelig",-7); //djevel
			} else {
				getSpiller().tegn(g,"spiller_udodelig",0); //udodelig
			}
		} else {
			if (isCheat(CHEAT_CHUCKNORRIS)) {
				getSpiller().tegn(g,"spiller_chucknorris",0); //Tegner Chuck Norris som spiller
			} else if (isCheat(CHEAT_DETERVARMTHER)) {
				getSpiller().tegn(g,"hell_spiller",-7); //djevel
			} else {
				getSpiller().tegn(g,"spiller",0); //normal
			}
		}
		
		//Tegn alle aktører:
		for (Aktor aktor : getAktorer()){
			aktor.tegn(g);
		}
		
		//Tegner alle visuelle effekter:
		if (fxOn) {
			for (int i=0; i < fx.size(); i++) {
				if (fx.get(i).lever()) {
					fx.get(i).tegn(g);
				} else {
					fx.remove(i);
					i--;
				}
			}
		}
		
		//Tegner "udødelig" på skjermen, hvis man er det!
		if (udodeligStart != -1) {
			int antsek = 20; //Antall sek man er udødelig i
			if ((System.currentTimeMillis()-udodeligStart) < antsek*1000) {				
				g2d.setFont(new Font("Courier New", Font.BOLD, 50));
				if (isCheat(CHEAT_DETERVARMTHER)) {
					g2d.setColor(new Color(0,0,0,100));
					g2d.drawString("Udødelig i " + (int)Math.floor((antsek*1000+udodeligStart-System.currentTimeMillis())/1000 + 1) + " sek!", 128, 198);
					g2d.setColor(new Color(255,255,255,100));
				} else {
					g2d.setColor(new Color(255,255,255,50));
				}
				g2d.drawString("Udødelig i " + (int)Math.floor((antsek*1000+udodeligStart-System.currentTimeMillis())/1000 + 1) + " sek!", 130, 200);
			} else {
				udodeligStart = -1;
			}
		}
			
		g2d.setFont(new Font("Courier New", Font.BOLD, 17));
		g2d.setColor(new Color(255,255,255)); //Tekstfarge på poeng og tid
		g2d.drawString("Poeng: " + getPoeng(), 10, 20); //Tegn poeng
		g2d.drawString("Liv: " + getLiv(), 10, 40); //Tegn poeng
		
		// Tegn tid:
		String min = Integer.toString(getMin());
		String sek = Integer.toString(getSek());
		if (Integer.parseInt(min) <10) {
			min = "0" + min;
		}
		if (Integer.parseInt(sek) < 10) {
			sek = "0" + sek;
		}
		String tid = min + ":" + sek;
		g2d.drawString(tid, 735, 20);
		
		//CHEATCODE:
		//Når en kode skrives inn, vises hvilken kode som ble skrevet nederst på skjermen en lsiten stund:
		if (tidCheatSkrevet != -1 && (System.currentTimeMillis()-tidCheatSkrevet) < 2000) {
			int dy = 0;
			if (game.isFullscreen()) {
				dy = 15; //Må justere skriften i fullskjerm
			}
			g2d.setFont(new Font("Courier New", Font.BOLD, 17));
			g2d.setColor(new Color(255,255,255));
			g2d.drawString("Kode: " + lastCheat, 10, 560+dy); //Skriver hvilken cheat som er brukt
		} else {
			tidCheatSkrevet = -1;
			lastCheat = "";
		}
		
		//Følgende skal tegnes hvis spillet er ferdig:
		if (!running) {
			g2d.setColor(new Color(0,0,0,50)); //Setter fargen til svart, gjennomsiktig
			g2d.fillRect(0, 0, 800, 600);
			g.drawImage(Bilder.getInstance().getBilde("spill_gameover"), 209, 200, null);
		}
		
	}
	
	//CHEATCODE:
	//Skriver ut litt statistikk på skjermen... greit å bruke under kontruksjonen
	private void tegnStatistikk(Graphics2D g2d) {
		g2d.setFont(new Font("Courier New", Font.BOLD, 15));
		g2d.setColor(new Color(255,255,255,50));
		
		int dy = 0;
		if (game.isFullscreen()) {
			dy = 15; //Må justere skriften i fullskjerm
		}
		int xPos = 10;
		int yPos = 100 + dy;
		int space = 15; //Mellomrom mellom linjene
		
		ArrayList<String> linjer = new ArrayList<String>();
		linjer.add("Spilltid: " + (System.currentTimeMillis()-startTid) + "ms");
		linjer.add("");
		for (Type t : Type.values()) {
			int prosent = 0;
			if (t.getAntLaget() != 0) {
				prosent = (int)Math.round(((1.0*t.getAntTatt())/t.getAntLaget()) * 100);
			} else {
				prosent = 100;
			}
			linjer.add(t.toString() + " [" + t.getAntTatt() + "/" + t.getAntLaget() + "] (" + prosent + "%)");
		}
		
		linjer.add("");
		
		if (game.isFullscreen()) {
			linjer.add("Oppløsning: " + TouchBalls.getRunDM().getWidth() + "*" + TouchBalls.getRunDM().getHeight());			
			linjer.add("Original systemoppløsning: " + TouchBalls.getOriginalDM().getWidth() + "*" + TouchBalls.getOriginalDM().getHeight());
		} else {
			linjer.add("Oppløsning: " + TouchBalls.getOriginalDM().getWidth() + "*" + TouchBalls.getOriginalDM().getHeight());
		}
		linjer.add("Available Accelerated Memory: " + TouchBalls.getDevice().getAvailableAcceleratedMemory());
		
		for (int i=0; i < linjer.size(); i++) {
			g2d.drawString(linjer.get(i) , xPos, yPos + i*space);
		}
	}

	private void visMusepeker (boolean vis) 
	{
		if (vis) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else {
			//-------------------------------------------------------------------
			//Kode hentet fra from http://www.rgagnon.com/javadetails/java-0440.html
			//Fjerner musepekeren
			int[] pixels = new int[16 * 16];
			Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
			Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor (image, new Point(0, 0), "invisiblecursor");
			setCursor(transparentCursor);
			//-------------------------------------------------------------------
		}
	}
	
	//Sjekker om en cheat er lagt til eller ikke
	private boolean isCheat (int cheat) 
	{
		boolean returnValue = false;
		if (cheats != null) {
			for (int i : cheats) {
				if (i == cheat) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}
	
	//Fjerner en cheatcode
	private void removeCheat (int cheat) 
	{
		if (cheats != null) {
			for (int i=0; i < cheats.size(); i++) {
				if ( cheat == cheats.get(i)) {
					cheats.remove(i);
				}
			}
		}
	}

	public boolean isRunning() 
	{
		return running;
	}
}