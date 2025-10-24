package com.company.models;

public class EdgeResult {
    private String from;
    private String to;
    private int weight;

    public EdgeResult(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() { return from; }

    public String getTo() { return to; }

    public int getWeight() { return weight; }

    @Override
    public String toString() {
        return from + "-" + to + "(" + weight + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EdgeResult)) return false;
        EdgeResult other = (EdgeResult) obj;
        return this.weight == other.weight &&
                ((this.from.equals(other.from) && this.to.equals(other.to)) ||
                        (this.from.equals(other.to) && this.to.equals(other.from)));
    }

}
