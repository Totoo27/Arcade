package Clases;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Tile {
	public int x, y;
	private int width, height;
	private boolean solid;
	private Image image;

	public Tile(int x, int y, int width, int height, String imagePath, boolean solid) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.solid = solid;

		if (imagePath != null && !imagePath.isEmpty()) {
			this.image = new ImageIcon(imagePath).getImage();
		}
	}

	public void draw(Graphics g, int cameraX, int cameraY) {
		if (image != null) {
			g.drawImage(image, x - cameraX, y - cameraY, width, height, null);
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