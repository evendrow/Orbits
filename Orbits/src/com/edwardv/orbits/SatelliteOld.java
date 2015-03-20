package com.edwardv.orbits;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import com.edwardv.orbits.bigGravity.OrbitingBody;

public class SatelliteOld {
	
	protected double x, y;
	
	protected Vector velocityVec;
	protected double velocity;
	
	protected double energy;
	
	protected double apoapsis;
	protected double periapsis;
	protected double sMajorAxis;
	protected double sMinorAxis;
	
	protected double eccentricity;
	protected Vector eccentricityVector;
	
	protected double meanMotion;
	protected double meanAnomaly;
	protected double eccentricAnomaly;
	protected double trueAnomaly;
	protected double trueAnomaly2;
	
	protected double orbitalPeriod;
	protected double time;
	
	protected double angularMomentum;
	
	protected double radius;
	protected Vector radiusVector;
	protected Vector radiusDelta;
	
	protected double angleOffset;
	
	protected OrbitingBody planet;
	protected OrbitOld orbit;
	
	protected Polygon player;
	protected double playerAngle;
	
	public SatelliteOld(Vector radiusVector, Vector velocityVec, double timeInOrbit, OrbitingBody planet) {
		this.x = radiusVector.x + planet.getX();
		this.y = radiusVector.y + planet.getY();
		this.radiusVector = radiusVector;
		
		this.velocityVec = velocityVec;
		this.radiusDelta = new Vector(velocityVec.x, velocityVec.y);
		if (radiusVector.x < planet.getX())
			radiusDelta.x = -radiusDelta.x;
		if (radiusVector.y < planet.getY())
			radiusDelta.y = -radiusDelta.y;
		
		this.planet = planet;
		
		this.radius = radiusVector.magnitude();
		
		this.velocity = velocityVec.magnitude();
		this.energy = (Math.pow(velocity, 2)/2d) - (planet.getGM()/radius);
		this.sMajorAxis = -planet.getGM()/(2*energy);
		
		this.orbitalPeriod = 2d * Math.PI * Math.sqrt(Math.pow(sMajorAxis, 3)/planet.getGM());
		this.time = orbitalPeriod * timeInOrbit; //start at apoapsis
		if (time > orbitalPeriod) {
			time = orbitalPeriod - time;
		}
//		this.angularVelocity = 2d * Math.PI / orbitalPeriod;
//		System.out.println(angularVelocity);
		this.angularMomentum = Vector.cross(radiusVector, velocityVec);
		
//		this.eccentricity = Math.sqrt(1 + ((2 * energy * Math.pow(angularMomentum, 2))/Math.pow(planet.getGM(), 2)));
		this.eccentricityVector = radiusVector.multiply(Math.pow(velocity, 2) - (planet.getGM()/radius)).subtract(velocityVec.multiply(Vector.dot(radiusVector, velocityVec))).divide(planet.getGM());
//		this.eccentricityVector = Vector.cross(velocityVec, angularMomentum
		this.eccentricity = eccentricityVector.magnitude();
		
		this.sMinorAxis = sMajorAxis * Math.sqrt(1-Math.pow(eccentricity, 2));
		
		this.apoapsis = (1 + eccentricity) * sMajorAxis;
		this.periapsis = (1 - eccentricity) * sMajorAxis;
		
		this.meanMotion = Math.sqrt(planet.getGM()/Math.pow(sMajorAxis, 3));
		this.meanAnomaly = time * meanMotion;
		
		this.eccentricAnomaly = meanAnomaly;
		double oldAnomaly = meanAnomaly;
		for (int i=0;i<100;i++) { 
			eccentricAnomaly = eccentricAnomaly - (  (eccentricAnomaly - (eccentricity * Math.sin(eccentricAnomaly)) - meanAnomaly)  /  (1d - (eccentricity * Math.cos(eccentricAnomaly))));
			if (eccentricAnomaly == oldAnomaly) {
				break;
			}
			
			oldAnomaly = eccentricAnomaly;
		}
		
		trueAnomaly2 = Math.acos(((sMajorAxis * (1 - Math.pow(eccentricity, 2))) - radius)/(eccentricity*radius));//Math.acos(Vector.dot(radiusVector, eccentricityVector)/(radiusVector.magnitude()*eccentricityVector.magnitude()));
		trueAnomaly = Math.acos((Math.cos(eccentricAnomaly) - eccentricity)/(1-eccentricity*Math.cos(eccentricAnomaly)));
		if (time > orbitalPeriod/2) {
			trueAnomaly += Math.PI;
		}
		
		angleOffset = Math.atan2(-radiusVector.y, radiusVector.x) - trueAnomaly;;
		
		orbit = new OrbitOld(planet.getX(), planet.getY(), sMajorAxis, sMinorAxis, periapsis, 0);
		
		player = new Polygon(new int[] {5, -5, -5}, new int[] {0, 4, -4}, 3);
		playerAngle = 0;
		
//		trueAnomaly = Math.acos(((sMajorAxis*(1-Math.pow(eccentricity, 2))/radius)-1)/eccentricity);
		
//		System.out.println(((sMajorAxis*(1-Math.pow(eccentricity, 2))/radius)-1)/eccentricity);
//		System.out.println(Math.toDegrees(trueAnomaly));
//		System.out.println(Math.toDegrees(Math.atan2(-radiusVector.y, radiusVector.x)));		
	}
	
