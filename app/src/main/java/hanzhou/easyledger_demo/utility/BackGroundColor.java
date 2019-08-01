package hanzhou.easyledger_demo.utility;

import android.graphics.Color;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.Random;

public class BackGroundColor {

    private Random random;
    private ArrayList<Integer> output;

    private SparseBooleanArray colorsHM;


    public BackGroundColor() {

        random = new Random();
    }

    public ArrayList<Integer> getNonRepeatingDarkColors(int number) {
        colorsHM = new SparseBooleanArray();
        output = new ArrayList<>();
        int temp = 0;
        for (int i = 0; i < number; i++) {
            temp = generateADarkColor();
            while (colorsHM.get(temp)) {
                temp = generateADarkColor();
            }
            output.add(temp);
            colorsHM.put(temp, true);
        }
        return output;
    }

    public ArrayList<Integer> getNonRepeatingLightColors(int number) {
        colorsHM = new SparseBooleanArray();
        output = new ArrayList<>();
        int temp = 0;
        for (int i = 0; i < number; i++) {
            temp = generateALightColor();
            while (colorsHM.get(temp)) {
                temp = generateALightColor();
            }
            output.add(temp);
            colorsHM.put(temp, true);
        }
        return output;
    }


    private int generateADarkColor() {
        return Color.argb(
                255,
                (random.nextInt(100) + 100),
                (random.nextInt(100) + 100),
                (random.nextInt(100) + 100));
    }

    private int generateALightColor() {
        return Color.argb(
                255,
                (random.nextInt(50) + 200),
                (random.nextInt(50) + 200),
                (random.nextInt(50) + 200));
    }
}
