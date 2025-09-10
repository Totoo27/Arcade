package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

	// Timers
	private Timer timer;

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
	public int BalaJugWidth = 10;

	// Hitbox
	private boolean showHitboxes = true;

	// Gravedad
	public int gravedad = 1;

	// Enemigos
	private ArrayList<Enemigo> EnemigosBasicos = new ArrayList<>();

	// Jugador
	private Player player;
	private int MonedasJug = 0;

	public GamePanel() {
		setBackground(new Color(50, 50, 50));
		setFocusable(true);

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

		// Instanciar Jugador
		player = new Player(200, 200, this);
		addKeyListener(this);

		EnemigosBasicos.add(new Enemigo(400, 400, player));

		// Toggler de hitboxes
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("H"), "toggleHitboxes");
		getActionMap().put("toggleHitboxes", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHitboxes = !showHitboxes; // alternar hitboxes
				repaint();
			}
		});

		tiles.add(new Tile(400, 550, 200, 50, "src/sprites/suelo.png", true));

		this.setLayout(null);
		this.add(contadorMonedas);
		this.add(tiempo);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

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
			tile.draw(g);
		}

		// Dibujar Monedita

		g.drawImage(new ImageIcon("src/sprites/coin.png").getImage(), 60, 80, 37, 30, this);

		// --------------- HITBOXES

		if (showHitboxes) {
			for (Balas bala : balas) {
				g.setColor(Color.red);
				g.fillRect(bala.x, bala.y, bala.width, bala.height);
			}

			for (Enemigo enemigo : EnemigosBasicos) {
				g.setColor(new Color(255, 0, 0, 125));
				g.fillRect(enemigo.x, enemigo.y, enemigo.width, enemigo.height);
			}

			if (player != null) {
				g.setColor(new Color(0, 255, 255, 125));
				g.fillRect(player.x, player.y, player.width, player.height);
			}

		}
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

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

		// Movimiento Jugador
		if (player != null) {
			player.move(this.getWidth(), this.getHeight(), gravedad, tiles);
		}

		// Movimiento Balas
		for (Balas bala : balas) {
			bala.move();
		}

		// Movimiento y Colision Enemigos
		for (Enemigo enemigo : EnemigosBasicos) {
			enemigo.movimiento(getHeight(), gravedad);

			// Colision con jugador
			if (enemigo.getBounds().intersects(player.getBounds())) {
				player.RecibirHit();
			}
		}

		// Enemigos Recibiendo Golpe

		for (int i = 0; i < balas.size(); i++) {
			Balas bala = balas.get(i);
			boolean impactada = false;

			for (Enemigo enemigo : EnemigosBasicos) {

				if (bala.getBounds().intersects(enemigo.getBounds())) {
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

		}

		// Muerte Enemigos

		for (int i = 0; i < EnemigosBasicos.size(); i++) {
			Enemigo enemigo = EnemigosBasicos.get(i);

			if (enemigo.vida <= 0) {
				MonedasJug += enemigo.monedas;
				EnemigosBasicos.remove(i);
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
		segundos = 0;
		minutos = 0;
		ultimoSegundo = System.currentTimeMillis();
		MonedasJug = 0;
		contadorMonedas.setText("" + MonedasJug);
		balas.clear();
		EnemigosBasicos.clear();
		EnemigosBasicos.add(new Enemigo(400, 400, player)); // Reiniciar enemigos
		player.x = 200; // Coordenada inicial X
		player.y = 200; // Coordenada inicial Y
		player.vida = player.max_vida; // Restaurar la vida del jugador

		timer.start();
	}

	public void disparoJugador(int x, int y, int direccion) {
		int velocidad = 15;
		int height = 10;

		balas.add(new Balas(x, y, BalaJugWidth, height, velocidad, direccion));
	}

	// --- Implementación del KeyListener en GamePanel ---
	// El GamePanel delega los eventos de teclado al objeto Player
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			player.leftPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			player.rightPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			player.spacePressed = true;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			player.disparo = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			player.leftPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			player.rightPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			player.spacePressed = false;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			player.disparo = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
