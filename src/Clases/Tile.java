package Clases;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Tile {
	public int x, y;
	private int width, height;
	private boolean solid;
	public boolean plataforma;
	private Image image;

	public Tile(int x, int y, int width, int height, String imagePath, boolean solid, boolean plataforma) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.solid = solid;
		this.plataforma = plataforma;
		
		if (imagePath != null && !imagePath.isEmpty()) {
			this.image = new ImageIcon(imagePath).getImage();
		}
	}

	public void draw(Graphics g, int cameraX, int cameraY) {
	    if (image != null) {
	        int imgW = 50;
	        int imgH = 50;
	        if(plataforma) {
	        	imgH = 25;
	        }

	        for (int i = 0; i < width; i += imgW) {
	            for (int j = 0; j < height; j += imgH) {
	                g.drawImage(image, x - cameraX + i, y - cameraY + j, imgW, imgH, null);
	            }
	        }
	    } else {
	        g.fillRect(x - cameraX, y - cameraY, width, height);
	    }
	}


	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public boolean isSolid() {
		return solid;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}