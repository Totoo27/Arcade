package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class GamePanel extends JPanel implements ActionListener, KeyListener {

	private Thread gameThread; 
	private final int FPS = 60; 
	private final double TIME_PER_UPDATE = 1_000_000_000.0 / FPS; // Tiempo por frame en nanosegundos
	public boolean enPausa = false;
	
	// Botones
	private JButton continuar;
	private JButton Salir;

	// Tiempo
	public int segundos = 0;
	public int minutos = 0;
	private long ultimoSegundo = 0;
	
	// Rangos
	public int tiempoObjetivo = 0; // En segundos
	public int P_Monedas = 0;

	// Labels
	private JLabel contadorMonedas;
	private JLabel tiempo;

	// TileMap
	public ArrayList<Tile> tiles = new ArrayList<>();

	// Proyectiles
	private ArrayList<Balas> balas = new ArrayList<>();
	private ArrayList<BalaMortero> morteros = new ArrayList<>();
	public int BalaJugWidth = 15;

	// Hitbox
	private boolean showHitboxes = true;
	
	// Movimiento Jugador
	public int cameraX = 0;
	public int cameraY = 0;
	public int VelFondo = 10;

	// Mundo
	public int gravedad = 1;
	public int FinalY = 1500;
	public int FinalX = 3000;
	private boolean win = false;
	public int nivel = 0;
	public boolean bossSpawn = false;
	public Tile puertaFinal;
	public boolean[] SpawnEnemigos = new boolean[10];
	
	// GeneracionNiveles
	private Niveles niveles = new Niveles();
	
	// CheckPoint
	
	private int C_x = 0;
	private int C_y = 0;
	private int C_monedas = 0;
	private boolean[] C_SpawnEnemigos = new boolean[10];
	public int C_segundos = 0;
	public int C_minutos = 0;
	private int Restarts = 0;
	public boolean hayCheckpoint = false;
	
	// Enemigos
	public ArrayList<Enemigo> EnemigosBasicos = new ArrayList<>();
	public ArrayList<Boss> bosses = new ArrayList<>();
	
	// Monedas y Bonus
	public ArrayList<Bonus> bonuses = new ArrayList<>();
	private int probBotiquin = 50;
	private Random rand = new Random();
	
	// Jugador
	private Player player;
	public int MonedasJug = 0;


	public GamePanel() {
		setBackground(new Color(50, 50, 50));
		setFocusable(true);

		// --- Botones ---
		
        Salir = GameMain.crearBoton("Salir", 250, 75);
        Salir.setFont(GameMain.Pixelart.deriveFont(28f));
        Salir.setBounds(275, 450, 200, 60);
        Salir.setVisible(false);
        this.add(Salir);
        
        continuar = GameMain.crearBoton("Continuar", 250, 75);
        continuar.setFont(GameMain.Pixelart.deriveFont(28f));
        continuar.setBounds(525, 450, 200, 60);
        continuar.setVisible(false);
        this.add(continuar);

        Salir.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose(); // Cerrar GamePanel
            new GameMain().setVisible(true);
        });
		
        continuar.addActionListener(e -> {
        	hayCheckpoint = false;
        	Restarts = 0;
            nivel++;
            iniciarJuego();
        });
        
		// --- Labels ---

		// ContadorMonedas
		contadorMonedas = new JLabel("" + MonedasJug);
		contadorMonedas.setForeground(Color.WHITE); // color del texto
		contadorMonedas.setFont(GameMain.Pixelart.deriveFont(36f)); // fuente
		contadorMonedas.setBounds(105, 80, 150, 40); // posición y tamaño

		// Tiempo
		tiempo = new JLabel("00:00");
		tiempo.setForeground(Color.WHITE);
		tiempo.setFont(GameMain.Pixelart.deriveFont(36f));
		tiempo.setBounds(860, 20, 200, 40);
		
		// Toggler de hitboxes
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("H"), "toggleHitboxes");
		getActionMap().put("toggleHitboxes", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHitboxes = !showHitboxes; // alternar hitboxes
				repaint();
			}
		});

		this.setLayout(null);
		this.add(contadorMonedas);
		this.add(tiempo);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(NivelToFondo(nivel), 0 - cameraX/VelFondo, 0, getWidth()*2, getHeight(), this);
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, getWidth(), getHeight());

		if (player != null) {

			// Sprite Jugador

		}

		// Dibujar Tiles
		for (Tile tile : tiles) {
			tile.draw(g, cameraX, cameraY);
		}

		// Dibujar Monedas del jugador
		
		if(!win) {
			g.drawImage(new ImageIcon("src/sprites/Bonus/coin.png").getImage(), 60, 80, 37, 30, this);
		}
		
		// --------------- SPRITES
		
		for(Bonus bonus : bonuses) {
			g.drawImage(
					bonus.getSprite(),
					bonus.x - cameraX,
					bonus.y - cameraY,
					bonus.width,
					bonus.height,
					this
					);
		}
		
		for(Enemigo enemigo : EnemigosBasicos) {
			g.drawImage(
					enemigo.getSprite(),
					enemigo.x - cameraX,
					enemigo.y - cameraY,
					enemigo.width,
					enemigo.height,
					this
					);
		}
		
		if(player != null) {
			// Dibujar vida del jugador
			int coordX = 60;
			for (int i = 0; i < player.vida; i++) {
				g.drawImage(new ImageIcon("src/sprites/barravida.png").getImage(), coordX, 20, 40, 39, this);
				coordX += 38;
			}

			// Dibujar lo que le falta de vida
			for (int i = 0; i < player.max_vida - player.vida; i++) {
				g.setColor(Color.BLACK);
				g.fillRect(coordX, 20, 40, 39);
				coordX += 38;
			}
		}
		
		// Pantalla de Ganar
        if(win == true) {
        	
        	
            contadorMonedas.setVisible(false);
            tiempo.setVisible(false);
            
            removeKeyListener(this);
            player = null;
            
            // Mostrar Botones
            
            continuar.setVisible(true);
            Salir.setVisible(true);
            
            // Fondo Negro
            g.setColor(new Color(0,0,0,125));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            // Mensaje Nivel Completado
            g.setColor(Color.YELLOW);
            g.setFont(GameMain.Pixelart.deriveFont(60f));
            g.drawString("Nivel Completado!", 265, 200);
            
            
            // Strings
            g.setColor(Color.WHITE);
            g.setFont(GameMain.Pixelart.deriveFont(35f));
            
            // Tiempo en el que se completa
            String tiempoTexto = String.format("%02d:%02d", minutos, segundos);
            g.drawString("Tiempo: " + tiempoTexto, 320, 275);
            
            // Monedas Conseguidas
            g.drawImage(new ImageIcon("src/sprites/Bonus/coin.png").getImage(), 320, 295, 47, 40, this);
            g.drawString("" + MonedasJug, 370, 325);
            
            // Resets del Jugador
            if(Restarts > 0) {
            	g.setColor(new Color(255, 100, 100));
            	g.drawString("- Restarts: " + Restarts, 275, 385);
            } else {
            	g.drawString("No Restarts", 270, 385);
            }
           
            
            // Dibujar Rangos
            g.drawImage(StatsToRank(1), 270, 245, 40, 40, this);
            g.drawImage(StatsToRank(0), 270, 295, 40, 40, this);
            
            // Calcular Rango Final
            String rankMonedas = StatsToRankLetter(0);
            String rankTiempo = StatsToRankLetter(1);
            int valorMonedas = rankToValue(rankMonedas);
            int valorTiempo = rankToValue(rankTiempo);

            // Sacar promedio
            int promedio = (valorMonedas + valorTiempo) / 2;

            // Cada 2 restarts restar 1 rango
            promedio -= Restarts / 2;

            // Limitar entre 1 y 5
            if(promedio < 1) promedio = 1;
            if(promedio > 5) promedio = 5;

            // Obtener letra final
            if(promedio == 5 && Restarts > 0) {
            	promedio = 4;
            }
            String rankFinal = valueToRank(promedio);

            // Cargar imagen final
            g.drawImage(new ImageIcon("src/sprites/rangos/" + rankFinal + "rank.png").getImage(), 600, 235, 150, 150, this);
        }

		// --------------- HITBOXES

		if (showHitboxes) {
			if (player != null) {
				
				if(player.vida > 0) {
					g.setColor(new Color(0, 255, 255, 125));
					g.fillRect(player.x - cameraX, player.y - cameraY, player.width, player.height);
				}
				
			}
			
			for (Boss boss : bosses) {
				g.setColor(new Color(255, 0, 0, 125));
				g.fillRect(boss.x - cameraX, boss.y - cameraY, boss.width, boss.height);
			}
			
			for (Balas bala : balas) {
				g.setColor(Color.red);
				g.fillRect(bala.x - cameraX, bala.y - cameraY, bala.width, bala.height);
			}
			
			for (BalaMortero mortero : morteros) {
				g.setColor(Color.red);
				g.fillRect(mortero.x - cameraX, mortero.y - cameraY, mortero.width, mortero.height);
			}

			for (Enemigo enemigo : EnemigosBasicos) {
				g.setColor(new Color(255, 0, 0, 125));
				g.fillRect(enemigo.x - cameraX, enemigo.y - cameraY, enemigo.width, enemigo.height);
			}

			for (Bonus bonus : bonuses) {
				g.setColor(new Color(255, 0, 255, 125));
				g.fillRect(bonus.x - cameraX, bonus.y - cameraY, bonus.width, bonus.height);
			}

		}
		repaint();
	}
	
	public void pausarJuego() {
	    enPausa = true;
	    player.leftPressed = false;
	    player.rightPressed = false;
	    player.spacePressed = false;
	    player.downPressed = false;
	    player.disparo = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (enPausa) {
		    return; // No actualiza nada mientras esté en pausa
		}

		
		// Generacion Enemigos
		niveles.GeneracionEventosNivel(nivel, player, this);

		// Tiempo juego
		if (System.currentTimeMillis() >= ultimoSegundo + 1000) {
			segundos++;
			ultimoSegundo = System.currentTimeMillis();
			if (segundos == 60) {
				segundos = 0;
				minutos++;
			}
		}
		String tiempoTexto = String.format("%02d:%02d", minutos, segundos);
		tiempo.setText(tiempoTexto);
		
		// Movimiento Jugador y Ganar
		if (player != null) {
			player.move(this.getWidth(), this.getHeight(), gravedad, tiles);
			
			if(player.x > FinalX) {
				win = true;
			}
		}

		// Movimiento Balas
		for (Balas bala : balas) {
			bala.move();
		}
		
		// Movimiento Morteros
		
		for(BalaMortero mortero : morteros) {
			mortero.move(gravedad, tiles);
		}
		
		// Movimiento y Colision de Monedas, botiquines, etc.
		
		for (Bonus bonus : bonuses) {
			bonus.move();
		}
		
		for(int i = 0; i<bonuses.size(); i++) {
			Bonus bonus = bonuses.get(i);
			
			if(player.getBounds().intersects(bonus.getBounds())) {
				bonus.darBonus();
				bonuses.remove(i);
				i--;
			}
		}
		
		// Disparo Caniones
		for (Enemigo enemigo : EnemigosBasicos) {
		    if (enemigo instanceof EnemigoCanon) {
		        ((EnemigoCanon) enemigo).disparo();
		    }
		}

		// Movimiento y Colision Enemigos
		for (Enemigo enemigo : EnemigosBasicos) {
			enemigo.movimiento(gravedad, tiles);

			// Colision con jugador
			if (enemigo.getBounds().intersects(player.getBounds())) {
				player.RecibirHit();
			}
		}
		
		for(Boss boss : bosses) {
			boss.move(gravedad, tiles);

			// Colision con jugador
			if (boss.getBounds().intersects(player.getBounds())) {
				player.RecibirHit();
			}
		}
		
		// Desaparición Balas
		
		for (int i = 0; i < balas.size(); i++) {
			Balas bala = balas.get(i);
			if(bala.x > FinalX || bala.x + bala.width < 0) {
				balas.remove(i);
			}
		}

		// Enemigos Recibiendo Golpe

		for (int i = 0; i < balas.size(); i++) {
			Balas bala = balas.get(i);
			boolean impactada = false;

			for (Enemigo enemigo : EnemigosBasicos) {
				
				if (enemigo instanceof EnemigoCanon) {
					continue;
				}

				if (bala.getBounds().intersects(enemigo.getBounds()) && !enemigo.estatico && bala.balaEnemiga == false) {
					enemigo.recibirGolpe();
					balas.remove(i);
					impactada = true;
					break;
				}

			}

			if (impactada) {
				i--;
				continue;
			}
			
			for(Boss boss : bosses) {
				if (bala.getBounds().intersects(boss.getBounds()) && !bala.balaEnemiga) {
					boss.recibirGolpe();
					balas.remove(i);
					impactada = true;
					break;
				}
			}

			if (impactada) {
				i--;
				continue;
			}
			
			if (player != null) {

				if (bala.getBounds().intersects(player.getBounds()) && bala.balaEnemiga == true) { 
					player.RecibirHit();
					balas.remove(i);
					impactada = true;
					break;
				}
				

			}

			if (impactada) {
				i--;
				continue;
			}

		}
		
		// Mortero Desapareciendo
		
		for(int i = 0; i<morteros.size(); i++) {
			BalaMortero mortero = morteros.get(i);
			boolean impactada = false;
			
			if(mortero.getBounds().intersects(player.getBounds())) {
				player.RecibirHit();
				morteros.remove(i);
				impactada = true;
				break;
			}
			
			if (impactada) {
				i--;
				continue;
			}
			
			for(Tile tile : tiles) {
				if(mortero.getBounds().intersects(tile.getBounds())) {
					morteros.remove(i);
					impactada = true;
					break;
				}
				
				if(impactada) {
					break;
				}
			}
			
			if (impactada) {
				i--;
				continue;
			}
			
		}

		// Muerte Enemigos

		for (int i = 0; i < EnemigosBasicos.size(); i++) {
			Enemigo enemigo = EnemigosBasicos.get(i);

			if (enemigo.y >= FinalY) {
				EnemigosBasicos.remove(i);
			}
			
			if(enemigo.vida <= 0) {
				MonedasJug += enemigo.monedas;
				if (rand.nextInt(100) < probBotiquin) {
					bonuses.add(new Bonus(enemigo.x + enemigo.width/2 - 13, enemigo.y + 10, 1, this, player));
				}
				EnemigosBasicos.remove(i);
			}
		}
		
		for(int i = 0;i<bosses.size();i++) {
			Boss boss = bosses.get(i);
			
		    if (boss.vida <= 0) {
		        MonedasJug += boss.monedas;

		        // solo si el boss es un Boss1
		        if (boss instanceof Boss1 b1 && b1.ultimoPalazo != null) {
		            EnemigosBasicos.remove(b1.ultimoPalazo);
		        }
		        
		        bosses.remove(i);
		    }
		}


		// Escribir monedas
		contadorMonedas.setText("" + MonedasJug);
		
	}
	
	// Empezar los niveles
	
