package com.edwardv.orbits;

import java.awt.Color;
import java.awt.Graphics2D;

import com.edwardv.orbits.bigGravity.BigBody;

public class Sun extends BigBody {
	
	public static final double SUNMASS = 1.989 * Math.pow(10, 30);
	public static final double SUNRADIUS = 695800 * Game.KM;

	public Sun(double x, double y) {
		super(x, y, SUNMASS, SUNRADIUS, "sun");
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillOval((int) ((x*Game.scale) - (radius*Game.scale)), (int) ((y*Game.scale) - (radius*Game.scale)), (int) (radius*Game.scale)*2, (int) (radius*Game.scale)*2);
	}

}
