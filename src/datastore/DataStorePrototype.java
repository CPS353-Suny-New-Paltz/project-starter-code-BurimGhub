package datastore;


public class DataStorePrototype {

    public void prototype(DataStore store) {
        // Specifically: Store the string "Hello world"
        
        // store bytes
        
        DataStoreResponse hwKey = store.insertRequest(new DataRequest("Hello world".getBytes()));
        DataStoreResponse classKey = store.insertRequest(new DataRequest("CPS 353".getBytes()));
        
        if (hwKey.getResultCode().success()) {
            //        Later, get back the string that we stored (Hello world)
            String result = new String(store.loadData(hwKey.getId()));
        }

    }
}
