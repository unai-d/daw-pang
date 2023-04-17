/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.busterbros.basic;

import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.media.MediaPlayer;
import javafx.util.Pair;
import pedro.ieslaencanta.com.busterbros.Resources;

/**
 *
 * @author Administrador
 */
public class Level {

    protected int x;
    protected int y;
    protected static int squar_size = 8;
    protected static int width = 384;
    protected static int height = 208;
    protected static int max_balls = 15;
    protected String soundname;
    protected String imagename;
    protected String backgroundname;

    private Block foreground[][];
    protected ArrayList<Pair<ElementType, Rectangle2D>> figures;

    protected int time;

    public Level() {
        this.figures = new ArrayList<>();
    }

    private enum Block {
        FIXEDHORIZONAL("FH"),
        FIXEDVERTICAL("FV"),
        FIXEDEND("FE"),
        FIXEDANDLADDER("FL"),
        FIXEDADDLADDERPROCESS("FP"),
        FIXEDENDANDLADDER("FX"),
        FIXEDENDANDLADDERPROCESS("LP"),
        BROKENHORIZONTAL("BH"),
        BROKENVERTICAL("BV"),
        BROKENEND("BE"),
        BROKENANDLADDER("BL"),
        BROKENENDANDLADDER("LY"),
        NONE("NO"),
        ICE("IC"),
        LADDER("LA"),
        BORDER("BO");

        private final String value;

