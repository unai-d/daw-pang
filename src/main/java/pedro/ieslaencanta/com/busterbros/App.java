package pedro.ieslaencanta.com.busterbros;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application
{
	private Game game;
	private Canvas foregroundCanvas;
	private Canvas backgroundCanvas;
	private Scene scene;
	public static final int WIDTH = 384;
	public static final int HEIGHT = 208;

	@Override
	public void start(Stage stage)
	{
		foregroundCanvas = new Canvas(WIDTH * Game.SCALE, (HEIGHT + Game.INFOAREA) * Game.SCALE);
		backgroundCanvas = new Canvas(WIDTH * Game.SCALE, (HEIGHT + Game.INFOAREA) * Game.SCALE);

		Pane root = new Pane(backgroundCanvas, foregroundCanvas);

		scene = new Scene(root, WIDTH * Game.SCALE, (HEIGHT + Game.INFOAREA) * Game.SCALE);
		this.game = new Game(foregroundCanvas.getGraphicsContext2D(),
			backgroundCanvas.getGraphicsContext2D(),
			new Dimension2D(WIDTH, HEIGHT));
		this.registerKeys();
		stage.setResizable(false);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent t)
			{
				Platform.exit();
				System.exit(0);
			}
		});
		stage.setScene(scene);
		stage.show();
		this.game.start();
	}

	private void registerKeys()
	{
		scene.setOnKeyReleased(e ->
		{
			this.game.onKeyReleased(e.getCode());
		});
		scene.setOnKeyPressed(e ->
		{
			this.game.onKeyPressed(e.getCode());
		});
	}

	public static void main(String[] args)
	{
		launch();
	}
}
