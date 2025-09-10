package Clases;

import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GameMain extends JFrame {

	private CardLayout cardLayout;
	private JPanel mainPanel;
	private GamePanel gamePanel;

	public static Font Pixelart; // Font

	public GameMain() {

		setTitle("Arcade");
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		// Layout para alternar pantallas
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		// Crear menú
		JPanel menuPanel = crearMenu();
		menuPanel.setLayout(null);

		// Crear menú de muerte
		JPanel deathMenuPanel = crearDeathMenu();
		deathMenuPanel.setLayout(null);

		// Crear juego
		gamePanel = new GamePanel();

		mainPanel.add(menuPanel, "Menu");
		mainPanel.add(gamePanel, "Juego");
		mainPanel.add(deathMenuPanel, "DeathMenu");

		add(mainPanel);
		setVisible(true);
	}

	private JPanel crearMenu() {

		// Cargar imagen de fondo
		ImageIcon fondoIcon = new ImageIcon("src/resources/portada.png");
		Image fondo = fondoIcon.getImage();

		// Crear panel personalizado
		JPanel menuPanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

				// Oscurecer fondo con un rectángulo negro semitransparente
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setColor(new Color(0, 0, 0));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
			}
		};

		menuPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 0, 15, 0);

		JLabel titulo = new JLabel("The Pirate Odyssey");
		titulo.setFont(Pixelart.deriveFont(100f));
		titulo.setForeground(new Color(0xFFC000));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridy = 0;
		menuPanel.add(titulo, gbc);

		// Botón Jugar
		JButton startButton = crearBoton("Jugar", 250, 75);
		gbc.gridy = 1;
		menuPanel.add(startButton, gbc);

		// Botón niveles
		JButton nivelButton = crearBoton("Niveles", 250, 75);
		gbc.gridy = 2;
		menuPanel.add(nivelButton, gbc);

		// Botón Salir
		JButton exitButton = crearBoton("Salir", 250, 75);
		gbc.gridy = 3;
		menuPanel.add(exitButton, gbc);

		// Eventos de botones
		startButton.addActionListener(e -> {
			cardLayout.show(mainPanel, "Juego");
			gamePanel.requestFocus();
			gamePanel.requestFocusInWindow();
			gamePanel.iniciarJuego();
		});

		exitButton.addActionListener(e -> System.exit(0)); // Salir

		titulo.setBounds(-110, 150, 1200, 100);
		startButton.setBounds(350, 300, 250, 80); // x, y, ancho, alto
		nivelButton.setBounds(350, 400, 250, 80);
		exitButton.setBounds(350, 500, 250, 80);

		return menuPanel;
	}

	private JPanel crearDeathMenu() {
		// Cargar imagen de fondo
		ImageIcon fondoIcon = new ImageIcon("src/resources/portada.png");
		Image fondo = fondoIcon.getImage();

		// Crear panel personalizado
		JPanel deathMenuPanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

				// Oscurecer fondo con un rectángulo negro semitransparente
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setColor(new Color(0, 0, 0, 150));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
			}
		};

		deathMenuPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 0, 15, 0);

		JLabel titulo = new JLabel("Game Over");
		titulo.setFont(Pixelart.deriveFont(100f));
		titulo.setForeground(new Color(0xFFC000));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridy = 0;
		deathMenuPanel.add(titulo, gbc);

		// Botón Retry
		JButton retryButton = crearBoton("Retry", 250, 75);
		gbc.gridy = 1;
		deathMenuPanel.add(retryButton, gbc);

		// Botón Exit
		JButton exitButton = crearBoton("Exit", 250, 75);
		gbc.gridy = 2;
		deathMenuPanel.add(exitButton, gbc);

		// Eventos de botones
		retryButton.addActionListener(e -> {
			cardLayout.show(mainPanel, "Juego");
			gamePanel.requestFocus();
			gamePanel.requestFocusInWindow();
			gamePanel.iniciarJuego();
		});

		exitButton.addActionListener(e -> System.exit(0)); // Salir

		titulo.setBounds(-110, 150, 1200, 100);
		retryButton.setBounds(350, 300, 250, 80);
		exitButton.setBounds(350, 400, 250, 80);

		return deathMenuPanel;
	}

	public void mostrarMenuMuerte() {
		cardLayout.show(mainPanel, "DeathMenu");
	}

	public static JButton crearBoton(String texto, int ancho, int alto) {
		JButton boton = new JButton(texto) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();

				// Cambia la transparencia según estado
				float alpha = 0.45f; // normal
				if (getModel().isPressed())
					alpha = 0.9f;
				else if (getModel().isRollover())
					alpha = 0.7f;

				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
				g2.setColor(new Color(0, 0, 0)); // fondo negro
				if (getModel().isPressed()) {
					g2.setColor(new Color(0xBBB000));
				}
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

				g2.dispose();
				super.paintComponent(g);
			}
		};

		boton.setFont(Pixelart.deriveFont(35f));
		boton.setForeground(new Color(0xFFFFFF));
		boton.setOpaque(false);
		boton.setContentAreaFilled(false);
		boton.setBorder(BorderFactory.createLineBorder(new Color(0xFFFFFF), 5));
		boton.setFocusPainted(false);
		boton.setPreferredSize(new Dimension(ancho, alto));
		return boton;
	}

	public static void main(String[] args) {

		try {
			File fuenteArchivo = new File("src/resources/Pixellari.ttf");

			// Crear la fuente
			Pixelart = Font.createFont(Font.TRUETYPE_FONT, fuenteArchivo);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Pixelart);

			Pixelart = Pixelart.deriveFont(24f); // Tamaño default

		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> new GameMain());
	}

}
