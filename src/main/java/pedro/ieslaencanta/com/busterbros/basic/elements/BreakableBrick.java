package pedro.ieslaencanta.com.busterbros.basic.elements;

public class BreakableBrick extends Brick
{
	public BreakableBrick()
	{
		super();
	}

	public BreakableBrick(double x, double y, double width, double height)
	{
		super(x, y, width, height);
	}

	public String getExplosionAnimationName()
	{
		return String.format("brick_%dx%d_ex", (int)width / 8, (int)height / 8);
	}
}