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
	private Canvas primer_plano;
	private Canvas fondo;
	private Scene scene;
	public static final int WIDTH = 384;
	public static final int HEIGHT = 208;
	private int info = 48;

	@Override
	public void start(Stage stage)
	{
		//var javaVersion = SystemInfo.javaVersion();
		//var javafxVersion = SystemInfo.javafxVersion();

		primer_plano = new Canvas(WIDTH * Game.SCALE, (HEIGHT + Game.INFOAREA) * Game.SCALE); //Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
		fondo = new Canvas(WIDTH * Game.SCALE, (HEIGHT + Game.INFOAREA) * Game.SCALE); //Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

		Pane root = new Pane(fondo, primer_plano);

		scene = new Scene(root, WIDTH * Game.SCALE, (HEIGHT + Game.INFOAREA) * Game.SCALE);
		this.game = new Game(primer_plano.getGraphicsContext2D(),
			fondo.getGraphicsContext2D(),
			new Dimension2D(WIDTH, HEIGHT));
		this.registerKeys();
		stage.setResizable(false);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				// game.stop();
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
