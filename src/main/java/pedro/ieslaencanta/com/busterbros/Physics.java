package pedro.ieslaencanta.com.busterbros;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.ElementBullet;
import pedro.ieslaencanta.com.busterbros.basic.ElementMovable;
import pedro.ieslaencanta.com.busterbros.basic.elements.Ball;
import pedro.ieslaencanta.com.busterbros.basic.elements.BreakableBrick;
import pedro.ieslaencanta.com.busterbros.basic.elements.Brick;
import pedro.ieslaencanta.com.busterbros.basic.elements.Ladder;
import pedro.ieslaencanta.com.busterbros.basic.elements.Player;
import pedro.ieslaencanta.com.busterbros.basic.elements.ViewportLimits;

public class Physics
{
	public static void updatePhysics(Collision c)
	{
		if (!updatePhysicsImpl(c, false))
		{
			updatePhysicsImpl(c, true);
		}
	}

	private static boolean updatePhysicsImpl(Collision c, boolean reversed)
	{
		var a = reversed ? c.getB() : c.getA();
		var b = reversed ? c.getA() : c.getB();
		var c1 = a.getCenter();
		var c2 = b.getCenter();
		var distanceVector = new Point2D(c2.getX() - c1.getX(), c2.getY() - c1.getY());

		if (b instanceof Ladder || b instanceof ElementBullet)
		{
			// Those types don't have collisions.
			return true;
		}
		else if (a instanceof ElementMovable)
		{
			var amov = (ElementMovable)a;
			boolean aIsPlayer = a instanceof Player;
			boolean bIsPlayer = b instanceof Player;

			if (b instanceof ViewportLimits)
			{
				double newX = Utils.clamp(amov.getX(), b.getX(), b.getX() + b.getWidth() - amov.getWidth());
				double newY = Utils.clamp(amov.getY(), b.getY(), b.getY() + b.getHeight() - amov.getHeight());
				amov.setPosition(newX, newY);

				if (amov instanceof Ball) // Ball -> Viewport
				{
					if ((c.getUserData() & 0b0011) != 0) // Collided with top and/or bottom boundaries.
					{
						// TODO: Change 0.25 with constant.
						if (Math.abs(amov.getYSpeed()) < 0.25)
						{
							amov.setYSpeed(-0.25);
						}
						else
						{
							amov.setYSpeed(-amov.getYSpeed());
						}
					}
					if ((c.getUserData() & 0b1100) != 0) // Collided with left and/or right boundaries.
					{
						amov.setXSpeed(-amov.getXSpeed());
					}
				}
				else
				{
					amov.setSpeed(0, 0, 0.1);
				}
			}
			else if (b instanceof Ladder)
			{
				// Do nothing.
			}
			else if (a instanceof Ball && b instanceof Ball)
			{
				// Disable ball to ball collision.
			}
			else if (a instanceof Ball && b instanceof Brick)
			{
				Ball aBall = (Ball)a;

				Rectangle2D collArea = Utils.intersection(a.getRectangle(), b.getRectangle());
				Rectangle2D collAreaNoX1 = new Rectangle2D(a.getX() + collArea.getWidth(), a.getY(), a.getWidth(), a.getHeight());
				Rectangle2D collAreaNoX2 = new Rectangle2D(a.getX() - collArea.getWidth(), a.getY(), a.getWidth(), a.getHeight());
				Rectangle2D collAreaNoY1 = new Rectangle2D(a.getY(), a.getY() + collArea.getHeight(), a.getWidth(), a.getHeight());
				Rectangle2D collAreaNoY2 = new Rectangle2D(a.getY(), a.getY() - collArea.getHeight(), a.getWidth(), a.getHeight());
				if (!collAreaNoX1.intersects(b.getRectangle()) || !collAreaNoX2.intersects(b.getRectangle()))
				{
					aBall.setXSpeed(-aBall.getXSpeed());
				}
				if (!collAreaNoY1.intersects(b.getRectangle()) || !collAreaNoY2.intersects(b.getRectangle()))
				{
					aBall.setYSpeed(-aBall.getYSpeed());
				}

				for (int i = 0; i < 16 && aBall.getRectangle().intersects(b.getRectangle()); i++)
				{
					aBall.move(aBall.getXSpeed(), aBall.getYSpeed());
				}
			}
			else if (!(aIsPlayer && ((Player)a).getClimbingLadderMode()) && (b instanceof Brick || b instanceof BreakableBrick)) // Non-climbing player -> Brick
			{
				var ax1 = amov.getX();
				var ax2 = ax1 + amov.getWidth();
				var bx1 = b.getX();
				var bx2 = bx1 + b.getWidth();

				var ay1 = amov.getY();
				var ay2 = ay1 + amov.getHeight();
				var by1 = b.getY();
				var by2 = by1 + b.getHeight();

				var ac = amov.getCenter();
				var bc = b.getCenter();

				// Move the player's center closer to the brick's center.
				var newXCenter = Utils.clamp(ac.getX(), bx1, bx2);
				var newYCenter = Utils.clamp(ac.getY(), by1, by2);
				var newDistanceVector = new Point2D(newXCenter, newYCenter).subtract(ac);
				distanceVector = distanceVector.interpolate(newDistanceVector, 0.95);
				double distance = distanceVector.magnitude();
				if (distance > 0.01)
				{
					var intersection = Utils.intersection(a.getRectangle(), b.getRectangle());
					var smallerWidth = Math.min(a.getWidth(), b.getWidth());
					var smallerHeight = Math.min(a.getHeight(), b.getHeight());
					
					Point2D pushVec = Utils.squarifyAngle(distanceVector, 4.0).multiply(-1);
					c.setPushVectors(pushVec, null);

					double newX = 4.0 * pushVec.getX() * ((intersection.getWidth() / smallerWidth) / distance);
					double newY = 4.0 * pushVec.getY() * ((intersection.getHeight() / smallerHeight) / distance);
	
					amov.setSpeed(newX, newY, 0.5);

					newX /= 64.0;
					newY /= 64.0;
					for (int i = 0; amov.getRectangle().intersects(b.getRectangle()) && i < 6; i++)
					{
						amov.move(newX, newY);
						newX *= 2;
						newY *= 2;
					}
					//System.out.println(bc.getY() + " -> " + ac.getY() + " = " + ylerp);
				}
			}
			else if (b instanceof ElementMovable) // This is unused.
			{
				var bmov = (ElementMovable)b;

				if (c.getASurfacePoint().isPresent() && c.getBSurfacePoint().isPresent())
				{
					var aSurface = c.getASurfacePoint().get();
					var bSurface = c.getBSurfacePoint().get();
					var radii = aSurface.magnitude() + bSurface.magnitude();
					double distance = distanceVector.magnitude();
					var penetration = radii - distance; // This shouldn't be a negative value.
					var magnitude = (radii / distance) - 1.0;
					magnitude = Utils.clamp(magnitude, 0.0, 10.0);
					distanceVector = distanceVector.multiply(magnitude);
					System.out.println("[PHY] [EMOV→EMOV] " + radii + " - " + distance + " = " + penetration + " fac " + magnitude);
				}

				amov.setSpeed(-distanceVector.getX(), -distanceVector.getY(), aIsPlayer ? 0.01 : 0.05);
				bmov.setSpeed(distanceVector.getX(), distanceVector.getY(), bIsPlayer ? 0.01 : 0.05);
			}

			return true;
		}

		return false;
	}
}
