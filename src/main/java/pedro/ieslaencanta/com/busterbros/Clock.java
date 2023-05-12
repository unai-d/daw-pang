/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.busterbros;

/**
 *
 * @author Administrador
 */
public abstract class Clock implements Runnable
{
    protected int frequency;
    protected int delta;
    protected static long counter;
    protected long time;
    private Thread t;
    protected Clock.ClockState state;
    private static final Object lock = new Object();

    public enum ClockState
	{
        STARTED,
        STOPED,
        PAUSED,
        CLOSED
    }

    public Clock(int frecuency)
	{
        this.frequency = frecuency;
        updateDelta();
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
        while (this.state == ClockState.STARTED)
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
            }
        }
		System.exit(0);
    }

    /**
     * incrementa la frecuencia
     */
    public void incrementFrequency()
	{
        this.frequency++;
        updateDelta();
    }

    /**
     * decrementa la frecuenca
     */
    public void decrementFrequency()
	{
        this.frequency--;
        updateDelta();
    }

	private void updateDelta()
	{
		delta = (int)(1000.0 / frequency);
	}
}
