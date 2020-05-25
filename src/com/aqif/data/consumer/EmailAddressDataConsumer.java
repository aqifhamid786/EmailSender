package com.aqif.data.consumer;

import com.aqif.data.provider.IDataProvider;


public class EmailAddressDataConsumer<T> {
    private final int batchSize;
    private IDataProvider<T> dataProvider;
    private ConsumableDataPartition[] partitions;

    public EmailAddressDataConsumer(int partitionsCount, int batchSize, IDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
        this.batchSize = batchSize;
        this.partitions = new ConsumableDataPartition[partitionsCount];

        int maxRecordsPerPartition = Math.round(dataProvider.recordsCount()/partitionsCount);

        for(int partitionId = 0; partitionId<partitionsCount; partitionId++) {
            int startPosition = maxRecordsPerPartition * partitionId;
            int endPosition = startPosition + maxRecordsPerPartition;

            if(endPosition>dataProvider.recordsCount())
                endPosition = dataProvider.recordsCount();

            partitions[partitionId] = new ConsumableDataPartition(startPosition, endPosition);
        }
    }

    public T[] consume(int partitionId) {
        ConsumableDataPartition consumable = partitions[partitionId];
        int start = consumable.getCurrentPosition();
        int count = consumable.consume(batchSize);

        return this.dataProvider.getRecords(start, count);
    }

    public boolean isConsumed() {
        boolean isConsumed = true;
        for(ConsumableDataPartition partition: partitions) {
            isConsumed &= partition.isConsumed();
        }
        return isConsumed;
    }
}
