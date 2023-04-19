package pedro.ieslaencanta.com.busterbros.basic.elements;

import java.util.Optional;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import pedro.ieslaencanta.com.busterbros.App;
import pedro.ieslaencanta.com.busterbros.Resources;
import pedro.ieslaencanta.com.busterbros.State;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;
import pedro.ieslaencanta.com.busterbros.basic.ElementWithGravity;

public class Ball extends ElementWithGravity
{
	public enum BallSize
	{
		BIG(1, 6, 48, 40),
		MEDIUM(52, 13, 32, 26),
		SMALL(86, 19, 16, 14),
		TINY(106, 23, 8, 7);

		int x, y, w, h;

		BallSize(int x, int y, int w, int h)
		{
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		public static BallSize getRandom()
		{
			return BallSize.values()[(int)(Math.random() * 4)];
		}
	}

	protected BallSize ballSize = BallSize.BIG;

	public Ball()
	{
		this(BallSize.getRandom(), 0, 0, 0, 0);
	}

	public Ball(double x, double y, double w, double h)
	{
		this(BallSize.getRandom(), x, y, w, h);
	}

	public Ball(BallSize ballSize, double x, double y, double w, double h)
	{
		super();
		this.ballSize = ballSize;
		setRectangle(new Rectangle2D(x, y, ballSize.w, ballSize.h));
		setImageCoordinates(new Rectangle2D(ballSize.x, ballSize.y, ballSize.w, ballSize.h));
		setImage(Resources.getInstance().getImage("ballons"));
	}

	@Override
	public boolean isActive()
	{
		return isActiveHorizontalGravity() && isActiveVerticalGravity();
	}

	@Override
	public boolean isActiveHorizontalGravity()
	{
		return activeHorizontalGravity;
	}

	@Override
	public boolean isActiveVerticalGravity()
	{
		return activeVerticalGravity;
	}

	@Override
	public void activeGravity()
	{
		activeHorizontalGravity();
		activeVerticalGravity();
	}

	@Override
	public void activeHorizontalGravity()
	{
		activeHorizontalGravity = true;
	}

	@Override
	public void activeVerticalGravity()
	{
		activeVerticalGravity = true;
	}

	@Override
	public void unactiveGravity()
	{
		unactiveHorizontalGravity();
		unactiveVerticalGravity();
	}

	@Override
	public void unactiveHorizontalGravity()
	{
		activeHorizontalGravity = false;
	}

	@Override
	public void unactiveVerticalGravity()
	{
		activeVerticalGravity = false;
	}

	@Override
	public void setHorizontalGravity(double gravity)
	{
		gx = gravity;
	}

	@Override
	public void setVerticalGravity(double gravity)
	{
		gy = gravity;
	}

	@Override
	public double getHorizontalGravity()
	{
		return gx;
	}

	@Override
	public double getVerticalGravity()
	{
		return gy;
	}

	@Override
	public void move(double x, double y)
	{
		setPosition(rectangle.getMinX() + x, rectangle.getMinY() + y);
	}

	@Override
	public void move(double x, double y, double lerp)
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
	public void moveLeft()
	{
		moveLeft(1);
	}

	@Override
	public void moveLeft(double inc)
	{
		setPosition(rectangle.getMinX() + inc, rectangle.getMinY());
	}

	@Override
	public void moveRight()
	{
		moveRight(1);
	}

	@Override
	public void moveRight(double inc)
	{
		setPosition(rectangle.getMinX() - 1, rectangle.getMinY());
	}

	@Override
	public void moveUp()
	{
		moveUp(1);
	}

	@Override
	public void moveUp(double inc)
	{
		setPosition(rectangle.getMinX(), rectangle.getMinY() - inc);
	}

	@Override
	public void moveDown()
	{
		moveDown(1);
	}

	@Override
	public void moveDown(double inc)
	{
		setPosition(rectangle.getMinX(), rectangle.getMinY() + inc);
	}

	@Override
	public void start()
	{
		state = State.STARTED;
	}

	@Override
	public void stop()
	{
		state = State.STOPPED;
	}

	@Override
	public void pause()
	{
		state = State.PAUSED;
	}

	@Override
	public State getState()
	{
		return state;
	}

	@Override
	public void setState(State s)
	{
		state = s;
	}

	public void update()
	{
		if (state == State.STARTED)
		{
			if (activeVerticalGravity) vy += gy;
			if (activeHorizontalGravity) vx += gx;
		}
		if (rectangle.getMaxY() > App.HEIGHT - 8)
		{
			vy *= -0.75;
		}
		setPosition(getRectangle().getMinX() + vx, getRectangle().getMinY() + vy);
	}

	@Override
	public Optional<Collision> collision(Element e)
	{
		if (getRectangle().intersects(e.getRectangle()))
		{
			return Optional.of(new Collision(this, e));
		}
		return Optional.empty();
	}
}
