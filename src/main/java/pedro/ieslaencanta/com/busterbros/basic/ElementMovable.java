package pedro.ieslaencanta.com.busterbros.basic;

import javafx.geometry.Point2D;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IMovable;

public abstract class ElementMovable extends ElementDynamic implements IMovable
{
	protected double vx = 0;
	protected double vy = 0;

	@Override
	public void setSpeed(double x, double y)
	{
		vx = x;
		vy = y;
	}

	@Override
	public void setSpeed(double x, double y, double lerp)
	{
		var moveVector = new Point2D(vx, vy).interpolate(new Point2D(x, y), lerp);
		vx = moveVector.getX();
		vy = moveVector.getY();
	}

	@Override
	public void stopMovement()
	{
		vx = 0;
		vy = 0;
	}

	@Override
	public void move(double x, double y)
	{
		this.x += x;
		this.y += y;
	}

	@Override
	public void moveLeft()
	{
		moveLeft(1);
	}

	@Override
	public void moveLeft(double inc)
	{
		setPosition(x + inc, y);
	}

	@Override
	public void moveRight()
	{
		moveRight(1);
	}

	@Override
	public void moveRight(double inc)
	{
		setPosition(x - inc, y);
	}

	@Override
	public void moveUp()
	{
		moveUp(1);
	}

	@Override
	public void moveUp(double inc)
	{
		setPosition(x, y - inc);
	}

	@Override
	public void moveDown()
	{
		moveDown(1);
	}

	@Override
	public void moveDown(double inc)
	{
		setPosition(x, y + inc);
	}

	@Override
	public double getXSpeed()
	{
		return vx;
	}

	@Override
	public double getYSpeed()
	{
		return vy;
	}

	@Override
	public void setXSpeed(double x)
	{
		vx = x;
	}

	@Override
	public void setYSpeed(double y)
	{
		vy = y;
	}
}
