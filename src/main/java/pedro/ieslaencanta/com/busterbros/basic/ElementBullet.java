package pedro.ieslaencanta.com.busterbros.basic;

public abstract class ElementBullet extends ElementResizable
{
	protected double lifespan = 0.0;
	protected double timeToLive = 60.0;
	protected boolean markedForDeletion = false;

	public boolean isMarkedForDeletion()
	{
		return markedForDeletion;
	}
}
