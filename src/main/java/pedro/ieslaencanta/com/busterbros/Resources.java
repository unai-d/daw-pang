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
	
    private String path_imagenes[][] =
	{
        {"backgrounds", "Backgrounds.png"},
        {"player", "Player.png"},
        {"animals", "Animals.png"},
        {"weapons", "ItemsWeapons.png"},
        {"ballons", "Balloons.png"},
        {"bricks", "Foreground.png"},
		{"ball_explosions", "ball_explosions.png"}

    };
    private String path_sonidos[][] =
	{
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
			try
			{
				String url = classLoader.getResource(path).toString();
            	this.imagenes.put(this.path_imagenes[i][0], new Image(url));
			}
			catch (Exception ex)
			{
				System.out.println("Error loading '" + path + "'\n" + ex.getMessage());
			}
        }

        for (int i = 0; i < this.path_sonidos.length; i++)
		{
			String path = this.path_sonidos[i][1];
			try
			{
				var url = classLoader.getResource(path).toString();
				var mediaPlayer = new MediaPlayer(new Media(url));
				this.sonidos.put(this.path_sonidos[i][0], mediaPlayer);
			}
			catch (Exception ex)
			{
				System.out.println("Error loading '" + path + "'\n" + ex.getMessage());
			}
        }
    }

    public static Resources getInstance()
	{
        if (Resources.resource == null)
		{
            Resources.resource = new Resources();
        }
        return Resources.resource;
    }

    public Image getImage(String name)
	{
        Image i = this.imagenes.get(name);
        return i;
    }

    public MediaPlayer getSound(String name)
	{
        return this.sonidos.get(name);
    }

	public String getLevelName(int level)
	{
		if (level >= 0 && level < 3)   return " MT.FUJI  "; // Original
		if (level >= 3 && level < 6)   return " MT.KEIRIN"; // Original
		if (level >= 6 && level < 9)   return " EMERALD\n TEMPLE"; // Original
		if (level >= 9 && level < 12)  return " ANKOR WATT"; // Original
		if (level >= 12 && level < 15) return "AYERS ROCK ";
		if (level >= 15 && level < 18) return " TAJ MAHAL ";
		if (level >= 18 && level < 21) return " LENINGRAD "; // Original
		if (level >= 21 && level < 24) return "  PARIS   ";
		if (level >= 24 && level < 27) return "  LONDON  ";
		if (level >= 27 && level < 30) return "BARCELONA "; // Original
		if (level >= 30 && level < 33) return "  ATHENS  "; // Original
		if (level >= 33 && level < 36) return "  EGYPT   ";
		if (level >= 36 && level < 39) return "KILIMANJARO";
		if (level >= 39 && level < 42) return " NEW YORK ";
		if (level >= 42 && level < 45) return "MAYAN RUINS";
		if (level >= 45 && level < 48) return "ANTARCTICA";
		if (level >= 48 && level < 50) return "  EASTER\n ISLAND ";
		return "";
	}
}
