package com.best.worm.storage;

public class Section {
    private int id;
    private long initValue;
    private long currentValue;

    public Section(int id, long initValue, long currentValue) {
        this.id = id;
        this.initValue = initValue;
        this.currentValue = currentValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getInitValue() {
        return initValue;
    }

    public void setInitValue(long initValue) {
        this.initValue = initValue;
    }

    public long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(long currentValue) {
        this.currentValue = currentValue;
    }

    public void step(int step) {
        this.currentValue += step;
    }

    public static int calcSectionId(int uid) {
        return uid / 100000 + 1;
    }

}
