/*
 * Copyright (c) Salvatore Penitente 2017.
 */

package main.java.model;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Tuple<T, L> {
    private T firstTupleElement;
    private L secondTupleElement;

    public Tuple() {}

    public Tuple(T firstTupleElement, L secondTupleElement) {
        this.firstTupleElement = firstTupleElement;
        this.secondTupleElement = secondTupleElement;
    }

    public T getFirstTupleElement() {
        return firstTupleElement;
    }

    public L getSecondTupleElement() {
        return secondTupleElement;
    }

    public void setFirstTupleElement(T firstTupleElement) {
        this.firstTupleElement = firstTupleElement;
    }

    public void setSecondTupleElement(L secondTupleElement) {
        this.secondTupleElement = secondTupleElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(firstTupleElement, tuple.firstTupleElement) &&
                Objects.equals(secondTupleElement, tuple.secondTupleElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstTupleElement, secondTupleElement);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "firstTupleElement=" + firstTupleElement +
                ", secondTupleElement=" + secondTupleElement +
                '}';
    }
}
