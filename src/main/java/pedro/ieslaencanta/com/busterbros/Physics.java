package pedro.ieslaencanta.com.busterbros;

import javafx.geometry.Point2D;
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
			if (!updatePhysicsImpl(c, true))
			{
				//System.out.println("Collision between generics.");
			}
		}
	}

	private static boolean updatePhysicsImpl(Collision c, boolean reversed)
	{
		var a = reversed ? c.getB() : c.getA();
		var b = reversed ? c.getA() : c.getB();
		var c1 = a.getCenter();
		var c2 = b.getCenter();
		var distanceVector = c2.subtract(c1);

		if (b instanceof Ladder || b instanceof ElementBullet)
		{
			return true;
		}
		if (a instanceof Ball && !(b instanceof ViewportLimits)) // Ball -> !Viewport
		{
			var aBall = (Ball)a;
			
			double vx = aBall.getXSpeed();
			double vy = aBall.getYSpeed();
			double vmag = new Point2D(vx, vy).magnitude();
			Point2D finalSpeedVec = distanceVector.normalize().multiply(-vmag); // No friction.
			//System.out.println(finalSpeedVec.magnitude());

			if (b instanceof Brick)
			{
				finalSpeedVec = Utils.squarifyAngle(finalSpeedVec, 2.0);
			}

			System.out.println(vx + ", " + vy + " -> " + finalSpeedVec);

			aBall.setSpeed(finalSpeedVec.getX(), finalSpeedVec.getY());
			for (int i = 0; aBall.getRectangle().intersects(b.getRectangle()) && i < 4; i++)
			{
				aBall.move(finalSpeedVec.getX(), finalSpeedVec.getY());
			}
		}
		else if (a instanceof ElementMovable)
		{
			var amov = (ElementMovable)a;
			boolean aIsPlayer = a instanceof Player;

			if (b instanceof ViewportLimits)
			{
				double newX = Utils.clamp(amov.getX(), b.getX(), b.getX() + b.getWidth() - amov.getWidth());
				double newY = Utils.clamp(amov.getY(), b.getY(), b.getY() + b.getHeight() - amov.getHeight());
				amov.setPosition(newX, newY);

				if (amov instanceof Ball) // Ball -> Viewport
				{
					if ((c.getUserData() & 1) != 0) // Collided with bottom.
					{
						// TODO: Change 0.25 with constant.
						if (Math.abs(amov.getYSpeed()) < 0.25)
						{
							amov.setYSpeed(-0.25);
						}
						else
						{
							double newYSpeed = -amov.getYSpeed();
							//newYSpeed -= 0.25;
							//System.out.println(amov.getYSpeed() + " -> " + newYSpeed);
							amov.setYSpeed(newYSpeed);
						}
					}
					if ((c.getUserData() & 0b0110) != 0) // Collided with left/right boundaries.
					{
						double newXSpeed = amov.getXSpeed() * -0.98;
						//System.out.println(amov.getYSpeed() + " -> " + newXSpeed);
						amov.setXSpeed(newXSpeed);
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
			else if (!(aIsPlayer && ((Player)a).getClimbingLadderMode()) && (b instanceof Brick || b instanceof BreakableBrick))
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

				// Move the A-object center closer to the B-object center.
				var newXCenter = Utils.clamp(ac.getX(), bx1, bx2);
				var newYCenter = Utils.clamp(ac.getY(), by1, by2);
				var newDistanceVector = new Point2D(newXCenter, newYCenter).subtract(ac);
				distanceVector = distanceVector.interpolate(newDistanceVector, 0.95);
				if (distanceVector.magnitude() > 0.01)
				{
					var intersection = Utils.intersection(a.getRectangle(), b.getRectangle());
					var smallerWidth = Math.min(a.getWidth(), b.getWidth());
					var smallerHeight = Math.min(a.getHeight(), b.getHeight());
					var pushAngle = distanceVector.angle(new Point2D(1.0, 0.0)) / 180.0 * Math.PI;
					pushAngle = Utils.squarifyAngle(pushAngle, 4.0);
					Point2D pushVec = new Point2D(-Math.cos(pushAngle), -Math.sin(pushAngle));
					pushVec.multiply(distanceVector.magnitude());
					c.setPushVectors(pushVec, null);

					double xlerp = 2.0 * pushVec.getX() * (intersection.getWidth() / smallerWidth);
					double ylerp = 2.0 * pushVec.getY() * (intersection.getHeight() / smallerHeight);

					amov.setSpeed(xlerp, ylerp, 0.5);
					//System.out.println(bc.getY() + " -> " + ac.getY() + " = " + ylerp);
				}
				// else
				// {
				// 	System.out.println("Error: direction vector too short: " + distanceVector);
				// }
			}
			else if (b instanceof ElementMovable)
			{
				var bmov = (ElementMovable)b;
				Point2D adir = new Point2D(amov.getXSpeed(), bmov.getYSpeed());
				Point2D bdir = new Point2D(bmov.getXSpeed(), bmov.getYSpeed());

				if (c.getASurfacePoint().isPresent() && c.getBSurfacePoint().isPresent())
				{
					var aSurface = c.getASurfacePoint().get();
					var bSurface = c.getBSurfacePoint().get();
					var radii = aSurface.magnitude() + bSurface.magnitude();
					var distanceGap = distanceVector.magnitude() - radii;
					var magnitude = radii / distanceVector.magnitude();
					magnitude = Utils.clamp(magnitude, -1.0, 1.0);
					distanceVector = distanceVector.multiply(magnitude);
					System.out.println(distanceVector.magnitude() + " - " + radii + " = " + distanceGap + " fac " + (magnitude));
				}

				//amov.setSpeed(bdir.getX(), bdir.getY(), 0.1);
				//bmov.setSpeed(adir.getX(), adir.getY(), 0.1);

				amov.setSpeed(-distanceVector.getX(), -distanceVector.getY(), aIsPlayer ? 0.01 : 0.05);
				bmov.setSpeed(distanceVector.getX(), distanceVector.getY(), 0.05);
			}

			return true;
		}

		return false;
	}
}
