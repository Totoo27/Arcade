package Clases;

import java.awt.Rectangle;

public class Tentaculo{
	
	public int x,y;
	public int width = 120;
	public int height = 600; 
	public int velocidadSubida = 15;
	
	private GamePanel panel;
	
	// Ataque
	private boolean ataque = false;
	public boolean bajando = false;
	private boolean spawneando = true;
	private long FirstAtaque = 0;
	private int EsperaAtaque = 250;
	private boolean Botiquin = false;
	
	public Tentaculo(int startX, int startY, GamePanel panel) {
		this.x = startX;
		this.y = startY;
		this.panel = panel;
	}
	
	public void move() {
		// Spawnear por debajo
		if(y > panel.PixelCoord(59) && spawneando) {
			y -= velocidadSubida;
		} else if (y <= panel.PixelCoord(59)){
			
			if(!ataque) {
				FirstAtaque = System.currentTimeMillis();
			}
			if(!bajando) {
				ataque = true;
			}
			
			spawneando = false;
		}
		
		// Atacar
		if(System.currentTimeMillis() >= FirstAtaque + EsperaAtaque && ataque) {
			if(y > panel.PixelCoord(52)) {
				y -= velocidadSubida * 3;
			} else {
				bajando = true;
				ataque = false;
			}
		}
		
		// Bajar
		if(bajando) {
			if(!Botiquin) {
				double prob = Math.random();
				
				if(prob < 0.3) {
					panel.bonuses.add(new Bonus(x + width/2, panel.PixelCoord(56), 1, panel, panel.player));
					
				}
				Botiquin = true;
			}
			
			y += velocidadSubida/2;
		}
	}
	
	public void recibirHit() {
		panel.Kvida--;
		if(panel.Kvida <= panel.KMax_vida/2) {
			panel.delayAttack = 1250;
		}
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
}
