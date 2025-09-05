package Clases;

import java.awt.Rectangle;

public class Player {
    
    public int x,y;
    public int width = 40;
    public int height = 80;
    
    private int dx = 0;
    private int dy = 0;
    private int speed = 10;
    
    // Movimientos
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public boolean spacePressed = false;
    private boolean tocandoPiso = false;
    
    public Player(int startX, int startY) { 
        this.x = startX;
        this.y = startY;
    }
    
    // Mover
    public void move(int panelWidth, int panelHeight, int gravedad) {
        dx = 0;
        if (leftPressed) dx = -speed;
        if (rightPressed) dx = speed;
        if (spacePressed && tocandoPiso) {
        	y -= 5;
        	dy = -17;
        }
       

        x += dx;
        if (x < 0) x = 0;
        if (x + width > panelWidth) x = panelWidth - width;

        if(y + height >= panelHeight) {
            dy = 0;
            y = panelHeight - height;
            tocandoPiso = true;
        } else {
        	tocandoPiso = false;
            dy += gravedad;
        }

        y += dy;
        
 
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
}
