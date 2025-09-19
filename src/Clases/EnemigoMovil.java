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
	private int max_vida;
	private boolean disparador = false;
	
	private GamePanel panel;
	
	// Variables disparador
	
	private int delayDisparo = 1500; // milisegundos entre disparos
	private long lastAttackTime = 0;
	private int margenDisparo;
	
	// HealthBar
	private int barraAnchoMax = 50;
	private int barraAlto = 5;
	private int barraMargenY = 15;

	public EnemigoMovil(int x, int y, Player jugador, int tipoEnemigo, GamePanel panel) {
		super(x, y, 40, 80, 3, 3, false);
		this.jugador = jugador;
		this.panel = panel;
		
		switch(tipoEnemigo) {
		
		case 0: // enemigo normal
			
			velocidad = 3;
			max_vida = 3;
			vida = max_vida;
			monedas = 3;
			
		break;
		
		case 1: // tanque
			
			velocidad = 2;
			max_vida = 6;
			vida = max_vida;
			monedas = 5;
			
			break;
			
		case 2: // rapido
			
			velocidad = 5;
			max_vida = 1;
			vida = max_vida;
			monedas = 2;
			
			break;
			
		case 3: // disparador
			
			velocidad = 3;
			max_vida = 2;
			vida = max_vida;
			disparador = true;
			monedas = 4;
			
			break;
		
		
		}
		

		
	}

	@Override
	public void movimiento(int gravedad, ArrayList<Tile> tiles) {
		
		int distancia = Math.abs(x - jugador.x);
		
		if (jugador.x < x) {
			dx = -velocidad;
			direccion = -1;
			margenDisparo = -panel.BalaJugWidth;
		} else if (jugador.x > x) {
			dx = velocidad;
			margenDisparo =	width;
			direccion = 1;
		}
		
		// Si es disparador y el jugador está a menos de 300 → quedarse quieto
	    if (disparador && distancia <= 300) {
	        dx = 0;
	    }

		// Saltar si el jugador está cerca
		
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
	   
	    if(disparador == true && distancia <= 600) {
	    	
	    	if(System.currentTimeMillis() >= lastAttackTime + delayDisparo) {
	    		disparo();
	    		lastAttackTime = System.currentTimeMillis();
	    	} 
	    	
	    }
	    
	    
	}
		
	public void disparo() {
		panel.disparoEnemigo(x + margenDisparo, y + height / 3, direccion);		
	}
	
	public void dibujarHP(Graphics g) {             

        int barraVida = (int)((vida / (double)max_vida) * barraAnchoMax);
        
        g.setColor(Color.BLACK);
        g.fillRect(x - 4 - panel.cameraX - barraAnchoMax/2 + width/2, y -4 - panel.cameraY - barraMargenY, barraAnchoMax+8, barraAlto+8);

        g.setColor(Color.RED);
        g.fillRect(x-panel.cameraX - barraAnchoMax/2 + width/2, y-panel.cameraY - barraMargenY, barraVida, barraAlto);
		
	}
	
}