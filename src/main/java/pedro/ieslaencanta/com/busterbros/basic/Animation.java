package pedro.ieslaencanta.com.busterbros.basic;

import java.util.Map;
import java.util.Optional;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.Resources;

public class Animation implements Cloneable
{
	private static Map<String, Animation> animations = Map.of(
		"brick_1x3_ex", new Animation(
			"weapons",
			new Rectangle2D(8, 477, 8, 24),
			new Rectangle2D(24, 477, 8, 24),
			new Rectangle2D(40, 477, 8, 24),
			new Rectangle2D(56, 477, 8, 24),
			new Rectangle2D(72, 477, 8, 24)
		),
		"brick_1x4_ex", new Animation(
			"weapons",
			new Rectangle2D(102, 469, 8, 32),
			new Rectangle2D(118, 469, 8, 32),
			new Rectangle2D(134, 469, 8, 32),
			new Rectangle2D(150, 469, 8, 32),
			new Rectangle2D(165, 469, 8, 32)
		),
		"brick_1x1_ex", new Animation(
			"weapons",
			new Rectangle2D(8, 525, 8, 8),
			new Rectangle2D(24, 525, 8, 8),
			new Rectangle2D(40, 525, 8, 8),
			new Rectangle2D(56, 525, 8, 8),
			new Rectangle2D(72, 525, 8, 8)
		),
		"brick_2x1_ex", new Animation(
			"weapons",
			new Rectangle2D(8, 557, 16, 8),
			new Rectangle2D(32, 557, 16, 8),
			new Rectangle2D(56, 557, 16, 8),
			new Rectangle2D(80, 557, 16, 8),
			new Rectangle2D(104, 557, 16, 8)
		),
		"brick_3x1_ex", new Animation(
			"weapons",
			new Rectangle2D(8, 589, 24, 8),
			new Rectangle2D(40, 589, 24, 8),
			new Rectangle2D(72, 589, 24, 8),
			new Rectangle2D(104, 589, 24, 8),
			new Rectangle2D(136, 589, 24, 8)
		),
		"brick_4x1_ex", new Animation(
			"weapons",
			new Rectangle2D(8, 622, 32, 8),
			new Rectangle2D(48, 622, 32, 8),
			new Rectangle2D(88, 622, 32, 8),
			new Rectangle2D(128, 622, 32, 8),
			new Rectangle2D(168, 622, 32, 8)
		)
	);

	public static Optional<Animation> getAnimation(String name)
	{
		Animation ret = null;

		if (animations.containsKey(name))
		{
			try
			{
				ret = (Animation)animations.get(name).clone();
			}
			catch (CloneNotSupportedException ex)
			{
				System.out.println(ex.getMessage());
			}
		}

		return ret != null ? Optional.of(ret) : Optional.empty();
	}

	String textureName = null;
	int frameDuration = 2;
	double currentFrame = 0.0;
	Rectangle2D[] frames = {};
	Point2D position = Point2D.ZERO;

	public Animation(String textureName, Rectangle2D... frames)
	{
		this.textureName = textureName;
		this.frames = frames;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	public void setPosition(Point2D p)
	{
		position = p;
	}

	public void update()
	{
		currentFrame += 1.0 / frameDuration;
	}

	public void render(GraphicsContext gc)
	{
		Image image = Resources.getInstance().getImage(textureName);
		int currentFrame = (int)this.currentFrame;
		if (currentFrame >= frames.length) return;
		Rectangle2D imageUv = frames[currentFrame];

		gc.drawImage(
			image,
			imageUv.getMinX(),
			imageUv.getMinY(),
			imageUv.getWidth(),
			imageUv.getHeight(),
			position.getX() * Game.SCALE,
			position.getY() * Game.SCALE,
			imageUv.getWidth() * Game.SCALE,
			imageUv.getHeight() * Game.SCALE
		);
	}

	public boolean isMarkedForDeletion()
	{
		return currentFrame > frames.length;
	}
}
