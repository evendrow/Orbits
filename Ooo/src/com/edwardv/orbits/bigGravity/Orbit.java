package com.edwardv.orbits.bigGravity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.edwardv.orbits.Game;

public class Orbit {
	
	private double a;
	private double b;
	private int scaledA;
	private int scaledB;
	
	private double periapsis;
	private double angle;
	private double x;
	private double y;
	
	private double focusX, focusY;
	private int centerX, centerY;
	
	private BigInfoMarker perMarker;
	
	public Orbit(double focusX, double focusY, double sMajorAxis, double sMinorAxis, double periapsis, double angle) {
		
		this.focusX = focusX;
		this.focusY = focusY;
		
		this.a = sMajorAxis;
		this.b = sMinorAxis;
		
		this.scaledA = (int) (Game.scale*a);
		this.scaledB = (int) (Game.scale*b);
		
		this.periapsis = periapsis;
		this.angle = angle;
		
		double adjustedAngle = Math.atan2(b, a*2 - periapsis) - angle;
//		double centerX = (sMajorAxis-periapsis)*Math.cos(angle);
//		double centerY = (sMajorAxis-periapsis)*Math.sin(angle);
		
		double d = Math.sqrt(Math.pow(b, 2) + Math.pow(a*2 - periapsis, 2));
		x = focusX - d*Math.cos(adjustedAngle);
		y = focusY - d*Math.sin(adjustedAngle);
        
        centerX = (int) (Game.scale*(focusX - Math.cos(angle)*(a-periapsis)));
        centerY = (int) (Game.scale*(focusY + Math.sin(angle)*(a-periapsis)));
        
//        System.out.println(Math.toDegrees(Math.atan2(sMinorAxis, sMajorAxis*2 - periapsis)) + "  " + Math.toDegrees(adjustedAngle) + " " + x*Game.scale + " " + y*Game.scale + " " + (int) (cornerX*Game.scale) + " " + (int) (cornerY*Game.scale) + "  " + (int) (sMajorAxis*2d*Game.scale) + "  " + (int) (sMinorAxis*2d*Game.scale));
        
        perMarker = new BigInfoMarker(focusX + periapsis*Math.cos(angle), focusY + periapsis*Math.sin(angle), "P", null);
	}
	
	public void render(Graphics2D g) {
		
		
		centerX = (int) (Game.scale*(focusX - Math.cos(angle)*(a-periapsis)));
        centerY = (int) (Game.scale*(focusY + Math.sin(angle)*(a-periapsis)));
        scaledA = (int) (Game.scale*a);
		scaledB = (int) (Game.scale*b);
		
		AffineTransform t = g.getTransform();
		
		g.translate(centerX, centerY);
//		System.out.println(g.getTransform().getTranslateX() + centerX + "  " + (g2.getTransform().getTranslateY() + centerY));
        g.rotate(-angle);
        
        g.setColor(new Color(0.2f, 1f, 0.2f, 0.3f));
        g.drawOval(-scaledA, -scaledB, (int) (scaledA)*2, (int) (scaledB)*2);
        
//        g.setColor(Color.YELLOW);
//        g.drawLine((int) (-scaledA), 0, (int) (scaledA), 0);
//        g.drawLine(0, (int) (-scaledB), 0, (int) (scaledB));
        
        
        g.setTransform(t);
        
        perMarker.render(g);
	}
}
