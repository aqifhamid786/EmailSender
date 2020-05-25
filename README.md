# EmailSender

This is a mass email sending emulator. It is designed to exploit multi core architecture for fast processing of email sending.

## Aproach
The base of our solution is to exploit multi core architecure. We deploy N threads to fully utilize N cores. To avoid any contention, we divide data source into N disjoint partitions. Each of the partition is consumed by each of the thread independently.

## Components
* **IDataProvider** is a source of data. Under the hood, it could be an interface to a database, web endpoint, static file reader, on the fly data generator, or any other data source.
* **DataConsumer** is used as an interface to efficiently read data from the data source. It also keeps track of current pointers to the data and how much data is read at any point.
* **EmailSender** is responsible to simply read data via DataConsumer to process it.

## Assumptions
* We assume that data is readily available at data provider. Or a dedicated thread is reading from data source, fast enough to meet data consumer's throughput.
* We also assume that data reading is not I/O bound.
* We further assume that the thread sleeping to mimic email sending is actual work done by the thread. Thus in actual use case, increasing the number of threads beyond N may adversly effect application throughput.

## Configuration
* BatchSize is number of tuples read from DataConsumer in a single call. This is be used to further fine tune overhead of calling DataConsumer. How frequently you want to fetch data from DataConsumer?
* Right now number of available Cores is hardcoded in Config.java file.
* Number of total tuples (email addresses) is set to 1000. One can easily modify this number in Config.java file to mimic processing of 1Mi or any number of tuple.
