package pedro.ieslaencanta.com.busterbros.basic.elements;

import pedro.ieslaencanta.com.busterbros.basic.ElementResizable;
import pedro.ieslaencanta.com.busterbros.basic.Weapon;

public class FixedCrossbow extends Weapon
{
	public static int TTL = 10;
	public FixedHook currentShotHook = null;

	@Override
	public ElementResizable shoot()
	{
		if (currentShotHook != null && !currentShotHook.isMarkedForDeletion()) return null;

		currentShotHook = new FixedHook(player);
		currentShotHook.setPosition(player.getCenterX() - (currentShotHook.getWidth() / 2), (int)(player.getRectangle().getMaxY() - 8.5));
		return currentShotHook;
	}
}
