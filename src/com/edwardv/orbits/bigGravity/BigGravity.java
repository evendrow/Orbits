package com.edwardv.orbits.bigGravity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.edwardv.orbits.Game;
import com.edwardv.orbits.PlanetRunner;
import com.edwardv.orbits.Vector;
import com.edwardv.orbits.bigGravity.BigBody;

public class BigGravity {
	
	public static final int CIRCLE = 0;
	public static final int ELLIPSE = 1;
	public static final int PARABOLA = 2;
	public static final int HYPERBOLA = 3;
	
	private double x, y;
	
	private Vector velVec; //velocity Vector
	private double vel; //velocity
	private double velAngle;
	
	private double energy;
	
	private double apoapsis;
	private double periapsis;
	private double a; //Semi major axis
	private double b; //Semi minor axis
	
	private double e; //eccentricity
	private Vector eVec; //eccentricityVector
	
	private double n; //Mean Motion
	private double M; //mean anomaly
	private double E; //eccentric anomaly
	private double v; //true anomaly
	private double vOffset;
	
	private double vMin;
	private double vMax;
	
	private double period; //orbital period
	private double t; //time
	
	private double h; //angular momentum
	private double p; //semilatus rectum
	
	private double r; //radius
	private Vector rVec; //radiu Vector
	
	private BigBody body;
	private Orbit orbit;
	
	private int state;
	private boolean clockWise;
	
	
	public BigGravity(Vector radiusVector, Vector velocityVec, BigBody body) {
		this.x = radiusVector.x + body.getX();
		this.y = radiusVector.y + body.getY();
		this.rVec = radiusVector;
		this.r = rVec.magnitude();
		this.velVec = velocityVec;
		this.vel = velocityVec.magnitude();
		this.body = body;
		
		recalculateOrbit();
	}
	
	public static double atanh(double a) {
		return 0.5d * Math.log((1d+a)/(1d-a));
	}
	
	public static double asinh(double a) {
		return Math.log(a + Math.sqrt(Math.pow(a, 2)+1));
	}
	
	public static double acosh(double a) {
		return Math.log(a + Math.sqrt(a-1)*Math.sqrt(a+1));
	}
	
