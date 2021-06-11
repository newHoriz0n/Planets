package phx;

public class Vektor2D {

	private double x;
	private double y;

	public Vektor2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vektor2D(Vektor2D v) {
		this.x = v.x;
		this.y = v.y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getIntX() {
		return (int) x;
	}

	public int getIntY() {
		return (int) y;
	}

	public double calcAbstand(Vektor2D pos) {
		return Math.sqrt(Math.pow(pos.x - x, 2) + Math.pow(pos.y - y, 2));
	}

	public Vektor2D scale(double scale) {
		this.x *= scale;
		this.y *= scale;
		return this;
	}

	public Vektor2D add(Vektor2D summand) {
		this.x += summand.x;
		this.y += summand.y;
		return this;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	public double calcLaenge() {
		return Math.sqrt(x * x + y * y);
	}

	public Vektor2D normalisiere() {
		double l = calcLaenge();
		this.x = x / l;
		this.y = y / l;
		return this;
	}

}
