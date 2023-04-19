package pedro.ieslaencanta.com.busterbros.basic.interfaces;

import pedro.ieslaencanta.com.busterbros.State;

public interface IState
{
	public void start();
	public void stop();
	public void pause();
	public State getState();
	public void setState(State s);
}
