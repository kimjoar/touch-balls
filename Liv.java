package spillprosjekt;

public class Liv 
{
	private int liv;

	public int getLiv() 
	{
		return liv;
	}

	public void setLiv(int liv) 
	{
		this.liv = liv;
	}

	public void leggTilLiv(int n) 
	{
		setLiv(liv + n);
	}

	public void leggTilLiv() 
	{
		setLiv(liv + 1);
	}
}
