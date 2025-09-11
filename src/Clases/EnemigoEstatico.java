package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class EnemigoEstatico extends Enemigo {

	public EnemigoEstatico(int x, int y, String RutaSprite) {
		super(x, y, 40, 40, 1, 0, true); // Tamaño específico para el Pincho
		this.sprite = new ImageIcon(RutaSprite).getImage();
	}

	@Override
	public void movimiento(int gravedad, ArrayList<Tile> tiles) {}
}