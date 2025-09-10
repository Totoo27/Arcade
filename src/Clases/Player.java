package Clases;

import java.awt.Rectangle;

public class Player {
    
    public int x,y;
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
    public void move(int panelWidth, int panelHeight, int gravedad) {
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
        if(disparo) {
        	Disparar();
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
    
    // Bajar la vida y eso
    public void RecibirHit() {
    	if(!inmunidad) {
    		inmunidad = true;
    		vida--;
    		if(vida <= 0) {
    			vida = 0;
    		}
    		// Reproducir Sonido
    		LastHitTime = System.currentTimeMillis();
    	}
    	
    	// Después de un tiempito se le va la inmunidad
    	if(System.currentTimeMillis() >= LastHitTime + 500) {
    		LastHitTime = System.currentTimeMillis();
    		inmunidad = false;
    	}
    	
    }
    
    // Disparar (un toque obvio)
    private void Disparar() {
    	
    	if(System.currentTimeMillis() >= LastAttackTime + 500) {
    		panel.disparoJugador(x + margenDisparo, y + height/3, direccion);
    		LastAttackTime = System.currentTimeMillis();
    	}
    		
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
}
