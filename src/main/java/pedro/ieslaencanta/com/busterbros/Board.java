/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.busterbros;

import java.util.ArrayList;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import pedro.ieslaencanta.com.busterbros.basic.Collision;
import pedro.ieslaencanta.com.busterbros.basic.Element;
import pedro.ieslaencanta.com.busterbros.basic.ElementDynamic;
import pedro.ieslaencanta.com.busterbros.basic.ElementMovable;
import pedro.ieslaencanta.com.busterbros.basic.Level;
import pedro.ieslaencanta.com.busterbros.basic.elements.Ball;
import pedro.ieslaencanta.com.busterbros.basic.elements.BreakableBrick;
import pedro.ieslaencanta.com.busterbros.basic.elements.Brick;
import pedro.ieslaencanta.com.busterbros.basic.elements.Ladder;
import pedro.ieslaencanta.com.busterbros.basic.elements.Player;
import pedro.ieslaencanta.com.busterbros.basic.elements.ViewportLimits;

/**
 * Tablero del juego, posee un fondo (imagen estática, solo se cambia cuando se
 * cambia el nivel), Una bola, un vector de niveles, un disparador y una matriz
 * de bolas gestiona la pulsación de tecla derecha e izquierda
 *
 * @author Pedro
 * @see Bubble Level Shttle BallGrid
 */
public class Board implements IKeyListener
{
    private Rectangle2D game_zone;

    private GraphicsContext gc;
    private GraphicsContext bggc;
    private final Dimension2D original_size;

    private boolean debug;
	private boolean physicsEnabled = true;
    private boolean left_press, right_press, up_press, down_press;
    private Level levels[];
    private int actual_level = -1;
    private MediaPlayer backgroundsound;
    private Element[] elements;
	private Player player;

	private ArrayList<Collision> collisionDataList = new ArrayList<Collision>();

    public Board(Dimension2D original)
	{
        this.gc = null;
        this.game_zone = new Rectangle2D(8, 8, 368, 192);
        this.original_size = original;
        this.right_press = false;
        this.left_press = false;
        this.up_press = false;
        this.down_press = false;

        this.debug = false;

        this.actual_level = 12;

        this.createLevels();
        this.nextLevel();
    }

    private void createLevels()
	{
        int y = 44;
        this.levels = new Level[50];
        for (int i = 0; i < 25; i++)
		{
            this.levels[2 * i] = new Level();
            this.levels[2 * i].setX(8);
            this.levels[2 * i].setY(y);
            this.levels[2 * i].setImagename("bricks");
            this.levels[2 * i].setBackgroundname("backgrounds");
            this.levels[2 * i].setSoundName("fondo");
            this.levels[2 * i].setTime(30);

            this.levels[2 * i + 1] = new Level();
            this.levels[2 * i + 1].setX(400);
            this.levels[2 * i + 1].setY(y);
            this.levels[2 * i + 1].setImagename("bricks");
            this.levels[2 * i + 1].setBackgroundname("backgrounds");
            this.levels[2 * i + 1].setSoundName("fondo");
            this.levels[2 * i + 1].setTime(30);

            y += 216;
        }
    }

