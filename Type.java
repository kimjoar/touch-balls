package spillprosjekt;

import java.awt.Image;

public enum Type 
{
	// Det er de siste verdiene som viser realtiv hyppighet (ikke sannsynlighet):
	POENG_50( "ball_50",       50,  0, 0.8),
	POENG_100("ball_100",     100,  0, 0.3),
	POENG_200("ball_200",     200,  0, 0.05),
	POENG_500("ball_500",     500,  0, 0.0),
	POENG_1000("ball_1000",   1000,  0, 0.0),
	OND(      "ball_ond",      -1, -1, 0.8),
	LIV(      "ball_liv",      -1,  1, 0.05),
	UDODELIG( "ball_udodelig", -1,  0, 0.0);

	private Image bilde;
	private int poeng;
	private int liv;
	private double hyppighet;
	private int antLaget; //Husker hvor mange som er laget av en type i et spill
	private int antTatt;

	private Type (String bilde, int poeng, int liv, double hyppighet) 
	{
		this.bilde = Bilder.getInstance().getBilde(bilde);
		this.poeng = poeng;
		this.liv = liv;
		this.hyppighet = hyppighet;
	}

	public static Type getRandomType () 
	{
		//Denne metoden legger sammen alle hyppighetene til en totalt sannsynlighet,
		//og gir så hver type et intervall innenfor den totale sannsynligheten
		//som tilsvarer deres egen hyppighet. Så velger den et tilfeldig tall
		//innenfor hele intervallet.
		
		Type[] typer = Type.values();
		//return typer[ (int)(Math.random()*typer.length) ]; //Gamle løsningen
		double totsans = 0; //Total hyppighet

		for (Type type : typer) {
			totsans += type.hyppighet;
		}

		double tilftall = Math.random()*totsans; //Gir et tilfelig tall mellom 0 og totsans.
		int returnValue = -1;
		double sans = 0;

		for (int i=0; i < typer.length; i++) {
			if (sans < tilftall && tilftall < (sans+typer[i].hyppighet)) {
				returnValue = i;
				break;
			}
			sans += typer[i].hyppighet;
		}

		return typer[returnValue];	
	}

	public Image getBilde() 
	{
		return bilde;
	}
	
	public int getPoeng() 
	{
		return poeng;
	}
	
	public int getSize() 
	{
		return 50;
	}

	public int getLiv() 
	{
		return liv;
	}
	
	public double getHyppighet() 
	{
		return hyppighet;
	}

	public void setHyppighet(double hyppighet) 
	{
		this.hyppighet = hyppighet;
	}
	
	public static void resetCounters () 
	{
		for (Type t : values()) {
			t.antLaget = 0;
			t.antTatt = 0;
		}
	}
	
	public int getAntTatt() 
	{
		return antTatt;
	}
	
	public void setAntTatt(int antTatt) {
		this.antTatt = antTatt;
	}
	
	public int getAntLaget() {
		return antLaget;
	}
	
	public void setAntLaget(int antLaget) {
		this.antLaget = antLaget;
	}
}