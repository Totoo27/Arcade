package Clases;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

public class BalaMortero {
	
	public int x,y;
	public int width, height;
	public int pos_caida;
	private Player jugador;
	public Image sprite;
	
	private double px, py; // posición de física en double
	private double vx, vy;
	private boolean iniciado = false;

	
	public BalaMortero(int x, int y, int width, int height, int direccion, Player jugador) {
	    this.x = x;
	    this.y = y;
	    this.width = width;
	    this.height = height;
	    this.pos_caida = direccion;
	    this.px = x;
	    this.py = y;
	    this.jugador = jugador;
	}

	// move corregido
	public void move(int gravedad, ArrayList<Tile> tiles) {
	    if (!iniciado) {
	    	
	        double targetX = pos_caida;
	        double targetY = jugador.y + jugador.height/2;

	        // Distancias
	        double dx = targetX - px;
	        double dy = targetY - py;

	        double t = 50; // Tiempo de caida

	        // Velocidades iniciales
	        vx = dx / t;
	        vy = (dy - 0.5 * gravedad * t * t) / t;

	        iniciado = true;
	    }
	    
	    // Movimiento
	    px += vx;
	    vy += gravedad;
	    py += vy;

	    // Actualizar ints para render / colisiones
	    x = (int)Math.round(px);
	    y = (int)Math.round(py);
	}

	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public Image getSprite() {
		return sprite;
	}

}
