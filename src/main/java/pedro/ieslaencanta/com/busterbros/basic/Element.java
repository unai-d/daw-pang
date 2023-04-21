package pedro.ieslaencanta.com.busterbros.basic;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IDebuggable;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IDrawable;

public class Element implements IDebuggable, IDrawable
{
    private boolean debug;
    protected Rectangle2D rectangle;
    private Color color;
	protected Rectangle2D imageUv;
	protected Image image;

    public Element()
	{
        this.rectangle = Rectangle2D.EMPTY;
    }

    public Element(double x, double y, double width, double height)
	{
        this.rectangle = new Rectangle2D(x, y, width, height);
    }

    public void setPosition(double x, double y)
	{
        rectangle = new Rectangle2D(x, y, rectangle.getWidth(), rectangle.getHeight());
    }

	public void setSize(double w, double h)
	{
		rectangle = new Rectangle2D(rectangle.getMinX(), rectangle.getMinY(), w, h);
	}

    @Override
    public void setDebug(boolean value)
	{
        this.debug = value;
    }

    @Override
    public boolean isDebug()
	{
        return this.debug;
    }

    public double getWidth()
	{
        return this.getRectangle().getWidth();
    }

    public double getHeight()
	{
        return this.getRectangle().getHeight();
    }

    public Point2D getCenter()
	{
        return new Point2D(this.getCenterX(), this.getCenterY());
    }

    public double getCenterX()
	{
        return this.getRectangle().getMinX() + this.getRectangle().getWidth() / 2;
    }

    public double getCenterY()
	{
        return this.getRectangle().getMinY() + this.getRectangle().getHeight() / 2;
    }

    public double getDistance(Element e)
	{
        return this.getCenter().distance(e.getCenter());
    }

    public Color getStokecolor()
	{
        return color;
    }

    public void setColor(Color color)
	{
        this.color = color;
    }

    public Rectangle2D getRectangle()
	{
        return rectangle;
    }

    public void setRectangle(Rectangle2D rectangle)
	{
        this.rectangle = rectangle;
    }

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}

	public Rectangle2D getImageCoordinates()
	{
		return imageUv;
	}

	public void setImageCoordinates(Rectangle2D uv)
	{
		imageUv = uv;
	}

    @Override
    public void debug(GraphicsContext gc)
	{
        gc.setFill(Color.WHITE);

        gc.fillOval(this.getRectangle().getMinX() * Game.SCALE - 5,
			this.getRectangle().getMinY() * Game.SCALE - 5,
			10, 10);

        gc.fillOval(this.getRectangle().getMaxX() * Game.SCALE - 5,
			this.getRectangle().getMinY() * Game.SCALE - 5,
			10, 10);

        // gc.strokeText(" X:" + (int) (this.getRectangle().getMaxX())
        //         + " Y:" + (int) (this.getRectangle().getMinY()),
        //         (this.getRectangle().getMaxX()) * Game.SCALE + 12,
        //         (this.getRectangle().getMinY()) * Game.SCALE);

        gc.fillOval(this.getRectangle().getMinX() * Game.SCALE - 5,
			this.getRectangle().getMaxY() * Game.SCALE - 5,
			10, 10);

        // gc.strokeText(" X:" + (int) (this.getRectangle().getMinX())
        //         + " Y:" + (int) (this.getRectangle().getMaxY()),
        //         (this.getRectangle().getMinX()) * Game.SCALE,
        //         (this.getRectangle().getMaxY()) * Game.SCALE);

        gc.fillOval(this.getRectangle().getMaxX() * Game.SCALE - 5,
			this.getRectangle().getMaxY() * Game.SCALE - 5,
			10, 10);

        // gc.strokeText(" X:" + (int) (this.getRectangle().getMaxX())
        //         + " Y:" + (int) (this.getRectangle().getMaxY()),
        //         (this.getRectangle().getMaxX()) * Game.SCALE,
        //         (this.getRectangle().getMaxY()) * Game.SCALE);

        // gc.strokeText(" X:" + (int) (this.getCenterX())
        //         + " Y:" + (int) (this.getCenterY()),
        //         (this.getCenterX()) * Game.SCALE,
        //         (this.getCenterY()) * Game.SCALE);

        gc.setFill(this.color);

        gc.fillRect(
			this.getRectangle().getMinX() * Game.SCALE,
			this.getRectangle().getMinY() * Game.SCALE,
			this.getRectangle().getWidth() * Game.SCALE,
			this.getRectangle().getHeight() * Game.SCALE
		);

		gc.setFill(Color.YELLOW);

        gc.fillOval(
			this.getCenterX() * Game.SCALE - 5,
			this.getCenterY() * Game.SCALE - 5,
			10,
			10
		);

		gc.setStroke(Color.WHITE);
		
		gc.strokeText(
			" X:" + (int)(this.getRectangle().getMinX()) + " Y:" + (int)(this.getRectangle().getMinY()),
			(this.getRectangle().getMinX()) * Game.SCALE,
			(this.getRectangle().getMinY()) * Game.SCALE
		);
    }

    @Override
    public void paint(GraphicsContext gc)
	{
		if (image == null || imageUv == null)
		{
			gc.setFill(this.color);
        	gc.fillRect(
				this.getRectangle().getMinX() * Game.SCALE,
				this.getRectangle().getMinY() * Game.SCALE,
				this.getRectangle().getWidth() * Game.SCALE,
				this.getRectangle().getHeight() * Game.SCALE
			);
			return;
		}
		
		gc.drawImage(
			image,
			imageUv.getMinX(),
			imageUv.getMinY(),
			imageUv.getWidth(),
			imageUv.getHeight(),
			getRectangle().getMinX() * Game.SCALE,
			getRectangle().getMinY() * Game.SCALE,
			getWidth() * Game.SCALE,
			getHeight() * Game.SCALE
		);
    }
}
