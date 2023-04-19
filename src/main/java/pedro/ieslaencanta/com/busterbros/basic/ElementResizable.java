package pedro.ieslaencanta.com.busterbros.basic;

import pedro.ieslaencanta.com.busterbros.basic.interfaces.IResizable;

public abstract class ElementResizable extends ElementDynamic implements IResizable
{
	protected double widthResizeIncrement = 1;
	protected double heightResizeIncrement = 1;
}
