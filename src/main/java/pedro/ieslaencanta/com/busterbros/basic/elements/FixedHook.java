package pedro.ieslaencanta.com.busterbros.basic.elements;

import java.util.Optional;

import javafx.geometry.Rectangle2D;
import pedro.ieslaencanta.com.busterbros.Resources;
import pedro.ieslaencanta.com.busterbros.Utils;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;
import pedro.ieslaencanta.com.busterbros.basic.ElementBullet;

public class FixedHook extends ElementBullet
{
	double lifespan = 0.0;
	boolean isStuck = false;

	public FixedHook(Player player)
	{
		x = player.getX();
		y = player.getY();
		width = 9.0;
		height = 8.0;
		timeToLive = 192.0;

		image = Resources.getInstance().getImage("weapons");
	}

	@Override
	public Optional<Collision> collision(Element e)
	{
		return Collision.getGenericCollision(this, e);
	}

	@Override
	public void update()
	{
		lifespan++;
		if (lifespan > timeToLive)
		{
			markedForDeletion = true;
		}
		if (!isStuck)
		{
			if (lifespan > (timeToLive / 2))
			{
				y += 2;
				resizeHeight(-2);
			}
			else
			{
				y -= 2;
				resizeHeight(2);
			}

			imageUv = getDynamicImageCoordinates();
			
			if (y <= 8) setStuckState(true);
		}
	}

	public boolean getStuckState()
	{
		return isStuck;
	}

	public void setStuckState(boolean isStuck)
	{
		this.isStuck = isStuck;
		if (isStuck) lifespan = 0.0;
	}

	private Rectangle2D getDynamicImageCoordinates()
	{
		int frame = Utils.clamp(((int)height - 34), 0, 192);
		if (frame >= 23) frame++;
		
		int row = frame / 24;
		int column = frame % 24;
		double facX = (frame % 24.0) / 24.0;
		int rowStart = (row == 0) ? 867 : (row == 1 ? 958 : 1103);
		int rowHeight = (row == 0) ? 83 : (row == 1 ? 137 : 191);
		int rowMinHeight = (row == 0) ? 34 : (row == 1 ? 86 : 140);

		double x = 8 + (column * 17);
		double y = rowStart;

		y += (1.0 - facX) * (rowHeight - rowMinHeight);
		return new Rectangle2D(x, y, 9, height);
	}
}
