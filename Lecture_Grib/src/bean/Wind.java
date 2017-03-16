package bean;

public class Wind {
	
	double speed;
	double direction;
	
	public Wind(double speed, double direction) {
		this.speed = speed;
		this.direction = direction;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getDirection() {
		return direction;
	}
	public void setDirection(double direction) {
		this.direction = direction;
	}
	@Override
	public String toString() {
		return "Wind [speed=" + speed + ", direction=" + direction + "]";
	}
}
