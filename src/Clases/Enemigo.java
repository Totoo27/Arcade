package Clases;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.Math;

public class Enemigo {
	
	public int x;
	public int y;

	public int height = 80;
	public int width = 40;

	private int velocidadX = 3;
	public int velocidadY;
	private boolean TocandoPiso = false;

	private Player jugador;
	
	public int vida = 3;
	public int monedas = 3;
	
	public Enemigo (int x, int y, Player jugador) { // Constructor con los objetos de esta clase.
		
		this.x = x;
		this.y = y;
		this.jugador = jugador;
		
	}
	
	public void movimiento(int panelHeight, int gravedad){
		
		// Vel en X
		if(jugador.x < x) {
			x -= velocidadX;
		}else if (jugador.x > x) {	
			x += velocidadX;
		}
	
		// Física de gravedad
		if(y + height >= panelHeight) {
			velocidadY = 0;
			y = panelHeight - height;
			TocandoPiso = true;
		} else {
			velocidadY += gravedad;
    	 	TocandoPiso = false;
		}

		y += velocidadY;
		
		// Sacar módulo de la distancia con el jug
		int distancia = Math.abs(x - jugador.x);
    
		if(distancia < 200 && TocandoPiso) {
    	
			if(jugador.y + jugador.height < y) {
				velocidadY = -15;
			}

			y += velocidadY;
	
		}
		
	}
	
	// Recibir Golpe
	public void recibirGolpe() {
		vida--;
	}
	
	public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}
