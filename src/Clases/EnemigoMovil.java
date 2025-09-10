package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.Math;

public class EnemigoMovil extends Enemigo {
	private int velocidadX = 3;
	public int velocidadY;
	private boolean TocandoPiso = false;
	private Player jugador;

	public int monedas = 3;

	public EnemigoMovil(int x, int y, Player jugador) {
		super(x, y, 40, 80, 3); // (x, y, width, height, vida) -> como el Enemigo original
		this.jugador = jugador;
	}

	@Override
	public void movimiento(int panelHeight, int gravedad) {
		// Movimiento horizontal: sigue al jugador
		if (jugador.x < x) {
			x -= velocidadX;
		} else if (jugador.x > x) {
			x += velocidadX;
		}

		// Gravedad
		if (y + height >= panelHeight) {
			velocidadY = 0;
			y = panelHeight - height;
			TocandoPiso = true;
		} else {
			velocidadY += gravedad;
			TocandoPiso = false;
		}

		y += velocidadY;

		// Saltar si el jugador está cerca
		int distancia = Math.abs(x - jugador.x);

		if (distancia < 200 && TocandoPiso) {
			if (jugador.y + jugador.height < y) {
				velocidadY = -15;
			}
			y += velocidadY;
		}
	}

	@Override
	public void dibujar(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, height); // Representación simple
	}
}
