package pedro.ieslaencanta.com.busterbros.basic;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.Utils;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IDebuggable;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IDrawable;

public class Element implements IDebuggable, IDrawable
{
    private boolean debug;
    //protected Rectangle2D rectangle;
	protected double x = 0, y = 0, width = 0, height = 0;
	@Deprecated
    protected Color color;
	protected Rectangle2D imageUv;
	protected Image image;

    public Element()
	{
        //this.rectangle = Rectangle2D.EMPTY;
    }

    public Element(double x, double y, double width, double height)
	{
        this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
    }

    public void setPosition(double x, double y)
	{
        this.x = x;
		this.y = y;
    }

	public void setSize(double w, double h)
	{
		this.width = w;
		this.height = h;
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

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

    public double getWidth()
	{
        return width;
    }

    public double getHeight()
	{
        return height;
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

	public Point2D getPosition()
	{
		return new Point2D(x, y);
	}

    public Rectangle2D getRectangle()
	{
        return new Rectangle2D(x, y, width, height);
    }

    public void setRectangle(Rectangle2D rectangle)
	{
        this.x = rectangle.getMinX();
		this.y = rectangle.getMinY();
		this.width = rectangle.getWidth();
		this.height = rectangle.getHeight();
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
		// Center point.

		gc.setFill(Color.WHITE);

        gc.fillOval(
			this.getCenterX() * Game.SCALE - 5,
			this.getCenterY() * Game.SCALE - 5,
			10,
			10
		);

		// Bounding box.

		gc.setLineWidth(2.0);

		Utils.renderRectangleBorder(gc, Color.WHITE, x, y, width, height);
		
		gc.setLineWidth(1.0);

		gc.strokeText(
			" X:" + (int)(this.getRectangle().getMinX()) + " Y:" + (int)(this.getRectangle().getMinY()),
			(this.getRectangle().getMinX() - 2) * Game.SCALE,
			(this.getRectangle().getMinY() - 2) * Game.SCALE
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
