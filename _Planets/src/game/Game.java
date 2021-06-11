package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import phx.Vektor2D;

public class Game {

	private Planet me;

	private List<Planet> planets;
	private int width;
	private int height;

	private static double startMasse = 100.0;

	private long lastUpdate;
	private long gameTime;

	private long newPlanetTime = 10000;
	private long lastPlanetTime;

	private static final Object planetLock = new Object();

	private int level;

	public Game(int width, int height, int level) {

		this.level = level;

		this.width = width;
		this.height = height;

		calcPlanets(level);

		this.me = new Planet(new Vektor2D(width / 2, height / 2), new Vektor2D(0, 0), startMasse * level, Color.GREEN);
		planets.add(me);

	}

	private void calcPlanets(int level) {
		this.planets = new ArrayList<Planet>();

		int anzahl = width * height / 100000 + level;
		for (int i = 0; i < anzahl; i++) {
			createRandomPlanet(level);
		}
	}

	public void update() {

		long dt = System.currentTimeMillis() - lastUpdate;
		lastUpdate = System.currentTimeMillis();
		if (dt < 1000) {
			gameTime += dt;
		}

		synchronized (planetLock) {
			for (int i = planets.size() - 1; i >= 0; i--) {
				if (!planets.get(i).isAlive()) {
					planets.remove(i);
				}
			}
		}

		if (gameTime > lastPlanetTime + newPlanetTime) {
			createRandomPlanet(1);
			lastPlanetTime = gameTime;
		}

		synchronized (planetLock) {
			for (Planet p : planets) {
				p.update();
			}
		}

		if (gameTime > 1000) {
			calcCollision();
		}

	}

	/**
	 * Erstellt einen neuen Planeten und fügt ihn der Planet Liste hinzu
	 * 
	 * @param level
	 */
	private void createRandomPlanet(int level) {

		Random r = new Random();

		double mass = 0;
		mass += 100 + r.nextDouble() * startMasse * level;

		int posX = width / 2;
		int posY = height / 2;

		double speedX = (r.nextDouble() - 0.5) * 100 * (level + 2);
		double speedY = (r.nextDouble() - 0.5) * 100 * (level + 2);

		Color c = new Color(50 + r.nextInt(50), 50 + r.nextInt(50), 50 + r.nextInt(50));

		Planet p = new Planet(new Vektor2D(posX, posY), new Vektor2D(speedX, speedY), mass, c);

		synchronized (planetLock) {
			planets.add(p);
		}

	}

	private void calcCollision() {
		// Wände
		for (Planet p : planets) {
			boolean collide = false;
			if (p.getPos().getX() < 0) {
				p.setSpeed(-p.getSpeed().getX(), p.getSpeed().getY());
				collide = true;
			} else if (p.getPos().getX() > width) {
				p.setSpeed(-p.getSpeed().getX(), p.getSpeed().getY());
				collide = true;
			}
			if (p.getPos().getY() < 0) {
				p.setSpeed(p.getSpeed().getX(), -p.getSpeed().getY());
				collide = true;
			} else if (p.getPos().getY() > height) {
				p.setSpeed(p.getSpeed().getX(), -p.getSpeed().getY());
				collide = true;
			}
			if (collide) {
				p.schrumpfe(p.getMasse() / 100.0);
			}
		}

		// Gegenseitig

		for (int i = 0; i < planets.size(); i++) {
			Planet p1 = planets.get(i);
			if (!p1.isCreating() && !p1.isDying() && !p1.isWaechst() && !p1.isSchrumpft()) {
				for (int j = i + 1; j < planets.size(); j++) {

					Planet p2 = planets.get(j);

					if (!p2.isDying() && !p2.isCreating() && !p2.isWaechst() && !p2.isSchrumpft()) {
						double dist = p1.getPos().calcAbstand(p2.getPos());
						if (dist < p1.getRadius() + p2.getRadius()) {
							if (p1.getMasse() > p2.getMasse()) {
								p1.wachse(p2.getMasse() / level);
								p2.sterbe();
								createRandomPlanet(level);
							} else {
								p2.wachse(p1.getMasse() / level);
								p1.sterbe();
								createRandomPlanet(level);
							}
						}
					}
				}
			}
		}

	}

	public void draw(Graphics2D g) {
		synchronized (planetLock) {
			for (Planet p : planets) {
				p.draw(g);
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void handleClick(int x, int y) {
		int dx = x - me.getPos().getIntX();
		int dy = y - me.getPos().getIntY();

		Vektor2D v = new Vektor2D(dx, dy);
		v.normalisiere();

		v.scale(100);

		me.setSpeed(v);
		me.schrumpfe(me.getMasse() / 20.0);

	}

}
