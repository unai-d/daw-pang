package pedro.ieslaencanta.com.busterbros.basic.elements;

import javafx.scene.canvas.GraphicsContext;
import pedro.ieslaencanta.com.busterbros.basic.Element;

public class Brick extends Element
{
	public Brick()
	{
		super();
	}

	public Brick(double x, double y, double width, double height)
	{
		super(x, y, width, height);
	}

	@Override
	public void paint(GraphicsContext gc)
	{
		super.paint(gc);
	}
}
