package services.auth.interfaces;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationService {

    boolean authenticateFromRequest(HttpServletRequest request);

}
