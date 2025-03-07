package pedro.ieslaencanta.com.busterbros.basic.interfaces;

public interface IMovable
{
	public void setSpeed(double x, double y);
	public void setSpeed(double x, double y, double lerp);
	public void addSpeed(double x, double y);
	public void move(double x, double y);
	public void moveLeft();
	public void moveLeft(double inc);
	public void moveRight();
	public void moveRight(double inc);
	public void moveUp();
	public void moveUp(double inc);
	public void moveDown();
	public void moveDown(double inc);
	public void stopMovement();
	public double getXSpeed();
	public double getYSpeed();
	public void setXSpeed(double x);
	public void setYSpeed(double y);
}
