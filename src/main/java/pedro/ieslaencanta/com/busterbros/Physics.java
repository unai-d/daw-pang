package pedro.ieslaencanta.com.busterbros;

import java.util.Optional;

import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;
import pedro.ieslaencanta.com.busterbros.basic.ElementMovable;

public class Physics
{
	public static void updatePhysics(Element a, Element b)
	{
		if (!updatePhysicsImpl(a, b))
		{
			if (!updatePhysicsImpl(b, a))
			{
				System.out.println("Collision between generics.");
			}
		}
	}

	private static boolean updatePhysicsImpl(Element a, Element b)
	{
		var c1 = a.getCenter();
		var c2 = b.getCenter();
		var directionVector = c2.subtract(c1);

		if (a instanceof ElementMovable)
		{
			var amov = (ElementMovable)a;

			if (b instanceof ElementMovable)
			{
				var bmov = (ElementMovable)b;

				amov.setSpeed(-directionVector.getX(), -directionVector.getY(), 0.1);
				bmov.setSpeed(directionVector.getX(), directionVector.getY(), 0.1);

				return true;
			}
		}

		return false;
	}
}
