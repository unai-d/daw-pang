package pedro.ieslaencanta.com.busterbros.basic.elements;

import java.util.Optional;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import pedro.ieslaencanta.com.busterbros.Clock;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.Resources;
import pedro.ieslaencanta.com.busterbros.State;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;
import pedro.ieslaencanta.com.busterbros.basic.ElementDynamic;
import pedro.ieslaencanta.com.busterbros.basic.ElementResizable;
import pedro.ieslaencanta.com.busterbros.basic.ElementWithGravity;
import pedro.ieslaencanta.com.busterbros.basic.Weapon;

public class Player extends ElementWithGravity
{
	public static final double WIDTH = 30;
	public static final double HEIGHT = 32;

	private Weapon weapon;
	private double px = 0;
	private double py = 0;

	private boolean lookingAtLeft = false;
	private boolean climbingLadder = false;
	private boolean isShooting = false;

	public Player(double x, double y)
	{
		setRectangle(new Rectangle2D(x, y, WIDTH / 2, HEIGHT));
		setImage(Resources.getInstance().getImage("player"));
		setImageCoordinates(getPlayerImageCoordinates());
	}

	@Override
	public Optional<Collision> collision(Element e)
	{
		if (e instanceof ElementDynamic)
		{
			return ((ElementDynamic)e).collision(this);
		}
		else if (e instanceof ViewportLimits)
		{
			return ((ViewportLimits)e).collision(this);
		}
		return Collision.getGenericCollision(this, e);
	}

	@Override
	public double getXSpeed()
	{
		return vx;
	}

	@Override
	public double getYSpeed()
	{
		return vy;
	}

	@Override
	public void setXSpeed(double x)
	{
		vx = x;
	}

	@Override
	public void setYSpeed(double y)
	{
		vy = y;
	}

	public void setWeapon(Weapon w)
	{
		weapon = w;
		weapon.setPlayer(this);
	}

	public ElementResizable shoot()
	{
		isShooting = true;
		return weapon.shoot();
	}

	@Override
	public void update()
	{
		if (climbingLadder)
		{
			vy *= 0.5;
			vx *= 0.5;
		}

		// Set new position.
		double newX = getRectangle().getMinX() + vx;
		double newY = getRectangle().getMinY() + vy;

		setPosition(newX, newY);
		setImageCoordinates(getPlayerImageCoordinates());

		// Prepare values for next tick (including gravity).
		px = 0; py = 0;

		// Update gravity.
		if (state == State.STARTED)
		{
			if (activeVerticalGravity) vy += gy;
			if (activeHorizontalGravity) vx += gx;
		}
	}

	@Override
	public void paint(GraphicsContext gc)
	{
		if (image == null || imageUv == null)
		{
			super.paint(gc);
			return;
		}

		gc.drawImage(
			image,
			lookingAtLeft ? imageUv.getMaxX() : imageUv.getMinX(),
			imageUv.getMinY(),
			imageUv.getWidth() * (lookingAtLeft ? -1 : 1),
			imageUv.getHeight(),
			(getRectangle().getMinX() - (WIDTH / 4)) * Game.SCALE,
			getRectangle().getMinY() * Game.SCALE,
			WIDTH * Game.SCALE,
			HEIGHT * Game.SCALE
		);
	}

	public boolean getClimbingLadderMode()
	{
		return climbingLadder;
	}

	public void setClimbingLadderMode(boolean isClimbingLadder)
	{
		System.out.println(isClimbingLadder ? "Climbing ladder." : "NOT climbing ladder.");
		climbingLadder = isClimbingLadder;
		activeVerticalGravity = !climbingLadder;
		activeHorizontalGravity = !climbingLadder;
		stopMovement();
	}

	public void moveAsPlayerInput(double x, double y)
	{
		if (!getClimbingLadderMode()) y = 0;

		px = x;
		py = y;

		move(x, y);

		if ((Math.abs(px) + Math.abs(py)) > 0.1)
		{
			isShooting = false;
		}
	}

	private Rectangle2D getPlayerImageCoordinates()
	{
		lookingAtLeft = false;

		long tickCount = Clock.getCounter();
		int frame = 0;

		if (climbingLadder)
		{
			if (Math.abs(px + py) > 0.1) frame = (int)(tickCount / 4) % 4;
			return new Rectangle2D(12 + (frame * 34), 36, WIDTH, HEIGHT);
		}
		else if (isShooting)
		{
			//frame = (int)(tickCount / 4) % 2;
			return new Rectangle2D(12 + (frame * 34), 36 + 34 + 8, WIDTH, HEIGHT);
		}
		else
		{
			if (Math.abs(px) > 0.1) frame = (int)(tickCount / 4) % 5;
			lookingAtLeft = px < 0;
			return new Rectangle2D(12 + (frame * 34), 2, WIDTH, HEIGHT);
		}
	}
}
