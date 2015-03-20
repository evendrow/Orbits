package com.edwardv.orbits.bigGravity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.edwardv.orbits.Game;
import com.edwardv.orbits.Vector;

public class OrbitingBody extends BigBody {

	protected BigBody body;
	
	protected double a;
	protected double soi;
	
	protected Color color;
	
	protected BigGravity bigGravity;
	
	public OrbitingBody(Vector rVec, Vector velVec, double mass, double radius, BigBody body, Color color, String id) {
		super(body.getX() + rVec.x, body.getY() + rVec.y, mass, radius, id);
		this.color = color;
		
		this.a = rVec.magnitude();
		soi = a*Math.pow(mass/body.getMass(), 2d/5d);
		
		bigGravity = new BigGravity(rVec, velVec, body);
		
		body.addBigSatellite(this);
		this.body = body;
	}
	
	public void tick(int delta) {
		bigGravity.tick(delta);
		
		x = bigGravity.getX();
		y = bigGravity.getY();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(new Color(1, 1, 1, 0.1f));
		g.fillOval((int) ((x*Game.scale) - (soi*Game.scale)), (int) ((y*Game.scale) - (soi*Game.scale)), (int) (soi*Game.scale)*2, (int) (soi*Game.scale)*2);
		
		
		g.setColor(color);
		g.fillOval((int) ((x-radius)*Game.scale), (int) ((y-radius)*Game.scale), (int) (radius*Game.scale*2d), (int) (radius*Game.scale*2d));
		
		g.setColor(Color.BLACK);
		g.drawLine((int) ((x*Game.scale) - (radius*Game.scale)), (int) (y*Game.scale), (int) ((x*Game.scale) + (radius*Game.scale)), (int) (y*Game.scale));
		g.drawLine((int) (x*Game.scale), (int) ((y*Game.scale) - (radius*Game.scale)), (int) (x*Game.scale), (int) ((y*Game.scale) + (radius*Game.scale)));
		
		bigGravity.renderOrbit(g);
	}
	
	public double getSoi() {
		return soi;
	}
	
	public BigBody getBody() {
		return body;
	}
	
	@Override
	public Vector getVelVec() {
		return bigGravity.getVelVec();
	}
	
	@Override
	public Vector getNonrelativeVelVec() {
		return bigGravity.getNonrelativeVelVec();
	}
	
}
