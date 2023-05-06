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
import pedro.ieslaencanta.com.busterbros.basic.ElementBullet;
import pedro.ieslaencanta.com.busterbros.basic.ElementDynamic;
import pedro.ieslaencanta.com.busterbros.basic.Level;
import pedro.ieslaencanta.com.busterbros.basic.elements.Ball;
import pedro.ieslaencanta.com.busterbros.basic.elements.BreakableBrick;
import pedro.ieslaencanta.com.busterbros.basic.elements.Brick;
import pedro.ieslaencanta.com.busterbros.basic.elements.FixedCrossbow;
import pedro.ieslaencanta.com.busterbros.basic.elements.FixedHook;
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
	private boolean debugPhysics = true;
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
		int ballCount = 1 + (int)(Math.random() * 5);

        Pair<Level.ElementType, Rectangle2D>[] fi = this.levels[this.actual_level].getFigures();

        this.elements = new Element[256];

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
			double y = Math.random() * (App.HEIGHT / 4.0);

			Ball b = new Ball(x, y);
			b.activeVerticalGravity();
			b.setVerticalGravity(0.25);
			b.start();
			elements[i] = b;
		}

		this.elements[i] = new Player(App.WIDTH / 2, App.HEIGHT - Player.HEIGHT - 8);
		this.player = (Player)elements[i];
		this.player.activeGravity();
		this.player.setVerticalGravity(0.25);
		this.player.setWeapon(new FixedCrossbow());
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
		gc.setImageSmoothing(false);
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

		for (int i = 0; i < elements.length; i++)
		{
			var lhe = elements[i];

			if (lhe == null) continue;
			if (lhe instanceof ElementBullet)
			{
				var lheBullet = (ElementBullet)lhe;
				if (lheBullet.isMarkedForDeletion())
				{
					elements[i] = null;
					continue;
				}
			}
			if (lhe instanceof ElementDynamic)
			{
				var lheDynamic = (ElementDynamic)lhe;
				lheDynamic.update();

				for (int j = 0; j < elements.length; j++)
				{
					var rhe = elements[j];

					if (lhe == rhe) continue;
					if (rhe == null) continue;

					if (Utils.elementsExistInCollisionList(collisionDataList, rhe, lhe)) continue;

					var collisionData = lheDynamic.collision(rhe);
					if (collisionData.isPresent())
					{
						Collision col = collisionData.get();
						collisionDataList.add(col);

						// Update objects status based on collision data.
						//if (physicsEnabled)
						//{
							Physics.updatePhysics(col);
						//}

						// Player-specific events (e. g.: ladder climbing).
						if (lhe == player)
						{
							if (rhe instanceof Ladder) // Player is on ladder.
							{
								playerIsInsideLadder = true;
								if (up_press || down_press) player.setClimbingLadderMode(true);
							}
						}

						// Weapon/bullet events.
						if ((lhe instanceof ElementBullet && rhe instanceof Ball) || (lhe instanceof Ball && rhe instanceof ElementBullet))
						{
							boolean lheIsBullet = lhe instanceof ElementBullet;
							
							Ball ball = (Ball)(lheIsBullet ? rhe : lhe);
							Ball[] newBalls = ball.explode();

							// Add/replace balls.
							elements[lheIsBullet ? j : i] = newBalls[0];
							if (newBalls[1] != null) addElement(newBalls[1]);

							// Remove ElementBullet.
							elements[lheIsBullet ? i : j] = null;
						}

						if (lhe instanceof FixedHook && rhe instanceof Brick)
						{
							var fixedHook = (FixedHook)lhe;
							if (!fixedHook.getStuckState())
								fixedHook.setStuckState(true);
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

	private boolean addElement(Element element)
	{
		for (int i = 0; i < elements.length; i++)
		{
			if (elements[i] == null) 
			{
				elements[i] = element;
				return true;
			}
		}
		return false;
	}

    private void render()
	{
        this.clear();
        if (this.elements != null)
		{
            for (int i = 0; i < this.elements.length; i++)
			{
				if (this.elements[i] == null) continue;

                this.elements[i].paint(gc);
				
				if (this.elements[i].isDebug() || debug)
				{
					this.elements[i].debug(gc);
				}
            }
        }

		if (debugPhysics)
		{
			int i = 0;
			for (var col : collisionDataList)
			{
				col.debug(gc);
				gc.setLineWidth(1.0);
				gc.setStroke(Color.WHITE);
				gc.strokeText(col.toString(), 16, 16 + ++i * 12);
			}
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

			case SPACE:
				for (int i = 0; i < elements.length; i++)
				{
					if (elements[i] == null)
					{
						elements[i] = this.player.shoot();
						break;
					}
				}
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
				debugPhysics = !debugPhysics;
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
