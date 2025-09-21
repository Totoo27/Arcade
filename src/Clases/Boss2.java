package Clases;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Boss2 extends Boss {

	private int velocidad = 18;
	private int dx = 0;
	private int dy;
	
	private boolean tocandoPiso = false;
	
	private long LastAttackTime = 0;
	private long DuracionAtaque = 3000;
	private int delayAtaque = 4000;
	
	
	private boolean ataque = false;
	
	public Boss2(int startX, int startY, GamePanel panel, Player player) {
		super(startX, startY, 90, 110, 12, panel, player);
		monedas = 30;
		sprite = new ImageIcon("src/sprites/enemigos/mordo.png").getImage();
	}
	
	@Override
	public void move(int gravedad, ArrayList<Tile> tiles) {
		
		if(!ataque && tocandoPiso) {
			if(player.x < x) {
				direccion = -1;
			} else {
				direccion = 1;
			}
			
			dx = 0;
		}
		
		if(System.currentTimeMillis() >= LastAttackTime + delayAtaque) {
			ataque();
			LastAttackTime = System.currentTimeMillis();
		}
		
		if(ataque) {
			dx = velocidad * direccion;
			if(tocandoPiso) {
				dy -= 13;
			}
			sprite = new ImageIcon("src/sprites/enemigos/mordoBall.png").getImage();
			if(System.currentTimeMillis() >= LastAttackTime + DuracionAtaque) {
				ataque = false;
				y = y - 20;
				width = 90;
				height = 110;
				sprite = new ImageIcon("src/sprites/enemigos/mordo.png").getImage();
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
	                    direccion = -1;
	                    dy -= 15;
	                } else if (dx < 0) {
	                    this.x = tile.getX() + tile.getWidth();
	                    direccion = 1;
	                    dy -= 15;
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
	}
	
	@Override
	public void ataque() {
		ataque = true;
		width = 90;
		height = 90;
		double prob = Math.random();
		if(prob < 0.5) {
			GameMain.reproducirSonido("src/Sonidos/gordo1.wav");
		} else {
			GameMain.reproducirSonido("src/Sonidos/gordo2.wav");
		}
		
	}
	
}
