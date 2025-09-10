package Clases;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Enemigo {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int vida;

    public Enemigo(int x, int y, int width, int height, int vida) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.vida = vida;
     
    }

    public abstract void movimiento(int panelHeight, int gravedad);

    public void recibirGolpe() {
        vida--;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public abstract void dibujar(Graphics g);
}