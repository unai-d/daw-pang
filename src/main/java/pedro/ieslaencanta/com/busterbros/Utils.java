package pedro.ieslaencanta.com.busterbros;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;

public class Utils
{
	public static double lerp(double v1, double v2, double t)
	{
		return (v1 * (1 - t)) + (v2 * t);
	}

	public static double clamp(double v, double min, double max)
	{
		return Math.max(min, Math.min(max, v));
	}

	public static int clamp(int v, int min, int max)
	{
		return Math.max(min, Math.min(max, v));
	}

	public static double area(Rectangle2D r)
	{
		return r.getWidth() * r.getHeight();
	}

	public static double squarifyAngle(double a, double scalar)
	{
		double quarterAngle = Math.PI / 2.0; // 90º
		double halfQuarterAngle = quarterAngle / 2.0; // 45º

		int quarters = (int)(a / (Math.PI / 2));

		double ret = a;
		ret -= (quarters * quarterAngle) + halfQuarterAngle;
		ret *= scalar;
		ret = Utils.clamp(ret, -halfQuarterAngle, quarterAngle - halfQuarterAngle);
		ret += (quarters * quarterAngle) + halfQuarterAngle;

		return ret;
	}

	public static Point2D squarifyAngle(Point2D p, double scalar)
	{
		double angle = p.angle(new Point2D(1.0, 0.0)) / 180.0 * Math.PI;
		angle = Utils.squarifyAngle(angle, scalar);

		Point2D ret = new Point2D(Math.copySign(Math.cos(angle), p.getX()), Math.copySign(Math.sin(angle), p.getY()));
		ret = ret.multiply(p.magnitude());

		return ret;
	}

	public static String getGauge(double value, int size)
	{
		String ret = "[";

		int threshold = (int)(value * (double)size);
		for (int i = 0; i < size; i++)
		{
			ret += (i < threshold) ? "\u2588" : " ";
		}

		ret += "]";
		return ret;
	}

	public static String getGauge(Point2D value, int size)
	{
		Point2D nv = value.normalize();
		return getGauge((nv.getX() + 1.0) / 2.0, size) + " " + getGauge((nv.getY() + 1.0) / 2.0, size);
	}

	public static String toString(Point2D p)
	{
		return String.format("[X%.2f Y%.2f M%.2f]", p.getX(), p.getY(), p.magnitude());
	}

	public static String toStringGraph(Point2D p)
	{
		StringBuffer[] ret = new StringBuffer[]
		{
			new StringBuffer("┌───────────────┐"), // Inner space is 15×5.
			new StringBuffer("│               │"), 
			new StringBuffer("│               │"),
			new StringBuffer("│               │"),
			new StringBuffer("│               │"),
			new StringBuffer("│               │"),
			new StringBuffer("└───────────────┘")
		};

		Point2D increment = p.normalize().multiply(1.0 / 16.0);
		Point2D currentPos = new Point2D(0, 0);
		for (int i = 0; i < 16; i++)
		{
			int x = 8 + (int)(currentPos.getX() * 8);
			int y = 3 + (int)(currentPos.getY() * 3);
			if (x > 0 && x < ret[0].length() && y > 0 && y < ret.length)
			{
				ret[y].setCharAt(x, 'X');
			}

			//System.out.println(Utils.toString(currentPos) + " -> " + x + " " + y);
			currentPos = currentPos.add(increment);
		}

		String retstr = "";
		for (var strbld : ret)
		{
			retstr += strbld.toString() + "\n";
		}
		return retstr + Utils.toString(p);
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

	public static boolean elementsExistInCollisionList(List<Collision> colList, Element a, Element b)
	{
		for (Collision col : colList)
		{
			if ((col.getA() == a && col.getB() == b) /*||
				(col.getB() == a && col.getA() == b)*/)
			{
				return true;
			}
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

	public static void renderText(GraphicsContext gc, Point2D target, String text)
	{
		renderText(gc, target.getX(), target.getY(), text);
	}

	public static void renderText(GraphicsContext gc, double targetX, double targetY, String text)
	{
		gc.setFontSmoothingType(FontSmoothingType.GRAY);
		gc.setFont(new Font("MS PMincho", 16 * 1.5));
		gc.setLineWidth(1.0);

		double x = 0, y = 0;
		for (int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if (c == ' ') { x++; continue; }
			else if (c == '\n') { x = 0; y++; continue; }
			
			gc.strokeText(new String(new char[] { c }), (targetX + (x * 8)) * Game.SCALE, 16 + (targetY + (y * 8)) * Game.SCALE);

			x++;
		}
	}
}
