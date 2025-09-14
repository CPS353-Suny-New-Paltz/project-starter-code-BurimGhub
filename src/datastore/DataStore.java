package datastore;

public interface DataStore {

     DataStoreResponse insertRequest(DataRequest dataRequest);

     byte[] loadData(int dataId);
}