private void actualizarLogica() {
    if (enPausa) {
        return; // No actualiza nada mientras esté en pausa
    }

    // Generacion Enemigos
    niveles.GeneracionEventosNivel(nivel, player, this);

    // Tiempo del juego
    if (System.currentTimeMillis() >= ultimoSegundo + 1000) {
        segundos++;
        ultimoSegundo = System.currentTimeMillis();
        if (segundos == 60) {
            segundos = 0;
            minutos++;
        }
    }
    String tiempoTexto = String.format("%02d:%02d", minutos, segundos);
    tiempo.setText(tiempoTexto);

    // Movimiento del jugador, balas, enemigos, etc.
    if (player != null) {
        player.move(this.getWidth(), this.getHeight(), gravedad, tiles);

        if (player.x > FinalX) {
            win = true;
        }
    }

    // Movimiento de balas
    for (Balas bala : balas) {
        bala.move();
    }

    // Movimiento de morteros
    for (BalaMortero mortero : morteros) {
        mortero.move(gravedad, tiles);
    }

    // Movimiento y colisiones de monedas, botiquines, etc.
    for (Bonus bonus : bonuses) {
        bonus.move();
    }

    // Colisiones y lógica adicional...
}

	public void iniciarJuego() {
    if (gameThread == null) {
        gameThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            double delta = 0;

            while (true) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / TIME_PER_UPDATE;
                lastTime = currentTime;

                // Actualizar lógica del juego en intervalos fijos
                while (delta >= 1) {
                    actualizarLogica();
                    delta--;
                }

                // Renderizar el juego
                repaint();

                // Dormir para limitar el uso de CPU
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();
    }

    // Reiniciar el estado del juego
    enPausa = false;
    win = false;
    Salir.setVisible(false);
    continuar.setVisible(false);
    contadorMonedas.setVisible(true);
    tiempo.setVisible(true);

    bossSpawn = false;
    puertaFinal = null;
    Arrays.fill(SpawnEnemigos, false);

    segundos = 0;
    minutos = 0;
    MonedasJug = 0;
    ultimoSegundo = System.currentTimeMillis();
    contadorMonedas.setText("" + MonedasJug);

    // Reiniciar Estructura del nivel
    bonuses.clear();
    balas.clear();
    morteros.clear();
    EnemigosBasicos.clear();
    bosses.clear();
    tiles.clear();

    // Inicializar Jugador y Estructura del nivel
    player = new Player(50, 1050, this);
    player.vida = player.max_vida;
    addKeyListener(this);
    niveles.GeneracionNivel(nivel, player, this);

    // Reiniciar movimiento
    player.leftPressed = false;
    player.rightPressed = false;
    player.spacePressed = false;
    player.disparo = false;

    if (hayCheckpoint) {
        System.arraycopy(C_SpawnEnemigos, 0, SpawnEnemigos, 0, C_SpawnEnemigos.length);
        player.x = C_x;
        player.y = C_y;
        MonedasJug = C_monedas;
        segundos = C_segundos;
        minutos = C_minutos;
        Restarts++;
    } else {
        Arrays.fill(SpawnEnemigos, false);
    }
}
	
	// Checkpoint
	
	public void checkpoint(int x, int y) {
		hayCheckpoint = true;
		
		C_x = x;
		C_y = y;
		C_monedas = MonedasJug;
		System.arraycopy(SpawnEnemigos, 0, C_SpawnEnemigos, 0, C_SpawnEnemigos.length);
		C_segundos = segundos;
		C_minutos = minutos;
		player.vida = player.max_vida;
		
	}
	
	// DISPAROS

	public void disparoJugador(int x, int y, int direccion) {
		int velocidad = 30;
		int height = 15;

		balas.add(new Balas(x, y, BalaJugWidth, height, velocidad, direccion, false));
		GameMain.reproducirSonido("src/Sonidos/disparo.wav");
	}
	
	public void disparoEnemigo(int x, int y, int direccion) {
		int velocidad = 30;
		int height = 15;

		balas.add(new Balas(x, y, BalaJugWidth, height, velocidad, direccion, true));
		GameMain.reproducirSonido("src/Sonidos/disparo.wav");
	}
	
	public void disparoCanion(int x, int y, int direccion) {
		int velocidad = 28;
		int height = 20;

		balas.add(new Balas(x, y, 20, height, velocidad, direccion, true));
		GameMain.reproducirSonido("src/Sonidos/disparoHeavy.wav");
	}
	
	public void disparoBoss(int x, int y, int direccion, Player jugador) {
		int height = 30;
		int width = 30;
		
		morteros.add(new BalaMortero(x, y, width, height, direccion, jugador));
		GameMain.reproducirSonido("src/Sonidos/disparoHeavy.wav");
	}
	
	public Image NivelToFondo(int nivel) {
		if(nivel == 1 || nivel == 2) {
			return new ImageIcon("src/resources/Fondo1.png").getImage();
		} else if (nivel == 3 || nivel == 4) {
			return new ImageIcon("src/resources/Fondo1.png").getImage();
		} else {
			return new ImageIcon("src/resources/Fondo1.png").getImage();
		}
	}
	
	public int PixelCoord(int pixel) { // Pasaje de Pixeles a coordenadas reales
		return pixel * 50;
	}
	
	// Ranking
	
	private int rankToValue(String rank) {
	    switch(rank) {
	        case "S": return 5;
	        case "A": return 4;
	        case "B": return 3;
	        case "C": return 2;
	        default: return 1; // D
	    }
	}

	private String valueToRank(int value) {
	    switch(value) {
	        case 5: return "S";
	        case 4: return "A";
	        case 3: return "B";
	        case 2: return "C";
	        default: return "D";
	    }
	}
	
	public String StatsToRankLetter(int tipoRango) {
	    String letra = "D"; // default
	    
	    if(tipoRango == 0) {
	        if(MonedasJug >= P_Monedas) letra = "S";
	        else if(MonedasJug >= 4 * P_Monedas / 5) letra = "A";
	        else if(MonedasJug >= 3 * P_Monedas / 4) letra = "B";
	        else if(MonedasJug >= P_Monedas / 2) letra = "C";
	        else letra = "D";
	    } else { // Tiempo
	        int tiempoJugador = minutos * 60 + segundos;

	        if(tiempoJugador <= tiempoObjetivo) letra = "S";
	        else if(tiempoJugador <= (tiempoObjetivo * 5) / 4) letra = "A";
	        else if(tiempoJugador <= (tiempoObjetivo * 4) / 3) letra = "B";
	        else if(tiempoJugador <= tiempoObjetivo * 2) letra = "C";
	        else letra = "D";
	    }

	    return letra;
	}

	public Image StatsToRank(int tipoRango) {
	    String letra = StatsToRankLetter(tipoRango);
	    return new ImageIcon("src/sprites/rangos/" + letra + "rank.png").getImage();
	}
	
	// Ultimo Frame
	
	public BufferedImage capturarFrame() {
	    BufferedImage imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = imagen.createGraphics();
	    paint(g2); // "pinta" el panel en el BufferedImage
	    g2.dispose();
	    return imagen;
	}

	
	// ----------- ACTION LISTENER
	@Override
	public void keyPressed(KeyEvent e) {
		if(player != null) {
			if (e.getKeyCode() == KeyEvent.VK_A)
				player.leftPressed = true;
			if (e.getKeyCode() == KeyEvent.VK_D)
				player.rightPressed = true;
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				player.spacePressed = true;
			if(e.getKeyCode() == KeyEvent.VK_S)
				player.downPressed = true;
			if (e.getKeyCode() == KeyEvent.VK_SHIFT)
				player.disparo = true;
			}
		}
		
		

	@Override
	public void keyReleased(KeyEvent e) {
		if(player != null) {
			if (e.getKeyCode() == KeyEvent.VK_A)
				player.leftPressed = false;
			if (e.getKeyCode() == KeyEvent.VK_D)
				player.rightPressed = false;
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				player.spacePressed = false;
			if(e.getKeyCode() == KeyEvent.VK_S)
				player.downPressed = false;
			if (e.getKeyCode() == KeyEvent.VK_SHIFT)
				player.disparo = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}