	public void recalculateOrbit() {
		
		if ((velVec.getRelAngle() - rVec.getRelAngle() + Math.PI*2)%(Math.PI*2) > Math.PI) {
			clockWise = true;
//			System.out.println("clockwise");
		} else {
			clockWise = false;
		}
		//-Mathf.Acos(-(1f / eccentricity)) < theta < Mathf.Acos(-(1f / eccentricity))
		//is range of true anomaly for hyperbolic orbit
		
		h = Vector.cross(rVec, velVec);
//		System.out.println(h);
//		p = Math.pow(h, 2)/body.getGM();
		energy = (Math.pow(vel, 2)/2d) - (body.getGM()/r);
		
		eVec = new Vector((velVec.y * h/body.getGM()) - (rVec.x/r), -(velVec.x*h/body.getGM()) - (rVec.y/r));
		e = eVec.magnitude();
		if (e == 0) {
			state = CIRCLE;
//			System.out.println("Circular Orbit");
			
			a = r;
			b = r;
			p = r;
			period = 2d * Math.PI * Math.sqrt(Math.pow(a, 3)/body.getGM());
			
			apoapsis = (1 + e) * a;
			periapsis = (1 - e) * a;
			
			v = Math.acos(Vector.dot(eVec, rVec)/(e*r));
			if (new Vector(rVec.magnitude(), velVec.magnitude()).magnitude() < 0)
				v = 2*Math.PI - v;
			
			vOffset =  (Math.atan2(-rVec.y, rVec.x) + Math.PI*2)%(Math.PI*2) - v;
			////////////////////////
			//IM RIGHT HERE
//			vOffset = -vOffset;
			
			E = 2*Math.atan(Math.sqrt((1-e)/(1+e))*Math.tan(v/2));
			M = E - e*Math.sin(E);
			
			n = Math.sqrt(body.getGM()/Math.pow(a, 3));
		}
		else if (e > 0 && e < 1) {
			state = ELLIPSE;
//			System.out.println("Elliptical Orbit");
			
			a = -body.getGM()/(2*energy);
			b = a * Math.sqrt(1d-Math.pow(e, 2));
			p = a*(1-Math.pow(e, 2));
			period = 2d * Math.PI * Math.sqrt(Math.pow(a, 3)/body.getGM());
			
			apoapsis = (1 + e) * a;
			periapsis = (1 - e) * a;
			
			v = Math.acos(Math.min(Vector.dot(eVec, rVec)/(e*r), 1));
			if (new Vector(rVec.magnitude(), velVec.magnitude()).magnitude() < 0)
				v = 2*Math.PI - v;
			
//			System.out.println("new V: " + v + "    " + (Vector.dot(eVec, rVec)/(e*r)));
//			System.out.println("new V: " + v + "    " + e + "    " + eVec.x + " " + eVec.y + "    " + rVec.x + " " + rVec.y);
			vOffset =  (Math.atan2(-rVec.y, rVec.x) + Math.PI*2)%(Math.PI*2) - v;
			////////////////////////
			//IM RIGHT HERE
//			vOffset = -vOffset;
			
			E = 2*Math.atan(Math.sqrt((1-e)/(1+e))*Math.tan(v/2));
			M = E - e*Math.sin(E);
			
			n = Math.sqrt(body.getGM()/Math.pow(a, 3));
		}
//		else if (e == 1) {
//			state = PARABOLA;
//		}
//		else if (e > 1) {
		else {
			state = HYPERBOLA;
//			System.out.println("Hyperbolic Orbit");
			
//			energy = (Math.pow(vel, 2)/2d) - (body.getGM()/r);
			
			a = -body.getGM()/(2*energy);
			b = a * Math.sqrt(Math.pow(e, 2)-1d);
			p = a*(Math.pow(e, 2)-1);
			period = 0;
			
			apoapsis = 0;
			periapsis = (1 - e) * a;
			
			vMin = -Math.acos(-1d/e);
			vMax = Math.acos(-1d/e);
			
//			v = Math.acos(Vector.dot(eVec, rVec)/(e*r));
			v = Math.acos((a*(1-Math.pow(e, 2)) - r)/(e*r));
			if (new Vector(rVec.magnitude(), velVec.magnitude()).magnitude() < 0)
				v = 2*Math.PI - v;
			
//			System.out.println("new V: " + v + "    " + " " + eVec.x + " " + eVec.y + "    " + rVec.x + " " + rVec.y);
			
			vOffset =  (Math.atan2(-rVec.y, rVec.x) + Math.PI*2)%(Math.PI*2) - v;
			////////////////////////
			//IM RIGHT HERE
//			vOffset = -vOffset;
			
			E = 2*BigGravity.atanh(Math.sqrt((e-1d)/(e+1d))*Math.tan(v/2d));//System.out.println("new E: " + E);
			M = (e*Math.sinh(E) - E)%(Math.PI*2);//System.out.println("new M: " + M);
			
			n = Math.sqrt(body.getGM()/-Math.pow(a, 3));
			
		}
		
		
//		eVec = rVec.multiply(Math.pow(velVec.magnitude(), 2)).divide(body.getGM());
//		eVec = eVec.subtract(velVec.multiply(Vector.dot(velVec, rVec)).divide(body.getGM()));
//		eVec = eVec.subtract(rVec.divide(rVec.magnitude()));
//		System.out.println(eVec.magnitude());
		
		
//		orbit = new Orbit(body.getX(), body.getY(), a, b, periapsis, vOffset);
		
		
		t = M/n;
		
		if (clockWise) {
			t = period - t;
			vOffset += (v-Math.PI)*2;
		}
		
		
//		if (state == HYPERBOLA) {
//			System.out.println("E: " + E + "  " + e + "  " + h);
//			System.out.println("M: " + M);
//			System.out.println("v: " + v);
//			System.out.println("t: " + t + " " + n + " " + a + " " + period);
//		}
//		
		
//		r = p/(1+e*Math.cos(v));
//		rVec.x = Math.cos(v + vOffset)*r;
//		rVec.y = -Math.sin(v + vOffset)*r;
//
//		x = body.getX() + rVec.x;
//		y = body.getY() + rVec.y;
		
	}
	
