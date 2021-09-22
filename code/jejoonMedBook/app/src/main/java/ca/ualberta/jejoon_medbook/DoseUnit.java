package ca.ualberta.jejoon_medbook;

import java.util.Locale;

public enum DoseUnit {
    MG,
    MCG,
    DROP;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
