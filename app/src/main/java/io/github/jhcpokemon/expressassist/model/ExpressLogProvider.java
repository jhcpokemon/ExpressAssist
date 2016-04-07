package io.github.jhcpokemon.expressassist.model;

import java.util.Collections;
import java.util.List;

public class ExpressLogProvider {
    public static List<ExpressLog> getLOGS() {
        List<ExpressLog> logs = ExpressLog.listAll(ExpressLog.class);
        Collections.reverse(logs);
        return logs;
    }
}
