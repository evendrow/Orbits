package com.edwardv.orbits;

import java.awt.Color;
import java.awt.Graphics2D;

import com.edwardv.orbits.bigGravity.BigBody;

public class CopyOfPlanet extends BigBody {

	protected Sun sun;
	
	protected double a;
	protected double soi;
	
	protected Color color;
	
	public CopyOfPlanet(double x, double y, double mass, double radius, Sun sun, Color color) {
		super(x, y, mass, radius, "planet");
		this.color = color;
		
		this.a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		soi = a*Math.pow(mass/sun.getMass(), 2d/5d);
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(new Color(1, 1, 1, 0.1f));
		g.fillOval((int) ((x*Game.scale) - (soi*Game.scale)), (int) ((y*Game.scale) - (soi*Game.scale)), (int) (soi*Game.scale)*2, (int) (soi*Game.scale)*2);
		
		
		g.setColor(color);
		g.fillOval((int) ((x-radius)*Game.scale), (int) ((y-radius)*Game.scale), (int) (radius*Game.scale*2d), (int) (radius*Game.scale*2d));
		
		g.setColor(Color.BLACK);
		g.drawLine((int) ((x*Game.scale) - (radius*Game.scale)), (int) (y*Game.scale), (int) ((x*Game.scale) + (radius*Game.scale)), (int) (y*Game.scale));
		g.drawLine((int) (x*Game.scale), (int) ((y*Game.scale) - (radius*Game.scale)), (int) (x*Game.scale), (int) ((y*Game.scale) + (radius*Game.scale)));
	}
	
	public double getSoi() {
		return soi;
	}
	
}
