package pedro.ieslaencanta.com.busterbros.basic;

import pedro.ieslaencanta.com.busterbros.basic.interfaces.IGravity;

public abstract class ElementWithGravity extends ElementMovable implements IGravity
{
	protected double gx;
	protected double gy;
	protected boolean activeHorizontalGravity;
	protected boolean activeVerticalGravity;

	@Override
	public boolean isActive()
	{
		return isActiveHorizontalGravity() && isActiveVerticalGravity();
	}

	@Override
	public boolean isActiveHorizontalGravity()
	{
		return activeHorizontalGravity;
	}

	@Override
	public boolean isActiveVerticalGravity()
	{
		return activeVerticalGravity;
	}

	@Override
	public void activeGravity()
	{
		activeHorizontalGravity();
		activeVerticalGravity();
	}

	@Override
	public void activeHorizontalGravity()
	{
		activeHorizontalGravity = true;
	}

	@Override
	public void activeVerticalGravity()
	{
		activeVerticalGravity = true;
	}

	@Override
	public void unactiveGravity()
	{
		unactiveHorizontalGravity();
		unactiveVerticalGravity();
	}

	@Override
	public void unactiveHorizontalGravity()
	{
		activeHorizontalGravity = false;
	}

	@Override
	public void unactiveVerticalGravity()
	{
		activeVerticalGravity = false;
	}

	@Override
	public void setHorizontalGravity(double gravity)
	{
		gx = gravity;
	}

	@Override
	public void setVerticalGravity(double gravity)
	{
		gy = gravity;
	}

	@Override
	public double getHorizontalGravity()
	{
		return gx;
	}

	@Override
	public double getVerticalGravity()
	{
		return gy;
	}
}
