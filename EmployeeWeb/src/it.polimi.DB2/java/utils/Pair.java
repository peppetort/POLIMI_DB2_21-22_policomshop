package utils;

import java.util.Objects;

public class Pair<T, K> {
    private final T object1;
    private final K object2;

    public Pair(T object1, K object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    public T getObject1() {
        return object1;
    }

    public K getObject2() {
        return object2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(object1, pair.object1) && Objects.equals(object2, pair.object2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object1, object2);
    }
}
