package Clases;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Enemigo {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int vida;
    protected int monedas;
    protected boolean estatico;
    
    protected Image sprite;
    public int margenX = 0;
    
    protected int direccion = -1;

    public Enemigo(int x, int y, int width, int height, int vida, int monedas, boolean estatico) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.vida = vida;
        this.monedas = monedas;
        this.estatico = estatico;
    }

    public abstract void movimiento(int gravedad, ArrayList<Tile> tiles);
    
    public void recibirGolpe() {
        vida--;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public Image getSprite() {
    	return sprite;
    }
}