package pedro.ieslaencanta.com.busterbros.basic.interfaces;

import java.util.Optional;

import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;

public interface ICollidable
{
	public Optional<Collision> collision(Element e);
}
