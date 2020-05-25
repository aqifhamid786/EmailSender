package com.aqif;

import com.aqif.data.consumer.EmailAddressDataConsumer;
import com.aqif.data.provider.EmailAddressDataProvider;
import com.aqif.data.provider.IDataProvider;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class EmailSender  implements EmailTask.IEmailTaskListener {

    public static void main(String[] args) {
        new EmailSender();
    }

    private ThreadPoolExecutor executor;
    private EmailAddressDataConsumer<Integer> dataConsumer;
    private IDataProvider<Integer> dataProvider;


    public EmailSender() {
        int partitionsCount = Utils.getNumberOfCores();

        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(partitionsCount);
        this.dataProvider = new EmailAddressDataProvider();
        this.dataConsumer = new EmailAddressDataConsumer(partitionsCount, Config.BATCH_SIZE, this.dataProvider);

        for(int partitionId=0; partitionId<partitionsCount; partitionId++) {
            this.executor.submit(new EmailTask(partitionId, this.dataConsumer, this));
        }
    }

    @Override
    public void OnSuccess(int taskId, int[] emailIds) {
        if(this.dataConsumer.isConsumed()) {
            this.executor.shutdown();
        }
        System.out.println(String.format("Sent for ids: %s", Arrays.toString(emailIds)));
    }

    @Override
    public void OnFailure(int taskId, int emailId) {
        System.out.println(String.format("Failed for id: %d", emailId));
    }

}
