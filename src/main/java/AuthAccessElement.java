
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author majo
 */
@XmlRootElement
public class AuthAccessElement implements Serializable {

    public static final String PARAM_AUTH_ID = "auth-id";
    public static final String PARAM_AUTH_TOKEN = "auth-token";

    private String authId;
    private String authToken;
    private String authPermission;

    public AuthAccessElement() {
    }

    public AuthAccessElement(String authId, String authToken, String authPermission) {
        this.authId = authId;
        this.authToken = authToken;
        this.authPermission = authPermission;
    }

    
    // Getters and setters

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthPermission() {
        return authPermission;
    }

    public void setAuthPermission(String authPermission) {
        this.authPermission = authPermission;
    }
}