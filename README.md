# Arcade Game

Este es un proyecto de un juego arcade desarrollado en Java utilizando `Swing` para la interfaz gráfica. El juego incluye un jugador, enemigos, balas, plataformas y un sistema de menús.

## Estructura del Proyecto

El proyecto tiene la siguiente estructura:

```

.
├── src/
│ ├── Clases/
│ │ ├── Balas.java
│ │ ├── Enemigo.java
│ │ ├── GameMain.java
│ │ ├── GamePanel.java
│ │ ├── Player.java
│ │ ├── Tile.java
│ ├── module-info.java
│ ├── resources/
│ │ ├── Pixellari.ttf
│ ├── sprites/
│ ├── barravida.png
│ ├── coin.png
│ ├── suelo.png
├── bin/
├── .classpath
├── .project
├── .settings/
└── README.md

```

## Características

- **Jugador**: Controlado por el teclado, puede moverse, saltar y disparar.
- **Enemigos**: Se mueven hacia el jugador y pueden ser eliminados con disparos.
- **Plataformas**: El jugador y los enemigos interactúan con las plataformas.
- **Sistema de Menús**: Incluye un menú principal y un menú de muerte.
- **Gráficos**: Utiliza imágenes y fuentes personalizadas.

## Controles

- **Flecha Izquierda**: Moverse a la izquierda.
- **Flecha Derecha**: Moverse a la derecha.
- **Espacio**: Saltar.
- **Shift**: Disparar.
- **H**: Alternar la visualización de hitboxes.

## Requisitos

- Java 21 o superior.
- Una fuente personalizada (`Pixellari.ttf`) ubicada en `src/resources/`.
- Imágenes para los sprites ubicadas en `src/sprites/`.

## Instalación

1. Clona este repositorio o descarga los archivos.
2. Asegúrate de tener Java 21 o superior instalado.
3. Abre el proyecto en tu IDE favorito (por ejemplo, Eclipse o Visual Studio Code).
4. Ejecuta la clase principal: `Clases.GameMain`.

## Ejecución

Al ejecutar el proyecto, se mostrará el menú principal. Desde allí, puedes iniciar el juego, seleccionar niveles (si está implementado) o salir.

## Archivos Clave

- **[`GameMain.java`](src/Clases/GameMain.java)**: Clase principal que inicializa el juego y los menús.
- **[`GamePanel.java`](src/Clases/GamePanel.java)**: Panel principal donde se desarrolla el juego.
- **[`Player.java`](src/Clases/Player.java)**: Lógica del jugador.
- **[`Enemigo.java`](src/Clases/Enemigo.java)**: Lógica de los enemigos.
- **[`Tile.java`](src/Clases/Tile.java)**: Representación de las plataformas.
- **[`Balas.java`](src/Clases/Balas.java)**: Lógica de las balas.

## Créditos

- **Desarrollador**: Diaz Diaz Ignacio, Santa Cruz Tomas, Zielinski Mauro, Muñoz Juan
- **Fuente**: `Pixellari.ttf` (fuente personalizada para el texto del juego).
- **Imágenes**: Sprites personalizados ubicados en `src/sprites/`.
