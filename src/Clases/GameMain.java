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
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GameMain extends JFrame {

	private CardLayout cardLayout;
	private JPanel mainPanel;
	private GamePanel gamePanel;
	
	private BufferedImage ultimoFrame;

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

		// Crear menu pausa
		JPanel pauseMenuPanel = crearMenuPausa();
		pauseMenuPanel.setLayout(null);
		
		JPanel levelMenuPanel = crearNivelesMenu();
		levelMenuPanel.setLayout(null);
		
		// Crear juego
		gamePanel = new GamePanel();
		
		// Vincular tecla Escape para pausar el juego
		gamePanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pausa");
		gamePanel.getActionMap().put("pausa", new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        gamePanel.pausarJuego();
		        ultimoFrame = gamePanel.capturarFrame();
		        cardLayout.show(mainPanel, "Pausa");
		    }
		});


		mainPanel.add(menuPanel, "Menu");
		mainPanel.add(gamePanel, "Juego");
		mainPanel.add(pauseMenuPanel, "Pausa");
		mainPanel.add(deathMenuPanel, "DeathMenu");
		mainPanel.add(crearNivelesMenu(), "Niveles");

		

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
				g2.dispose();
			}
		};

		menuPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 0, 15, 0);

		// Botón Jugar
		JButton startButton = crearBoton("Jugar", 250, 75);
		gbc.gridy = 0;
		menuPanel.add(startButton, gbc);

		// Botón niveles
		JButton nivelButton = crearBoton("Niveles", 250, 75);
		gbc.gridy = 1;
		menuPanel.add(nivelButton, gbc);

		// Botón Salir
		JButton exitButton = crearBoton("Salir", 250, 75);
		gbc.gridy = 2;
		menuPanel.add(exitButton, gbc);

		// Eventos de botones
		startButton.addActionListener(e -> {
			
			cardLayout.show(mainPanel, "Juego");
			gamePanel.requestFocus();
			gamePanel.requestFocusInWindow();
			gamePanel.hayCheckpoint = false;
			gamePanel.nivel = 1;
			gamePanel.iniciarJuego();
		});

		exitButton.addActionListener(e -> System.exit(0)); // Salir
		
		nivelButton.addActionListener(e -> { // ir hacia niveles
		    cardLayout.show(mainPanel, "Niveles");
		});

		

		startButton.setBounds(180, 330, 250, 80); // x, y, ancho, alto
		nivelButton.setBounds(180, 430, 250, 80);
		exitButton.setBounds(180, 530, 250, 80);

		return menuPanel;
	}
	
	private JPanel crearMenuPausa() {

		// Cargar imagen de fondo
		if(gamePanel != null) {
			ultimoFrame = gamePanel.capturarFrame();
		}
		Musica.reproducirMusica("src/Canciones/Menu.wav");

		// Crear panel personalizado
		JPanel menuPanel = new JPanel() {
		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        if (ultimoFrame != null) {
		            g.drawImage(ultimoFrame, 0, 0, getWidth(), getHeight(), this);
		        } else {
		            g.setColor(Color.BLACK);
		            g.fillRect(0, 0, getWidth(), getHeight());
		        }

		        // Oscurecer un poco para resaltar los botones
		        Graphics2D g2 = (Graphics2D) g.create();
		        g2.setColor(new Color(0, 0, 0, 150));
		        g2.fillRect(0, 0, getWidth(), getHeight());
		        g2.dispose();
		    }
		};

		menuPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 0, 15, 0);

		JLabel titulo = new JLabel("Pausa");
		titulo.setFont(Pixelart.deriveFont(100f));
		titulo.setForeground(new Color(0xFFC000));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridy = 0;
		menuPanel.add(titulo, gbc);

		// Botón Jugar
		JButton returnButton = crearBoton("Reaunudar", 250, 75);
		gbc.gridy = 1;
		menuPanel.add(returnButton, gbc);

		// Reiniciar Nivel
		JButton restartButton = crearBoton("Reiniciar", 250, 75);
		gbc.gridy = 2;
		menuPanel.add(restartButton, gbc);
		
		// Cargar Ultimo Checkpoint
		JButton checkpointButton = crearBoton("CheckPoint", 250, 75);
		gbc.gridy = 3;
		menuPanel.add(checkpointButton, gbc);
		
		// Botón niveles
		JButton MenuButton = crearBoton("Menu", 250, 75);
		gbc.gridy = 4;
		menuPanel.add(MenuButton, gbc);

		returnButton.addActionListener(e -> {
		    // Volver al juego
			gamePanel.enPausa = false;
		    cardLayout.show(mainPanel, "Juego");
		    gamePanel.requestFocus();
		    gamePanel.requestFocusInWindow();
		});
		
		restartButton.addActionListener(e -> {
			cardLayout.show(mainPanel, "Juego");
			gamePanel.requestFocus();
			gamePanel.requestFocusInWindow();
			gamePanel.hayCheckpoint = false;
			gamePanel.iniciarJuego();
		});
		
		checkpointButton.addActionListener(e -> {
			cardLayout.show(mainPanel, "Juego");
			gamePanel.requestFocus();
			gamePanel.requestFocusInWindow();
			gamePanel.iniciarJuego();
		});

		MenuButton.addActionListener(e -> {
		    // Volver al menú principal
		    cardLayout.show(mainPanel, "Menu");
		    Musica.reproducirMusica("src/Canciones/Menu.wav");
		});

		titulo.setBounds(-110, 100, 1200, 100);
		returnButton.setBounds(365, 250, 250, 80); // x, y, ancho, alto
		restartButton.setBounds(365, 350, 250, 80);
		checkpointButton.setBounds(365, 450, 250, 80);
		MenuButton.setBounds(365, 550, 250, 80);

		return menuPanel;
	}
	
	private JPanel crearNivelesMenu() {
	    // Panel con fondo personalizado (el mapa)
	    JPanel nivelesMenuPanel = new JPanel() {
	    	
	    	
	        private Image fondo;

	        {
	            // Cargar la imagen de fondo del mapa (cambiá la ruta a la tuya)
	            fondo = new ImageIcon("src/resources/fondoMenuNiveles.png").getImage();
	            setLayout(null); // Layout absoluto para ubicar botones manualmente
	        }

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            if (fondo != null) {
	                // Dibuja el mapa ocupando todo el panel
	                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
	            }
	        }
	    };
	    
	    // === Botón Volver al menú ===
	    JButton volverButton = GameMain.crearBoton("Volver", 300, 100);
	    volverButton.setBounds(50, 500, 300, 100);
	    volverButton.setFont(Pixelart.deriveFont(50f));
	    volverButton.addActionListener(e -> {
	        cardLayout.show(mainPanel, "Menu");
	        Musica.reproducirMusica("src/Canciones/Menu.wav");
	    });

	    // === Botón Isla 1 ===
	    JButton isla1 =  crearBoton("1", 80, 80);
	    isla1.setBounds(300, 50, 80, 80); // x, y, ancho, alto (posición sobre el mapa)
	    isla1.setContentAreaFilled(false); // quita el relleno del botón
	    isla1.setFocusPainted(false);      // quita el recuadro de enfoque
	    isla1.addActionListener(e -> {
	        cardLayout.show(mainPanel, "Juego");  // cambia a la pantalla de juego
	        gamePanel.hayCheckpoint = false;
	        gamePanel.nivel = 1;
	        gamePanel.iniciarJuego();            // inicia el nivel 1
		    gamePanel.requestFocusInWindow();
	        
	    });

	    // === Botón Isla 2 ===
	    JButton isla2 =  crearBoton("2", 80, 80);
	    isla2.setBounds(730, 120, 80, 80);
	    isla2.setContentAreaFilled(false);
	    // isla2.setBorderPainted(false);
	    isla2.setFocusPainted(false);
	    isla2.addActionListener(e -> {
	        cardLayout.show(mainPanel, "Juego");
	        gamePanel.hayCheckpoint = false;
	        gamePanel.nivel = 2;
	        gamePanel.iniciarJuego();
		    gamePanel.requestFocusInWindow();
	    });

	    // === Botón Isla 3 ===
	    JButton isla3 =  crearBoton("3", 80, 80);
	    isla3.setBounds(320, 300, 80, 80);
	    isla3.setContentAreaFilled(false);
	    isla3.setFocusPainted(false);
	    isla3.addActionListener(e -> {
	        cardLayout.show(mainPanel, "Juego");
	        gamePanel.hayCheckpoint = false;
	        gamePanel.nivel = 3;
	        gamePanel.iniciarJuego();
		    gamePanel.requestFocusInWindow();
	    });

	    // === Botón Isla 4 ===
	    JButton isla4 =  crearBoton("4", 80, 80);
	    isla4.setBounds(740, 400, 80, 80);
	    isla4.setContentAreaFilled(false);
	    isla4.setFocusPainted(false);
	    isla4.addActionListener(e -> {
	        cardLayout.show(mainPanel, "Juego");
	        gamePanel.hayCheckpoint = false;
	        gamePanel.nivel = 4;
	        gamePanel.iniciarJuego();
		    gamePanel.requestFocusInWindow();
	    });

	    // Agregar botones al panel (encima del mapa)
	    nivelesMenuPanel.add(isla1);
	    nivelesMenuPanel.add(isla2);
	    nivelesMenuPanel.add(isla3);
	    nivelesMenuPanel.add(isla4);
	    nivelesMenuPanel.add(volverButton);

	    return nivelesMenuPanel;
	}


	private JPanel crearDeathMenu() {
			
		if(gamePanel != null) {
			ultimoFrame = gamePanel.capturarFrame();
		}

		// Crear panel personalizado
		JPanel deathMenuPanel = new JPanel() {
			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        if (ultimoFrame != null) {
		            g.drawImage(ultimoFrame, 0, 0, getWidth(), getHeight(), this);
		        } else {
		            g.setColor(Color.BLACK);
		            g.fillRect(0, 0, getWidth(), getHeight());
		        }

		        // Oscurecer un poco para resaltar los botones
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
		JButton retryButton = crearBoton("Reintentar", 250, 75);
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

		exitButton.addActionListener(e -> {
			// Volver al menú principal
		    cardLayout.show(mainPanel, "Menu");
		    Musica.reproducirMusica("src/Canciones/Menu.wav");
		});

		titulo.setBounds(-110, 150, 1200, 100);
		retryButton.setBounds(350, 300, 250, 80);
		exitButton.setBounds(350, 400, 250, 80);

		return deathMenuPanel;
	}

	public void mostrarMenuMuerte() {
	    ultimoFrame = gamePanel.capturarFrame();
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
	
	public static void reproducirSonido(String rutaArchivo) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(rutaArchivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error al reproducir sonido: " + e.getMessage());
        }
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
