package pl.mitelski.a238006.spoon;

import java.util.Random;

public class Spoon {
    private int[] spoons = {R.drawable.spoon_1, R.drawable.spoon_2, R.drawable.spoon_3};
    private int[] foodImgs = {R.drawable.access, R.drawable.hamburger, R.drawable.salad, R.drawable.sandwich, R.drawable.sausage};
    private boolean isEmpty = true;
    private int eatCount = 0;
    private int level = 0;
    private int lastFood;

    public int getFood() {
        Random rand = new Random();
        isEmpty = false;
        lastFood = foodImgs[0];

        if (rand.nextDouble() > 0.05) {
            lastFood = foodImgs[rand.nextInt(foodImgs.length-1) + 1];
        }
        return lastFood;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void eat() {
        eatCount += 1;
        setLevel();
        isEmpty = true;
    }

    private void setLevel() {
        if (lastFood == foodImgs[0]) {
            eatCount = 0;
            level = 0;
        } else {
            if (eatCount >= 10) {
                level = 2;
            } else if (eatCount >= 5) {
                level = 1;
            } else {
                level = 0;
            }
        }
    }

    public int getSpoon() {
        return spoons[level];
    }
}
