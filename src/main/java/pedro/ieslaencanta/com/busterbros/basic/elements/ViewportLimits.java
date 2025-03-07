package pedro.ieslaencanta.com.busterbros.basic.elements;

import java.util.Optional;

import javax.crypto.spec.RC2ParameterSpec;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import pedro.ieslaencanta.com.busterbros.App;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;

public class ViewportLimits extends Element
{
	public ViewportLimits(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Optional<Collision> collision(Element e)
	{
		int exteriorCollisions = 0;

		final double MAX_LENGTH = 1048576;
		Rectangle2D[] boundaries =
		{
			new Rectangle2D(-MAX_LENGTH, y + height, width + MAX_LENGTH * 2, MAX_LENGTH), // 1 - bottom
			new Rectangle2D(-MAX_LENGTH, -MAX_LENGTH, MAX_LENGTH * 2, MAX_LENGTH + y), // 2 - top
			new Rectangle2D(-MAX_LENGTH, -MAX_LENGTH, MAX_LENGTH + x, height + MAX_LENGTH * 2), // 3 - left
			new Rectangle2D(x + width, -MAX_LENGTH, MAX_LENGTH, height + MAX_LENGTH * 2) // 4 - right
		};

		int i = 1;
		for (Rectangle2D boundary : boundaries)
		{
			if (boundary.intersects(e.getRectangle()))
			{
				exteriorCollisions |= i;
				break;
			}
			i *= 2;
		}

		return (exteriorCollisions > 0) ? Optional.of(new Collision(this, e, i)) : Optional.empty();
	}

	@Override
	public void paint(GraphicsContext gc)
	{
		return;
	}
}
