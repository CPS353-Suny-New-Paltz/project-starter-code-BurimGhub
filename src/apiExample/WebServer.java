package apiExample;

public interface WebServer {

    LoginResponse login(LoginRequest loginRequest);

    ProfileLoadResponse loadProfile(ProfileLoadRequest profileLoadRequest);

    ProfileChangeResponse updateProfile(ProfileChangeRequest profileChangeRequest);

    LogoutResponse logout(UserIdentifier userId);
}
