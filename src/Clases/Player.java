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
	private int speed = 10;

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
	public void move(int panelWidth, int panelHeight, int gravedad, ArrayList<Tile> tiles) {
		dx = 0;
		if (leftPressed) {
			dx = -speed;
			direccion = -1;
			margenDisparo = -panel.BalaJugWidth; // Width de las balas del jugador

		}
		if (rightPressed) {
			dx = speed;
			direccion = 1;
			margenDisparo = width;
		}
		if (spacePressed && tocandoPiso) {
			y -= 5;
			dy = -17;
		}
		if (disparo) {
			Disparar();
		}

		x += dx;
		if (x < 0)
			x = 0;
		if (x + width > panelWidth)
			x = panelWidth - width;

		if (y + height >= panelHeight) {
			dy = 0;
			y = panelHeight - height;
			tocandoPiso = true;
		} else {
			tocandoPiso = false;
			dy += gravedad;
		}

		// Colision con los Tiles
		// Colisión con los Tiles en el eje X
		for (Tile tile : tiles) {
			if (this.getBounds().intersects(tile.getBounds()) && tile.isSolid()) {
				if (this.dx > 0) { // Si el jugador se mueve a la derecha
					this.x = tile.getX() - this.width;
					this.dx = 0;
				} else if (this.dx < 0) { // Si el jugador se mueve a la izquierda
					this.x = tile.getX() + tile.getWidth();
					this.dx = 0;
				}
			}
		}

		// Movimiento y colisión en el eje Y
		this.y += this.dy;

		// Colisión con los Tiles en el eje Y
		for (Tile tile : tiles) {
			if (this.getBounds().intersects(tile.getBounds()) && tile.isSolid()) {
				if (this.dy > 0) { // Si el jugador cae
					this.y = tile.getY() - this.height;
					this.dy = 0;
					this.tocandoPiso = true;
				} else if (this.dy < 0) { // Si el jugador choca por debajo
					this.y = tile.getY() + tile.getHeight();
					this.dy = 0;
				}
			}
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
