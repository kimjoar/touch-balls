package spillprosjekt;

public class Poeng 
{
	private int poeng;

	public int getPoeng() 
	{
		return poeng;
	}

	public void setPoeng(int poeng) 
	{
		this.poeng = poeng;
	}

	public void leggTilPoeng(int n) 
	{
		setPoeng(poeng + n);
	}
}