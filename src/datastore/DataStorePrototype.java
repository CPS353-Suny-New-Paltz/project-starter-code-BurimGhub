package datastore;


public class DataStorePrototype {

    public void prototype(DataStore store) {
        // Store the string "Hello world"
        
        // store bytes
        
        store.insertRequest(new DataRequest("Hello world".getBytes()));
        
//        Later, get back the string that we stored (Hello world)

    }
}
