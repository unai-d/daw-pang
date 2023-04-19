package pedro.ieslaencanta.com.busterbros.basic;

import pedro.ieslaencanta.com.busterbros.State;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.ICollidable;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IState;

public abstract class ElementDynamic extends Element implements ICollidable, IState
{
	protected State state = State.STOPPED;

	public abstract void update();
}
