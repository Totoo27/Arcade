package Clases;

public class Niveles {
	
	private int coordX;
	private int coordY;
	
	public Niveles() {}
	
	public int PixelCoord(int pixel) { // Pasaje de Pixeles a coordenadas reales
		return pixel * 50;
	}
	
	public void GeneracionNivel(int nivel, Player player, GamePanel panel) {
		
		switch(nivel) {
		
		// LEVEL 1
		case 1:
			Musica.reproducirMusica("src/Canciones/musica1erMundo.wav");
			panel.VelFondo = 10;
			
			// Parámetros Rangos
			panel.P_Monedas = 52;
			panel.tiempoObjetivo = 42;
			
			// Límites Nivel
			panel.FinalX = PixelCoord(144);
			panel.FinalY = PixelCoord(30);
			
			panel.puertaFinal = new Tile(PixelCoord(143), 0, 50, panel.FinalY, "src/sprites/Tiles/madera.png", true, false);
			panel.tiles.add(panel.puertaFinal);
			
			// Plataforma
			coordY = 1200;
			panel.tiles.add(new Tile(0, coordY, PixelCoord(8), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(0, coordY + 50, PixelCoord(8), panel.FinalY - 1250, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			coordY = 1200;
			panel.EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(14), coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.tiles.add(new Tile(PixelCoord(14), coordY, PixelCoord(12), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(14), coordY + 50, PixelCoord(12), panel.FinalY - 1250, "src/sprites/Tiles/arena.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(17), coordY - 175, PixelCoord(6), 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.EnemigosBasicos.add(new EnemigoEstatico(1260, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.EnemigosBasicos.add(new EnemigoEstatico(1210, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 800;
			for(int i=0;i<2;i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 200;
			}
			panel.bonuses.add(new Bonus(975, coordY - 235, 0, panel, player));
			
			// Plataforma
			coordY = 875;
			panel.tiles.add(new Tile(1300, coordY, 600, 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(1300, coordY + 50, 600, panel.FinalY - 925, "src/sprites/Tiles/arena.png", true, false));
			panel.tiles.add(new Tile(1450, coordY - 175, 300, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 1505;
			for(int i = 0; i<4; i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			coordX = 1485;
			for(int i=0;i<2;i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 235, 0, panel, player));
				coordX += 200;
			}
			
			// Plataforma
			panel.tiles.add(new Tile(2200, 1050, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 2235;
			for(int i=0;i<2;i++) {
				panel.bonuses.add(new Bonus(coordX, 990, 0, panel, player));
				coordX += 150;
			}
			
			// Plataforma
			coordY = 1250;
			panel.tiles.add(new Tile(2600, coordY, 400, 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.EnemigosBasicos.add(new EnemigoEstatico(2600, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.EnemigosBasicos.add(new EnemigoEstatico(2645, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.EnemigosBasicos.add(new EnemigoEstatico(2960, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.tiles.add(new Tile(2600, coordY + 50, 400, panel.FinalY - 1300, "src/sprites/Tiles/arena.png", true, false));
			coordX = 2735;
			for(int i=0;i<3;i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 75;
			}
			
			// Plataforma
			panel.tiles.add(new Tile(3300, 1250, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordY = 1250;
			for(int i=0;i<3;i++) {
				panel.bonuses.add(new Bonus(3420, coordY - 60, 0, panel, player));
				coordY -= 120;
			}
			
			// Plataforma
			coordY = 1100;
			panel.tiles.add(new Tile(3700, coordY, 550, 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(3700, coordY + 50, 550, panel.FinalY - 1150, "src/sprites/Tiles/arena.png", true, false));
			panel.tiles.add(new Tile(3850, coordY - 175, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 3855;
			for(int i = 0; i<3; i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, 885, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			coordX = 3850;
			for(int i=0;i<3;i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 100;
			}
			
			// Plataforma
			coordY = 850;
			panel.tiles.add(new Tile(4500, coordY, 800, 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(4500, coordY + 50, 800, panel.FinalY - 900, "src/sprites/Tiles/arena.png", true, false));
			coordX = 4905;
			for(int i = 0; i<4; i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			
			// Plataforma
			coordY = PixelCoord(20);
			panel.tiles.add(new Tile(PixelCoord(114), coordY, PixelCoord(30), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(114), coordY + 50, PixelCoord(30), panel.FinalY - 1050, "src/sprites/Tiles/arena.png", true, false));
			panel.EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(114), coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.bonuses.add(new Bonus(PixelCoord(116), PixelCoord(20) - 100, 3, panel, player));
			coordX = 6355;
			for(int i = 0; i<4; i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			panel.tiles.add(new Tile(6000, coordY - 175, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.tiles.add(new Tile(6650, coordY - 175, 250, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.bonuses.add(new Bonus(6425, coordY - 235, 2, panel, player));
			
			
			break;
			
			
		// LEVEL 2
		case 2:
			Musica.reproducirMusica("src/Canciones/musica1erMundo.wav");
			
			// Parámetros Rangos
			panel.P_Monedas = 115;
			panel.tiempoObjetivo = 115;
			
			// Limites Nivel
			panel.VelFondo = 15;
			panel.FinalX = PixelCoord(267);
			panel.FinalY = PixelCoord(81);
			panel.puertaFinal = new Tile(PixelCoord(266), 0, 50, PixelCoord(24), "src/sprites/Tiles/madera.png", true, false);
			panel.tiles.add(panel.puertaFinal);
			
			// Spawn Jugador
			player.y = PixelCoord(66);
			
			// Plataforma
			coordY = PixelCoord(68);
			panel.tiles.add(new Tile(0, coordY, PixelCoord(15), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(0, coordY + 50, PixelCoord(15), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));

			// Plataforma
			panel.tiles.add(new Tile(PixelCoord(22), coordY, PixelCoord(9), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(22), coordY + 50, PixelCoord(9), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			panel.tiles.add(new Tile(PixelCoord(35), coordY, PixelCoord(9), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(35), coordY + 50, PixelCoord(9), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			coordY = PixelCoord(64) + 25;
			coordX = PixelCoord(27);
			panel.tiles.add(new Tile(PixelCoord(26), coordY, PixelCoord(14), 25, "src/sprites/Tiles/plataforma.png", true, true));
			for(int i=0; i<4; i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 200;
			}
			
			// Plataforma
			coordY = 3050;
			panel.tiles.add(new Tile(1500, coordY, 300, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.bonuses.add(new Bonus(1630, coordY - 210, 0, panel, player));
			
			// Plataforma
			coordY = 3400;
			panel.tiles.add(new Tile(2450, coordY, 550, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.EnemigosBasicos.add(new EnemigoEstatico(2450, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.EnemigosBasicos.add(new EnemigoEstatico(2960, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 2500;
			for(int i=0; i<3; i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 200;
			}
			
			// Plataforma
			panel.tiles.add(new Tile(3200, coordY, 550, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.EnemigosBasicos.add(new EnemigoEstatico(3200, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.EnemigosBasicos.add(new EnemigoEstatico(3710, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 3250;
			for(int i=0; i<3; i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 200;
			}
			
			// Plataforma
			panel.tiles.add(new Tile(3900, coordY, 550, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.EnemigosBasicos.add(new EnemigoEstatico(3900, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.EnemigosBasicos.add(new EnemigoEstatico(4410, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = 3950;
			for(int i=0; i<3; i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 200;
			}
			
			// Plataforma
			coordY = 3250;
			panel.tiles.add(new Tile(4650, coordY, 700, 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(4650, coordY + 50, 700, panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			coordY = 3075;
			panel.tiles.add(new Tile(5500, coordY, 800, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 5600;
			for(int i=0; i<4; i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 200;
			}
			
			// Plataforma
			coordY = 2900;
			panel.tiles.add(new Tile(5500, coordY, 800, 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordX = 5600;
			for(int i=0; i<4; i++) {
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				coordX += 200;
			}
			
			// Plataforma
			coordY = 2750;
			panel.tiles.add(new Tile(6550, coordY, 1300, 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(6550, coordY + 50, 1300, panel.FinalY - coordY - 50, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataformas
			coordY = 2600;
			coordX = 7750;
			for(int i=0;i<5;i++) {
				panel.tiles.add(new Tile(7450, coordY, 400, 25, "src/sprites/Tiles/plataforma.png", true, true));
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				panel.EnemigosBasicos.add(new EnemigoEstatico(7650, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				panel.EnemigosBasicos.add(new EnemigoEstatico(7700, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordY -= 300;
			}
			
			// Plataformas
			coordY = 2450;
			coordX = 6960;
			for(int i=0;i<4;i++) {
				panel.tiles.add(new Tile(6900, coordY, 400, 25, "src/sprites/Tiles/plataforma.png", true, true));
				panel.bonuses.add(new Bonus(coordX, coordY - 60, 0, panel, player));
				panel.EnemigosBasicos.add(new EnemigoEstatico(7020, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				panel.EnemigosBasicos.add(new EnemigoEstatico(7070, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordY -= 300;
			}
			
			// Plataforma
			coordY = 1225;
			panel.tiles.add(new Tile(7450, coordY, 400, 25, "src/sprites/Tiles/plataforma.png", true, true));
			
			// Plataforma
			coordY = 1200;
			coordX = 8105;
			panel.tiles.add(new Tile(7850, coordY, 1250, 50, "src/sprites/Tiles/suelo.png", true, false));
			for(int i=0;i<4;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX+=50;
			}
			
			coordX = 8805;
			for(int i=0;i<4;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX+=50;
			}
			
			
			// Plataforma
			coordY = 1200;
			panel.tiles.add(new Tile(9400, coordY, 1500, 50, "src/sprites/Tiles/suelo.png", true, false));
			coordX = 9605;
			for(int i=0;i<4;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX+=50;
			}
			coordX = 10505;
			for(int i=0;i<4;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX+=50;
			}
			
			// Plataforma
			coordY = 1025;
			panel.tiles.add(new Tile(9800, coordY, 700, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.bonuses.add(new Bonus(10130, coordY - 100, 2, panel, player));
			
			// Plataforma
			coordY = 1200;
			panel.tiles.add(new Tile(PixelCoord(224), coordY, 2150, 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.bonuses.add(new Bonus(PixelCoord(225), coordY - 100, 3, panel, player));
			coordX = PixelCoord(228) + 5;
			coordX = PixelCoord(244) + 5;
			for(int i=0;i<5;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			
			// Plataforma
			coordY = 1025;
			panel.tiles.add(new Tile(PixelCoord(229), coordY, 350, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(232) + 5, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			panel.tiles.add(new Tile(PixelCoord(257), coordY, 350, 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(260), coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
						
			break;
			
		// LEVEL 3
		case 3:
			Musica.reproducirMusica("src/Canciones/musica1erMundo.wav");
			
			
			// Limites Nivel
			panel.VelFondo = 15;
			panel.FinalX = PixelCoord(255);
			panel.FinalY = PixelCoord(56);
			panel.puertaFinal = new Tile(PixelCoord(254), 0, 50, PixelCoord(16), "src/sprites/Tiles/madera.png", true, false);
			panel.tiles.add(panel.puertaFinal);
			
			// Parámetros Rangos
			panel.P_Monedas = 229;
			panel.tiempoObjetivo = 190;
			
			// Spawn Jug
			player.y = PixelCoord(18);
			player.x = PixelCoord(2);
			
			// Plataforma
			coordY = PixelCoord(20);
			panel.tiles.add(new Tile(0, coordY, PixelCoord(11), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(0, coordY + 50, PixelCoord(11), panel.FinalY - coordY - 50, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataforma
			coordY = PixelCoord(24);
			panel.tiles.add(new Tile(PixelCoord(19), coordY, PixelCoord(13), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(19), coordY + 50, PixelCoord(13), PixelCoord(10), "src/sprites/Tiles/arena.png", true, false));
			coordX = PixelCoord(30) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			
			coordY = PixelCoord(39);
			panel.tiles.add(new Tile(PixelCoord(19), coordY, PixelCoord(13), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(19), coordY + 50, PixelCoord(13), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			panel.EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(31) + 5, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			
			// Plataforma
			coordY = PixelCoord(31);
			panel.tiles.add(new Tile(PixelCoord(32), coordY, PixelCoord(6), 50, "src/sprites/Tiles/madera.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(56), coordY, PixelCoord(5), 50, "src/sprites/Tiles/madera.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(38), coordY, PixelCoord(18), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(38), coordY + 50, PixelCoord(18), PixelCoord(8), "src/sprites/Tiles/arena.png", true, false));
			panel.EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(58) + 5, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = PixelCoord(64) + 5;
			for(int i=0;i<3;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			coordX = PixelCoord(85) + 5;
			for(int i=0;i<3;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			coordX = PixelCoord(43) + 5;
			for(int i=0;i<8;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			coordY = PixelCoord(24);
			panel.tiles.add(new Tile(PixelCoord(41), coordY, PixelCoord(12), 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordY = PixelCoord(28) - 25;
			panel.tiles.add(new Tile(PixelCoord(41), coordY, PixelCoord(12), 25, "src/sprites/Tiles/plataforma.png", true, true));
			
			coordY = PixelCoord(44);
			panel.tiles.add(new Tile(PixelCoord(38), coordY, PixelCoord(18), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(38), coordY + 50, PixelCoord(18), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			coordX = PixelCoord(54) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += 50;
			}
			
			
			// Plataforma
			coordY = PixelCoord(48);
			panel.tiles.add(new Tile(PixelCoord(56), coordY, PixelCoord(5), 50, "src/sprites/Tiles/madera.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(61), coordY, PixelCoord(30), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(61), coordY + 50, PixelCoord(30),  panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(90), PixelCoord(43), 50, 250, "src/sprites/Tiles/madera.png", true, false));
			
			coordY = PixelCoord(45);
			panel.tiles.add(new Tile(PixelCoord(64), coordY, PixelCoord(5), 25, "src/sprites/Tiles/plataforma.png", true, true));
			panel.tiles.add(new Tile(PixelCoord(82), coordY, PixelCoord(5), 25, "src/sprites/Tiles/plataforma.png", true, true));

			coordY = PixelCoord(31);
			panel.tiles.add(new Tile(PixelCoord(61), coordY, PixelCoord(30), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(61), coordY + 50, PixelCoord(30), PixelCoord(8), "src/sprites/Tiles/arena.png", true, false));
			
			// Plataformas
			panel.tiles.add(new Tile(PixelCoord(95), coordY, PixelCoord(8), 25, "src/sprites/Tiles/plataforma.png", true, true));
			
			coordY = PixelCoord(34);
			panel.tiles.add(new Tile(PixelCoord(108), coordY, PixelCoord(8), 25, "src/sprites/Tiles/plataforma.png", true, true));
			
			coordY = PixelCoord(45);
			panel.tiles.add(new Tile(PixelCoord(96), coordY, PixelCoord(8), 25, "src/sprites/Tiles/plataforma.png", true, true));
			coordY = PixelCoord(43);
			panel.tiles.add(new Tile(PixelCoord(109), coordY, PixelCoord(8), 25, "src/sprites/Tiles/plataforma.png", true, true));
			
			// Plataforma
			coordY = PixelCoord(41);
			panel.tiles.add(new Tile(PixelCoord(122), coordY, PixelCoord(23), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(122), coordY + 50, PixelCoord(23), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			coordX = PixelCoord(124) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(1);
			}
			coordX = PixelCoord(142) + 5;
			for(int i=0;i<3;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(1);
			}
			coordY = PixelCoord(32);
			panel.tiles.add(new Tile(PixelCoord(145), coordY, PixelCoord(10), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(145), coordY + 50, PixelCoord(10), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			coordY = PixelCoord(32) + 25;
			panel.tiles.add(new Tile(PixelCoord(139), coordY - 25, PixelCoord(6), 50, "src/sprites/Tiles/madera.png", true, false));
			for(int i = 0; i<3; i++) {
				panel.tiles.add(new Tile(PixelCoord(128), coordY, PixelCoord(11), 25, "src/sprites/Tiles/plataforma.png", true, true));
				coordY += 150;
			}
			
			// Plataforma
			
			coordY = PixelCoord(34);
			panel.tiles.add(new Tile(PixelCoord(162), coordY, PixelCoord(16), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(162), coordY + 50, PixelCoord(16), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			coordX = PixelCoord(164) + 5;
			for(int i=0;i<3;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(1);
			}
			coordY = PixelCoord(16);
			panel.tiles.add(new Tile(PixelCoord(178), coordY, PixelCoord(16), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(178), coordY + 50, PixelCoord(16), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			panel.EnemigosBasicos.add(new EnemigoEstatico(PixelCoord(179) + 5, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
			coordX = PixelCoord(192) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(1);
			}
			coordY = PixelCoord(19) + 25;
			for(int i = 0; i<3; i++) {
				panel.tiles.add(new Tile(PixelCoord(172), coordY, PixelCoord(5), 25, "src/sprites/Tiles/plataforma.png", true, true));
				coordY += 300;
			}
			
			coordY = PixelCoord(33);
			for(int i = 0; i<3; i++) {
				panel.EnemigosBasicos.add(new EnemigoCanon(PixelCoord(177), coordY, panel, -1, player, PixelCoord(16)));
				coordY -= PixelCoord(6);
			}
			
			coordY = PixelCoord(22) + 25;
			for(int i = 0; i<2; i++) {
				panel.tiles.add(new Tile(PixelCoord(165), coordY, PixelCoord(5), 25, "src/sprites/Tiles/plataforma.png", true, true));
				coordY += 300;
			}
			
			// Plataforma
			
			coordY = PixelCoord(23);
			panel.tiles.add(new Tile(PixelCoord(194), coordY, PixelCoord(29), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(PixelCoord(194), coordY + 50, PixelCoord(29), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			// Plataformas
			
			coordY = PixelCoord(17) + 25;
			coordX = PixelCoord(197) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(5);
			}
			coordX = PixelCoord(214) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(5);
			}
			for(int i = 0; i<2; i++) {
				panel.tiles.add(new Tile(PixelCoord(197), coordY, PixelCoord(6), 25, "src/sprites/Tiles/plataforma.png", true, true));
				panel.tiles.add(new Tile(PixelCoord(214), coordY, PixelCoord(6), 25, "src/sprites/Tiles/plataforma.png", true, true));
				coordY += 150;
			}
			
			panel.EnemigosBasicos.add(new EnemigoCanon(PixelCoord(194), PixelCoord(19), panel, 1, player, PixelCoord(29)));
			
			// Plataforma
			coordY = PixelCoord(16);
			coordX = PixelCoord(223);
			for(int i=0; i<2; i++) {
				panel.tiles.add(new Tile(coordX, coordY, PixelCoord(8), 50, "src/sprites/Tiles/suelo.png", true, false));
				panel.tiles.add(new Tile(coordX, coordY + 50, PixelCoord(8), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
				coordX += PixelCoord(12);
			}
			panel.tiles.add(new Tile(coordX, coordY, PixelCoord(9), 50, "src/sprites/Tiles/suelo.png", true, false));
			panel.tiles.add(new Tile(coordX, coordY + 50, PixelCoord(9), panel.FinalY - coordY, "src/sprites/Tiles/arena.png", true, false));
			
			coordX = PixelCoord(226) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(1);
			}
			coordX = PixelCoord(250) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(1);
			}
			coordX = PixelCoord(235) + 5;
			for(int i=0;i<2;i++) {
				panel.EnemigosBasicos.add(new EnemigoEstatico(coordX, coordY - 40, 40, 40, "src/sprites/Obstaculos/pincho.png")); // Pincho
				coordX += PixelCoord(7);
			}
			
			// --- Monedas ---
			
			coordX = 0;
			for(int i = 0; i<3;i++) {
				panel.bonuses.add(new Bonus(coordX + PixelCoord(22), PixelCoord(23) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(22), PixelCoord(38) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(130), PixelCoord(31), 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(130), PixelCoord(34), 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(130), PixelCoord(37), 0, panel, player));
				coordX += PixelCoord(3);
			}
			
			coordX = 0;
			for(int i = 0; i<2;i++) {
				panel.bonuses.add(new Bonus(coordX + PixelCoord(33) + 30, PixelCoord(30) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(56) + 30, PixelCoord(30) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(45) + 30, PixelCoord(23) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(65), PixelCoord(44) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(83), PixelCoord(44) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(198), PixelCoord(20) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(215), PixelCoord(20) - 30, 0, panel, player));
				coordX += PixelCoord(2) + 25;
			}
			
			coordX = 0;
			for(int i = 0; i<2;i++) {
				panel.bonuses.add(new Bonus(coordX + PixelCoord(73), PixelCoord(46) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(96) + 30, PixelCoord(30) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(109) + 30, PixelCoord(33) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(97) + 30, PixelCoord(44) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(110) + 30, PixelCoord(42) - 30, 0, panel, player));
				coordX += PixelCoord(4);
			}
			
			coordX = 0;
			for(int i = 0; i<4;i++) {
				panel.bonuses.add(new Bonus(coordX + PixelCoord(43), PixelCoord(27) - 30, 0, panel, player));
				panel.bonuses.add(new Bonus(coordX + PixelCoord(43), PixelCoord(43) - 30, 0, panel, player));
				coordX += PixelCoord(2) + 25;
			}
			
			coordY = PixelCoord(35);
			for(int i = 0; i<3; i++) {
				panel.bonuses.add(new Bonus(PixelCoord(142), coordY - 30, 0, panel, player));
				coordY += PixelCoord(2);
			}
			
			// PowerUps y CheckPoints
			panel.bonuses.add(new Bonus(PixelCoord(207) + 25,PixelCoord(18), 2, panel, player));
			panel.bonuses.add(new Bonus(PixelCoord(58), PixelCoord(45), 2, panel, player));
			
			panel.bonuses.add(new Bonus(PixelCoord(90), PixelCoord(29), 3, panel, player));
			panel.bonuses.add(new Bonus(PixelCoord(90), PixelCoord(41), 3, panel, player));
			panel.bonuses.add(new Bonus(PixelCoord(238) + 25, PixelCoord(14), 3, panel, player));
			panel.bonuses.add(new Bonus(PixelCoord(190), PixelCoord(14), 3, panel, player));
			
			// Plataforma Soporte
			panel.tiles.add(new Tile(PixelCoord(119), PixelCoord(36), PixelCoord(5), 25, "src/sprites/Tiles/plataforma.png", true, true));
			break;
		
		// LEVEL 4
		case 4:

			player.y = PixelCoord(20);
			panel.FinalX = PixelCoord(60);
			panel.tiles.add(new Tile(0, PixelCoord(21), PixelCoord(200), PixelCoord(1), "src/sprites/Tiles/madera.png", true, false));
			panel.bosses.add(new Boss3(PixelCoord(30), PixelCoord(18), panel, player));
			
			break;
		
		// LEVEL 5
		case 5:
			
			break;
			
		// LEVEL 6
		case 6:
			
			break;
		}
		
	}
	
public void GeneracionEventosNivel(int nivel, Player player, GamePanel panel) {
		
		switch(nivel) {
		case 1:
			
			if(player.x >= 400 && panel.SpawnEnemigos[0] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(20), PixelCoord(22), player, 0, panel));
				panel.SpawnEnemigos[0] = true;
			}
			
			if(player.x >= 2400 && panel.SpawnEnemigos[1] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(2900, PixelCoord(22), player, 0, panel));
				panel.SpawnEnemigos[1] = true;
			}
			
			if(player.x >= 3500 && panel.SpawnEnemigos[2] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(4000, PixelCoord(16), player, 0, panel));
				panel.EnemigosBasicos.add(new EnemigoMovil(4200, PixelCoord(20), player, 2, panel));
				panel.SpawnEnemigos[2] = true;
			}
			
			if(player.x >= 4500 && panel.SpawnEnemigos[3] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(5100, PixelCoord(14), player, 1, panel));
				panel.SpawnEnemigos[3] = true;
			}
			
			if(player.x >= 6000 && !panel.bossSpawn) {
				panel.bosses.add(new Boss1(6500, PixelCoord(16), panel, player));
				panel.bossSpawn = true;
			}
			
			if (!panel.bosses.isEmpty()) {
			    // Mientras el boss esté vivo, la puerta se mantiene
			    if (!panel.tiles.contains(panel.puertaFinal)) {
			    	panel.tiles.add(panel.puertaFinal);
			    }
			} else {
			    // Cuando el boss muere, eliminas la puerta una sola vez
				panel.tiles.remove(panel.puertaFinal);
			}
			
			break;
			
		case 2:
			
			if(player.x >= 900 && panel.SpawnEnemigos[0] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(28), PixelCoord(66), player, 0, panel));
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(42), PixelCoord(66), player, 3, panel));
				panel.SpawnEnemigos[0] = true;
			}
			
			if(player.x >= 4500 && panel.SpawnEnemigos[1] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(103), PixelCoord(63), player, 1, panel));
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(104), PixelCoord(63), player, 2, panel));
				panel.SpawnEnemigos[1] = true;
			}
			
			if(player.x >= 5550 && panel.SpawnEnemigos[2] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(123), PixelCoord(58), player, 3, panel));
				panel.SpawnEnemigos[2] = true;
			}
			
			if(player.x >= 6450 && panel.SpawnEnemigos[3] == false) {
				panel.bosses.add(new Boss1(PixelCoord(145), PixelCoord(52), panel, player));
				panel.SpawnEnemigos[3] = true;
			}
			
			if(player.x >= 7850 && panel.SpawnEnemigos[4] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(177), PixelCoord(22), player, 2, panel));
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(178), PixelCoord(22), player, 2, panel));
				panel.SpawnEnemigos[4] = true;
			}
			
			if(player.x >= 9350 && panel.SpawnEnemigos[5] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(197), PixelCoord(22), player, 0, panel));
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(198), PixelCoord(22), player, 1, panel));
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(198), PixelCoord(18), player, 3, panel));
				panel.SpawnEnemigos[5] = true;
			}
			
			if(player.x >= 11300 && !panel.bossSpawn) {
				panel.tiles.add(new Tile(PixelCoord(224), 0, PixelCoord(1), PixelCoord(24), "src/sprites/Tiles/madera.png", true, false));
				panel.bosses.add(new Boss2(PixelCoord(246), PixelCoord(19), panel, player));
				panel.bossSpawn = true;
			}
			
			if (!panel.bosses.isEmpty()) {
			    if (!panel.tiles.contains(panel.puertaFinal)) {
			    	panel.tiles.add(panel.puertaFinal);
			    }
			} else {
				panel.tiles.remove(panel.puertaFinal);
			}
			
			break;
			
		case 3:
			
			if(player.x >= PixelCoord(34) && player.x < PixelCoord(63)) {
				if(player.y < PixelCoord(31) && panel.SpawnEnemigos[0] == false) {
					panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(41), PixelCoord(29), player, 0, panel));
					panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(55), PixelCoord(29), player, 0, panel));
					panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(47), PixelCoord(25), player, 3, panel));
					panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(52), PixelCoord(22), player, 3, panel));
					panel.SpawnEnemigos[0] = true;
				} else if(player.y > PixelCoord(31) && panel.SpawnEnemigos[1] == false) {
					panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(49), PixelCoord(42), player, 1, panel));
					panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(52), PixelCoord(42), player, 3, panel));
					panel.SpawnEnemigos[1] = true;
				}
			}
			
			if(player.x >= PixelCoord(58) && player.x < PixelCoord(89)) {
				if(player.y < PixelCoord(31) && panel.SpawnEnemigos[2] == false) {
					panel.bosses.add(new Boss1(PixelCoord(76), PixelCoord(28), panel, player));
					panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(80), PixelCoord(28), player, 3, panel));
					panel.SpawnEnemigos[2] = true;
				} else if(player.y > PixelCoord(31) && panel.SpawnEnemigos[3] == false) {
					panel.bosses.add(new Boss2(PixelCoord(76), PixelCoord(46), panel, player));
					panel.SpawnEnemigos[3] = true;
				}
			}
			
			if(player.x >= PixelCoord(123) && panel.SpawnEnemigos[4] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(135), PixelCoord(39), player, 1, panel));
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(138), PixelCoord(39), player, 1, panel));
				panel.SpawnEnemigos[4] = true;
			}
			
			if(player.x >= PixelCoord(138) && player.y <= PixelCoord(30) && panel.SpawnEnemigos[5] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(149), PixelCoord(30), player, 1, panel));
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(154), PixelCoord(30), player, 2, panel));
				panel.SpawnEnemigos[5] = true;
			}
			
			if(player.x >= PixelCoord(179) && panel.SpawnEnemigos[6] == false) {
				panel.EnemigosBasicos.add(new EnemigoMovil(PixelCoord(192), PixelCoord(14), player, 1, panel));
				panel.SpawnEnemigos[6] = true;
			}
			
			if(player.x >= PixelCoord(197) && panel.SpawnEnemigos[7] == false) {
				panel.bosses.add(new Boss2(PixelCoord(207), PixelCoord(20), panel, player));
				panel.SpawnEnemigos[7] = true;
			}
			
			// Spawnear Bosses
			if(player.x >= PixelCoord(238) && !panel.bossSpawn) {
				if(!panel.bosses.isEmpty()) {
					panel.bosses.clear(); // Matar a los bosses q queden dando vueltas por ahí
				}
				panel.tiles.add(new Tile(PixelCoord(222), 0, PixelCoord(1), PixelCoord(22), "src/sprites/Tiles/madera.png", true, false));
				panel.bosses.add(new Boss3(PixelCoord(226), PixelCoord(7), panel, player));
				panel.bosses.add(new Boss3(PixelCoord(250), PixelCoord(7), panel, player));
				panel.bossSpawn = true;
			}
			
			if (!panel.bosses.isEmpty()) {
			    if (!panel.tiles.contains(panel.puertaFinal)) {
			    	panel.tiles.add(panel.puertaFinal);
			    }
			} else {
				panel.tiles.remove(panel.puertaFinal);
			}
			
			break;
			
		case 4:
			
			break;
			
		case 5:
			
			break;
			
		case 6:
			
			break;
		}
		
	}

}
