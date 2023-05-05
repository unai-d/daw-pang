package pedro.ieslaencanta.com.busterbros.basic.elements;

import pedro.ieslaencanta.com.busterbros.basic.ElementResizable;
import pedro.ieslaencanta.com.busterbros.basic.Weapon;

public class FixedCrossbow extends Weapon
{
	public static int TTL = 10;
	public int counter = 10;

	@Override
	public ElementResizable shoot()
	{
		counter--;
		var ret = new FixedHook(player);
		ret.setPosition(player.getX(), player.getY());
		return ret;
	}
}
