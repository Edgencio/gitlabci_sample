package mz.co.technosupport.security;

import mz.co.technosupport.service.UserService;
import mz.co.technosupport.util.Log;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * @author Americo Chaquisse
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter{

    @Inject
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        final String token = authorizationHeader.substring("Bearer".length()).trim();

        requestContext.setSecurityContext(new SecurityContext() {
            public Principal getUserPrincipal() {
                return new Principal() {
                    public String getName() {

                        return userService.getUserByToken(token).getUsername();
                    }
                };
            }

            public boolean isUserInRole(String s) {
                return true;
            }

            public boolean isSecure() {
                return false;
            }

            public String getAuthenticationScheme() {
                return null;
            }
        });

        try {

            // Validate the token
            if(!userService.validateToken(token)){
                throw new Exception("invalid token");
            }

        } catch (Exception e) {
            Log.info("invalid auth token "+token);
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }

    }


}
