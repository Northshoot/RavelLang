package ai.harmony.ravel.primitives.Fields;

/**
 * Created by lauril on 9/4/16.
 */
public class FieldOption<T> {
    String name;
    T value;

    public FieldOption() {}

    public FieldOption(String name, T value){
        this.name = name;
        this.value = value;
    }
}
