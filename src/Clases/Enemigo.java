package Clases;

import java.awt.Graphics;
import java.lang.Math;

public class Enemigo {

	public int x;
	public int y;
	public int velocidadY;
	public int alto = 80;
	public int ancho = 40;
	private int velocidadX = 3;
	private Player jugador;

	public Enemigo(int x, int y, Player jugador) { // Constructor con los objetos de esta clase.

		this.x = x;
		this.y = y;
		this.jugador = jugador;

	}

	public void movimiento(int panelHeight, int gravedad) {

		if (jugador.x < x) {

			x -= velocidadX;
		} else if (jugador.x > x) {

			x += velocidadX;
		}

		if (y + alto >= panelHeight) {
			velocidadY = 0;
			y = panelHeight - alto;

		} else {

			velocidadY += gravedad;
		}

		y += velocidadY;

		double distancia = Math.sqrt(Math.pow(jugador.x - x, 2) + Math.pow(jugador.y - y, 2));// calcula la distancia

		if (distancia > 350) {

			if (jugador.y + jugador.height < y) {

				velocidadY = -12;
			}

			y += velocidadY;

		}

	}

}
