package com.edwardv.orbits;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;

import com.edwardv.orbits.bigGravity.BigBody;
import com.edwardv.orbits.bigGravity.BigGravity;
import com.edwardv.orbits.bigGravity.OrbitingBody;

public class Satellite {
	
	public static final double PTURNSPEED = 0.1d;
	public static final double POWER = 50d;
	
	protected double x, y;
	
	protected BigBody body;
	protected BigGravity bigGravity;
	
	protected Polygon player;
	protected double playerAngle;
	
	public Satellite(Vector radiusVector, Vector velocityVec, BigBody body) {
		this.x = radiusVector.x + body.getX();
		this.y = radiusVector.y + body.getY();
		this.body = body;
		
		player = new Polygon(new int[] {5, -5, -5}, new int[] {0, 4, -4}, 3);
		playerAngle = 0;
		
		this.bigGravity = new BigGravity(radiusVector, velocityVec, body);
		
	}
	
	public double distanceTo(OrbitingBody p) {
		return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
	}
	
	public void setBody(BigBody body) {
		System.out.println("new Body: " + body.getId());
		this.body = body;
		bigGravity.setBody(body);
	}
	
	public void tick(int delta) {
		bigGravity.tick(delta);
		
		x = bigGravity.getX();
		y = bigGravity.getY();
		
		boolean foundBody = false;
		for (OrbitingBody b : body.getBigSatellites()) {
//			System.out.println("Remaining distance to: " + b.getId() + ": " + (bigGravity.getDistanceTo(b) - b.getSoi()));
			if (bigGravity.getDistanceTo(b) < b.getSoi()) {
				setBody(b);
				foundBody = true;
				break;
			}
		}
		
		if (body instanceof OrbitingBody && !foundBody) {
			if (bigGravity.getRadius() > ((OrbitingBody) body).getSoi()) {
				setBody(((OrbitingBody) body).getBody());
			}
			
//			if (bigGravity.getRadius() > ((OrbitingBody) body).getSoi()) {
//				setBody(Game.sun);
//			}
//			if (bigGravity.getRadius() > ((OrbitingBody) body).getSoi()) {
//				setBody(Game.sun);
//				bigGravity.recalculateOrbit();
//			} else {
//			
//			if (!(body.getId().equals(Game.moon.getId()) && bigGravity.getDistanceTo(Game.moon) < Game.moon.getSoi())) {
//				if (!body.getId().equals(Game.planet.getId()) && bigGravity.getDistanceTo(Game.planet) < Game.planet.getSoi())  {
//					setBody(Game.planet);System.out.println("new Body: planet");
//				} 
//			}
//				
//				if (!body.getId().equals(Game.planet.getId()) && bigGravity.getDistanceTo(Game.planet) < Game.planet.getSoi())  {}
//				else if (!body.getId().equals(Game.moon.getId()) && bigGravity.getDistanceTo(Game.moon) < Game.moon.getSoi()) {
//					setBody(Game.moon);System.out.println("new Body: moon");
//				}
//				
//				else if (!body.getId().equals(Game.sun.getId()) && bigGravity.getDistanceTo(Game.planet) > Game.planet.getSoi() && bigGravity.getDistanceTo(Game.moon) > Game.moon.getSoi()) {
//					setBody(Game.sun);System.out.println("new Body: Sun");
//				}
////			}
		}
		
		
		if (KeyHandler.isKeyPressed(KeyEvent.VK_A))
			playerAngle += PTURNSPEED*((double) (delta) / 1000d);
		else if (KeyHandler.isKeyPressed(KeyEvent.VK_D))
			playerAngle -= PTURNSPEED*((double) (delta) / 1000d);
		else if (KeyHandler.isKeyPressed(KeyEvent.VK_SPACE)) {
			bigGravity.increaseVelocity(Math.cos(playerAngle)*POWER*((double) (delta) / 1000d), 
									   -Math.sin(playerAngle)*POWER*((double) (delta) / 1000d));
		}

	}
	
	public void renderBig(Graphics2D g) {
		
		bigGravity.renderOrbit(g);
		
		g.setColor(Color.GREEN);
//		g.fillOval((int) (x*Game.scale)-2, (int) (y*Game.scale)-2, 4, 4);
//		g.fillPolygon(player);
		
		
//		g.drawString("Distance: " + distanceTo(planet), 30, 30);
//		g.drawString("Apoapsis: " + apoapsis, 30, 60);
//		g.drawString("Periapsis: " + periapsis, 30, 90);
//		g.drawString("Velocity: " + velocity, 30, 120);
//		g.drawString("Vel: " + vel, 30, 150);
//		g.drawString("Vel Angle: " + velAngle, 30, 180);
//		g.drawString("Time: " + t + " of " + period, 30, 210);
//		g.drawString("Mean Anomaly: " + M, 30, 240);
//		g.drawString("True Anomaly: " + v, 30, 270);
//		g.drawString("Real Angle: " + angleOffset, 30, 300);
//		g.drawString("Eccentric Anomaly: " + E, 30, 330);
//		
//		g.setColor(Color.YELLOW);
//		g.drawLine((int) (body.getX()*Game.scale), (int) (body.getY()*Game.scale), (int) ((body.getX() + rVec.x)*Game.scale), (int) ((body.getY() + rVec.y)*Game.scale));
		
		
//		double angle = Math.toRadians(20);
		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.translate((int) (x*Game.scale), (int) (y*Game.scale));
//		g.setColor(Color.PINK);
//		g2d.drawLine(0, 0, (int) (Math.cos(playerAngle)*30d), (int) (-Math.sin(playerAngle)*30d));
		g2d.setColor(Color.RED);
//		g2d.drawLine(0, 0, (int) (velVec.x*400d*Game.scale), (int) (velVec.y*300d*Game.scale));
		
		g2d.rotate(-playerAngle);
		g2d.fillPolygon(player);
//        g2d.rotate(angle);
//        
//        double newx = 0;
//        double newy = 0;
//        newx = 100*Math.cos(-angle) + -20*Math.sin(-angle);
//        newy = 100*Math.sin(-angle) + 20*Math.cos(-angle);
//        g2d.drawOval((int) newx-3, (int) newy-3, 6, 6);
//        g2d.drawLine(0, 0, 200, 0);
        
        
        
	}
}
