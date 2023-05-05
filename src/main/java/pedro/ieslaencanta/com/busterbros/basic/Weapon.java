package pedro.ieslaencanta.com.busterbros.basic;

import javafx.scene.image.Image;
import pedro.ieslaencanta.com.busterbros.basic.elements.Player;

public abstract class Weapon
{
	protected Player player;
	protected Image texture;
	protected ElementResizable[] ammo;

	public void setPlayer(Player p)
	{
		player = p;
	}

	public ElementResizable[] getAmmo()
	{
		return ammo;
	}

	public abstract ElementResizable shoot();
}