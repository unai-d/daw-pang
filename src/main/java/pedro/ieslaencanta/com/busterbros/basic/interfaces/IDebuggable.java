package pedro.ieslaencanta.com.busterbros.basic.interfaces;

import javafx.scene.canvas.GraphicsContext;

public interface IDebuggable
{
    public void setDebug(boolean value);
    public boolean isDebug();
    public void debug(GraphicsContext gc);
}
