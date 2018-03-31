package com.javaedge.concurrency.aqs.semaphore;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * @author JavaEdge
 * @date 2021/4/22
 */
class Food {

    public String name;

    private long warmTime;

    public Food(String name, long warmTime) {
        this.name = name;
        this.warmTime = warmTime;
    }

    public String getName() {
        return name;
    }

    public long getWarmTime() {
        return warmTime;
    }
}

class MicrowaveOven {

    public String name;

    public MicrowaveOven(String name) {
        this.name = name;
    }

    public Food warm(Food food) {
        long second = food.getWarmTime() * 1000;
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("%s warm %s %d seconds food.", name, food.getName(), food.getWarmTime()));
        return food;
    }

    public String getName() {
        return name;
    }
}

/**
 * @author JavaEdge
 */
public class MicrowaveOvenPool {

    private List<MicrowaveOven> microwaveOvens;

    private Semaphore semaphore;

    public MicrowaveOvenPool(int size, @NotNull List<MicrowaveOven> microwaveOvens) {
        this.microwaveOvens = new Vector<>(microwaveOvens);
        this.semaphore = new Semaphore(size);
    }

    public Food exec(Function<MicrowaveOven, Food> func) {
        MicrowaveOven microwaveOven = null;
        try {
            semaphore.acquire();
            microwaveOven = microwaveOvens.remove(0);
            return func.apply(microwaveOven);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            microwaveOvens.add(microwaveOven);
            semaphore.release();
        }
        return null;
    }

}
