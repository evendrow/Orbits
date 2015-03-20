package com.edwardv.orbits;

public class Vector {

	public double x, y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static double dot(Vector v1, Vector v2) {
		return v1.x*v2.x + v1.y*v2.y;
	}
	public static double cross(Vector v1, Vector v2) {
		return v1.x*v2.y - v1.y*v2.x;
	}
	
	public Vector multiply(double m) {
		return new Vector(x*m, y*m);
	}
	
	public Vector divide(double m) {
		return new Vector(x/m, y/m);
	}
	
	public Vector add(Vector v) {
		return new Vector(x+v.x, y+v.y);
	}
	
	public Vector subtract(Vector v) {
		return new Vector(x-v.x, y-v.y);
	}
	
	public double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public double getRelAngle() {
		return (Math.atan2(-y, x)+(Math.PI*2))%(Math.PI*2);
	}
	
	public void rotate(double angle) {
//		double mag = magnitude();
//		double newAngle = getRelAngle() + angle;
//		x = mag * Math.cos(newAngle);
//		y = mag * Math.sin(newAngle);
		x = x * Math.cos(angle) - y * Math.sin(angle);
	    y = x * Math.sin(angle) + y * Math.cos(angle);
	}
	
}
