package Clases;

import java.awt.Rectangle;

public class Balas {

	public int x, y;
	public int height; 
	public int width;
	public int velocidad;
	
	public Balas(int x, int y, int width, int height, int velocidad, int direccion) {
		
		this.x = x;
		this.y = y;
		
		this.height = height;
		this.width = width;
		
		this.velocidad = velocidad * direccion;
	}
	
	public void move(){
		
		x += velocidad; 
		
	}
	
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
	
	
	
}
