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

	public enum BallColor
	{
		RED(0),
		GREEN(50),
		BLUE(99);

		int yOffset;

		BallColor(int yOffset)
		{
			this.yOffset = yOffset;
		}

		public static BallColor getRandom()
		{
			return BallColor.values()[(int)(Math.random() * 3)];
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
		this(ballSize, BallColor.getRandom(), x, y, w, h);
	}

	public Ball(BallSize ballSize, BallColor ballColor, double x, double y, double w, double h)
	{
		super();
		this.ballSize = ballSize;
		setRectangle(new Rectangle2D(x, y, ballSize.w, ballSize.h));
		setImageCoordinates(new Rectangle2D(ballSize.x, ballSize.y + ballColor.yOffset, ballSize.w, ballSize.h));
		setImage(Resources.getInstance().getImage("ballons"));
	}

	public void update()
	{
		if (state == State.STARTED)
		{
			if (activeVerticalGravity) vy += gy;
			if (activeHorizontalGravity) vx += gx;
		}
		if ((y + height) > App.HEIGHT - 8)
		{
			//System.out.println(vy + " -> " + (vy * -0.5));
			vy *= -0.5;
			if (Math.abs(vy) <= 0.1) vy = 0;
		}
		if (x < 8 || (x + width) > App.WIDTH - 8)
		{
			vx *= -0.75;
		}
		setPosition(getRectangle().getMinX() + vx, getRectangle().getMinY() + vy);
	}

	public double getRadiusAtAngle(double angle)
	{
		return Math.abs(Math.sin(angle) * ballSize.h + Math.cos(angle) * ballSize.w) / 2;
	}

	public Point2D getSurfacePositionAtAngle(double angle)
	{
		return new Point2D(Math.cos(angle) * ballSize.w / -2, Math.sin(angle) * ballSize.h / -2);
	}

	@Override
	public Optional<Collision> collision(Element e)
	{
		Collision ret = null;

		var c1 = getCenter();
		var c2 = e.getCenter();
		var distanceVector = c1.subtract(c2);
		double angle = distanceVector.angle(new Point2D(1, 0));
		angle = angle / 180 * Math.PI;

		// Ball -> Ball
		if (e instanceof Ball)
		{
			Ball eBall = (Ball)e;
			var r1 = getRadiusAtAngle(angle);
			var r2 = eBall.getRadiusAtAngle(angle);
			if (r1 + r2 > distanceVector.magnitude())
			{
				ret = new Collision(this, e);
				Point2D aSurface = distanceVector.normalize().multiply(-r1);
				Point2D bSurface = distanceVector.normalize().multiply(r2);
				ret.setSurfacePoints(aSurface, bSurface);
			}
		}
		// Ball -> Generic element.
		else if (getRectangle().intersects(e.getRectangle()))
		{
			ret = new Collision(this, e);
		}
		
		return ret != null ? Optional.of(ret) : Optional.empty();
	}
}
