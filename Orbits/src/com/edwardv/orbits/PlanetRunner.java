package com.edwardv.orbits;

import java.awt.*;

import javax.swing.*;

public class PlanetRunner extends JFrame {
	
	public static String title = "Tag";
	public static Dimension size = new Dimension(720, 480);
	
	public PlanetRunner() {
		setTitle(title);
		setSize(size);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		init();
	}
	
	public void init() {
		setLayout(new GridLayout(1, 1, 0, 0));
		
		Screen screen = new Screen(this);
		add(screen);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		PlanetRunner frame = new PlanetRunner();
	}

}
