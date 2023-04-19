package pedro.ieslaencanta.com.busterbros.basic.interfaces;

public interface IMovable
{
	public void move(double x, double y);
	public void move(double x, double y, double lerp);
	public void moveLeft();
	public void moveLeft(double inc);
	public void moveRight();
	public void moveRight(double inc);
	public void moveUp();
	public void moveUp(double inc);
	public void moveDown();
	public void moveDown(double inc);
	public void stopMovement();
}
