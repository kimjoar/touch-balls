package spillprosjekt;

import java.awt.*;
import java.awt.geom.*;

public class Spiller extends Rectangle2D.Double	implements Sprites 
{
	Rectangle2D rektangel;
	int size = 70; //Størrelsen på kvadratet
	
	public Spiller(){
		this.height = size;
		this.width = size;
		setPosisjon(400,300);
	}
	
	//Tegner et rektangel
	public void tegn(Graphics g) 
	{
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor( new Color(0,0,0) );
//		g2d.fillRect( (int)Math.round(x), (int)Math.round(y), size, size);
		g.drawImage( Bilder.getInstance().getBilde("spiller"), (int)Math.round(x), (int)Math.round(y), null);		
	}
	
	public void tegn(Graphics g, String bilde, int dy) 
	{
		g.drawImage( Bilder.getInstance().getBilde(bilde), (int)Math.round(x), (int)Math.round(y+dy), null);
	}
	
	//Flytter rektangelet ettersom musa flytter seg.
	public void flytt() 
	{
		//Gjør ingen ting
	}
	
	public void setPosisjon (int x, int y) 
	{
		this.x = x - (size/2 + 5);
		this.y = y - (size/2 + 15);
	}	
}