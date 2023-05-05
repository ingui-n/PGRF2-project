package project;

import java.util.ArrayList;

public class Utils<E> {
    public int getRandomNumber(int range) {
        return (int) (Math.random() * range);
    }

    public int getRandomNumberInRage(int min, int max) {
        return (int) ((Math.random() * (max + 1 - min)) + min);
    }

    public int getRandomWithProbabilities(double[] probabilities) {
        double randomNum = Math.random() * 10;
        double totalProb = 0;

        for (int i = 0; i < probabilities.length; i++) {
            totalProb += probabilities[i];

            if (randomNum <= totalProb) {
                return i;
            }
        }

        return 0;
    }

    public E getRandomFromArrayList(ArrayList<E> arrayList) {
        return arrayList.get(getRandomNumber(arrayList.size()));
    }
}
