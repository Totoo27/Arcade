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
	
	// Rangos
	private int tiempoObjetivo = 0; // En segundos
	private int P_Monedas = 0;

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
	private int VelFondo = 10;

	// Mundo
	public int gravedad = 1;
	public int FinalY = 1500;
	public int FinalX = 3000;
	private boolean win = false;
	public int nivel = 0;
	private boolean bossSpawn = false;
	private Tile puertaFinal;
	private int EnemigosGenerados = 0;
	private boolean hayCheckpoint = false;
	
	// CheckPoint
	
	private int C_x = 0;
	private int C_y = 0;
	private int C_monedas = 0;
	private int C_EnemigosGenerados = 0;
	private int C_segundos = 0;
	private int C_minutos = 0;
	private int Restarts = 0;
	
	// Enemigos
	public ArrayList<Enemigo> EnemigosBasicos = new ArrayList<>();
	private ArrayList<Boss> bosses = new ArrayList<>();
	
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
            g.drawString("Restarts: " + Restarts, 270, 375);
            
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
            String rankFinal = valueToRank(promedio);

            // Cargar imagen final
            g.drawImage(new ImageIcon("src/sprites/rangos/" + rankFinal + "rank.png").getImage(), 600, 235, 150, 150, this);
        }

		// --------------- HITBOXES

		if (showHitboxes) {
			if (player != null) {
				g.setColor(new Color(0, 255, 255, 125));
				g.fillRect(player.x - cameraX, player.y - cameraY, player.width, player.height);
			}
			
			for (Boss boss : bosses) {
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
				System.out.print("chau bala");
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
		bosses.clear();
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
		
		if(hayCheckpoint) {
			EnemigosGenerados = C_EnemigosGenerados;
			player.x = C_x;
			player.y = C_y;
			MonedasJug = C_monedas;
			segundos = C_segundos;
			minutos = C_minutos;
			Restarts++;
		}

		timer.start();
	}
	
	public void checkpoint(int x, int y) {
		hayCheckpoint = true;
		
		C_x = x;
		C_y = y;
		C_monedas = MonedasJug;
		C_EnemigosGenerados = EnemigosGenerados - 1;
		C_segundos = segundos;
		C_minutos = minutos;
		
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
			VelFondo = 10;
			
			// Parámetros Rangos
			P_Monedas = 52;
			tiempoObjetivo = 42;
			
			// Límites Nivel
			FinalX = PixelCoord(144);
			
			puertaFinal = new Tile(PixelCoord(143), 0, 50, FinalY, "src/sprites/Tiles/arena.png", true, false);
			tiles.add(puertaFinal);
			
			// Plataforma
			coordY = 1200;
			tiles.add(new Tile(0, coordY, PixelCoord(8), 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(0, coordY + 50, PixelCoord(8), FinalY - 1250, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			coordY = 1200;
			EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(14), coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			tiles.add(new Tile(PixelCoord(14), coordY, PixelCoord(12), 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(PixelCoord(14), coordY + 50, PixelCoord(12), FinalY - 1250, "src/sprites/Tiles/arena.png", true, false));
			tiles.add(new Tile(PixelCoord(17), coordY - 175, PixelCoord(6), 25, "src/sprites/Tiles/plataforma.png", true, true));
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
			coordY = PixelCoord(20);
			tiles.add(new Tile(PixelCoord(114), coordY, PixelCoord(30), 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(PixelCoord(114), coordY + 50, PixelCoord(30), FinalY - 1050, "src/sprites/Tiles/arena.png", true, false));
			EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(114), coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			bonuses.add(new Bonus(PixelCoord(116), PixelCoord(20) - 100, 3, this, player));
			coordX = 6355;
			for(int i = 0; i<4; i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			tiles.add(new Tile(6000, coordY - 175, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			tiles.add(new Tile(6650, coordY - 175, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			bonuses.add(new Bonus(6425, coordY - 235, 2, this, player));
			
			
			break;
			
			
		// LEVEL 2
		case 2:
			Musica.reproducirMusica("src/Canciones/musica1erMundo.wav");
			
			// Parámetros Rangos
			P_Monedas = 115;
			tiempoObjetivo = 115;
			
			// Limites Nivel
			VelFondo = 15;
			FinalX = PixelCoord(267);
			FinalY = PixelCoord(81);
			puertaFinal = new Tile(PixelCoord(266), 0, 50, PixelCoord(24), "src/sprites/Tiles/arena.png", true, false);
			tiles.add(puertaFinal);
			
			// Spawn Jugador
			player.y = PixelCoord(66);
			
			// Plataforma
			coordY = PixelCoord(68);
			tiles.add(new Tile(0, coordY, PixelCoord(15), 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(0, coordY + 50, PixelCoord(15), FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));

			// Plataforma
			tiles.add(new Tile(PixelCoord(22), coordY, PixelCoord(9), 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(PixelCoord(22), coordY + 50, PixelCoord(9), FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			tiles.add(new Tile(PixelCoord(35), coordY, PixelCoord(9), 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(PixelCoord(35), coordY + 50, PixelCoord(9), FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			coordY = PixelCoord(64) + 25;
			coordX = PixelCoord(27);
			tiles.add(new Tile(PixelCoord(26), coordY, PixelCoord(14), 25, "src/sprites/Tiles/plataforma.png", true, true));
			for(int i=0; i<4; i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 200;
			}
			
			// Plataforma
			coordY = 3050;
			tiles.add(new Tile(1500, coordY, 300, 25, "src/sprites/Tiles/plataforma.png", true, true));
			bonuses.add(new Bonus(1630, coordY - 210, 0, this, player));
			
			// Plataforma
			coordY = 3400;
			tiles.add(new Tile(2450, coordY, 550, 25, "src/sprites/Tiles/plataforma.png", true, true));
			EnemigosBasicos.add(new EnemigoEstatico(2450, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			EnemigosBasicos.add(new EnemigoEstatico(2960, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 2500;
			for(int i=0; i<3; i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 200;
			}
			
			// Plataforma
			tiles.add(new Tile(3200, coordY, 550, 25, "src/sprites/Tiles/plataforma.png", true, true));
			EnemigosBasicos.add(new EnemigoEstatico(3200, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			EnemigosBasicos.add(new EnemigoEstatico(3710, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 3250;
			for(int i=0; i<3; i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 200;
			}
			
			// Plataforma
			tiles.add(new Tile(3900, coordY, 550, 25, "src/sprites/Tiles/plataforma.png", true, true));
			EnemigosBasicos.add(new EnemigoEstatico(3900, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			EnemigosBasicos.add(new EnemigoEstatico(4410, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 3950;
			for(int i=0; i<3; i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 200;
			}
			
			// Plataforma
			coordY = 3250;
			tiles.add(new Tile(4650, coordY, 700, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(4650, coordY + 50, 700, FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			coordY = 3075;
			tiles.add(new Tile(5500, coordY, 800, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 5600;
			for(int i=0; i<4; i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 200;
			}
			
			// Plataforma
			coordY = 2900;
			tiles.add(new Tile(5500, coordY, 800, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 5600;
			for(int i=0; i<4; i++) {
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				coordX += 200;
			}
			
			// Plataforma
			coordY = 2750;
			tiles.add(new Tile(6550, coordY, 1300, 50, "src/sprites/Tiles/suelo.png", true, false));
			tiles.add(new Tile(6550, coordY + 50, 1300, FinalY - coordY - 50, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataformas
			coordY = 2600;
			coordX = 7750;
			for(int i=0;i<5;i++) {
				tiles.add(new Tile(7450, coordY, 400, 25, "src/sprites/Tiles/plataforma.png", true, true));
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				EnemigosBasicos.add(new EnemigoEstatico(7650, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				EnemigosBasicos.add(new EnemigoEstatico(7700, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordY -= 300;
			}
			
			// Plataformas
			coordY = 2450;
			coordX = 6960;
			for(int i=0;i<4;i++) {
				tiles.add(new Tile(6900, coordY, 400, 25, "src/sprites/Tiles/plataforma.png", true, true));
				bonuses.add(new Bonus(coordX, coordY - 60, 0, this, player));
				EnemigosBasicos.add(new EnemigoEstatico(7020, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				EnemigosBasicos.add(new EnemigoEstatico(7070, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordY -= 300;
			}
			
			// Plataforma
			coordY = 1225;
			tiles.add(new Tile(7450, coordY, 400, 25, "src/sprites/Tiles/plataforma.png", true, true));
			
			// Plataforma
			coordY = 1200;
			coordX = 8105;
			tiles.add(new Tile(7850, coordY, 1250, 50, "src/sprites/Tiles/suelo.png", true, false));
			for(int i=0;i<4;i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX+=50;
			}
			
			coordX = 8805;
			for(int i=0;i<4;i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX+=50;
			}
			
			
			// Plataforma
			coordY = 1200;
			tiles.add(new Tile(9400, coordY, 1500, 50, "src/sprites/Tiles/suelo.png", true, false));
			coordX = 9605;
			for(int i=0;i<4;i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX+=50;
			}
			coordX = 10505;
			for(int i=0;i<4;i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX+=50;
			}
			
			// Plataforma
			coordY = 1025;
			tiles.add(new Tile(9800, coordY, 700, 25, "src/sprites/Tiles/plataforma.png", true, true));
			bonuses.add(new Bonus(10130, coordY - 100, 2, this, player));
			
			// Plataforma
			coordY = 1200;
			tiles.add(new Tile(PixelCoord(224), coordY, 2150, 50, "src/sprites/Tiles/suelo.png", true, false));
			bonuses.add(new Bonus(PixelCoord(225), coordY - 100, 3, this, player));
			coordX = PixelCoord(228) + 5;
			coordX = PixelCoord(244) + 5;
			for(int i=0;i<5;i++) {
				EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			
			// Plataforma
			coordY = 1025;
			tiles.add(new Tile(PixelCoord(229), coordY, 350, 25, "src/sprites/Tiles/plataforma.png", true, true));
			EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(232) + 5, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			tiles.add(new Tile(PixelCoord(257), coordY, 350, 25, "src/sprites/Tiles/plataforma.png", true, true));
			EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(260), coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
						
			break;
			
		// LEVEL 3
		case 3:
			
			
			
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
			
			if(player.x >= 400 && EnemigosGenerados == 0) {
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(20), PixelCoord(22), player, 0, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 2400 && EnemigosGenerados == 1) {
				EnemigosBasicos.add(new EnemigoMovil(2900, PixelCoord(22), player, 0, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 3500 && EnemigosGenerados == 2) {
				EnemigosBasicos.add(new EnemigoMovil(4000, PixelCoord(16), player, 0, this));
				EnemigosBasicos.add(new EnemigoMovil(4200, PixelCoord(20), player, 2, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 4500 && EnemigosGenerados == 3) {
				EnemigosBasicos.add(new EnemigoMovil(5100, PixelCoord(14), player, 1, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 6000 && !bossSpawn) {
				bosses.add(new Boss1(6500, PixelCoord(16), this, player));
				bossSpawn = true;
				EnemigosGenerados++;
			}
			
			if (!bosses.isEmpty()) {
			    // Mientras el boss esté vivo, la puerta se mantiene
			    if (!tiles.contains(puertaFinal)) {
			        tiles.add(puertaFinal);
			    }
			} else {
			    // Cuando el boss muere, eliminas la puerta una sola vez
			    tiles.remove(puertaFinal);
			}
			
			break;
			
		case 2:
			
			if(player.x >= 900 && EnemigosGenerados == 0) {
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(28), PixelCoord(66), player, 0, this));
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(42), PixelCoord(66), player, 3, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 4500 && EnemigosGenerados == 1) {
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(103), PixelCoord(63), player, 1, this));
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(104), PixelCoord(63), player, 2, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 5550 && EnemigosGenerados == 2) {
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(123), PixelCoord(58), player, 3, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 6450 && EnemigosGenerados == 3) {
				bosses.add(new Boss1(PixelCoord(145), PixelCoord(52), this, player));
				EnemigosGenerados++;
			}
			
			if(player.x >= 7850 && EnemigosGenerados == 4) {
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(177), PixelCoord(22), player, 2, this));
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(178), PixelCoord(22), player, 2, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 9350 && EnemigosGenerados == 5) {
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(197), PixelCoord(22), player, 0, this));
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(198), PixelCoord(22), player, 1, this));
				EnemigosBasicos.add(new EnemigoMovil(PixelCoord(198), PixelCoord(18), player, 3, this));
				EnemigosGenerados++;
			}
			
			if(player.x >= 11300 && !bossSpawn) {
				tiles.add(new Tile(PixelCoord(224), 0, PixelCoord(1), PixelCoord(24), "src/sprites/Tiles/arena.png", true, false));
				bosses.add(new Boss2(PixelCoord(246), PixelCoord(19), this, player));
				bossSpawn = true;
				EnemigosGenerados++;
			}
			
			if (!bosses.isEmpty()) {
			    if (!tiles.contains(puertaFinal)) {
			        tiles.add(puertaFinal);
			    }
			} else {
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
