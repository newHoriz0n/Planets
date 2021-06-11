package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import phx.Vektor2D;

public class Planet {

	private double mass;

	private double rad;

	private Vektor2D pos;
	private Vektor2D speed; // [px / sek]

	private Color farbe;

	private long lastUpdate;

	private boolean dying;
	private boolean creating;
	private boolean waechst;
	private boolean schrumpft;

	private long createTime;

	private Font fMass = new Font("Arial", Font.PLAIN, 8);

	public Planet(Vektor2D pos, Vektor2D speed, double mass, Color farbe) {
		this.mass = mass;
		this.pos = pos;
		this.speed = speed;
		this.farbe = farbe;
		this.creating = true;
		this.createTime = System.currentTimeMillis();
		calcRadius();
	}

	private void calcRadius() {
		this.rad = Math.sqrt(mass / Math.PI);
	}

	public void draw(Graphics2D g) {
		g.setColor(farbe);
		if (isCreating() || isDying()) {
			g.drawOval((int) (pos.getX() - rad), (int) (pos.getY() - rad), (int) (rad * 2), (int) (rad * 2));
		} else {
			g.fillOval((int) (pos.getX() - rad), (int) (pos.getY() - rad), (int) (rad * 2), (int) (rad * 2));
		}

		g.setColor(Color.BLACK);
		g.setFont(fMass);
		g.drawString("" + (int) mass, pos.getIntX() - 7, pos.getIntY());
	}

	public Vektor2D getPos() {
		return pos;
	}

	public Vektor2D getSpeed() {
		return speed;
	}

	/**
	 * 
	 * @param m Wachsbetrag
	 */
	public void wachse(double m) {
		if (!dying && !creating) {
			WachsThread wt = new WachsThread(mass + m);
			wt.start();
			this.waechst = true;
			this.schrumpft = false;
		}
	}

	public void sterbe() {
		SchrumpfThread st = new SchrumpfThread(0);
		st.start();
		this.dying = true;
		this.waechst = false;
		this.schrumpft = true;
	}

	/**
	 * 
	 * @param d: Schrumpfbetrag
	 */
	public void schrumpfe(double d) {
		if (!dying && !creating) {
			SchrumpfThread st = new SchrumpfThread(mass - d);
			st.start();
			this.schrumpft = true;
			this.waechst = false;
		}
	}

	public void setSpeed(Vektor2D v) {
		this.speed = v;
	}

	public void update() {
		long now = System.nanoTime();
		long dt = now - lastUpdate;
		lastUpdate = now;
		if (dt < 1000000000) {
			double scale = (double) dt / (double) 1000000000.0;
			Vektor2D frameSpeed = new Vektor2D(speed).scale(scale);
			pos.add(frameSpeed);
		}
		if (System.currentTimeMillis() - createTime > 1000) {
			creating = false;
		}
	}

	public void setSpeed(double x, double y) {
		this.speed.set(x, y);
	}

	public double getRadius() {
		return rad;
	}

	public double getMasse() {
		return mass;
	}

	public boolean isAlive() {
		return mass > 0;
	}

	public boolean isDying() {
		return dying;
	}

	public boolean isCreating() {
		return creating;
	}

	public boolean isSchrumpft() {
		return schrumpft;
	}

	public boolean isWaechst() {
		return waechst;
	}

	class WachsThread extends Thread {

		double endMasse;
		double wachstum = 1.0;

		public WachsThread(double endmasse) {
			this.endMasse = endmasse;
		}

		@Override
		public void run() {
			while (mass < endMasse) {
				mass += wachstum;
				calcRadius();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			waechst = false;
		}

	}

	class SchrumpfThread extends Thread {

		double schrumpftum = 10.0;
		double endmasse;

		public SchrumpfThread(double endmasse) {
			this.endmasse = endmasse;
		}

		@Override
		public void run() {
			while (mass > endmasse) {
				mass -= schrumpftum;
				calcRadius();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				schrumpft = false;
			}
		}

	}

}
