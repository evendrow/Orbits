package com.edwardv.orbits;

import java.awt.event.*;
import java.util.ArrayList;

public class KeyHandler implements KeyListener{

	public static final int W_KEY = KeyEvent.VK_W;
	public static final int A_KEY = KeyEvent.VK_A;
	public static final int S_KEY = KeyEvent.VK_S;
	public static final int D_KEY = KeyEvent.VK_D;
	
	public static final int ATTACK_KEY = KeyEvent.VK_SPACE;
	public static final int SMOKE_KEY = KeyEvent.VK_SHIFT;
	
	
	public static boolean isW = false;
	public static boolean isA = false;
	public static boolean isS = false;
	public static boolean isD = false;
	
	private static ArrayList<Integer> keysPressed = new ArrayList<Integer>();
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (!keysPressed.contains((Integer)e.getKeyCode()))
			keysPressed.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove((Integer)e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public static boolean isKeyPressed(int key) {
		return keysPressed.contains((Integer) key);
	}
	
	public static void removeKey(int key) {
		keysPressed.remove((Integer) key);
	}

}