    private void createElementsLevel()
	{
		int ballCount = 1 + (int)(Math.random() * 10);

        Pair<Level.ElementType, Rectangle2D>[] fi = this.levels[this.actual_level].getFigures();

        this.elements = new Element[fi.length + ballCount + 2];

		int i;
        for (i = 0; i < fi.length; i++)
		{
			double x = fi[i].getValue().getMinX() - this.levels[this.actual_level].getX();
			double y = fi[i].getValue().getMinY() - this.levels[this.actual_level].getY();
			double w = fi[i].getValue().getWidth();
			double h = fi[i].getValue().getHeight();

			Element e = null;
            switch (fi[i].getKey())
			{
                case FIXED:
					e = new Brick(x, y, w, h);
					e.setColor(Color.ORANGERED);
					e.setImage(Resources.getInstance().getImage("bricks"));
					e.setImageCoordinates(fi[i].getValue());
                    break;

                case BREAKABLE:
					e = new BreakableBrick(x, y, w, h);
					e.setColor(Color.ORANGE);
					e.setImage(Resources.getInstance().getImage("bricks"));
					e.setImageCoordinates(fi[i].getValue());
                    break;

                case LADDER:
					e = new Ladder(x, y, w, h);
					e.setColor(Color.BLUE);
					e.setImage(Resources.getInstance().getImage("bricks"));
					e.setImageCoordinates(fi[i].getValue());
                    break;

				default:
					e = new Element(x, y, w, h);
					e.setColor(Color.rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
					break;
            }
			elements[i] = e;
        }

		for (; i < fi.length + ballCount; i++)
		{
			double x = Math.random() * App.WIDTH;
			double y = Math.random() * App.HEIGHT;

			Ball b = new Ball(x, y, 0, 0);
			b.activeVerticalGravity();
			b.setVerticalGravity(0.25);
			b.start();
			elements[i] = b;
		}

		this.elements[i] = new Player(App.WIDTH / 2, App.HEIGHT - Player.HEIGHT - 8);
		this.player = (Player)elements[i];
		this.player.activeGravity();
		this.player.setVerticalGravity(0.25);
		this.player.start();

		i++;
		this.elements[i] = new ViewportLimits(8, 8, App.WIDTH - 16, App.HEIGHT - 16);
    }

    public void setGraphicsContext(GraphicsContext gc)
	{
        this.gc = gc;
		gc.setImageSmoothing(false);
    }

    public void setBackGroundGraphicsContext(GraphicsContext gc)
	{
        this.bggc = gc;
        this.paintBackground();
    }

    /**
     * cuando se produce un evento
     */
    public synchronized void TicTac()
	{
        this.process_input();

        this.update();
        this.render();
    }

    private void update()
	{
		collisionDataList.clear();

		boolean playerIsInsideLadder = false;

		for (var e : elements)
		{
			if (e == null) continue;
			if (e instanceof ElementDynamic)
			{
				var edyn = (ElementDynamic)e;
				edyn.update();

				for (var e2 : elements)
				{
					if (e == e2) continue;
					if (e2 == null) continue;

					var collisionData = edyn.collision(e2);
					if (collisionData.isPresent())
					{
						Collision col = collisionData.get();
						collisionDataList.add(col);

						// Update objects status based on collision data.
						if (physicsEnabled)
						{
							var a = col.getA();
							var b = col.getB();
							var c1 = a.getCenter();
							var c2 = b.getCenter();
							var directionVector = c2.subtract(c1);

							if (a instanceof ElementMovable)
							{
								var amov = (ElementMovable)a;

								if (b instanceof ViewportLimits)
								{
									amov.setSpeed(0, 0);
									var x = amov.getX();
									var y = Utils.clamp(amov.getY(), 8, App.HEIGHT - 8 - amov.getHeight());
									amov.setPosition(x, y);
								}
								else if (a instanceof Ball && b instanceof Ball)
								{
									var bmov = (Ball)b;
									amov.setSpeed(-directionVector.getX(), -directionVector.getY(), 0.1);
									bmov.setSpeed(directionVector.getX(), directionVector.getY(), 0.1);
								}
								else if (!(a instanceof Player && ((Player)a).getClimbingLadderMode()) && (b instanceof Brick || b instanceof BreakableBrick))
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
									directionVector = new Point2D(newXCenter, newYCenter).subtract(ac);
									if (directionVector.magnitude() > 0.01)
									{
										var intersection = Utils.intersection(a.getRectangle(), b.getRectangle());
										var smallerWidth = Math.min(a.getWidth(), b.getWidth());
										var smallerHeight = Math.min(a.getHeight(), b.getHeight());
										var pushAngle = directionVector.angle(new Point2D(1.0, 0.0)) / 180.0 * Math.PI;
										pushAngle = Utils.squarifyAngle(pushAngle, 4.0);
										Point2D pushVec = new Point2D(-Math.cos(pushAngle), -Math.sin(pushAngle));
										col.setPushVectors(pushVec, null);

										double xlerp = 4.0 * pushVec.getX() * (intersection.getWidth() / smallerWidth);
										double ylerp = 4.0 * pushVec.getY() * (intersection.getHeight() / smallerHeight);

										amov.setSpeed(xlerp, ylerp, 0.5);
										//System.out.println(bc.getY() + " -> " + ac.getY() + " = " + ylerp);
									}
									else
									{
										//System.out.println("Error: direction vector too short: " + directionVector);
									}
								}
							}
						}

						// Player-specific events (e. g.: ladder climbing).
						if (e == player)
						{
							if (e2 instanceof Ladder) // Player is on ladder.
							{
								playerIsInsideLadder = true;
								if (!player.getClimbingLadderMode())
								{
									if (up_press || down_press) player.setClimbingLadderMode(true);
								}
							}
						}
					}
				}
			}
		}

		if (!playerIsInsideLadder && player.getClimbingLadderMode())
		{
			player.setClimbingLadderMode(false);
		}
    }

