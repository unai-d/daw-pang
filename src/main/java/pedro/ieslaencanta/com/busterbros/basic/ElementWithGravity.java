package pedro.ieslaencanta.com.busterbros.basic;

import pedro.ieslaencanta.com.busterbros.basic.interfaces.IGravity;

public abstract class ElementWithGravity extends ElementMovable implements IGravity
{
	protected double gx;
	protected double gy;
	protected boolean activeHorizontalGravity;
	protected boolean activeVerticalGravity;
}
