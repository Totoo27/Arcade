package Clases;

import java.util.ArrayList;

public class Boss3 extends Boss{
	
	private long LastAttackTime = 0;
	private int delayAtaque = 2000;
	private int direccion = -1;
	
	private int dy;
	private boolean tocandoPiso = false;
	private int posJugador;
	
	public Boss3(int startX, int startY, GamePanel panel, Player player) {
		super(startX, startY, 80, 80, 20, panel, player);
		monedas = 25;
	}
	
	@Override
	public void move(int gravedad, ArrayList<Tile> tiles) {
		
		if(System.currentTimeMillis() >= LastAttackTime + delayAtaque) {
			ataque();
			LastAttackTime = System.currentTimeMillis();
		}
		
		dy += gravedad;

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
	                        // Reproducir sonido caída
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
		
		posJugador = player.x + (player.dx*15);
		panel.disparoBoss(x+width/2, y+width/2, posJugador, player);
		
	}

}
