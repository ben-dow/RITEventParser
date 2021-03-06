package util;

public class Pair<X,Y> {

    private final X x;
    private final Y y;

    public Pair(X x, Y y ){
        this.x = x;
        this.y = y;
    }

    public X getKey() {
        return x;
    }

    public Y getValue(){
        return y;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + x +
                ", value=" + y +
                '}';
    }
}
