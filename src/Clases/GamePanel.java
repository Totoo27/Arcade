package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

	// Timers
	private Timer timer;
	
	// Botones
	private JButton continuar;
	private JButton Salir;

	// Tiempo
	private int segundos = 0;
	private int minutos = 0;
	private long ultimoSegundo = 0;

	// Labels
	private JLabel contadorMonedas;
	private JLabel tiempo;

	// TileMap
	private ArrayList<Tile> tiles = new ArrayList<>();

	// Proyectiles
	private ArrayList<Balas> balas = new ArrayList<>();
	public int BalaJugWidth = 15;

	// Hitbox
	private boolean showHitboxes = true;
	
	// Movimiento Jugador
	public int cameraX = 0;
	public int cameraY = 0;

	// Mundo
	public int gravedad = 1;
	public int FinalY = 1500;
	public int FinalX = 3000;
	private boolean win = false;
	public int nivel = 0;
	private boolean bossSpawn = false;
	private Tile puertaFinal;
	private int EnemigosGenerados = 0;
	
	// Enemigos
	public ArrayList<Enemigo> EnemigosBasicos = new ArrayList<>();
	private Boss boss;
	
	// Monedas y Bonus
	private ArrayList<Bonus> bonuses = new ArrayList<>();
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
		
		g.drawImage(NivelToFondo(nivel), 0 - cameraX/10, 0, getWidth()*2, getHeight(), this);
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, getWidth(), getHeight());

		if (player != null) {
			
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
		
		// Pantalla de Ganar
        if(win == true) {
        	
        	timer.stop();
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
            
            // Tiempo en el que se completa
            String tiempoTexto = String.format("%02d:%02d", minutos, segundos);
            g.setColor(Color.WHITE);
            g.setFont(GameMain.Pixelart.deriveFont(35f));
            g.drawString("Tiempo: " + tiempoTexto, 370, 300);
            
            // Monedas Conseguidas
            g.drawImage(new ImageIcon("src/sprites/Bonus/coin.png").getImage(), 370, 320, 47, 40, this);
            g.setColor(Color.WHITE);
            g.drawString("" + MonedasJug, 420, 350);
        }

		// --------------- HITBOXES

		if (showHitboxes) {
			if (player != null) {
				g.setColor(new Color(0, 255, 255, 125));
				g.fillRect(player.x - cameraX, player.y - cameraY, player.width, player.height);
			}
			
			if (boss != null) {
				g.setColor(new Color(255, 0, 0, 125));
				g.fillRect(boss.x - cameraX, boss.y - cameraY, boss.width, boss.height);
			}
			
			for (Balas bala : balas) {
				g.setColor(Color.red);
				g.fillRect(bala.x - cameraX, bala.y - cameraY, bala.width, bala.height);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Generacion Enemigos
		GeneracionEventosNivel(nivel, player);

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

		// Movimiento y Colision Enemigos
		for (Enemigo enemigo : EnemigosBasicos) {
			enemigo.movimiento(gravedad, tiles);

			// Colision con jugador
			if (enemigo.getBounds().intersects(player.getBounds())) {
				player.RecibirHit();
			}
		}
		
		if(boss != null) {
			boss.move(gravedad, tiles);

			// Colision con jugador
			if (boss.getBounds().intersects(player.getBounds())) {
				player.RecibirHit();
			}
		}
		
		

		// Enemigos Recibiendo Golpe

		for (int i = 0; i < balas.size(); i++) {
			Balas bala = balas.get(i);
			boolean impactada = false;

			for (Enemigo enemigo : EnemigosBasicos) {

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
			
			if(boss != null) {
				if (bala.getBounds().intersects(boss.getBounds())) {
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
		
		if (boss != null) {
		    if (boss.vida <= 0) {
		        MonedasJug += boss.monedas;

		        // solo si el boss es un Boss1
		        if (boss instanceof Boss1 b1 && b1.ultimoPalazo != null) {
		            EnemigosBasicos.remove(b1.ultimoPalazo);
		        }

		        boss = null;
		    }
		}


		// Escribir monedas
		contadorMonedas.setText("" + MonedasJug);
	}

	public void iniciarJuego() {
		if (timer == null) {
			timer = new Timer(16, this); // Masomenos 60fps
			requestFocusInWindow();
		}

		// Reiniciar el estado del juego
		win = false;
		Salir.setVisible(false);
		continuar.setVisible(false);
		contadorMonedas.setVisible(true);
		tiempo.setVisible(true);
		

		bossSpawn = false;
		puertaFinal = null;
		EnemigosGenerados = 0;
		
		segundos = 0;
		minutos = 0;
		MonedasJug = 0;
		ultimoSegundo = System.currentTimeMillis();
		contadorMonedas.setText("" + MonedasJug);
		
		// Reiniciar Estructura del nivel
		bonuses.clear();
		balas.clear();
		EnemigosBasicos.clear();
		tiles.clear();
		
		// Inicializar Jugador y Estructura del nivel
		player = new Player(50, 1050, this);
		player.vida = player.max_vida;
		addKeyListener(this);
		GeneracionNivel(nivel);
		
		// Reiniciar movimiento
		player.leftPressed = false;
		player.rightPressed = false;
		player.spacePressed = false;
		player.disparo = false;

		timer.start();
	}

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
	
	// Estructura Niveles (Tiles, Bonus, Pinchos)
	public void GeneracionNivel(int nivel) {
		
		int coordX;
		int coordY;
		
		switch(nivel) {
		
		// LEVEL 1
		case 1:
			Musica.reproducirMusica("src/Canciones/musica1erMundo.wav");
			
			EnemigosBasicos.add(new EnemigoEstatico(400, 560, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho estático
			
			tiles.add(new Tile(400, 600, 600, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(900, 450, 400, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(1500, 300, 1200, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(900, 300, 300, 25, "src/sprites/Tiles/plataforma.png", true, true));
			
			bonuses.add(new Bonus(600, 530, 2, this, player));
			bonuses.add(new Bonus(800, 540, 0, this, player));
			
			bonuses.add(new Bonus(1000, 390, 0, this, player));
			bonuses.add(new Bonus(1200, 390, 1, this, player));
			
			break;
			
			
		// LEVEL 2
		case 2:
			Musica.reproducirMusica("src/Canciones/musica1erMundo.wav");
			FinalX = 7200;
			
			puertaFinal = new Tile(7150, 0, 50, FinalY, "src/sprites/Tiles/arena.png", true, false);
			tiles.add(puertaFinal);
			
			// Plataforma
			coordY = 1200;
			tiles.add(new Tile(0, coordY, 400, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(0, coordY + 50, 400, FinalY - 1250, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			coordY = 1200;
			EnemigosBasicos.add(new EnemigoEstatico(700, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			tiles.add(new Tile(700, coordY, 600, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(700, coordY + 50, 600, FinalY - 1250, "src/sprites/Tiles/arena.png", true, false));
			tiles.add(new Tile(850, coordY - 175, 300, 25, "src/sprites/Tiles/plataforma.png", true, true));
			EnemigosBasicos.add(new EnemigoEstatico(1260, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			EnemigosBasicos.add(new EnemigoEstatico(1210, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 800;
			for(int i=0;i<2;i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 200;
			}
			bonuses.add(new Bonus(975, coordY - 235, 0, this, player));
			
			// Plataforma
			coordY = 875;
			tiles.add(new Tile(1300, coordY, 600, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(1300, coordY + 50, 600, FinalY - 925, "src/sprites/Tiles/arena.png", true, false));
			tiles.add(new Tile(1450, coordY - 175, 300, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 1505;
			for(int i = 0; i<4; i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			coordX = 1485;
			for(int i=0;i<2;i++) {
				bonuses.add(new Bonus(coordX, coordY - 235, 0, this, player));
				coordX += 200;
			}
			
			// Plataforma
			tiles.add(new Tile(2200, 1050, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 2235;
			for(int i=0;i<2;i++) {
				bonuses.add(new Bonus(coordX, 990, 0, this, player));
				coordX += 150;
			}
			
			// Plataforma
			coordY = 1250;
			tiles.add(new Tile(2600, coordY, 400, 50, "src/sprites/Tiles/suelo.png", true, false));
			EnemigosBasicos.add(new EnemigoEstatico(2600, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			EnemigosBasicos.add(new EnemigoEstatico(2645, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			EnemigosBasicos.add(new EnemigoEstatico(2960, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			tiles.add(new Tile(2600, coordY + 50, 400, FinalY - 1300, "src/sprites/Tiles/arena.png", true, false));
			coordX = 2735;
			for(int i=0;i<3;i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 75;
			}
			
			// Plataforma
			tiles.add(new Tile(3300, 1250, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordY = 1250;
			for(int i=0;i<3;i++) {
				bonuses.add(new Bonus(3420, coordY - 60, 0, this, player));
				coordY -= 120;
			}
			
			// Plataforma
			coordY = 1100;
			tiles.add(new Tile(3700, coordY, 550, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(3700, coordY + 50, 550, FinalY - 1150, "src/sprites/Tiles/arena.png", true, false));
			tiles.add(new Tile(3850, coordY - 175, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 3855;
			for(int i = 0; i<3; i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, 885, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			coordX = 3850;
			for(int i=0;i<3;i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 100;
			}
			
			// Plataforma
			coordY = 850;
			tiles.add(new Tile(4500, coordY, 800, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(4500, coordY + 50, 800, FinalY - 900, "src/sprites/Tiles/arena.png", true, false));
			coordX = 4905;
			for(int i = 0; i<4; i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			
			// Plataforma
			coordY = 1000;
			tiles.add(new Tile(5700, coordY, 1500, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(5700, coordY + 50, 1500, FinalY - 1050, "src/sprites/Tiles/arena.png", true, false));
			EnemigosBasicos.add(new EnemigoEstatico(5700, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 6355;
			for(int i = 0; i<4; i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			tiles.add(new Tile(6000, coordY - 175, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			tiles.add(new Tile(6650, coordY - 175, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			bonuses.add(new Bonus(6425, coordY - 235, 2, this, player));
			
			break;
			
		// LEVEL 3
		case 3:
			
			tiles.add(new Tile(0, 400, 2000, 50, "src/sprites/Tiles/suelo.png", true, false));
			boss = new Boss1(500, 200, this, player);
			
			break;
		
		// LEVEL 4
		case 4:
			
			break;
		
		// LEVEL 5
		case 5:
			
			break;
			
		// LEVEL 6
		case 6:
			
			break;
		}
		
	}
	
	public void GeneracionEventosNivel(int nivel, Player player) {
		
		switch(nivel) {
		case 1:
			
			break;
			
		case 2:
			
			if(player.x >= 400 && EnemigosGenerados == 0) {
				EnemigosBasicos.add(new EnemigoMovil(1000, 1100, player, 3, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 2400 && EnemigosGenerados == 1) {
				EnemigosBasicos.add(new EnemigoMovil(2900, 1100, player, 1, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 3500 && EnemigosGenerados == 2) {
				EnemigosBasicos.add(new EnemigoMovil(4000, 800, player, 2, this));
				EnemigosBasicos.add(new EnemigoMovil(4200, 1000, player, 2, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 4500 && EnemigosGenerados == 3) {
				EnemigosBasicos.add(new EnemigoMovil(5100, 700, player, 1, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 6000 && !bossSpawn) {
				boss = new Boss1(6500, 800, this, player);
				bossSpawn = true;
			}
			
			if (boss != null) {
			    // Mientras el boss esté vivo, la puerta se mantiene
			    if (!tiles.contains(puertaFinal)) {
			        tiles.add(puertaFinal);
			    }
			} else {
			    // Cuando el boss muere, eliminas la puerta una sola vez
			    tiles.remove(puertaFinal);
			}

			
			break;
			
		case 3:
			
			break;
			
		case 4:
			
			break;
			
		case 5:
			
			break;
			
		case 6:
			
			break;
		}
		
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
	
	// ----------- ACTION LISTENER
	@Override
	public void keyPressed(KeyEvent e) {
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

	@Override
	public void keyReleased(KeyEvent e) {
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

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
