package spillprosjekt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class FX implements Sprites 
{
	public static final int FX_RING=0, FX_EXPLOSION=1, FX_TEKST=2;
	
	private double x, y;
	private long startTid;
	private int type;
	private int levetid;
	private int var;
	private String tekst;
	
	public FX(int type, double x, double y, String tekst) 
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.tekst = tekst;
		startTid = System.currentTimeMillis();

		switch (type) {
			case (FX_RING):
				levetid = 400;
				var = 10;
				break;
			case (FX_EXPLOSION):
				levetid = 1000;
				var = 1;
				break;
			case (FX_TEKST):
				levetid = 700;
				var = 1;
				break;
		}
	}
	
	public void tegn (Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;

		switch (type) {
			case (FX_RING):
				int alpha = (int)Math.round(100-var/2);
				if (alpha < 0) {
					alpha = 0;
				}
				g2d.setColor(new Color(255,255,255,alpha));
				g2d.drawOval((int)Math.round((x+25) - (var/2)) , (int)Math.round((y+25) - (var/2)) , var, var);
				break;
			case (FX_EXPLOSION):
				String s = "";
				if (var < 10) {
					s = "0" + var;
				} else {
					s = "" + var;
				}
				g.drawImage(Bilder.getInstance().getBilde("fx_explosion_" + s), (int)Math.round(x-10), (int)Math.round(y-10), null);	
				break;
			case (FX_TEKST) :
				g2d.setFont(new Font("Courier New", Font.BOLD, 25));
				alpha = (int)Math.round(100-var); //Alphaverdien på teksten
				if (alpha < 0) {
					alpha = 0;
				}
				g2d.setColor(new Color(255,255,255, alpha)); //Tekstfarge
				g2d.drawString(tekst, (int)Math.round((x)), (int)Math.round(y - var + 25)); //Tegn tekst
				break;
		}
	}
	
	public void flytt() 
	{
		//Endrer på FX-en
		switch (type) {
			case (FX_RING):
				var += 15;
				break;
			case (FX_EXPLOSION):
				var += 1;
				break;
			case (FX_TEKST):
				var += 4;
				break;
		}
	}
	
	public boolean lever() 
	{
		return ((System.currentTimeMillis()-startTid) < levetid);
	}
	
}
