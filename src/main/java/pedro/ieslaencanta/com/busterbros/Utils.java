package pedro.ieslaencanta.com.busterbros;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;

public class Utils
{
	public static double clamp(double v, double min, double max)
	{
		return Math.max(min, Math.min(max, v));
	}

	public static double area(Rectangle2D r)
	{
		return r.getWidth() * r.getHeight();
	}

	public static double squarifyAngle(double a, double scalar)
	{
		double quarterAngle = Math.PI / 2.0; // 45º
		double halfQuarterAngle = quarterAngle / 2.0; // 22.5º

		int quarters = (int)(a / (Math.PI / 2));

		double ret = a;
		ret -= (quarters * quarterAngle) + halfQuarterAngle;
		ret *= scalar;
		ret = Utils.clamp(ret, -halfQuarterAngle, quarterAngle - halfQuarterAngle);
		ret += (quarters * quarterAngle) + halfQuarterAngle;

		return ret;
	}

	public static Rectangle2D intersection(Rectangle2D r1, Rectangle2D r2)
	{
		if (r1.getMaxX() < r2.getMinX() || r1.getMaxY() < r2.getMinY()) return new Rectangle2D(0, 0, 0, 0);

		double x = Math.max(r1.getMinX(), r2.getMinX());
		double y = Math.max(r1.getMinY(), r2.getMinY());
		double w = Math.min(r1.getMaxX(), r2.getMaxX()) - x;
		double h = Math.min(r1.getMaxY(), r2.getMaxY()) - y;

		if (w < 0) { x += w; w *= -1; }
		if (h < 0) { y += h; h *= -1; }

		return new Rectangle2D(x, y, w, h);
	}

	public static boolean elementsExistInCollisionList(ArrayList<Collision> colList, Element a, Element b)
	{
		for (Collision col : colList)
		{
			if (col.getA() == a && col.getB() == b) return true;
		}

		return false;
	}

	public static void renderRectangleBorder(GraphicsContext gc, Color color, Rectangle2D r)
	{
		renderRectangleBorder(gc, color, r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	public static void renderRectangleBorder(GraphicsContext gc, Color color, double x, double y, double width, double height)
	{
		gc.setStroke(color);
		gc.setFill(Color.TRANSPARENT);
		gc.beginPath();
		gc.moveTo(x * Game.SCALE, y * Game.SCALE);
		gc.lineTo((x + width) * Game.SCALE, y * Game.SCALE);
		gc.lineTo((x + width) * Game.SCALE, (y + height) * Game.SCALE);
		gc.lineTo(x * Game.SCALE, (y + height) * Game.SCALE);
		gc.closePath();
		gc.stroke();
	}
}