    private void render()
	{
        this.clear();
        if (this.elements != null)
		{
            for (int i = 0; i < this.elements.length; i++)
			{
                this.elements[i].paint(gc);
				if (this.elements[i].isDebug() || debug)
				{
					this.elements[i].debug(gc);
				}
            }
        }

		for (var col : collisionDataList)
		{
			col.debug(gc);
		}
    }

    private void process_input()
	{
		double dx = left_press ? -1 : (right_press ? 1 : 0);
		double dy = up_press ? -1 : (down_press ? 1 : 0);
		dx *= 2;
		dy *= 2;

        player.moveAsPlayerInput(dx, dy);
    }

    /**
     * limpiar la pantalla
     */
    private void clear()
	{
        this.gc.restore();
        this.gc.clearRect(
			0,
			0,
			this.original_size.getWidth() * Game.SCALE,
			this.original_size.getHeight() * Game.SCALE
		);
    }

    /**
     * pintar el fonodo
     */
    public void paintBackground()
	{
        if (this.bggc != null)
		{
            this.bggc.clearRect(0, 0, this.original_size.getWidth() * Game.SCALE, (this.original_size.getHeight() + Game.INFOAREA) * Game.SCALE);
            this.bggc.setFill(Color.BLACK);
            this.bggc.fillRect(0, 0, this.original_size.getWidth() * Game.SCALE, (this.original_size.getHeight() + Game.INFOAREA) * Game.SCALE);
            if (this.gc != null)
			{
                this.gc.clearRect(0, 0, this.original_size.getWidth() * Game.SCALE, (this.original_size.getHeight() + Game.INFOAREA) * Game.SCALE);
            }

            this.bggc.drawImage(
				this.levels[actual_level].getBackground(),
                this.levels[actual_level].getX(),
				this.levels[actual_level].getYBackground(),
				this.original_size.getWidth(),
				this.original_size.getHeight(),
                0,
				0,
				this.original_size.getWidth() * Game.SCALE,
				this.original_size.getHeight() * Game.SCALE
			);
        }
    }

    /**
     * gestión de pulsación
     *
     * @param code
     */
    @Override
    public void onKeyPressed(KeyCode code)
	{
        switch (code)
		{
            case LEFT:
                this.left_press = true;
                break;

            case RIGHT:
                this.right_press = true;
                break;

            case UP:
                this.up_press = true;
                break;

            case DOWN:
                this.down_press = true;
                break;

			case N:
				this.nextLevel();
				break;

			case D:
				debug = !debug;
				for (var e : elements)
				{
					if (e == null) break;
					e.setDebug(!e.isDebug());
				}
				break;

			case P:
				physicsEnabled = !physicsEnabled;
				break;

			default:
				break;
        }
    }

    @Override
    public void onKeyReleased(KeyCode code)
	{
        switch (code)
		{
            case LEFT:
                this.left_press = false;
                break;

            case RIGHT:
                this.right_press = false;
                break;

            case UP:
                this.up_press = false;
                break;

            case DOWN:
                this.down_press = false;
                break;

			default:
				break;
        }
    }

    private void nextLevel()
	{
        this.actual_level++;
        if (this.actual_level >= this.levels.length)
		{
            this.actual_level = 0;
        }
        this.levels[this.actual_level].analyze();
        this.createElementsLevel();

        this.paintBackground();
        Game.reset_counter();
    }
}
