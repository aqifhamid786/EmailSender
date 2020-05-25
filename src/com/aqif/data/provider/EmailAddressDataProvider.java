package com.aqif.data.provider;

import com.aqif.Config;

public class EmailAddressDataProvider implements IDataProvider<Integer> {

    private static int TOTAL_RECORDS = Config.NUMBER_OF_TUPLES;

    @Override
    public int recordsCount() {
        return TOTAL_RECORDS;
    }

    @Override
    public Integer[] getRecords(int position, int count) {

        int lastRecordId = position+count;
        if(lastRecordId>TOTAL_RECORDS)
            lastRecordId = TOTAL_RECORDS-1;

        Integer[] records = new Integer[lastRecordId-position];

        for(int recordId = position; recordId<lastRecordId; recordId++)
            records[recordId-position] = recordId;

        return records;
    }
}
