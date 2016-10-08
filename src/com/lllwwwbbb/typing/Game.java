package com.lllwwwbbb.typing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class Game extends JPanel{
	public final static int HZ = 300, CHARSIZE = 36, MAXMISS = 30;
	private int tick = 0, hit = 0, miss = 0;
	private List<CharNode> charList = new LinkedList<CharNode>();
	Timer timer = new Timer();
	private boolean timer_stop = false, game_over = false;
	private Image offscr;
	Font font;
	
	public Game() {
		setMinimumSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGH));
		setSize(MainFrame.WIDTH, MainFrame.HEIGH);
		setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGH));
		charList.add(new CharNode());
		font = new Font("宋体", Font.BOLD, CHARSIZE);
		setFont(font);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (timer_stop) {
					repaint();
					return;
				}
				for (int i = 0; i < charList.size(); i++) {
					charList.get(i).move();
					if (charList.get(i).isOut()) {
						charList.remove(i);
						miss ++;
						if (miss > MAXMISS) {
							gameOver();
							break;
						}
						i --;
						continue;
					}
					if (charList.get(i).isHit()) {
						charList.remove(i);
						hit ++;
						i --;
						continue;
					}
				}
				tick += 1000 / HZ;
				if (tick % MainFrame.NEW_MIL_SEC == 0) {
					tick = 0;
					charList.add(new CharNode());
				}
				repaint();
			}
		}, 0, 1000 / HZ);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if (offscr == null) {
			offscr = createImage(MainFrame.WIDTH, MainFrame.HEIGH);
		}
		Graphics2D g2d = (Graphics2D) offscr.getGraphics();
		g2d.setFont(font);
		g2d.setColor(Color.BLACK);
		g2d.fill3DRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGH, true);
		g2d.setColor(Color.RED);
		g2d.drawString("miss: " + miss, MainFrame.WIDTH - CHARSIZE * 5, 
				MainFrame.mf.getContentPane().getSize().height);
		if (timer_stop) {
			if (game_over) {
				g2d.drawString("you failed! press enter to retry", 0,
						MainFrame.mf.getContentPane().getSize().height);
			}
			else {
				g2d.drawString("stop. press enter to continue", 0,
						MainFrame.mf.getContentPane().getSize().height);
			}
		}
		g2d.setColor(Color.GREEN);
		g2d.drawString("hit: " + hit, 0, CHARSIZE);
		for (int i = 0; i < charList.size(); i++) {
			charList.get(i).draw(g2d);
		}
		g.drawImage(offscr, 0, 0, null);
	}
	
	public void onKeypressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			if (game_over) { 
				game_over = false;
				hit = 0;
				miss = 0;
				charList.clear();
			}
			timer_stop = !timer_stop;
		}
		String str = KeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
		if (str.length() > 1) { return; }
		char ch = str.charAt(0);
		for (int i = 0; i < charList.size(); i ++) {
			if (charList.get(i).hit(ch)) {
				break;
			}
		}
	}
	
	private void gameOver() {
		game_over = true;
		timer_stop = true;
	}
}

class CharNode {
	private int value;
	private double x, y, speed;

	public CharNode() {
		y = 0;
		x = Math.random() * (MainFrame.WIDTH - Game.CHARSIZE);
		value = (int)(Math.random() * 26);
		speed = (Math.random() * (MainFrame.SPEEDH - MainFrame.SPEEDL)) + MainFrame.SPEEDL;
	}
	
	public boolean isHit() {
		return y < 0;
	}

	public boolean hit(char ch) {
		boolean h = speed > 0 && (ch == (value + 'a'));
		if (h) {
			speed = - speed;
		}
		return h;
	}
	public void move() {
		y += speed / Game.HZ;
	}
	
	public boolean isOut() {
		return y > MainFrame.HEIGH;
	}
	
	public void draw(Graphics2D g2d) {
		char[] ch = new char[]{(char)('a' + value)};
		String str = new String(String.valueOf(ch));
		g2d.setColor(Color.WHITE);
		g2d.drawString(str, (float)x, (float)y);
	}
}