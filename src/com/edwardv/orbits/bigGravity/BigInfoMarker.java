package com.edwardv.orbits.bigGravity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.edwardv.orbits.Game;

public class BigInfoMarker {

	public static final int BOXSIZE = 20;
	
	private double x;
	private double y;
	
	private String marker;
	private String[] info;
	
	public BigInfoMarker(double x, double y, String marker, String[] info) {
		this.x = x;
		this.y = y;
		this.marker = marker;
		this.info = info;
	}
	
	public void render(Graphics2D g) {
		AffineTransform t = g.getTransform();
		
		
		g.translate(x*Game.scale, y*Game.scale);
		
		g.setColor(new Color(0.2f, 1f, 0.2f, 0.3f));
		g.fillOval(-3, -3, 6, 6);
		
		g.setColor(new Color(1f, 1f, 1f, 0.1f));
		g.fillOval(-BOXSIZE/2, -BOXSIZE - 4, BOXSIZE, BOXSIZE);
		g.setFont(new Font("Sans Serif", Font.PLAIN, BOXSIZE*3/4));
		g.setColor(Color.WHITE);
		g.drawString(marker, -BOXSIZE*1/6, -BOXSIZE*1/6 - 4);
		
		
		g.setTransform(t);
	}
}
