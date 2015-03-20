package com.edwardv.orbits;

import java.awt.Color;
import java.awt.Graphics2D;

public class OrbitOld {
	
	double sMajorAxis;
	double sMinorAxis;
	
	double periapsis;
	double angle;
	
	double x;
	double y;
	
	double focusX;
	double focusY;
	
	double cornerX;
	double cornerY;
	
	public OrbitOld(double focusX, double focusY, double sMajorAxis, double sMinorAxis, double periapsis, double angle) {
		
		this.focusX = focusX;
		this.focusY = focusY;
		
		this.sMajorAxis = sMajorAxis;
		this.sMinorAxis = sMinorAxis;
		
		this.periapsis = periapsis;
		this.angle = angle;
		
		double adjustedAngle = Math.atan2(sMinorAxis, sMajorAxis*2 - periapsis) - angle;
//		double centerX = (sMajorAxis-periapsis)*Math.cos(angle);
//		double centerY = (sMajorAxis-periapsis)*Math.sin(angle);
		
		double d = Math.sqrt(Math.pow(sMinorAxis, 2) + Math.pow(sMajorAxis*2 - periapsis, 2));
		x = focusX - d*Math.cos(adjustedAngle);
		y = focusY - d*Math.sin(adjustedAngle);
		
//		double x = focusX - (sMajorAxis*2-periapsis);//*Math.cos(angle);
//		double y = focusY - sMinorAxis;//*Math.sin(angle);
		
		this.cornerX = x*Math.cos(angle) + -y*Math.sin(angle);
        this.cornerY = x*Math.sin(angle) + y*Math.cos(angle);
        
//        System.out.println(Math.toDegrees(Math.atan2(sMinorAxis, sMajorAxis*2 - periapsis)) + "  " + Math.toDegrees(adjustedAngle) + " " + x*Planet.SCALE + " " + y*Planet.SCALE + " " + (int) (cornerX*Planet.SCALE) + " " + (int) (cornerY*Planet.SCALE) + "  " + (int) (sMajorAxis*2d*Planet.SCALE) + "  " + (int) (sMinorAxis*2d*Planet.SCALE));
	}
	
	public void render(Graphics2D g) {
//		g.setColor(Color.BLACK);
//        g.drawLine((int) (x*Planet.SCALE), (int) ((y + sMinorAxis)*Planet.SCALE), (int) ((x + sMajorAxis*2)*Planet.SCALE), (int) ((y + sMinorAxis)*Planet.SCALE));
//		g.drawLine((int) ((cornerX+sMajorAxis)*Planet.SCALE), (int) (cornerY*Planet.SCALE), (int) ((cornerX+sMajorAxis)*Planet.SCALE), (int) ((cornerY + sMinorAxis*2)*Planet.SCALE));
//	
		g.setColor(Color.GREEN);
		g.drawOval((int) (x*Game.scale)-4, (int) (y*Game.scale)-4, 8, 8);
        g.rotate(-angle);
        g.drawOval((int) (cornerX*Game.scale)-2, (int) (cornerY*Game.scale)-2, 4, 4);
        
        g.setColor(Color.YELLOW);
        g.drawOval((int) (cornerX*Game.scale), (int) (cornerY*Game.scale), (int) (sMajorAxis*2*Game.scale), (int) (sMinorAxis*2*Game.scale));
        
        g.setColor(Color.BLACK);
        g.drawLine((int) (cornerX*Game.scale), (int) ((cornerY + sMinorAxis)*Game.scale), (int) ((cornerX + sMajorAxis*2)*Game.scale), (int) ((cornerY + sMinorAxis)*Game.scale));
		g.drawLine((int) ((cornerX+sMajorAxis)*Game.scale), (int) (cornerY*Game.scale), (int) ((cornerX+sMajorAxis)*Game.scale), (int) ((cornerY + sMinorAxis*2)*Game.scale));
	}
}
