package pedro.ieslaencanta.com.busterbros.basic.elements;

import java.util.Optional;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import pedro.ieslaencanta.com.busterbros.Utils;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;
import pedro.ieslaencanta.com.busterbros.basic.ElementDynamic;

public class ViewportLimits extends ElementDynamic
{
	public ViewportLimits(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public Optional<Collision> collision(Element e)
	{
		// double ex1 = e.getX();
		// double ex2 = ex1 + e.getWidth();
		// double ey1 = e.getY();
		// double ey2 = ey1 + e.getHeight();

		Rectangle r1 = new Rectangle(x, y, width, height - 8);
		Rectangle r2 = new Rectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		//int r1s = ((Path)(Shape)r1).getElements().size();

		var interRet = Shape.intersect(r1, r2);
		var interPath = (Path)interRet;
		var interPathElemSize = interPath.getElements().size();
		boolean isOutside = interPathElemSize < 5;

		// if (e instanceof Player) System.out.println("Player inter. elem. count is " + interPathElemSize + ", OOB is " + isOutside);

		return isOutside ? Optional.of(new Collision(this, e)) : Optional.empty();
	}

	@Override
	public void update()
	{
		
	}
}
