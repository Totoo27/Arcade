package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

// Palador
public abstract class Boss {
	
	protected int x,y;
	protected int width;
	protected int height;
    protected int vida;
    protected int max_vida;
    protected int monedas;
	protected Player player;
	protected GamePanel panel;
	protected Image sprite;
	
	private int probBotiquin = 20;
	private Random rand = new Random();
	
	// HealthBar
	private int barraAnchoMax = 80; 
	private int barraAlto = 8;  
	private int barraMargenY = 25;
	
	public Boss(int startX, int startY, int width, int height, int vida, GamePanel panel, Player player) {
		
		this.x = startX;
		this.y = startY;
		this.width = width;
		this.height = height;
		this.max_vida = vida;
		this.vida = max_vida;
		this.player = player;
		this.panel = panel;
		
	}
	
	public void recibirGolpe() {
		vida--;
		if(panel.nivel == 4 && this instanceof Boss3 == false) {
			if (rand.nextInt(100) < probBotiquin) {
				panel.bonuses.add(new Bonus(x + width/2 - 13, y + 10, 1, panel, player));
			}
		}
	}
	
	public abstract void move(int gravedad, ArrayList<Tile> tiles);
    
    public abstract void ataque();
	
	public Rectangle getBounds(){
		return new Rectangle(x, y, width, height);
	}
	
	public Image getSprite() {
    	return sprite;
    }
	
	public void dibujarHP(Graphics g) {             

        int barraVida = (int)((vida / (double)max_vida) * barraAnchoMax);
        
        g.setColor(Color.BLACK);
        g.fillRect(x - 4 - panel.cameraX - barraAnchoMax/2 + width/2, y -4 - panel.cameraY - barraMargenY, barraAnchoMax+8, barraAlto+8);

        g.setColor(Color.RED);
        g.fillRect(x-panel.cameraX - barraAnchoMax/2 + width/2, y-panel.cameraY - barraMargenY, barraVida, barraAlto);
		
	}
	
}