	public void setBody(BigBody newBody) {
		rVec = new Vector(0, 0);
		rVec.x = x - newBody.getX();
		rVec.y = y - newBody.getY();
		r = rVec.magnitude();
		
		velVec.x = getNonrelativeVelVec().x - newBody.getNonrelativeVelVec().x;
		velVec.y = getNonrelativeVelVec().y - newBody.getNonrelativeVelVec().y;
//		if (newBody instanceof OrbitingBody) {
//			velVec.x += ((OrbitingBody) body).getNonrelativeVelVec().x;
//			velVec.y += ((OrbitingBody) body).getNonrelativeVelVec().y;
//		}
		vel = velVec.magnitude();
//		System.out.println("new vel: " + velVec.x + "  " + velVec.y);
		
		body = newBody;
		recalculateOrbit();
	}
	
//	public void setBody(BigBody body) {
//		this.body = body;
//		
//		rVec.x = x - body.getX();
//		rVec.y = y - body.getY();
//		r = rVec.magnitude();
//		
//		recalculateOrbit();
//	}
	
	public void tick(int delta) {
		
//		Vector changeVector = body.getGravitVectoryWithDistance(body.getX(), y, delta);
//		velVec.x += changeVector.x;
//		velVec.y += changeVector.y;
//		
//		x += velVec.x * ((double) (delta) / 1000d);
//		y += velVec.y * ((double) (delta) / 1000d);
//		rVec.x += velVec.x * ((double) (delta) / 1000d);//= x - planet.getX();
//		rVec.y += velVec.y * ((double) (delta) / 1000d);//= y - planet.getY();
//		r = rVec.magnitude();
//		vel = velVec.magnitude();
		
		
		if (clockWise) {
			t -= (double) (delta)/1000d;
			if (t < 0)
				t += period;
		}
		else {
			t += (double) (delta)/1000d;
		}
		
		if ((state == CIRCLE || state == ELLIPSE) && period > 0) {
			while (t > period) {
				t -= period;
			}
		}
		
		
		M = t*n;
//		System.out.println("tick M: " + M);
		
		if (state == CIRCLE || state == ELLIPSE) {
			E = 0;
			double oldE = 0;
			for (int i=0;i<500;i++) { 
				E = M + e*Math.sin(E);
				if (E == oldE)
					break;
				
				oldE = E;
			}
		} else if (state == HYPERBOLA) {
			E = M;
			double oldE = M;
			for (int i=0;i<500;i++) { 
//				E = -M + e*Math.sinh(E);
				E = E + (M - e*Math.sinh(E) + E)/(e*Math.cosh(E)-1);
				if (E == oldE)
					break;
				
				oldE = E;
			}
		}
		
//		System.out.println("tick E: " + E);
		
		if (state == CIRCLE || state == ELLIPSE)
			v = 2 * Math.atan2(Math.sqrt(1+e) * Math.sin(E/2), Math.sqrt(1-e) * Math.cos(E/2));
		else if (state == HYPERBOLA)
			v = 2 * Math.atan2(Math.sqrt(e+1) * Math.sinh(E/2), Math.sqrt(e-1) * Math.cosh(E/2));
		
//		System.out.println("tick v: " + v);
//		v = Math.acos((Math.cos(E) - e)/(1 - e*Math.cos(E)));
		
//		System.out.println("new v: " + v);
		
//		if (state == CIRCLE)
//			r = a;
//		else if (state == ELLIPSE)
//			r = p/(1+e*Math.cos(v));
//		else if (state == HYPERBOLA)
//			r = a*(1-e*Math.cosh(E));
		
		Vector newR = getRelPosWithTA(v);
//		x = body.getX() + newR.x;
//		y = body.getY() + newR.y;
		rVec.x = newR.x;
		rVec.y = newR.y;
		r = rVec.magnitude();
//		rVec.x = Math.cos(v + vOffset)*r;
//		rVec.y = -Math.sin(v + vOffset)*r;

		
		x = body.getX() + rVec.x;
		y = body.getY() + rVec.y;
		
		vel = Math.sqrt(body.getGM()*((2/r)-(1/a)));
		double k = r/a;
//		velAngle = (v + (Math.PI - Math.acos(((2-2*Math.pow(e, 2))/(k*(2-k)))-1))/2)%(2*Math.PI) + vOffset;
//		velAngle = Math.atan(e*r*Math.sin(v)/p);
		
		
		velAngle = (v + Math.PI*1/2 - Math.atan(e*Math.sin(v)/(1+e*Math.cos(v))))%(2*Math.PI) + vOffset;
		velVec.x = vel*Math.cos(velAngle);
		velVec.y = -vel*Math.sin(velAngle);
		
//		if (state == HYPERBOLA) {
//			velVec.x = vel*Math.cosh(velAngle);
//			velVec.y = -vel*Math.sinh(velAngle);
//		}
		
		
//		double vr = body.getGM()*e*Math.sin(v)/h;
//		double vf = h/r;
//		velVec.x = (vr*Math.cos(v) - vf*Math.sin(v));
//		velVec.y = vr*Math.sin(v) + vf*Math.cos(v);
		
		if (clockWise) {
			velVec.x = -velVec.x;
			velVec.y = -velVec.y;
		}
		
		
		
		eVec = new Vector((velVec.y * h/body.getGM()) - (rVec.x/r), -(velVec.x*h/body.getGM()) - (rVec.y/r));
		
		
//		eVec = rVec.multiply(Math.pow(velVec.magnitude(), 2)).divide(body.getGM());
//		eVec = eVec.subtract(velVec.multiply(Vector.dot(velVec, rVec)).divide(body.getGM()));
//		eVec = eVec.subtract(rVec.divide(rVec.magnitude()));	
		
//		v = Math.acos(Vector.dot(eVec, rVec)/(e*r));
//		if (t > period/2)
//			v = 2*Math.PI - v;
		if (state == HYPERBOLA) {
			
//			System.out.println("v: " + v + "    of  " + velVec.x + " " + velVec.y + "    with " + velAngle);
//			System.out.println("t: " + t + " " + n + " " + a + " " + period);
		}
		
	}
	