        Block(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum ElementType {
        FIXED,
        BREAKABLE,
        LADDER

    }

    public boolean isLadder(int pixel) {
        return pixel == 0x00aa7700;
    }

    private boolean border(int pixel) {
        return pixel == 0x00880000 || pixel == 0x00770000 || pixel == 0x00000077;
    }

    private boolean isFixed(int pixel) {
        //las escalreas siempre en fijo
        return pixel == 0x00bb7700 || pixel == 0x00aa5577 || pixel == 0x006677cc || pixel == 0x00aa7700;
    }

    private boolean isIce(int pixel) {
        return pixel == 0x000066;
    }

    private boolean isLadderBlock(Block b) {
        return b == Block.LADDER || b == Block.FIXEDENDANDLADDER || b == Block.FIXEDENDANDLADDER
                || b == Block.FIXEDADDLADDERPROCESS || b == Block.FIXEDENDANDLADDERPROCESS;
    }

    public void analyze() {

        PixelReader r = this.getImage().getPixelReader();
        boolean horizontal;
        boolean vertical;
        boolean fijo;
        boolean ladder;
        boolean ice;
        this.foreground = new Block[26][48];
        for (int i = 0; i < foreground.length; i++) {
            for (int j = 0; j < foreground[i].length; j++) {
                horizontal = this.border(r.getArgb(j * 8 + this.getX(), i * 8 + this.getY() + 7) & 0x00ffffff);
                vertical = this.border(r.getArgb(j * 8 + this.getX() + 7, i * 8 + this.getY()) & 0x00ffffff);
                ladder = this.isLadder(r.getArgb(j * 8 + this.getX() + 4, i * 8 + this.getY() + 2) & 0x00ffffff);
                ice = this.isIce(r.getArgb(j * 8 + this.getX(), i * 8 + this.getY() + 7) & 0x00ffffff);
                if (ice) {
                    this.foreground[i][j] = Block.ICE;
                } else {
                    if (!horizontal && !vertical) {
                        if (ladder) {
                            this.foreground[i][j] = Block.LADDER;
                        } else {
                            this.foreground[i][j] = Block.NONE;
                        }
                    } else if (horizontal && vertical) {

                        fijo = this.isFixed(r.getArgb(j * 8 + this.getX() + 6, i * 8 + this.getY()) & 0x00ffffff);

                        if (fijo) {
                            if (ladder) {
                                this.foreground[i][j] = Block.FIXEDENDANDLADDER;
                            } else {
                                this.foreground[i][j] = Block.FIXEDEND;
                            }
                        } else {
                            this.foreground[i][j] = Block.BROKENEND;
                        }
                    } else if ((horizontal && !vertical)) {
                        fijo = this.isFixed(r.getArgb(j * 8 + this.getX(), i * 8 + this.getY() + 6) & 0x00ffffff);

                        if (fijo) {
                            if (ladder) {
                                this.foreground[i][j] = Block.FIXEDANDLADDER;
                            } else {
                                this.foreground[i][j] = Block.FIXEDHORIZONAL;
                            }
                        } else {
                            this.foreground[i][j] = Block.BROKENHORIZONTAL;
                        }
                    } else if (!horizontal && vertical) {
                        //en vertical no se puede tener escalera
                        fijo = this.isFixed(r.getArgb(j * 8 + this.getX() + 6, i * 8 + this.getY()) & 0x00ffffff);

                        if (fijo) {
                            this.foreground[i][j] = Block.FIXEDVERTICAL;
                        } else {
                            this.foreground[i][j] = Block.BROKENVERTICAL;
                        }
                    } else {
                        this.foreground[i][j] = Block.ICE;
                    }
                }
            }
        }
        this.generateFigures();
    }

    private void imprimir() {
        for (int i = 0; i < this.foreground.length; i++) {
            for (int j = 0; j < this.foreground[i].length; j++) {
                System.out.print((this.foreground[i][j] == Block.NONE) ? "_ " : this.foreground[i][j].getValue());
            }
            System.out.println();
        }
    }

    private void generateFigures() {
        int contadorx = 0, contadory = 0;
        Pair<ElementType, Rectangle2D> item;
        for (int i = 0; i < this.foreground.length; i++) {
            for (int j = 0; j < this.foreground[i].length; j++) {
                if (this.foreground[i][j] != null) {
                    contadorx = 1;
                    contadory = 1;
                    switch (this.foreground[i][j]) {
                        //se inicia un fijo horzontal
                        case FIXEDHORIZONAL:
                            this.foreground[i][j] = Block.NONE;
                        case FIXEDANDLADDER:
                            if (this.foreground[i][j] == Block.FIXEDANDLADDER) {
                                this.foreground[i][j] = Block.FIXEDADDLADDERPROCESS;
                            } else {
                                this.foreground[i][j] = Block.NONE;
                            }
                            while (this.foreground[i][j + contadorx] == Block.FIXEDHORIZONAL || this.foreground[i][j + contadorx] == Block.FIXEDEND
                                    || this.foreground[i][j + contadorx] == Block.FIXEDANDLADDER || this.foreground[i][j + contadorx] == Block.FIXEDENDANDLADDER) {
                                //se borra lo que no sea escalera
                                if (this.foreground[i][j + contadorx] == Block.FIXEDHORIZONAL || this.foreground[i][j + contadorx] == Block.FIXEDEND) {
                                    this.foreground[i][j + contadorx] = Block.NONE;
                                } else {
                                    //escaleras que se cruzan se cambia de tipo para no pisarlos
                                    if (this.foreground[i][j + contadorx] == Block.FIXEDANDLADDER) {
                                        this.foreground[i][j + contadorx] = Block.FIXEDADDLADDERPROCESS;

                                    } else if (this.foreground[i][j + contadorx] == Block.FIXEDENDANDLADDER) {
                                        this.foreground[i][j + contadorx] = Block.FIXEDENDANDLADDERPROCESS;
                                    }
                                }
                                contadorx++;
                            }

                            item = new Pair<>(ElementType.FIXED, new Rectangle2D(8 * j + this.getX(), 8 * i + this.getY(), 8 * contadorx, 8 * contadory));
                            this.figures.add(item);

                            break;

                        case FIXEDVERTICAL:
                            this.foreground[i][j] = Block.NONE;
                            while (this.foreground[i + contadory][j] == Block.FIXEDVERTICAL) {
                                this.foreground[i + contadory][j] = Block.NONE;
                                contadory++;
                            }
                            contadory++;
                            this.foreground[i + contadory][j] = Block.NONE;

                            item = new Pair<>(ElementType.FIXED, new Rectangle2D(8 * j + this.getX(), 8 * i + this.getY(), 8, 8 * contadory));
                            this.figures.add(item);

                            break;
                        case FIXEDEND:
                            this.foreground[i][j] = Block.NONE;
                            item = new Pair<>(ElementType.FIXED, new Rectangle2D(8 * j + this.getX(), 8 * i + this.getY(), 8, 8 * contadory));
                            this.figures.add(item);
                            break;
                        case BROKENHORIZONTAL:
                            this.foreground[i][j] = Block.NONE;
                            while (this.foreground[i][j + contadorx] == Block.BROKENHORIZONTAL) {
                                this.foreground[i][j + contadorx] = Block.NONE;
                                contadorx++;
                            }
                            //tiene que estar el del final
                            this.foreground[i][j + contadorx] = Block.NONE;
                            contadorx++;
                            item = new Pair<>(ElementType.BREAKABLE, new Rectangle2D(8 * j + this.getX(), 8 * i + this.getY(), 8 * contadorx, 8 * contadory));
                            this.figures.add(item);

                            break;
                        case BROKENVERTICAL:
                            this.foreground[i][j] = Block.NONE;
                            while (this.foreground[i + contadory][j] == Block.BROKENVERTICAL) {

                                this.foreground[i + contadory][j] = Block.NONE;
                                contadory++;
                            }
                            //queda el del final
                            this.foreground[i + contadory][j] = Block.NONE;
                            contadory++;
                            item = new Pair<>(ElementType.BREAKABLE, new Rectangle2D(8 * j + this.getX(), 8 * i + this.getY(), 8 * contadorx, 8 * contadory));
                            this.figures.add(item);

                            break;
                        case BROKENEND:
                            item = new Pair<>(ElementType.BREAKABLE, new Rectangle2D(8 * j + this.getX(), 8 * i + this.getY(), 8 * contadorx, 8 * contadory));
                            this.figures.add(item);

                            this.foreground[i][j] = Block.NONE;
                            break;
                        case NONE:
                            break;
                        default:
                        //System.out.println("Se queda sin procesar i:" + i + " j:" + j + " " + this.foreground[i][j]);
                    }

                }

            }
        }
        //procesando las escaleras

        for (int i = 0; i < this.foreground.length; i++) {
            for (int j = 0; j < this.foreground[i].length; j++) {
                switch (this.foreground[i][j]) {
                    case LADDER:
                    case FIXEDANDLADDER:
                    case FIXEDEND:
                    case FIXEDADDLADDERPROCESS:
                    case FIXEDENDANDLADDERPROCESS:
                        contadory = 0;
                        //si esta la fila completa
                        while (isLadderBlock(this.foreground[i + contadory][j])
                                && isLadderBlock(this.foreground[i + contadory][j + 1])
                                && isLadderBlock(this.foreground[i + contadory][j + 2])) {
                            (this.foreground[i + contadory][j]) = Block.NONE;
                            (this.foreground[i + contadory][j + 1]) = Block.NONE;
                            this.foreground[i + contadory][j + 2] = Block.NONE;
                            contadory++;
                        }

                        item = new Pair<>(ElementType.LADDER, new Rectangle2D(8 * j + this.getX(), 8 * i + this.getY(), 8 * 3, 8 * contadory));
                        this.figures.add(item);

                }
            }
        }
    }

    /*public ArrayList<Pair<ElementType, Rectangle2D>> getFigures() {
        return figures;
    }*/
    /**
     *
     * @return
     */
    public Pair<ElementType, Rectangle2D>[] getFigures() {
        Pair<ElementType, Rectangle2D> e[] = null;
        e = new Pair[this.figures.size()];
        for (int i = 0; i < e.length; i++) {
            e[i] = this.figures.get(i);
        }
        return e;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getYBackground() {
        return this.y - 36;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static int getSquar_size() {
        return squar_size;
    }

    public static void setSquar_size(int aSquar_size) {
        squar_size = aSquar_size;
    }

    public static int getWidth() {
        return width;
    }

    public Image getBackground() {
        return Resources.getInstance().getImage(this.backgroundname);
    }

    public void setBackgroundname(String backgroundname) {
        this.backgroundname = backgroundname;
    }

    public static void setWidth(int aWidth) {
        width = aWidth;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int aHeight) {
        height = aHeight;
    }

    public MediaPlayer getSound() {
        return Resources.getInstance().getSound(soundname);
    }

    public void setSoundName(String soundname) {
        this.soundname = soundname;
    }

    public Image getImage() {
        return Resources.getInstance().getImage(this.imagename);
    }

    public void setImagename(String imagepath) {
        this.imagename = imagepath;
    }

}