	public double distanceTo(OrbitingBody p) {
		return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
	}
	
	public void gravitateTo(OrbitingBody planet, int delta) {
		Vector changeVector = planet.getGravitVectoryWithDistance(x, y, delta);
		velocityVec.x += changeVector.x;
		velocityVec.y += changeVector.y;
	}
	
	public void tick(int delta) {
		
		x += velocityVec.x * ((double) (delta) / 1000d);
		y += velocityVec.y * ((double) (delta) / 1000d);
		radiusVector.x += velocityVec.x * ((double) (delta) / 1000d);//= x - planet.getX();
		radiusVector.y += velocityVec.y * ((double) (delta) / 1000d);//= y - planet.getY();
		radius = radiusVector.magnitude();
		velocity = velocityVec.magnitude();
		
		radiusDelta.x = velocityVec.x;
		radiusDelta.y = velocityVec.y;
		if (radiusVector.x < planet.getX())
			radiusDelta.x = -radiusDelta.x;
		if (radiusVector.y < planet.getY())
			radiusDelta.y = -radiusDelta.y;
		
		time += (double) (delta)/1000d;
		while (time > orbitalPeriod) {
			time -= orbitalPeriod;
		}
		
		//FIND true anomaly
		this.meanAnomaly = time * meanMotion;
		
		//FIND eccentric anomaly
		this.eccentricAnomaly = meanAnomaly;
		double oldAnomaly = meanAnomaly;
		for (int i=0;i<100;i++) {
			eccentricAnomaly = eccentricAnomaly - (  (eccentricAnomaly - (eccentricity * Math.sin(eccentricAnomaly)) - meanAnomaly)  /  (1d - (eccentricity * Math.cos(eccentricAnomaly))));
			if (eccentricAnomaly == oldAnomaly) {
				break;
			}
			
			oldAnomaly = eccentricAnomaly;
		}
		
		eccentricityVector = radiusVector.multiply(Math.pow(velocity, 2) - (planet.getGM()/radius)).subtract(velocityVec.multiply(Vector.dot(radiusVector, velocityVec))).divide(planet.getGM());
//		trueAnomaly = Math.acos(Vector.dot(radiusVector, eccentricityVector)/(radius*eccentricity));
		
		//FIND true anomaly
		trueAnomaly2 = Math.acos(((sMajorAxis * (1 - Math.pow(eccentricity, 2))) - radius)/(eccentricity*radius));//Math.acos(Vector.dot(radiusVector, eccentricityVector)/(radiusVector.magnitude()*eccentricityVector.magnitude()));
		trueAnomaly = Math.acos((Math.cos(eccentricAnomaly) - eccentricity)/(1-eccentricity*Math.cos(eccentricAnomaly)));
		if (time > orbitalPeriod/2) {//Vector.dot(radiusVector, velocityVec) < 0)
			trueAnomaly2 = 2 * Math.PI - trueAnomaly2;
			trueAnomaly = 2*Math.PI - trueAnomaly;
		}
//		trueAnomaly = Math.acos(Vector.dot(radiusVector, eccentricityVector)/(radiusVector.magnitude()*eccentricityVector.magnitude()));
		
		angleOffset = (Math.atan2(-radiusVector.y, radiusVector.x) + (Math.PI*2)) % (Math.PI*2);
	}
	
	public void render(Graphics g) {
		orbit.render((Graphics2D) g);
		
		g.setColor(Color.GREEN);
//		g.fillOval((int) (x*Planet.SCALE)-2, (int) (y*Planet.SCALE)-2, 4, 4);
//		g.fillPolygon(player);
		
		
//		g.drawString("Distance: " + distanceTo(planet), 30, 30);
//		g.drawString("Apoapsis: " + apoapsis, 30, 60);
//		g.drawString("Periapsis: " + periapsis, 30, 90);
//		g.drawString("Velocity: " + velocity, 30, 120);
//		g.drawString("Energy: " + energy, 30, 150);
//		g.drawString("Eccentricity: " + eccentricity, 30, 180);
//		g.drawString("SMA: " + sMajorAxis, 30, 210);
		g.drawString("Mean Anomaly: " + meanAnomaly, 30, 240);
		g.drawString("True Anomaly: " + trueAnomaly, 30, 270);
		g.drawString("Real Angle: " + angleOffset, 30, 300);
		g.drawString("Eccentric Anomaly: " + eccentricAnomaly, 30, 330);
		g.drawString("True Anomaly2: " + (((sMajorAxis * (1 - Math.pow(eccentricity, 2))) - radius)/(eccentricity*radius)), 30, 360);
		g.drawString("True Anomaly DIFF: " + (trueAnomaly - trueAnomaly2), 30, 390);
		g.drawString("radius vec: " + velocityVec.x + "  " + velocityVec.y, 30, 420);
		
		g.setColor(Color.YELLOW);
		g.drawLine((int) (planet.getX()*Game.scale), (int) (planet.getY()*Game.scale), (int) ((planet.getX() + radiusVector.x)*Game.scale), (int) ((planet.getY() + radiusVector.y)*Game.scale));
		
		
//		double angle = Math.toRadians(20);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		g2d.translate((int) (x*Game.scale), (int) (y*Game.scale));
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
