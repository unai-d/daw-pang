package pedro.ieslaencanta.com.busterbros.basic;

import java.util.Optional;

import pedro.ieslaencanta.com.busterbros.basic.interfaces.IResizable;

public abstract class ElementResizable extends ElementDynamic implements IResizable
{
	protected double widthResizeIncrement = 1;
	protected double heightResizeIncrement = 1;

	@Override
	public void resizeWidth()
	{
		resizeWidth(widthResizeIncrement);
	}

	@Override
	public void resizeWidth(double inc)
	{
		width += inc;
	}

	@Override
	public void resizeHeight()
	{
		resizeHeight(heightResizeIncrement);
	}

	@Override
	public void resizeHeight(double inc)
	{
		height += inc;
	}

	@Override
	public void setDefaultIncWidth(double incw)
	{
		widthResizeIncrement = incw;
	}

	@Override
	public void setDefaultIncHeight(double inch)
	{
		heightResizeIncrement = inch;
	}
}
