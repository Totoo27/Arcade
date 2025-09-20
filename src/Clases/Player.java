package Clases;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

public class Player {

	public int x, y;
	public int width = 40;
	public int height = 80;
	private GamePanel panel;

	// Velocidad
	public int dx = 0;
	public int dy = 0;
	public int speed = 12;
	private int salto = -20;

	// Disparo
	public int direccion = 1;
	private int delayDisparo = 500;
	private int margenDisparo;
	private long LastAttackTime;

	// Vida
	public int max_vida = 4;
	public int vida = max_vida;
	private boolean inmunidad = false;
	private long LastHitTime;
	
	// Frenesi
	private long LastFrenesi;
	
	//Slide
	private int speedSlide = 30;
	private boolean sliding = false;
	private int delaySlide = 1000;
	private long LastSlide;
	private int slideDuration = 200;
	private long firstSlide;

	// Movimientos
	public boolean leftPressed = false;
	public boolean rightPressed = false;
	public boolean spacePressed = false;
	public boolean downPressed = false;
	public boolean disparo = false;
	public boolean tocandoPiso = false;
	public boolean ctrlPressed = false;
	
	private Image sprite = new ImageIcon("src/sprites/jugador.png").getImage();


	public Player(int startX, int startY, GamePanel panel) {
		this.x = startX;
		this.y = startY;
		this.panel = panel;
	}

	// Mover
	public void move(int panelWidth, int panelHeight, int gravedad, ArrayList<Tile> tiles) {
		
		if(!sliding) {
		    dx = 0;
		}
	    if (leftPressed) {
	    	if(sliding && direccion > 0) {
	        	StopSliding();
	        }
	    	if(!sliding) {
	    		dx = -speed;
		        direccion = -1;
		        margenDisparo = -panel.BalaJugWidth;
	    	}
	        
	        
	    }
	    if (rightPressed) {
	    	if(sliding && direccion < 0) {
	        	StopSliding();
	        }
	    	if(!sliding) {
	    		dx = speed;
	 	        direccion = 1;
	 	        margenDisparo = width;
	    	}
	    }
	    if (spacePressed && tocandoPiso) {
	    	if(sliding) {
	        	StopSliding();
	        }
	        dy = salto;
	        tocandoPiso = false;
	        
	    }
	    if (disparo) {
	        Disparar();
	    }
	    if(ctrlPressed && tocandoPiso) {
	    	Slide();
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
	                	
	                	if(tile.plataforma && downPressed) {
	                		continue;
	                	}
	                	
	                    // si es plataforma solo dejar pisar si venís desde arriba
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


	    // Cámara en coordenadas de mundo
	    panel.cameraX = this.x - panelWidth / 2 + this.width / 2;
	    panel.cameraY = this.y - panelHeight / 2 + this.height / 2;
	    
	    // Limite de la camara
	    panel.cameraY = Math.max(0, Math.min(panel.cameraY, panel.FinalY - panelHeight));
	    panel.cameraX = Math.max(0, Math.min(panel.cameraX, panel.FinalX - panelWidth));
	    
	    if(y > panel.FinalY) {
	    	vida = 0;
	    	RecibirHit();
	    }
	    
	    if(System.currentTimeMillis() >= LastFrenesi + 8000) {
			// Valores default
			speed = 12;
			delayDisparo = 500;
	    }
	    
	    if(System.currentTimeMillis() >= firstSlide + slideDuration && sliding) {
	    	StopSliding();
	    	dx = 0;
	    }

		// Después de un tiempito se le va la inmunidad
		if (System.currentTimeMillis() >= LastHitTime + 500 && inmunidad) {
			LastHitTime = System.currentTimeMillis();
			inmunidad = false;
		}
	    
	}


	// Bajar la vida y eso
	public void RecibirHit() {
		if (!inmunidad) {
			inmunidad = true;
			vida--;
			if (vida <= 0) {
				vida = 0;
				leftPressed = false;
				rightPressed = false;
				spacePressed = false;
				disparo = false;
				morir();
			}
			// Reproducir Sonido
			LastHitTime = System.currentTimeMillis();
		}

	}

	private void morir() {
		// Mostrar el menú de muerte
		
		if(panel.hayCheckpoint) {
			panel.C_segundos = panel.segundos;
			panel.C_minutos = panel.minutos;
		}
		
		panel.timer.stop();
		GameMain gameMain = (GameMain) SwingUtilities.getWindowAncestor(panel);
		gameMain.mostrarMenuMuerte();
		
		// Reiniciar el jugador después de morir
		SwingUtilities.invokeLater(() -> {
			this.x = 200; // Coordenada inicial X
			this.y = 200; // Coordenada inicial Y
			this.vida = max_vida; // Restaurar la vida máxima
			this.dx = 0;
			this.dy = 0;
			this.inmunidad = false;
			this.tocandoPiso = false;
		});
		
	}

	// Disparar (un toque obvio)
	private void Disparar() {

		if (System.currentTimeMillis() >= LastAttackTime + delayDisparo) {
			panel.disparoJugador(x + margenDisparo, y + height / 3 + 8, direccion);
			LastAttackTime = System.currentTimeMillis();
		}

	}
	
	private void Slide() {
		
		if(System.currentTimeMillis() >= LastSlide + delaySlide) {
			dx = speedSlide * direccion;
			sliding = true;
			
			firstSlide = System.currentTimeMillis();
			LastSlide = System.currentTimeMillis();
			
			width = 80;
			height = 40;
			y += 40;
			
			GameMain.reproducirSonido("src/Sonidos/slide.wav");
		}
	}
	
	private void StopSliding() {
		sliding = false;
		width = 40;
		height = 80;
		if(tocandoPiso) {
			y -= 40;
		}
		
	}
	
	public void frenesi() {
		speed = 17;
		delayDisparo = 350;
		LastFrenesi = System.currentTimeMillis();
	}

	public Rectangle getBounds() {
	    return new Rectangle(x, y, width, height);
	}
	
	public Image getSprite() {
		return sprite;
	}


}
