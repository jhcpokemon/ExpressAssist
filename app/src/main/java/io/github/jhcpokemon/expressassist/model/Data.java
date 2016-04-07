package io.github.jhcpokemon.expressassist.model;

public class Data {
    private String time;
    private String context;

    public Data(String time, String context) {
        this.time = time;
        this.context = context;
    }

    public Data() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Data{" +
                "time='" + time + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
