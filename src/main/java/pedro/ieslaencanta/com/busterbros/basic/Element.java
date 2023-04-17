/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.busterbros.basic;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IDebuggable;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IDrawable;

/**
 *
 * @author Administrador
 */
public class Element implements IDebuggable, IDrawable {

    private boolean debug;
    protected Rectangle2D rectangle;
    private Color color;

    public Element() {

        this.rectangle = Rectangle2D.EMPTY;
    }

    public Element(double x, double y, double width, double height) {
        this.rectangle = new Rectangle2D(x, y, width, height);
    }

    public void setPosition(double x, double y) {
        this.rectangle = new Rectangle2D(x, y, this.rectangle.getWidth(), this.rectangle.getHeight());
    }

    @Override
    public void setDebug(boolean value) {
        this.debug = value;
    }

    @Override
    public boolean isDebug() {

        return this.debug;
    }

    public double getWidth() {
        return this.getRectangle().getWidth();
    }

    public double getHeight() {
        return this.getRectangle().getHeight();
    }

    public Point2D getCenter() {
        return new Point2D(this.getCenterX(), this.getCenterY());
    }

    public double getCenterX() {
        return this.getRectangle().getMinX() + this.getRectangle().getWidth() / 2;
    }

    public double getCenterY() {
        return this.getRectangle().getMinY() + this.getRectangle().getHeight() / 2;

    }

    public double getDistance(Element e) {
        return this.getCenter().distance(e.getCenter());
    }

    public Color getStokecolor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle2D rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void debug(GraphicsContext gc) {
        //gc.setStroke(Color.RED);
        gc.setFill(Color.WHITE);

        gc.fillOval(this.getRectangle().getMinX() * Game.SCALE - 5,
                this.getRectangle().getMinY() * Game.SCALE - 5,
                10, 10);

        gc.strokeText(" X:" + (int) (this.getRectangle().getMinX())
                + " Y:" + (int) (this.getRectangle().getMinY()),
                (this.getRectangle().getMinX()) * Game.SCALE,
                (this.getRectangle().getMinY()) * Game.SCALE);

        gc.fillOval(this.getRectangle().getMaxX() * Game.SCALE - 5,
                this.getRectangle().getMinY() * Game.SCALE - 5,
                10, 10);

        gc.strokeText(" X:" + (int) (this.getRectangle().getMaxX())
                + " Y:" + (int) (this.getRectangle().getMinY()),
                (this.getRectangle().getMaxX()) * Game.SCALE + 12,
                (this.getRectangle().getMinY()) * Game.SCALE);

        gc.fillOval(this.getRectangle().getMinX() * Game.SCALE - 5,
                this.getRectangle().getMaxY() * Game.SCALE - 5,
                10, 10);

        gc.strokeText(" X:" + (int) (this.getRectangle().getMinX())
                + " Y:" + (int) (this.getRectangle().getMaxY()),
                (this.getRectangle().getMinX()) * Game.SCALE,
                (this.getRectangle().getMaxY()) * Game.SCALE);

        gc.fillOval(this.getRectangle().getMaxX() * Game.SCALE - 5,
                this.getRectangle().getMaxY() * Game.SCALE - 5,
                10, 10);

        gc.strokeText(" X:" + (int) (this.getRectangle().getMaxX())
                + " Y:" + (int) (this.getRectangle().getMaxY()),
                (this.getRectangle().getMaxX()) * Game.SCALE,
                (this.getRectangle().getMaxY()) * Game.SCALE);

        gc.fillOval(this.getCenterX() * Game.SCALE - 5,
                this.getCenterY() * Game.SCALE - 5,
                10, 10);

        gc.strokeText(" X:" + (int) (this.getCenterX())
                + " Y:" + (int) (this.getCenterY()),
                (this.getCenterX()) * Game.SCALE,
                (this.getCenterY()) * Game.SCALE);
        gc.setFill(this.color);
        gc.fillRect(this.getRectangle().getMinX() * Game.SCALE, this.getRectangle().getMinY() * Game.SCALE, this.getRectangle().getWidth() * Game.SCALE, this.getRectangle().getHeight() * Game.SCALE);

    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(this.color);
        //se tendrá que sustituro por img
        gc.fillRect(this.getRectangle().getMinX() * Game.SCALE, this.getRectangle().getMinY() * Game.SCALE, this.getRectangle().getWidth() * Game.SCALE, this.getRectangle().getHeight() * Game.SCALE);
        /* if (this.isDebug()) {

            this.debug(gc);
        }*/
    }

}
