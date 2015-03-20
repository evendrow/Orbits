package com.edwardv.orbits;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.edwardv.orbits.bigGravity.OrbitingBody;

public class Game {

	public static final int KM = 1000;
	public static final int XOFF = PlanetRunner.size.width/2;
	public static final int YOFF = PlanetRunner.size.height/2;
	
	public static double scale = 2d * Math.pow(10, -3);
	public static Vector scaleOffset = new Vector(150 * Math.pow(10, 6) * KM, 0);
	
	public static Sun sun;
	public static OrbitingBody planet;
	public static OrbitingBody moon;
	
	private Satellite satellite;
	
	private TimeAccel timeAccel;
	
	public Game() {
		sun = new Sun(0, 0);
		planet = new OrbitingBody(new Vector(150 * Math.pow(10, 6) * KM, 0*KM), new Vector(0, -30*KM), 5.972d * Math.pow(10, 24), 6371 * KM, sun, new Color(50, 100, 255), "planet");
		moon = new OrbitingBody(new Vector(384400d * KM, 0), new Vector(0, -1000), 7.34767d * Math.pow(10, 22), 1747.4d * KM, planet, new Color(220, 220, 220), "moon");
		
		
		
		satellite = new Satellite(new Vector(7000*KM, 0*KM), new Vector(0, -8750), planet);
//		satellites.add(new Satellite(400, 400, new Vector(2, -12), planet));
		timeAccel = new TimeAccel();
	}
	
	public void tick(int delta) {
		if (KeyHandler.isKeyPressed(KeyEvent.VK_W))
			scale *= Math.pow(1.01d, (double) (delta));
		else if (KeyHandler.isKeyPressed(KeyEvent.VK_S))
			scale /= Math.pow(1.01d, (double) (delta));
		
		
		timeAccel.tick();
		int timeAccelNum = timeAccel.getTimeAccel();
		
		planet.tick(delta*timeAccelNum);
		moon.tick(delta*timeAccelNum);
		satellite.tick(delta*timeAccelNum);
		//Max scale 0.001 (???)
	}
	
	public void render(Graphics g) {
		
		Graphics2D bigPhysics = (Graphics2D) g.create();
//		bigPhysics.translate((int) (scaleOffset.x*scale), (int) (scaleOffset.y*scale + PlanetRunner.size.height/2));
		bigPhysics.translate((int) (-satellite.x*scale) + XOFF, (int) (-satellite.y*scale) + YOFF);
		
		planet.render(bigPhysics);
		moon.render(bigPhysics);
		sun.render(bigPhysics);
		satellite.renderBig(bigPhysics);
		
		timeAccel.render(g);
	}
	
}
