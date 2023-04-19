package pedro.ieslaencanta.com.busterbros.basic.interfaces;

public interface IGravity
{
	public boolean isActive();
	public boolean isActiveHorizontalGravity();
	public boolean isActiveVerticalGravity();
	public void activeGravity();
	public void activeHorizontalGravity();
	public void activeVerticalGravity();
	public void unactiveGravity();
	public void unactiveHorizontalGravity();
	public void unactiveVerticalGravity();
	public void setHorizontalGravity(double gravity);
	public void setVerticalGravity(double gravity);
	public double getHorizontalGravity();
	public double getVerticalGravity();
}
