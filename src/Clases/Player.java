package Clases;

import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class Player {

	public int x, y;
	public int width = 40;
	public int height = 80;
	private GamePanel panel;

	// Velocidad
	public int dx = 0;
	public int dy = 0;
	private int speed = 12;

	// Disparo
	private int direccion = 1;
	private int margenDisparo;
	private long LastAttackTime;

	// Vida
	public int max_vida = 4;
	public int vida = max_vida;
	private boolean inmunidad = false;
	private long LastHitTime;

	// Movimientos
	public boolean leftPressed = false;
	public boolean rightPressed = false;
	public boolean spacePressed = false;
	public boolean disparo = false;
	public boolean tocandoPiso = false;

	public Player(int startX, int startY, GamePanel panel) {
		this.x = startX;
		this.y = startY;
		this.panel = panel;
	}

	// Mover
	// Mover
	public void move(int panelWidth, int panelHeight, int gravedad, ArrayList<Tile> tiles) {
	    // 1) Entrada
	    dx = 0;
	    if (leftPressed) {
	        dx = -speed;
	        direccion = -1;
	        margenDisparo = -panel.BalaJugWidth;
	    }
	    if (rightPressed) {
	        dx = speed;
	        direccion = 1;
	        margenDisparo = width;
	    }
	    if (spacePressed && tocandoPiso) {
	        dy = -20;
	        tocandoPiso = false;
	    }
	    if (disparo) {
	        Disparar();
	    }
	    
	    dy += gravedad;
	    
	    // Colisiones en X
	    x += dx;
	    for (Tile tile : tiles) {
	        if (!tile.isSolid()) continue;
	        if (this.getBounds().intersects(tile.getBounds())) {
	            if (this.y + this.height > tile.getY() && this.y < tile.getY() + tile.getHeight()) {
	                if (dx > 0) { // viene desde la izquierda -> choca por la derecha del tile
	                    this.x = tile.getX() - this.width;
	                } else if (dx < 0) { // viene desde la derecha -> choca por la izquierda del tile
	                    this.x = tile.getX() + tile.getWidth();
	                }
	                dx = 0;
	            }
	        }
	    }

	    // Colisiones en Y
	    y += dy;
	    tocandoPiso = false; // recalcularlo
	    for (Tile tile : tiles) {
	        if (!tile.isSolid()) continue;
	        if (this.getBounds().intersects(tile.getBounds())) {
	            // comprobar solapamiento en X (solo entonces resolvemos colisión en Y)
	            if (this.x + this.width > tile.getX() && this.x < tile.getX() + tile.getWidth()) {
	                if (dy > 0) { // está cayendo -> aterriza sobre el tile
	                    this.y = tile.getY() - this.height;
	                    dy = 0;
	                    tocandoPiso = true;
	                } else if (dy < 0) { // golpeó por abajo (techo)
	                    this.y = tile.getY() + tile.getHeight();
	                    dy = 0;
	                }
	            }
	        }
	    }

	    // Cámara en coordenadas de mundo: la pantalla mostrará [cameraX .. cameraX+panelWidth]
	    panel.cameraX = this.x - panelWidth / 2 + this.width / 2;
	    panel.cameraY = this.y - panelHeight / 2 + this.height / 2;
	    
	    // Limite de la camara en Y
	    panel.cameraY = Math.max(0, Math.min(panel.cameraY, panel.FinalY - panelHeight));
	    
	    if(y > panel.FinalY) {
	    	vida = 0;
	    	RecibirHit();
	    }
	}


	// Bajar la vida y eso
	public void RecibirHit() {
		if (!inmunidad) {
			inmunidad = true;
			vida--;
			if (vida <= 0) {
				vida = 0;
				morir();
			}
			// Reproducir Sonido
			LastHitTime = System.currentTimeMillis();
		}

		// Después de un tiempito se le va la inmunidad
		if (System.currentTimeMillis() >= LastHitTime + 500) {
			LastHitTime = System.currentTimeMillis();
			inmunidad = false;
		}

	}

	private void morir() {
		// Mostrar el menú de muerte
		GameMain gameMain = (GameMain) SwingUtilities.getWindowAncestor(panel);
		gameMain.mostrarMenuMuerte();

		// Reiniciar el jugador después de morir
		SwingUtilities.invokeLater(() -> {
			this.x = 200; // Coordenada inicial X
			this.y = 200; // Coordenada inicial Y
			this.vida = max_vida; // Restaurar la vida máxima
			this.dx = 0;
			this.dy = 0;
			this.inmunidad = false;
			this.tocandoPiso = false;
		});
	}

	// Disparar (un toque obvio)
	private void Disparar() {

		if (System.currentTimeMillis() >= LastAttackTime + 500) {
			panel.disparoJugador(x + margenDisparo, y + height / 3, direccion);
			LastAttackTime = System.currentTimeMillis();
		}

	}

	public Rectangle getBounds() {
	    return new Rectangle(x, y, width, height);
	}


}
