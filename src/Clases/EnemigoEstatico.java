package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class EnemigoEstatico extends Enemigo {

	public EnemigoEstatico(int x, int y) {
		super(x, y, 40, 20, 1, 0, true); // Tamaño específico para el Pincho
	}

	@Override
	public void movimiento(int gravedad, ArrayList<Tile> tiles) {}
}