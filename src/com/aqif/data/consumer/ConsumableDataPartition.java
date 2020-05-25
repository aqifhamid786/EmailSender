package com.aqif.data.consumer;

class ConsumableDataPartition {

    private int currentPosition;
    private int endPosition;

    public ConsumableDataPartition(int startPosition, int endPosition) {
        this.currentPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int consume(int batchSize) {
        int count = batchSize;
        if(currentPosition+count>endPosition) {
            count = endPosition - currentPosition;
            currentPosition = endPosition;
        }else {
            currentPosition+=batchSize;
        }
        return count;
    }

    public boolean isConsumed() {
        return currentPosition == endPosition;
    }
}