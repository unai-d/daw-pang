package pedro.ieslaencanta.com.busterbros.basic;

import java.util.Optional;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.Utils;
import pedro.ieslaencanta.com.busterbros.basic.elements.Ball;

public class Collision
{
	private Element a;
	private Element b;
	private Point2D distanceVector = Point2D.ZERO;
	private double distance = 0.0;
	private double angle = 0.0;
	private Optional<Point2D> aSurface = Optional.empty();
	private Optional<Point2D> bSurface = Optional.empty();
	private Optional<Point2D> aPushVector = Optional.empty();
	private Optional<Point2D> bPushVector = Optional.empty();
	private int userData = 0;

	public Collision()
	{

	}

	public Collision(Element a, Element b)
	{
		this(a, b, 0);
	}

	public Collision(Element a, Element b, int userData)
	{
		setA(a);
		setB(b);
		this.userData = userData;
		updateData();
	}

	public Element getA()
	{
		return a;
	}

	public void setA(Element e)
	{
		a = e;
	}

	public Element getB()
	{
		return b;
	}

	public void setB(Element e)
	{
		b = e;
	}

	public void setUserData(int userData)
	{
		this.userData = userData;
	}

	public int getUserData()
	{
		return userData;
	}

	public double getDistance()
	{
		return distance;
	}

	public void setDistance(double d)
	{
		distance = d;
	}

	private void updateData()
	{
		if (a == null || b == null) return;
		var c1 = a.getCenter();
		var c2 = b.getCenter();
		distanceVector = new Point2D(c2.getX() - c1.getX(), c2.getY() - c1.getY()); // `subtract` method only returns positive values.
		distance = distanceVector.magnitude();
		angle = distanceVector.angle(new Point2D(1, 0)) / 180.0 * Math.PI;

		if (a instanceof Ball)
		{
			Ball aBall = (Ball)a;
			Point2D aSurface = aBall.getSurfacePositionAtAngle(angle);
			this.aSurface = Optional.of(aSurface);
		}
		if (b instanceof Ball)
		{
			Ball bBall = (Ball)b;
			Point2D bSurface = bBall.getSurfacePositionAtAngle(angle + Math.PI);
			this.bSurface = Optional.of(bSurface);
		}
	}

	public Optional<Point2D> getASurfacePoint()
	{
		return aSurface;
	}

	public Optional<Point2D> getBSurfacePoint()
	{
		return bSurface;
	}

	public void setSurfacePoints(Point2D a, Point2D b)
	{
		aSurface = a != null ? Optional.of(a) : Optional.empty();
		bSurface = b != null ? Optional.of(b) : Optional.empty();
	}

	public void setPushVectors(Point2D a, Point2D b)
	{
		aPushVector = a != null ? Optional.of(a) : Optional.empty();
		bPushVector = b != null ? Optional.of(b) : Optional.empty();
	}
	
	public void debug(GraphicsContext gc)
	{
		gc.setLineWidth(2.0);
		gc.setStroke(Color.RED);

		gc.strokeLine(
			a.getCenterX() * Game.SCALE,
			a.getCenterY() * Game.SCALE,
			b.getCenterX() * Game.SCALE,
			b.getCenterY() * Game.SCALE
		);

		if (aSurface.isPresent() || bSurface.isPresent())
		{
			gc.setFill(Color.rgb(255, 0, 255));

			if (aSurface.isPresent())
			{
				gc.fillOval(
					(a.getCenterX() + aSurface.get().getX()) * Game.SCALE - 4,
					(a.getCenterY() + aSurface.get().getY()) * Game.SCALE - 4,
					8,
					8
				);

				System.out.println(Utils.toStringGraph(aSurface.get()));
			}

			if (bSurface.isPresent()) gc.fillOval(
				(b.getCenterX() + bSurface.get().getX()) * Game.SCALE - 4,
				(b.getCenterY() + bSurface.get().getY()) * Game.SCALE - 4,
				8,
				8
			);
		}

		if (aPushVector.isPresent() || bPushVector.isPresent())
		{
			gc.setStroke(Color.rgb(255, 0, 255));

			if (aPushVector.isPresent())
			{
				Point2D aPushVectorNorm = aPushVector.get().normalize().multiply(16.0);

				gc.strokeLine(
					a.getCenterX() * Game.SCALE,
					a.getCenterY() * Game.SCALE,
					(a.getCenterX() + aPushVectorNorm.getX()) * Game.SCALE,
					(a.getCenterY() + aPushVectorNorm.getY()) * Game.SCALE
				);
			}

			if (bPushVector.isPresent())
			{
				Point2D bPushVectorNorm = aPushVector.get().normalize().multiply(16.0);

				gc.strokeLine(
					b.getCenterX() * Game.SCALE,
					b.getCenterY() * Game.SCALE,
					(b.getCenterX() + bPushVectorNorm.getX()) * Game.SCALE,
					(b.getCenterY() + bPushVectorNorm.getY()) * Game.SCALE
				);
			}
		}

		Utils.renderRectangleBorder(gc, Color.RED, Utils.intersection(a.getRectangle(), b.getRectangle()));
	}

	public static Optional<Collision> getGenericCollision(Element e1, Element e2)
	{
		return e1.getRectangle().intersects(e2.getRectangle()) ? Optional.of(new Collision(e1, e2)) : Optional.empty();
	}

	@Override
	public String toString()
	{
		return "[A=" + a.getClass().getSimpleName() + ", B=" + b.getClass().getSimpleName() + "]";
	}
}
