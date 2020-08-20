package lean.api;


import lean.util.MessageFactory.Language;

import java.security.Principal;

/**
 * User class, represents the user entity in the data model.
 *
 * @author matthijs
 */
public class User implements Principal {
    private int id;
    private String UUID;
    private String email;
    private String name;
    private int privilege;
    private String authToken;
    private Language language;

    public User() {}

    public User(int id, String UUID, String email, String name, int privilege, Language language) {
        this.id = id;
        this.UUID = UUID;
        this.email = email;
        this.name = name;
        this.privilege = privilege;
        this.language = language;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getAuthToken() { return authToken; }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
