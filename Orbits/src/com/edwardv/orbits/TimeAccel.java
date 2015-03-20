package com.edwardv.orbits;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;

public class TimeAccel {

	public static final int[] TIMEACCELLIST = new int[] {1, 2, 5, 15, 50, 158, 500, 1580, 5000, 15800, 50000, 158000, 500000, 10000000};
	public static final Polygon TRIANGLE = new Polygon(new int[] {0, 16, 0}, new int[] {0, 7, 14}, 3);
	
	private int timeAccel;
	private int index;
	private int edgeOff;
	
	private Polygon[] polygons;
	
	public TimeAccel() {
		index = 0;
		timeAccel = TIMEACCELLIST[index];
		
		edgeOff = PlanetRunner.size.width - (TRIANGLE.xpoints[1]+2)*TIMEACCELLIST.length;
		polygons = new Polygon[TIMEACCELLIST.length];
		for (int i=0;i<polygons.length;i++) {
			Polygon newPoly = new Polygon(TRIANGLE.xpoints, TRIANGLE.ypoints, TRIANGLE.npoints);
			newPoly.translate(edgeOff + i*(TRIANGLE.xpoints[1]+2), 2); 
			polygons[i] = newPoly;
		}
	}
	
	public void tick() {
		if (KeyHandler.isKeyPressed(KeyEvent.VK_PERIOD)) {
			KeyHandler.removeKey(KeyEvent.VK_PERIOD);
			index++;
		} else if (KeyHandler.isKeyPressed(KeyEvent.VK_COMMA)) {
			KeyHandler.removeKey(KeyEvent.VK_COMMA);
			index--;
		}
		
		if (index < 0)
			index = 0;
		else if (index > TIMEACCELLIST.length-1)
			index = TIMEACCELLIST.length-1;
		
		timeAccel = TIMEACCELLIST[index];
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(edgeOff, 0, PlanetRunner.WIDTH-edgeOff, TRIANGLE.ypoints[2] + 4);
		g.setColor(Color.GREEN);
		for (int i=0;i<polygons.length;i++) {
			g.fillPolygon(polygons[i]);
			if (i == index)
				g.setColor(Color.GRAY);
		}
	}
	
	public int getTimeAccel() {
		return timeAccel;
	}
	
}
