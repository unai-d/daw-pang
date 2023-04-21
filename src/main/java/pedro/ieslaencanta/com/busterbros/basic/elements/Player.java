package pedro.ieslaencanta.com.busterbros.basic.elements;

import java.util.Optional;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import pedro.ieslaencanta.com.busterbros.App;
import pedro.ieslaencanta.com.busterbros.Clock;
import pedro.ieslaencanta.com.busterbros.Game;
import pedro.ieslaencanta.com.busterbros.Resources;
import pedro.ieslaencanta.com.busterbros.State;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;
import pedro.ieslaencanta.com.busterbros.basic.ElementWithGravity;

public class Player extends ElementWithGravity
{
	public static final double WIDTH = 30;
	public static final double HEIGHT = 32;

	private boolean lookingAtLeft = false;

	public Player(double x, double y)
	{
		setRectangle(new Rectangle2D(x, y, WIDTH, HEIGHT));
		setImage(Resources.getInstance().getImage("player"));
		setImageCoordinates(getPlayerImageCoordinates());
	}

	@Override
	public boolean isActive()
	{
		return isActiveHorizontalGravity() && isActiveVerticalGravity();
	}

	@Override
	public boolean isActiveHorizontalGravity()
	{
		return activeHorizontalGravity;
	}

	@Override
	public boolean isActiveVerticalGravity()
	{
		return activeVerticalGravity;
	}

	@Override
	public void activeGravity()
	{
		activeHorizontalGravity();
		activeVerticalGravity();
	}

	@Override
	public void activeHorizontalGravity() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'activeHorizontalGravity'");
	}

	@Override
	public void activeVerticalGravity() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'activeVerticalGravity'");
	}

	@Override
	public void unactiveGravity() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'unactiveGravity'");
	}

	@Override
	public void unactiveHorizontalGravity() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'unactiveHorizontalGravity'");
	}

	@Override
	public void unactiveVerticalGravity() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'unactiveVerticalGravity'");
	}

	@Override
	public void setHorizontalGravity(double gravity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setHorizontalGravity'");
	}

	@Override
	public void setVerticalGravity(double gravity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setVerticalGravity'");
	}

	@Override
	public double getHorizontalGravity() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getHorizontalGravity'");
	}

	@Override
	public double getVerticalGravity() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getVerticalGravity'");
	}

	@Override
	public void move(double x, double y)
	{
		// No.
	}

	@Override
	public void move(double x, double y, double lerp)
	{
		// No.
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'moveLeft'");
	}

	@Override
	public void moveLeft(double inc) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'moveLeft'");
	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'moveRight'");
	}

	@Override
	public void moveRight(double inc) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'moveRight'");
	}

	@Override
	public void moveUp() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'moveUp'");
	}

	@Override
	public void moveUp(double inc) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'moveUp'");
	}

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'moveDown'");
	}

	@Override
	public void moveDown(double inc) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'moveDown'");
	}

	@Override
	public void stopMovement() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'stopMovement'");
	}

	@Override
	public Optional<Collision> collision(Element e)
	{
		return Collision.getGenericCollision(this, e);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'start'");
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'stop'");
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'pause'");
	}

	@Override
	public State getState() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getState'");
	}

	@Override
	public void setState(State s) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setState'");
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

	@Override
	public void update()
	{
		double newX = getRectangle().getMinX() + vx;
		double newY = getRectangle().getMinY() + vy;
		setPosition(newX, newY);
		setImageCoordinates(getPlayerImageCoordinates());
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
			getRectangle().getMinX() * Game.SCALE,
			getRectangle().getMinY() * Game.SCALE,
			getWidth() * Game.SCALE,
			getHeight() * Game.SCALE
		);
	}

	private Rectangle2D getPlayerImageCoordinates()
	{
		lookingAtLeft = false;

		if (Math.abs(vx + vy) < 0.1)
		{
			return new Rectangle2D(12, 2, WIDTH, HEIGHT);
		}

		long tickCount = Clock.getCounter();
		int frame = (int)(tickCount / 4) % 5;
		lookingAtLeft = vx < 0;
		return new Rectangle2D(12 + (frame * 34), 2, WIDTH, HEIGHT);
	}
}
