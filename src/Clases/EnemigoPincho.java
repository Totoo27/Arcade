package Clases;

import java.awt.Color;
import java.awt.Graphics;

public class EnemigoPincho extends Enemigo {

	public EnemigoPincho(int x, int y) {
		super(x, y, 40, 10, 1); // Tamaño específico para el Pincho
	}

	@Override
	public void dibujar(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(x, y, width, height); // Representación gráfica del Pincho
	}

	@Override
	public void movimiento(int panelHeight, int gravedad) {

	}
}