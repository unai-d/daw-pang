package pedro.ieslaencanta.com.busterbros.basic;

import pedro.ieslaencanta.com.busterbros.State;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.ICollidable;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IState;

public abstract class ElementDynamic extends Element implements ICollidable, IState
{
	protected State state = State.STOPPED;

	@Override
	public void start()
	{
		state = State.STARTED;
	}

	@Override
	public void stop()
	{
		state = State.STOPPED;
	}

	@Override
	public void pause()
	{
		state = State.PAUSED;
	}

	@Override
	public State getState()
	{
		return state;
	}

	@Override
	public void setState(State s)
	{
		state = s;
	}

	public abstract void update();
}
