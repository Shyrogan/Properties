package fr.shyrogan.properties;

public class SomeObject {

    private boolean isEnabled;

    final Property<Integer> adadadad = Property.<Integer>newProperty()
            .name("Switch switch switch")
            .defaultValue(1)
            .build();
    final Property<Integer> switchHits = Property.<Integer>newProperty()
            .name("Switch hits")
            .defaultValue(1)
            .eat(adadadad)
            .build();
    final Property<Mode> mode = Property.<Mode>newProperty()
            .name("Mode")
            .defaultValue(Mode.SWITCH)
            .eat(switchHits)
            .build();

    enum Mode {
        SWITCH,
        SINGLE
    }

}
