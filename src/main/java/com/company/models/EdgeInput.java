package com.company.models;

public class EdgeInput {
    private String from;
    private String to;
    private int weight;

    public EdgeInput(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() { return from; }

    public String getTo() { return to; }

    public int getWeight() { return weight; }
}
