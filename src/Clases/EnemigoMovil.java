package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.lang.Math;
import java.util.ArrayList;

public class EnemigoMovil extends Enemigo {
	private int velocidad = 3;
	private int dx;
	private int dy;
	private boolean tocandoPiso = false;
	private Player jugador;

	public EnemigoMovil(int x, int y, Player jugador) {
		super(x, y, 40, 80, 3, 3, false);
		this.jugador = jugador;
	}

	@Override
	public void movimiento(int gravedad, ArrayList<Tile> tiles) {
		// Movimiento horizontal: sigue al jugador
		if (jugador.x < x) {
			dx = -velocidad;
		} else if (jugador.x > x) {
			dx = velocidad;
		}

		// Saltar si el jugador está cerca
		int distancia = Math.abs(x - jugador.x);
		if (distancia < 200 && tocandoPiso) {
			if (jugador.y + jugador.height < y) {
				dy = -20;
			}
		}
		
		dy += gravedad;
		
		// Colisiones en X
	    x += dx;
	    for (Tile tile : tiles) {
	        if (!tile.isSolid() || tile.plataforma) continue;
	        if (this.getBounds().intersects(tile.getBounds())) {
	            if (this.y + this.height > tile.getY() && this.y < tile.getY() + tile.getHeight()) {
	                if (dx > 0) {
	                    this.x = tile.getX() - this.width;
	                } else if (dx < 0) {
	                    this.x = tile.getX() + tile.getWidth();
	                }
	                dx = 0;
	            }
	        }
	    }

	    // Colisiones en Y
	    y += dy;
	    tocandoPiso = false;
	    for (Tile tile : tiles) {
	        if (!tile.isSolid()) continue;
	        if (this.getBounds().intersects(tile.getBounds())) {
	            // comprobar solapamiento en X
	            if (this.x + this.width > tile.getX() && this.x < tile.getX() + tile.getWidth()) {
	                
	                if (dy > 0) {
	                    // si es plataforma solo dejar pisar si venís desde arriba
	                	if(tile.plataforma && jugador.y >= y + height) {
	                		return;
	                	}
	                	
	                    if (!tile.plataforma || (this.y + this.height - dy <= tile.getY())) {
	                        this.y = tile.getY() - this.height;
	                        dy = 0;
	                        tocandoPiso = true;
	                    }
	                } 
	                else if (dy < 0 && !tile.plataforma) {
	                    this.y = tile.getY() + tile.getHeight();
	                    dy = 0;
	                }
	            }
	        }
	    }
	}
}