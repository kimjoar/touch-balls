package spillprosjekt;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.*;

public class Aktor extends Ellipse2D.Double implements Sprites 
{
	private Type type;
	private double dx,dy;

	public Aktor(Type type, int size, double x, double y, double dx, double dy) {
		this.setFrameFromDiagonal(0, 0, size, size);
		this.type = type;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.height = size;
		this.width = size;
	}

	public void tegn(Graphics g) {
		Image bilde = type.getBilde();
		g.drawImage( bilde, (int)Math.round(x), (int)Math.round(y), null);
	}

	public void flytt() {
		x += dx;
		y += dy;
	}

	public int getSize() {
		return (int)this.height;
	}

	public int getPoeng () {
		return type.getPoeng();
	}
	
	public Type getType() {
		return type;
	}

	public int getLiv() {
		return type.getLiv();
	}
}