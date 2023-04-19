/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.busterbros;

/**
 *
 * @author Administrador
 */
public abstract class Clock implements Runnable {

    protected int frecuency;
    protected int delta;
    protected static long counter;
    protected long time;
    private Thread t;
    protected Clock.ClockState state;
    private static final Object lock = new Object();

    public enum ClockState {
        STARTED,
        STOPED,
        PAUSED,
        CLOSED
    }

    public Clock(int frecuency) {
        this.frecuency = frecuency;
        this.delta = (int) ((1.0f / (float) frecuency) * 1000);
        this.time = System.currentTimeMillis();
        this.state = ClockState.STOPED;
    }

    public void start()
	{
        if (this.t == null)
		{
            this.t = new Thread(this);
            this.t.start();
            this.state = ClockState.STARTED;
        }
    }

    /**
     * parar el reloj
     */
    public void stop() {
        this.state = ClockState.STOPED;
    }

    /**
     * pausar el reloj
     */
    public void pause() {
        this.state = ClockState.PAUSED;
    }

    /**
     * cerrar el reloj
     */
    public void close() {
        this.state = ClockState.CLOSED;
    }

    private void inc_counter() {
        synchronized (lock) {
            counter++;
        }
    }

    public static void reset_counter() {
        synchronized (lock) {
            counter = 0;
        }
    }

    public static long getCounter() {
        synchronized (lock) {
            return counter;
        }
    }

    protected abstract void onEvent();

    @Override
    public void run()
	{
        long actual;
        while (this.state != ClockState.CLOSED)
		{
            actual = System.currentTimeMillis();
            if (actual - this.time > this.delta)
			{
                try
				{
                    this.inc_counter();
                    this.onEvent();
                    this.time = actual;

                    Thread.sleep((long)this.delta);
                }
				catch (InterruptedException ex)
				{
                    // Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
                }
				catch (Exception ex)
				{
					stop();
					t = null;
					start();
				}
            }
        }

    }

    /**
     * incrementa la frecuencia
     */
    public void incFrecuency() {
        this.frecuency++;
        this.delta = (int) (1.0f / (float) frecuency) * 100;
    }

    /**
     * decrementa la frecuenca
     */
    public void decFrencuecy() {
        this.frecuency--;
        this.delta = (int) (1.0f / (float) frecuency) * 100;
    }

}
