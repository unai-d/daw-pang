/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.busterbros;

import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Pedro
 */
public class Resources {

    private HashMap<String, Image> imagenes;
    private HashMap<String, MediaPlayer> sonidos;
    private String path_imagenes[][] = {
        {"backgrounds", "Backgrounds.png"},
        {"player", "Player.png"},
        {"animals", "Animals.png"},
        {"weapons", "ItemsWeapons.png"},
        {"ballons", "Balloons.png"},
        {"bricks", "Foreground.png"}

    };
    private String path_sonidos[][] = {
        {"pared", "BallBounce.wav"},
        {"romper", "DinoShot.wav"},
        {"disparo", "BubbleShot.wav"},
        {"fondo", "02-Mt. Fuji.mp3"}
    };
    private static Resources resource;

    {
        resource = null;

    }

    private Resources() {

        load();
    }

    private void load()
	{
        this.imagenes = new HashMap<>();
        this.sonidos = new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
		
        for (int i = 0; i < this.path_imagenes.length; i++)
		{
			String path = this.path_imagenes[i][1];
			String url = classLoader.getResource(path).toString();
			try
			{
            	this.imagenes.put(this.path_imagenes[i][0], new Image(url));
			}
			catch (Exception ex)
			{
				System.out.println("Error loading " + url + "\n" + ex.getMessage());
			}
        }

        for (int i = 0; i < this.path_sonidos.length; i++)
		{
			String path = this.path_sonidos[i][1];
			var url = classLoader.getResource(path).toString();
			try
			{
				var mediaPlayer = new MediaPlayer(new Media(url));
				this.sonidos.put(this.path_sonidos[i][0], mediaPlayer);
			}
			catch (Exception ex)
			{
				System.out.println("Error loading " + url + "\n" + ex.getMessage());
			}
        }
    }

    public static Resources getInstance() {
        if (Resources.resource == null) {
            Resources.resource = new Resources();
        }
        return Resources.resource;
    }

    public Image getImage(String name) {
        Image i = this.imagenes.get(name);
        return i;
    }

    public MediaPlayer getSound(String name) {
        return this.sonidos.get(name);
    }

}
