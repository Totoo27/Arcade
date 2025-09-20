package Clases;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Bonus {
	
	public int x,y;
	public int width, height;
	public int tipo_bonus; // 0 = Moneda, 1 = Botiquin
	
	// Funciones externas
	private Player player;
	private GamePanel panel;
	
	// Animacion
	private boolean subiendo = true;
	private int speed = 1;
	private int startY;
	
	// Sprite
	public Image sprite;
	
	public Bonus(int x, int y, int tipo_bonus, GamePanel panel, Player player){
		this.x = x;
		this.y = y;
		this.startY = y;
		
		this.tipo_bonus = tipo_bonus;
		
		this.panel = panel;
		this.player = player;
		
		switch(tipo_bonus) {
		
		case 0: // Monedas
			this.width = 37;
			this.height = 30;
			sprite = new ImageIcon("src/sprites/Bonus/coin.png").getImage();
			
			break;
			
		case 1: // Botiquin
			this.width = 26;
			this.height = 40;
			sprite = new ImageIcon("src/sprites/Bonus/vendas.png").getImage();
			break;
			
		case 2: // Frenesi
			this.width = 62;
			this.height = 54;
			sprite = new ImageIcon("src/sprites/Bonus/frenesi.png").getImage();
			break;
			
		case 3: // CheckPoint
			this.width = 50;
			this.height = 50;
			sprite = new ImageIcon("src/sprites/Bonus/checkpoint.png").getImage();
			break;
			
		case 4: // Llave Final
			this.width = 20;
			this.height = 50;
			sprite = new ImageIcon("src/sprites/Bonus/llave.png").getImage();
			break;
		}
	}
	
	public void move() {
		if (subiendo) {
	        y -= speed;
	        if (y <= startY - 10) {
	            subiendo = false;
	        }
	    } else {
	        y += speed;
	        if (y >= startY + 10) {
	            subiendo = true;
	        }
	    }
	}
	
	public void darBonus() {
		switch(tipo_bonus) {
		case 0:
			panel.MonedasJug++;
			GameMain.reproducirSonido("src/Sonidos/moneda.wav");
			break;
			
		case 1:
			if(player.vida < player.max_vida) {
				player.vida++;
			}
			GameMain.reproducirSonido("src/Sonidos/vendaje.wav");
			break;
			
		case 2:
			player.frenesi();
			GameMain.reproducirSonido("src/Sonidos/frenesi.wav");
			break;
			
		case 3:
			panel.checkpoint(x + width /2 - player.width / 2, y);
			GameMain.reproducirSonido("src/Sonidos/checkpoint.wav");
			break;
			
		case 4:
			panel.SpawnEnemigos[0] = true;
			panel.IntroTime = System.currentTimeMillis();
			GameMain.reproducirSonido("src/Sonidos/checkpoint.wav");
			break;
		}
	}
	
	public Image getSprite(){
		return sprite;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

}
