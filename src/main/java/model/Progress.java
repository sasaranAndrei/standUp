package model;
/*
    Clasa care indica progresul in cadrul unui Goal / Task.
    Valoare procentului dupa nivele:
    0 - Just Started
    1 - Something Done
    2 - Working
    3 - Almost Done
    4 - Done
    Valoarea progresului in procente:
    [0 - 20)    - nivel 0 - Just Started
    [20 - 40)   - nivel 1 - Something Done
    [40 - 70)   - nivel 2 - Working
    [70 - 100)  - nivel 3 - Almost Done
    100         - nivel 4 - Done
 */
public class Progress {
    private static final float PROGRESS_LIMITS[] = {0.2f, 0.4f, 0.7f, 1}; // folosit pt a determina niveleul
    private static final int PROGRESS_LEVEL[] = {0, 1, 2, 3, 4}; // folosit la afisari
    private static final String PROGRESS_LABEL[] = {"Just Started", "Something Done", "Working", "Almost Done", "Done"};// folosit la afisari
    private static final int FIRST_LEVEL  = 0;
    public static final int LAST_LEVEL   = 4;

    private float value; // procent [0,1]
    private int level; // [1..5]
    private String label; // descriere pe intelesul tuturor


    public Progress() {
        value = 0.0f;
        updateProgress(value);
    }

    // C preia din V procentul PROC, iar newValue = PROC / 100
    public void updateProgress(float newValue){
        if (newValue < value) {
            // exceptie [DEZV. ULTERIOARE]
        }

        value = newValue;
        // in loc sa fac if () else if else if ....
        level = calculateLevel(value);
        // selecteaza level-ul corect in functie de procentul
        // introdus de utilizator.

        // actualizam si label-ul
        label = PROGRESS_LABEL[level];
    }

    // functie care stabileste nivelul, in functie de procentul introdus de utilizator
    private int calculateLevel(float value) {
        for (int level = FIRST_LEVEL; level < LAST_LEVEL; level++){
            if (value < PROGRESS_LIMITS[level]) return level;
        }
        return LAST_LEVEL;
    }

    public int getLevel() {
        return level;
    }

    public float getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "value " + value
                + ", level " + level
                + ", " + label;
    }
}
