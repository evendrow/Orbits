package com.edwardv.orbits;

import java.awt.*;

import javax.swing.*;


public class Screen extends JPanel implements Runnable {
	
	Thread gameLoop;
	
	public static int myWidth;
	public static int myHeight;
	
	public Game game;
	
	private long lastTick;
	private int deltaTick;
	
	private int ticks;
	private int totalTicks;
	private long lastTickTime;
	
	public Screen(PlanetRunner frame) {
		frame.addKeyListener(new KeyHandler());
		
		myWidth = getWidth();
		myHeight = getHeight();
				
		game = new Game();
		
		gameLoop = new Thread(this);
		gameLoop.start();
	}
	
	public void paintComponent(Graphics g) {
		//Clear Screen
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		game.render(g);
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Helvetica", Font.PLAIN, 20));
		g.drawString("" + totalTicks, 10, 30);
	}
	
	public void run() {
		lastTick = System.currentTimeMillis();
		lastTickTime = System.currentTimeMillis();
		ticks = 0;
		totalTicks = 0;
		deltaTick = 0;
		
		while (true) {
			game.tick(deltaTick);
			ticks++;
			
			repaint();
			
			try {
				while (System.currentTimeMillis() - lastTick < 1000l/60l) {
					Thread.sleep(1);
				}
				deltaTick = (int) (System.currentTimeMillis() - lastTick);
				lastTick = System.currentTimeMillis();
				
				if (System.currentTimeMillis() - lastTickTime > 1000) {
					totalTicks = ticks;
					ticks = 0;
					lastTickTime = System.currentTimeMillis();
				}
				
			} catch(Exception e) { }
		}
	}
}
