package apiExample;

public class WebServerPrototype {

    public void prototype(WebServer server) {
        // log in the user
        LoginResponse response = server.login(new LoginRequest());
        
        // load their profile
        if (!response.success()) {
            return;
        }
        
        ProfileLoadResponse profileResponse = 
                server.loadProfile(new ProfileLoadRequest(response.getUserId()));
        
        // make a change to the profile
        ProfileChangeResponse changeResponse = 
                server.updateProfile(new ProfileChangeRequest(response.getUserId()));
        
        // reload the updated version of the profile
        profileResponse = 
                server.loadProfile(new ProfileLoadRequest(response.getUserId()));
        
        // logout
        server.logout(response.getUserId());
    }
}
