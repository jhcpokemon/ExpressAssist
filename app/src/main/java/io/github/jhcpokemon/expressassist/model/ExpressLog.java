package io.github.jhcpokemon.expressassist.model;

import com.orm.SugarRecord;

public class ExpressLog extends SugarRecord{
    public String order;
    public String date;

    public ExpressLog() {
    }

    public ExpressLog(String order, String date) {
        this.order = order;
        this.date = date;
    }
}
