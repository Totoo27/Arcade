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
	
	private boolean disparador = false;
	
	private GamePanel panel;
	
	//variables disparador
	
	private int delayDisparo = 1500; // milisegundos entre disparos
	private long lastAttackTime = 0;
	private int margenDisparo;
	private int direccion = -1;
	

	public EnemigoMovil(int x, int y, Player jugador, int tipoEnemigo, GamePanel panel) {
		super(x, y, 40, 80, 3, 3, false);
		this.jugador = jugador;
		this.panel = panel;
		
		switch(tipoEnemigo) {
		case 0: //enemigo normal
			
			velocidad = 3;
			vida = 3;
			
			
		break;
		
		case 1: //tanque
			
			velocidad = 2;
			vida = 6;
			
			break;
			
		case 2: //rapido
			
			velocidad = 5;
			vida = 2;
			
			break;
			
		case 3://disparador
			
			velocidad = 3;
			vida = 3;
			disparador = true;
			
			break;
		
		
		}
		

		
	}

	@Override
	public void movimiento(int gravedad, ArrayList<Tile> tiles) {
		// Movimiento horizontal: sigue al jugador
		if (jugador.x < x) {
			dx = -velocidad;
			direccion = -1;
			margenDisparo = -panel.BalaJugWidth;
		} else if (jugador.x > x) {
			dx = velocidad;
			margenDisparo =	width;
			direccion = 1;
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
	   
	    if(disparador == true) {
	    	
	    	if(System.currentTimeMillis() >= lastAttackTime + delayDisparo) {
	    		disparo();
	    		lastAttackTime = System.currentTimeMillis();
	    	} 
	    	
	    }
	    
	    
	}
		
	public void disparo() {
		panel.disparoEnemigo(x + margenDisparo, y + height / 3, direccion);		
	}
	
}