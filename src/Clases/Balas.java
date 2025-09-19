package Clases;

import java.awt.Image;
import java.awt.Rectangle;

public class Balas {

	public int x, y;
	public int height; 
	public int width;
	public int velocidad;
	public boolean balaEnemiga = false;
	public Image sprite;
	
	public Balas(int x, int y, int width, int height, int velocidad, int direccion, boolean balaEnemiga) {
		
		this.x = x;
		this.y = y;
		
		this.height = height;
		this.width = width;
		
		this.velocidad = velocidad * direccion;
		
		this.balaEnemiga = balaEnemiga;
	}
	
	public void move(){
		
		x += velocidad; 
		
	}
	
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public Image getSprite() {
    	return sprite;
    }
	
	
	
}