package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // Timers
    private Timer timer;

    // Hitbox
    private boolean showHitboxes = true;
    
    // Gravedad
    public int gravedad = 1;

    // Jugador
    private Player player;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        // Instanciar Jugador
        player = new Player(200, 200);
        
        // El GamePanel es el que escucha los eventos del teclado
        addKeyListener(this); 

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("H"), "toggleHitboxes");
        getActionMap().put("toggleHitboxes", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHitboxes = !showHitboxes; // alternar hitboxes
                repaint(); // refrescar pantalla
            }
        });

        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(player != null) {
            g.setColor(new Color(0, 255, 255, 125));
            g.fillRect(player.x, player.y, player.width, player.height);
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Movimiento Jugador
        if(player != null) {
            player.move(this.getWidth(), this.getHeight(), gravedad);
        }
    }

    public void iniciarJuego() {
        if (timer == null) {
            timer = new Timer(16, this); // Masomenos 60fps
            requestFocusInWindow();
        }
        timer.start();
    }
    
    // --- Implementación del KeyListener en GamePanel ---
    // El GamePanel delega los eventos de teclado al objeto Player
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.rightPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) player.spacePressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) player.leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.rightPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) player.spacePressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
