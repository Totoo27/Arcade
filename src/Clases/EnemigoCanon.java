package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.lang.Math;
import java.util.ArrayList;

public class EnemigoCanon extends Enemigo {

	private GamePanel panel;

	// variables disparador

	private int delayDisparo = 1500; // milisegundos entre disparos
	private long lastAttackTime = 0;
	private int margenDisparo;
	private int act_distancia;
	private int direccion;
	private Player player;
	
	public EnemigoCanon(int x, int y, GamePanel panel, int direccion, Player player, int act_distancia) {
		super(x, y, 50, 50, 1, 0, false);
		this.panel = panel;
		this.direccion = direccion;
		this.player = player;
		this.act_distancia = act_distancia;
		
		if(direccion > 0) {
			margenDisparo = width;
		} else {
			margenDisparo = 0;
		}
	}

	@Override
	public void movimiento(int gravedad, ArrayList<Tile> tiles) {}

	public void disparo() {
		
		int distancia = Math.abs(x - player.x);
		
		
		
		if(System.currentTimeMillis() >= lastAttackTime + delayDisparo && distancia <= act_distancia) {
			if(direccion == 1 && player.x < x) {
				return;
			} 
			if (direccion == -1 && player.x > x) {
				return;
			}
			panel.disparoCanion(x + margenDisparo, (y + height / 2) - 10, direccion);
			lastAttackTime = System.currentTimeMillis();
		}
			
	}

}