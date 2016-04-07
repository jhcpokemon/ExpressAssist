package io.github.jhcpokemon.expressassist.model;

import com.orm.SugarRecord;

import java.io.Serializable;

public class ExpressLog extends SugarRecord implements Serializable {
    public String express_order;
    public String express_date;
    public String company_name;
    public String company_code;

    public ExpressLog() {
    }

    public ExpressLog(String express_order, String express_date, String company_name, String company_code) {
        this.express_order = express_order;
        this.express_date = express_date;
        this.company_name = company_name;
        this.company_code = company_code;
    }

    @Override
    public String toString() {
        return "ExpressLog{" +
                "express_order='" + express_order + '\'' +
                ", express_date='" + express_date + '\'' +
                ", company_code='" + company_code + '\'' +
                '}';
    }
}