	public Vector getRelPosWithTA(double tA) {
		
//		double radius = a*((1-Math.pow(e, 2))/(1 + e*Math.cos(tA)));
		double radius = 0;
		if (state == CIRCLE)
			radius = a;
		else if (state == ELLIPSE)
			radius = p/(1+e*Math.cos(tA));
		else if (state == HYPERBOLA)
			radius = -p/(1+e*Math.cos(tA));			
//			radius = a*(1-e*Math.cosh(E));
		Vector posVector = new Vector(radius*Math.cos(tA+vOffset), -radius*Math.sin(tA+vOffset));
		
		return posVector;
	}
	
	public void increaseVelocity(double xIncrease, double yIncrease) {
//		System.out.println("ADD V origin: " + velVec.x + "  " + velVec.y);
		velVec.x += xIncrease;
		velVec.y += yIncrease;
		vel = velVec.magnitude();
//		System.out.println("ADD V new: " + velVec.x + "  " + velVec.y + " from " + xIncrease + " " + yIncrease);
		recalculateOrbit();
	}
	
	public void renderOrbit(Graphics2D g) {
//		orbit.render(g);
		g.setColor(Color.GREEN);
		double resolution = 0.1;
		
		if (state == CIRCLE || state == ELLIPSE) {
			for (double i=0;i<Math.PI*2;i+=Math.toRadians(resolution)) {
				Vector pos1 = getRelPosWithTA(i);
				Vector pos2 = getRelPosWithTA(i+Math.toRadians(resolution));
				g.drawLine((int) ((body.getX()+pos1.x)*Game.scale), (int) ((body.getY()+pos1.y)*Game.scale), (int) ((body.getX()+pos2.x)*Game.scale), (int) ((body.getY()+pos2.y)*Game.scale));
			}
		} else if (state == HYPERBOLA) {
			double maxSoiV = (body instanceof OrbitingBody) ? Math.acos((-1/e)-(p/(((OrbitingBody) (body)).getSoi()*e))) : vMax-Math.toRadians(resolution);
//			System.out.println(maxSoiV);
			double renderV = v > Math.PI ? v-(Math.PI*2) : v;
			double start = clockWise ? -maxSoiV : renderV;//vMin+0.0001d
			double end = clockWise ? renderV : maxSoiV;//vMax-Math.toRadians(resolution);
			
			for (double i=start;i<end;i+=Math.toRadians(resolution)) {
				if (!clockWise && i+Math.toRadians(resolution)>end)
					i = end-Math.toRadians(resolution);
				else if (clockWise && i+Math.toRadians(resolution)>renderV)
					i = renderV-Math.toRadians(resolution);
				Vector pos1 = getRelPosWithTA(i);
				Vector pos2 = getRelPosWithTA(i+Math.toRadians(resolution));
				g.drawLine((int) ((body.getX()+pos1.x)*Game.scale), (int) ((body.getY()+pos1.y)*Game.scale), (int) ((body.getX()+pos2.x)*Game.scale), (int) ((body.getY()+pos2.y)*Game.scale));
			}
			
//			g.fillOval((int) ((body.getX()+getRelPosWithTA(maxSoiV).x)*Game.scale)-2, (int) ((body.getY()+getRelPosWithTA(maxSoiV).y)*Game.scale)-2, 4, 4);
		}
		
		
//		g.setColor(Color.GREEN);
//		g.drawLine((int) (x*Game.scale), (int) (y*Game.scale), (int) (x*Game.scale + eVec.x*100d), (int) (y*Game.scale + eVec.y*100d));
		
		g.setColor(Color.ORANGE);
		g.drawLine((int) (x*Game.scale), (int) (y*Game.scale), (int) (x*Game.scale + velVec.x*0.01d), (int) (y*Game.scale + velVec.y*0.01d));
		
//		g.setColor(Color.YELLOW);
////		g.drawLine((int) (x*Game.scale), (int) (y*Game.scale), (int) (x*Game.scale - rVec.x*Game.scale*0.01), (int) (y*Game.scale - rVec.y*Game.scale*0.01));
//		
//		
//		g.setFont(new Font("Sans Serif", Font.PLAIN, 20));
//		
//		int stringX = (int) (x*Game.scale) - Game.XOFF + 30;
//		int stringY = (int) (y*Game.scale) - Game.YOFF + 30;
//		
//		g.drawString("Distance: " + r, stringX + 30, stringY + 30);
////		g.drawString("Apoapsis: " + apoapsis, 30, 60);
////		g.drawString("Periapsis: " + periapsis, 30, 90);
////		g.drawString("Velocity: " + velocity, 30, 120);
////		g.drawString("Energy: " + energy, 30, 150);
////		g.drawString("Eccentricity: " + eccentricity, 30, 180);
////		g.drawString("SMA: " + sMajorAxis, 30, 210);
//		g.drawString("Mean Anomaly: " + M, stringX + 30, stringY + 240);
//		g.drawString("True Anomaly: " + v, stringX + 30, stringY + 270);
//		g.drawString("Real Angle: " + vOffset, stringX + 30, stringY + 300);
//		g.drawString("Eccentric Anomaly: " + E, stringX + 30, stringY + 330);
////		g.drawString("True Anomaly2: " + (((a * (1 - Math.pow(eccentricity, 2))) - radius)/(eccentricity*radius)), 30, 360);
////		g.drawString("True Anomaly DIFF: " + (trueAnomaly - trueAnomaly2), 30, 390);
//		g.drawString("vel vec: " + velVec.x + "  " + velVec.y, stringX + 30, stringY + 420);
	}
	
	public double getDistanceTo(BigBody bigBody) {
		return Math.sqrt(Math.pow(x - bigBody.x, 2) + Math.pow(y - bigBody.y, 2));
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getApoapsis() {
		return apoapsis;
	}
	
	public double getPeriapsis() {
		return periapsis;
	}
	
	public Vector getVelVec() {
		return velVec;
	}
	
	public Vector getNonrelativeVelVec() {
		Vector newVector = new Vector(0, 0);
		newVector.x = velVec.x + body.getNonrelativeVelVec().x;
		newVector.y = velVec.y + body.getNonrelativeVelVec().y;
		return newVector;
	}
	
	public double getRadius() {
		return r;
	}
}

