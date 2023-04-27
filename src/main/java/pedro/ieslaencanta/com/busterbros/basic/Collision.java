package pedro.ieslaencanta.com.busterbros.basic;

import java.util.Optional;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.Utils;

public class Collision
{
	private Element a;
	private Element b;
	private double distance = 0.0;
	private Optional<Point2D> aSurface = Optional.empty();
	private Optional<Point2D> bSurface = Optional.empty();
	private Optional<Point2D> aPushVector = Optional.empty();
	private Optional<Point2D> bPushVector = Optional.empty();

	public Collision()
	{

	}

	public Collision(Element a, Element b)
	{
		setA(a);
		setB(b);
		updateDistance();
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

	public double getDistance()
	{
		return distance;
	}

	public void setDistance(double d)
	{
		distance = d;
	}

	private void updateDistance()
	{
		if (a == null || b == null) return;
		var c1 = a.getCenter();
		var c2 = b.getCenter();
		distance = Math.sqrt(c1.distance(c2));
	}

	public void setSurfacePoints(Point2D a, Point2D b)
	{
		aSurface = Optional.of(a);
		bSurface = Optional.of(b);
	}

	public void setPushVectors(Point2D a, Point2D b)
	{
		if (a != null) aPushVector = Optional.of(a);
		if (b != null) bPushVector = Optional.of(b);
	}
	
	public void debug(GraphicsContext gc)
	{
		gc.setStroke(Color.RED);

		gc.strokeLine(
			a.getCenterX() * Game.SCALE,
			a.getCenterY() * Game.SCALE,
			b.getCenterX() * Game.SCALE,
			b.getCenterY() * Game.SCALE
		);

		if (aSurface.isPresent() && bSurface.isPresent())
		{
			gc.setFill(Color.rgb(255, 64, 64));

			gc.fillOval(
				(a.getCenterX() + aSurface.get().getX()) * Game.SCALE - 4,
				(a.getCenterY() + aSurface.get().getY()) * Game.SCALE - 4,
				8,
				8
			);

			gc.fillOval(
				(b.getCenterX() + bSurface.get().getX()) * Game.SCALE - 4,
				(b.getCenterY() + bSurface.get().getY()) * Game.SCALE - 4,
				8,
				8
			);
		}

		if (aPushVector.isPresent())
		{
			Point2D aPushVectorNorm = aPushVector.get().normalize().multiply(16.0);

			gc.setLineWidth(2.0);
			gc.setStroke(Color.rgb(255, 0, 255));

			gc.strokeLine(
				a.getCenterX() * Game.SCALE,
				a.getCenterY() * Game.SCALE,
				(a.getCenterX() + aPushVectorNorm.getX()) * Game.SCALE,
				(a.getCenterY() + aPushVectorNorm.getY()) * Game.SCALE
			);
		}

		Utils.renderRectangleBorder(gc, Color.RED, Utils.intersection(a.getRectangle(), b.getRectangle()));
	}

	public static Optional<Collision> getGenericCollision(Element e1, Element e2)
	{
		return e1.getRectangle().intersects(e2.getRectangle()) ? Optional.of(new Collision(e1, e2)) : Optional.empty();
	}
}
