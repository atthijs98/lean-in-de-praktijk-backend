package lean.api;

import lean.util.MessageFactory.Language;

import java.security.Principal;

public class User implements Principal {
    private int id;
    private String UUID;
    private String email;
    private String name;
    private int privilege;
    private String authToken;
    private Language language;

    public User(int id, String uuid, String email, String name, int privilege, Language language){}

    public User(int id, String UUID, String email, String name, int privilege, String authToken, Language language) {
        this.id = id;
        this.UUID = UUID;
        this.email = email;
        this.name = name;
        this.privilege = privilege;
        this.authToken = authToken;
        this.language = language;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getPrivilege() {
        return privilege;
    }

    public Language getLanguage() {
        return language;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUUID() {
        return UUID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
