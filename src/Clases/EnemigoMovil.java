package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class EnemigoMovil extends Enemigo {
	private int velocidad = 3;
	private int dx;
	private int dy;
	private boolean tocandoPiso = false;
	private Player jugador;
	private int max_vida;
	private boolean disparador = false;
	
	private GamePanel panel;
	
	// Sprites direccionales
	private Image spriteRight;
	private Image spriteLeft;
	private int spriteScale = 2; // Factor de escala para agrandar sprites
	
	// Variables disparador
	
	private int delayDisparo = 1500; // milisegundos entre disparos
	private long lastAttackTime = 0;
	private int margenDisparo;
	
	// HealthBar
	private int barraAnchoMax = 50;
	private int barraAlto = 5;
	private int barraMargenY = 15;

	public EnemigoMovil(int x, int y, Player jugador, int tipoEnemigo, GamePanel panel) {
		super(x, y, 60, 120, 3, 3, false); // Aumentamos el tamaño para sprites escalados
		this.jugador = jugador;
		this.panel = panel;
		
		switch(tipoEnemigo) {
		
		case 0: // enemigo normal
			
			velocidad = 3;
			max_vida = 3;
			vida = max_vida;
			monedas = 3;
			sprite = new ImageIcon("src/sprites/enemigos/esqueleto.png").getImage();
			
		break;
		
		case 1: // tanque
			
			velocidad = 2;
			max_vida = 6;
			vida = max_vida;
			monedas = 5;
			sprite = new ImageIcon("src/sprites/enemigos/gordo.png").getImage();
			
			break;
			
		case 2: // rapido
			
			velocidad = 5;
			max_vida = 2;
			vida = max_vida;
			monedas = 2;
			sprite = new ImageIcon("src/sprites/enemigos/esqueleto.png").getImage();
			
			break;
			
		case 3: // disparador
			
			velocidad = 3;
			max_vida = 2;
			vida = max_vida;
			disparador = true;
			monedas = 4;
			sprite = new ImageIcon("src/sprites/enemigos/esqueleto-arma.png").getImage();
			
			break;
		
		
		}
		
		// Procesar sprites para crear versiones escaladas y reflejadas
		procesarSprites();

		
	}
	
	private void procesarSprites() {
		if (sprite != null) {
			// Crear versión escalada para la derecha
			spriteRight = escalarImagen(sprite, spriteScale);
			// Crear versión escalada y reflejada para la izquierda
			spriteLeft = reflejarImagen(spriteRight);
		}
	}
	
	private Image escalarImagen(Image imagen, int factor) {
		int nuevoAncho = imagen.getWidth(null) * factor;
		int nuevoAlto = imagen.getHeight(null) * factor;
		
		BufferedImage imagenEscalada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = imagenEscalada.createGraphics();
		g2d.drawImage(imagen, 0, 0, nuevoAncho, nuevoAlto, null);
		g2d.dispose();
		
		return imagenEscalada;
	}
	
	private Image reflejarImagen(Image imagen) {
		int ancho = imagen.getWidth(null);
		int alto = imagen.getHeight(null);
		
		BufferedImage imagenReflejada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = imagenReflejada.createGraphics();
		
		AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
		transform.translate(-ancho, 0);
		g2d.setTransform(transform);
		g2d.drawImage(imagen, 0, 0, null);
		g2d.dispose();
		
		return imagenReflejada;
	}

	@Override
	public void movimiento(int gravedad, ArrayList<Tile> tiles) {
		
		int distancia = Math.abs(x - jugador.x);
		
		if (jugador.x < x) {
			dx = -velocidad;
			direccion = -1;
			margenDisparo = -panel.BalaJugWidth;
		} else if (jugador.x > x) {
			dx = velocidad;
			margenDisparo =	width;
			direccion = 1;
		}
		
		// Si es disparador y el jugador está a menos de 300 → quedarse quieto
	    if (disparador && distancia <= 300) {
	        dx = 0;
	    }

		// Saltar si el jugador está cerca
		
		if (distancia < 200 && tocandoPiso) {
			if (jugador.y + jugador.height < y) {
				dy = -20;
			}
		}
		
		dy += gravedad;
		
		// Colisiones en X
	    x += dx;
	    for (Tile tile : tiles) {
	        if (!tile.isSolid() || tile.plataforma) continue;
	        if (this.getBounds().intersects(tile.getBounds())) {
	            if (this.y + this.height > tile.getY() && this.y < tile.getY() + tile.getHeight()) {
	                if (dx > 0) {
	                    this.x = tile.getX() - this.width;
	                } else if (dx < 0) {
	                    this.x = tile.getX() + tile.getWidth();
	                }
	                dx = 0;
	            }
	        }
	    }

	    // Colisiones en Y
	    y += dy;
	    tocandoPiso = false;
	    for (Tile tile : tiles) {
	        if (!tile.isSolid()) continue;
	        if (this.getBounds().intersects(tile.getBounds())) {
	            // comprobar solapamiento en X
	            if (this.x + this.width > tile.getX() && this.x < tile.getX() + tile.getWidth()) {
	                
	                if (dy > 0) {
	                    // si es plataforma solo dejar pisar si venís desde arriba
	                	if(tile.plataforma && jugador.y >= y + height) {
	                		return;
	                	}
	                	
	                    if (!tile.plataforma || (this.y + this.height - dy <= tile.getY())) {
	                        this.y = tile.getY() - this.height;
	                        dy = 0;
	                        tocandoPiso = true;
	                    }
	                } 
	                else if (dy < 0 && !tile.plataforma) {
	                    this.y = tile.getY() + tile.getHeight();
	                    dy = 0;
	                }
	            }
	        }
	    }
	   
	    if(disparador == true && distancia <= 600) {
	    	
	    	if(System.currentTimeMillis() >= lastAttackTime + delayDisparo) {
	    		disparo();
	    		lastAttackTime = System.currentTimeMillis();
	    	} 
	    	
	    }
	    
	    
	}
		
	public void disparo() {
		panel.disparoEnemigo(x + margenDisparo, y + height / 3, direccion);		
	}
	
	@Override
	public Image getSprite() {
		// Retornar el sprite correcto según la dirección
		if (direccion == 1) {
			return spriteRight != null ? spriteRight : sprite;
		} else {
			return spriteLeft != null ? spriteLeft : sprite;
		}
	}
	
	public void dibujarHP(Graphics g) {             

        int barraVida = (int)((vida / (double)max_vida) * barraAnchoMax);
        
        g.setColor(Color.BLACK);
        g.fillRect(x - 4 - panel.cameraX - barraAnchoMax/2 + width/2, y -4 - panel.cameraY - barraMargenY, barraAnchoMax+8, barraAlto+8);

        g.setColor(Color.RED);
        g.fillRect(x-panel.cameraX - barraAnchoMax/2 + width/2, y-panel.cameraY - barraMargenY, barraVida, barraAlto);
		
	}
	
}