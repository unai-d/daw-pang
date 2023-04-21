package pedro.ieslaencanta.com.busterbros.basic;

import java.util.Optional;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pedro.ieslaencanta.com.busterbros.Game;

public class Collision
{
	private Element a;
	private Element b;
	private double distance = 0.0;
	private Optional<Point2D> aSurface = Optional.empty();
	private Optional<Point2D> bSurface = Optional.empty();

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
	
	public void debug(GraphicsContext gc)
	{
		gc.setStroke(Color.RED);

		gc.strokeLine(
			a.getCenterX() * Game.SCALE,
			a.getCenterY() * Game.SCALE,
			b.getCenterX() * Game.SCALE,
			b.getCenterY() * Game.SCALE
		);

		//var c1 = a.getCenter();
		//var c2 = b.getCenter();
		//var directionVector = c1.subtract(c2).normalize().multiply(16);

		//gc.setStroke(Color.GREEN);

		// gc.strokeLine(
		// 	a.getCenterX() * Game.SCALE,
		// 	a.getCenterY() * Game.SCALE,
		// 	(a.getCenterX() + directionVector.getX()) * Game.SCALE,
		// 	(a.getCenterY() + directionVector.getY()) * Game.SCALE
		// );

		// gc.strokeLine(
		// 	b.getCenterX() * Game.SCALE,
		// 	b.getCenterY() * Game.SCALE,
		// 	(b.getCenterX() - directionVector.getX()) * Game.SCALE,
		// 	(b.getCenterY() - directionVector.getY()) * Game.SCALE
		// );

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
	}

	public static Optional<Collision> getGenericCollision(Element e1, Element e2)
	{
		return e1.getRectangle().intersects(e2.getRectangle()) ? Optional.of(new Collision(e1, e2)) : Optional.empty();
	}
}
