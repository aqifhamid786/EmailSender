package com.aqif;

import com.aqif.data.consumer.EmailAddressDataConsumer;

public class EmailTask implements Runnable {

    private int partitionId;
    private EmailAddressDataConsumer<Integer> consumer;
    private IEmailTaskListener listener;

    public EmailTask(int partitionId, EmailAddressDataConsumer<Integer> consumer, IEmailTaskListener listener) {
        this.partitionId = partitionId;
        this.consumer = consumer;
        this.listener = listener;
    }

    @Override
    public void run() {

        while(!this.consumer.isConsumed()) {
            int[] emailIds = unboxIntegerArray(this.consumer.consume(partitionId));

                for (int emailId=0; emailId<emailIds.length; emailId++) {
                    try {
                        Thread.currentThread().sleep(Config.EMAIL_SEND_SLEEP_TIME); // sending email
                    } catch (Exception e) {
                        if(this.listener!=null)
                            this.listener.OnFailure(this.partitionId ,emailId);
                    }
                }

            if(this.listener!=null)
                this.listener.OnSuccess(this.partitionId, emailIds);

        }
    }

    public static int[] unboxIntegerArray(Integer[] boxedArray) {
        int[] emailIds = new int[boxedArray.length];
        for (int index = 0; index<emailIds.length; index++) {
            emailIds[index] = boxedArray[index].intValue();
        }
        return emailIds;
    }

    public interface IEmailTaskListener {
        void OnSuccess(int taskId, int[] emailAddresses);
        void OnFailure(int taskId, int emailAddress);
    }
}