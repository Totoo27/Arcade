package Clases;

import java.util.ArrayList;

public class Boss1 extends Boss {
	
	private int velocidad = 4;
	private int lastDx;
	private int dx;
	private int dy;
	
	private long LastAttack;
	private int delayAttack = 2500;
	
	private boolean tocandoPiso;
	private boolean atacando = false;
	
	public EnemigoEstatico ultimoPalazo;
	private long tiempoCreacionPalazo;
	private int duracionPalazo = 150;
	
	private int ataquesRestantes = 0;
	private long lastPalazoTime = 0;
	private int delayEntrePalazos = 300;
	
	public Boss1(int startX, int startY, GamePanel panel, Player player) {
		super(startX, startY, 60, 120, 15, panel, player);
		monedas = 25;
	}
	
	@Override
	public void move(int gravedad, ArrayList<Tile> tiles) {
		if(!atacando) {
			if (player.x < x) {
				dx = -velocidad;
			} else if (player.x > x) {
				dx = velocidad;
			}
			
			// Saltar si el jugador está cerca
			int distancia = Math.abs(x - player.x);
			if (distancia < 300 && tocandoPiso) {
				if (player.y + player.height < y) {
					dy = -25;
				}
			}
		} else {
			dx = 0;
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

	    y += dy;
	    tocandoPiso = false;
	    for (Tile tile : tiles) {
	        if (!tile.isSolid()) continue;
	        if (this.getBounds().intersects(tile.getBounds())) {
	            // comprobar solapamiento en X
	            if (this.x + this.width > tile.getX() && this.x < tile.getX() + tile.getWidth()) {
	                
	                if (dy > 0) {
	                    // si es plataforma solo dejar pisar si venís desde arriba
	                	if(tile.plataforma && player.y >= y + height) {
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
	    
	    // Cooldown general del ataque
	    if(System.currentTimeMillis() > LastAttack + delayAttack && tocandoPiso && !atacando) {
	    	ataque(); // inicia secuencia de 3 palazos
	    	lastDx = dx;
	    	LastAttack = System.currentTimeMillis();
	    }
	    
	    // Borrar ataques después de un tiempo
	    if (ultimoPalazo != null && System.currentTimeMillis() - tiempoCreacionPalazo > duracionPalazo) {
	        panel.EnemigosBasicos.remove(ultimoPalazo);
	        ultimoPalazo = null;
	    }
	    
	    if (atacando && ataquesRestantes > 0) {
	    	if (System.currentTimeMillis() - lastPalazoTime >= delayEntrePalazos) {
	    		lanzarPalazo();
	    		ataquesRestantes--;
	    		lastPalazoTime = System.currentTimeMillis();
	    	}
	    	
	    	// cuando termina la secuencia
	    	if (ataquesRestantes == 0) {
	    		atacando = false;
	    	}
	    }
	    
	    

	}
	
	@Override
	public void ataque() {
		// inicia la secuencia
		atacando = true;
		ataquesRestantes = 3;
		lastPalazoTime = 0;
	}
	
	

	private void lanzarPalazo() {
	    int margenX;
	    if (lastDx > 0) {
	        margenX = x + width;
	    } else {
	        margenX = x - 300;
	    }

	    ultimoPalazo = new EnemigoEstatico(margenX, y + height/2, 300, 20, "src/sprites/Obstaculos/pincho.png");
	    panel.EnemigosBasicos.add(ultimoPalazo);
	    tiempoCreacionPalazo = System.currentTimeMillis();
	}
}
