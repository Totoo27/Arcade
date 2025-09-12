package Clases;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

// Palador
public abstract class Boss {
	
	protected int x,y;
	protected int width;
	protected int height;
    protected int vida;
    protected int monedas;
	protected Player player;
	protected GamePanel panel;
	protected Image sprite;
	
	public Boss(int startX, int startY, int width, int height, int vida, GamePanel panel, Player player) {
		
		this.x = startX;
		this.y = startY;
		this.width = width;
		this.height = height;
		this.vida = vida;
		this.player = player;
		this.panel = panel;
		
	}
	
	public void recibirGolpe() {
		vida--;
	}
	
	public abstract void move(int gravedad, ArrayList<Tile> tiles);
    
    public abstract void ataque();
	
	public Rectangle getBounds(){
		return new Rectangle(x, y, width, height);
	}
	
	public Image getSprite() {
    	return sprite;
    }
	
}
