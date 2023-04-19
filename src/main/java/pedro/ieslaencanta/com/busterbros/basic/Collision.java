package pedro.ieslaencanta.com.busterbros.basic;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IDebuggable;

public class Collision
{
	private Element a;
	private Element b;
	private double distance = 0.0;
	private Point2D separator = Point2D.ZERO;

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
	
	public void debug(GraphicsContext gc)
	{
		gc.setStroke(Color.RED);

		gc.strokeLine(
			a.getCenterX() * Game.SCALE,
			a.getCenterY() * Game.SCALE,
			b.getCenterX() * Game.SCALE,
			b.getCenterY() * Game.SCALE
		);

		var c1 = a.getCenter();
		var c2 = b.getCenter();
		var directionVector = c1.subtract(c2).normalize().multiply(16);

		gc.setStroke(Color.GREEN);

		gc.strokeLine(
			a.getCenterX() * Game.SCALE,
			a.getCenterY() * Game.SCALE,
			(a.getCenterX() + directionVector.getX()) * Game.SCALE,
			(a.getCenterY() + directionVector.getY()) * Game.SCALE
		);

		gc.strokeLine(
			b.getCenterX() * Game.SCALE,
			b.getCenterY() * Game.SCALE,
			(b.getCenterX() - directionVector.getX()) * Game.SCALE,
			(b.getCenterY() - directionVector.getY()) * Game.SCALE
		);
	}
}
