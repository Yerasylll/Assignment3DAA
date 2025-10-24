package com.company.metrics;

public class Metrics {
    private int operationsCount;
    private long executionTimeMs;

    public Metrics() {
        this.operationsCount = 0;
        this.executionTimeMs = 0;
    }

    public void incrementOperationsCount() { operationsCount++; }

    public int getOperationsCount() { return operationsCount; }

    public void setExecutionTimeMs(long time) { executionTimeMs = time; }

    public long getExecutionTimeMs() { return executionTimeMs; }


}

