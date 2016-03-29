package io.github.jhcpokemon.expressassist.model;

import java.util.ArrayList;
import java.util.List;

public class ExpressLogProvider {
    private static List<ExpressLog> LOGS = new ArrayList<>();

    public static List<ExpressLog> getLOGS() {
        return ExpressLog.listAll(ExpressLog.class);
    }
}
