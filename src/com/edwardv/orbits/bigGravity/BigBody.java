package com.edwardv.orbits.bigGravity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.edwardv.orbits.Game;
import com.edwardv.orbits.Vector;

public class BigBody {
	
	public static final double GRAVCONST = 6.673d * Math.pow(10, -11);

	protected double x, y;
	protected double radius;
	protected double mass;
	
	protected String id;
	
	protected ArrayList<OrbitingBody> bigSatellites;
	
	public BigBody(double x, double y, double mass, double radius, String id) {
		
		this.x = x;
		this.y = y;
		
		this.mass = mass;
		this.radius = radius; //(int) (Math.pow(mass*(3d/4d)/Math.PI, 1d/3d));
		
		this.id = id;
		
		bigSatellites = new ArrayList<OrbitingBody>();
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillOval((int) ((x*Game.scale) - (radius*Game.scale)), (int) ((y*Game.scale) - (radius*Game.scale)), (int) (radius*Game.scale)*2, (int) (radius*Game.scale)*2);
		g.setColor(Color.BLACK);
		g.drawLine((int) ((x*Game.scale) - (radius*Game.scale)), (int) (y*Game.scale), (int) ((x*Game.scale) + (radius*Game.scale)), (int) (y*Game.scale));
		g.drawLine((int) (x*Game.scale), (int) ((y*Game.scale) - (radius*Game.scale)), (int) (x*Game.scale), (int) ((y*Game.scale) + (radius*Game.scale)));
	}
	
	public Vector getGravitVectoryWithDistance(double satX, double satY, int delta) {
		double distanceX = x - satX;
		double distanceY = y - satY;
		double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
		
		
		double gravity = GRAVCONST * mass / Math.pow(distance, 2) * ((double) (delta)/1000d);
		double vectorX = gravity * distanceX/distance;
		double vectorY = gravity * distanceY/distance;
		
		return new Vector(vectorX, vectorY);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getMass() {
		return mass;
	}
	
	public String getId() {
		return id;
	}
	
	public double getGM() {
		return mass * GRAVCONST;
	}
	
	public Vector getVelVec() {
		return new Vector(0, 0);
	}
	
	public Vector getNonrelativeVelVec() {
		return new Vector(0, 0);
	}
	
	public void addBigSatellite(OrbitingBody bigBody) {
		bigSatellites.add(bigBody);
	}
	
	public void removeBigSatellite(String satId) {
		for (int i=0;i<bigSatellites.size();i++) {
			if (bigSatellites.get(i).getId().equals(satId)) {
				bigSatellites.remove(i);
				break;
			}
		}
	}
	
	public ArrayList<OrbitingBody> getBigSatellites() {
		return bigSatellites;
	}
	
}
