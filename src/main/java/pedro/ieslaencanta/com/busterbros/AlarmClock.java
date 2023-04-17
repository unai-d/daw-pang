/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.busterbros;

import java.util.ArrayList;
import java.util.Iterator;
import pedro.ieslaencanta.com.busterbros.basic.interfaces.IWarnClock;

/**
 *
 * @author Administrador
 */
public class AlarmClock {

    public enum TimeUnit {
        SECONDS("s", 1000),
        DECISECONDS("ds", 100),
        CENTISECONS("cs", 10),
        MILISECONDS("ms", 1);

        private final String unit;
        private final int value;

        private TimeUnit(String unit, int value) {
            this.unit = unit;
            this.value = value;
        }

        public String getUnit() {
            return this.unit;
        }

        public int getValue() {
            return this.value;
        }

    }
    private TimeUnit unit;
    private long limit;
    private ArrayList<IWarnClock> observers;
    private int substract;
    private long counter;
    private String name;

    /**
     *
     * @param value valor
     * @param unit unidad del valor
     * @param timecycle tiempo que dura un ciclo
     */
    public AlarmClock(String name, int value, TimeUnit unit, int timecycle) {
        this.limit = value * unit.getValue();
        this.unit = unit;
        this.substract = timecycle;
        this.counter = this.limit;
        this.name = name;
        this.observers = new ArrayList<>();

    }

    public void addListener(IWarnClock listener) {
        this.observers.add(listener);
    }

    public void removeListener(IWarnClock listener) {
        this.observers.remove(listener);
    }

    public void eventCycle() {
        this.counter -= this.substract;
        if (counter <= 0) {
            this.counter = this.limit;
            for (Iterator<IWarnClock> i = this.observers.iterator(); i.hasNext();) {
                i.next().TicTac(this.getName(), this.limit, this.unit);
            }

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
