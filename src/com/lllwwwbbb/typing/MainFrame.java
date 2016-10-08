package com.lllwwwbbb.typing;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
	public static final int WIDTH = 880, HEIGH = 620, SPEEDL = 50, SPEEDH = 250, NEW_MIL_SEC = 200;
	static public MainFrame mf;
	private Game game;
	
	public MainFrame() {
		super("A simple typing game   ---   by : lllwwwbbb");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGH);
		setMinimumSize(new Dimension(WIDTH, HEIGH));
		setVisible(true);
		setResizable(false);
		
		game = new Game();
		add(game);
		pack();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mf = new MainFrame();
				mf.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent e) {}
					public void keyReleased(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {
						mf.game.onKeypressed(e);						
					}
				});
			}
		});
	}
}
