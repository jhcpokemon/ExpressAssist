package io.github.jhcpokemon.expressassist.model;

import com.orm.SugarRecord;

import java.io.Serializable;

public class ExpressLog extends SugarRecord implements Serializable {
    public String order;
    public String date;
    public String name;
    public String code;

    public ExpressLog() {
    }

    public ExpressLog(String order, String date, String name, String code) {
        this.order = order;
        this.date = date;
        this.name = name;
        this.code = code;
    }
}
