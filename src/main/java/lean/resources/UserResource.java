package lean.resources;

import com.google.gson.Gson;
import io.dropwizard.auth.Auth;
import lean.api.User;
import lean.api.UserPrivilege;
import lean.core.Body;
import lean.util.MessageFactory.Language;
import lean.service.UserService;
import lean.util.MessageFactory.MessageFactory;
import lean.util.MessageFactory.MessageUtil;
import org.jdbi.v3.core.Jdbi;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static lean.util.MessageFactory.Message.*;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private Jdbi dao;
    private UserService userService;
    private MessageFactory messageFactory;
    MessageUtil english;
    MessageUtil dutch;

    public UserResource() {
        userService = new UserService();
        messageFactory = new MessageFactory();

        english = messageFactory.getMessageUtil(Language.english);
        dutch = messageFactory.getMessageUtil(Language.dutch);
    }

    /**
     * Endpoint for logging in
     *
     * @param email Form Param that contains the email
     * @param password Form Param that contains the password
     * @return A response made returned int the user service
     */
    @Path("/login")
    @POST
    public Response login(@NotNull @FormParam("email") String email,
                          @NotNull @FormParam("password") String password) {
        try {
            User loggedInUser = userService.login(email, password);
            return Body.buildResponse(
                    Response.Status.OK,
                    messageFactory.getMessageUtil(loggedInUser.getLanguage()).get(LOGIN_SUCCESS),
                    loggedInUser);
        } catch (BadRequestException e) {
            return Body.buildResponse(Response.Status.BAD_REQUEST,
                    english.get(LOGIN_FAILED) + "<br>"
                            + dutch.get(LOGIN_FAILED),
                    null
            );
        }
    }

    @Path("/password/{id}")
    @POST
    public Response changeUserPassword(@PathParam("id") int id, @NotNull @FormParam("password") String password, @Auth User user) {
        try {
            return Body.buildResponse(
                    Response.Status.OK,
                    messageFactory.getMessageUtil(user.getLanguage()).get(USER_PASSWORD_CHANGED),
                    userService.changeUserPassword(id, password, user)
            );
        } catch (NotAuthorizedException e) {
            return e.getResponse();
        }
    }


    /**
     * Endpoint for registering a new user
     *
     * @param email Form Param that contains the email
     * @param name Form Param that contains the name
     * @param password Form Param that contains the password
     * @return A response made returned int the user service
     */
    @Path("/register")
    @POST
    public Response register(@NotNull @FormParam("email") String email,
                             @NotNull @FormParam("name") String name,
                             @NotNull @FormParam("password") String password) {
        try {
            User user = userService.register(email,name,password);
            return Body.buildResponse(
                    Response.Status.OK,
                    messageFactory.getMessageUtil(user.getLanguage()).get(ACCOUNT_CREATED),
                    user.getId()
            );
        } catch (BadRequestException e ) {
            return Body.buildResponse(
                    Response.Status.BAD_REQUEST,
                    english.get(REGISTER_FAILED) + "<br>" +
                            dutch.get(REGISTER_FAILED),
                    null
            );
        }
    }

    /**
     *
     * @return user
     *
     */
    @GET
    public Response getUsers(@Auth User user) {
        return Body.buildResponse(
                Response.Status.OK,
                messageFactory.getMessageUtil(user.getLanguage()).get(ALL_USERS_RETRIEVED),
                userService.getUsers()
        );
    }

    /**
     *
     * @param authUser
     * @param id
     * @return
     *
     */
    @Path("{id}")
    @GET
    public Response getUserFromId(@Auth User authUser, @PathParam("id") int id) {
        try {
            return userService.getUserFromId(authUser, id);
        } catch (NotAuthorizedException e) {
            return e.getResponse();
        }
    }

    /**
     *
     * @param id
     * @return
     *
     */
    @Path("privilege/{id}")
    @GET
    public Response getPrivilegeOfUser(@PathParam("id") int id, @Auth User user) {
        return Body.buildResponse(
                Response.Status.OK,
                messageFactory.getMessageUtil(user.getLanguage()).get(USER_PRIVILEGE_RETRIEVED),
                userService.getPrivilegeOfUser(id)
        );
    }

    /**
     *
     * @param id
     * @param userPrivilegeJSON
     * @return
     *
     */
    @Path("privilege/{id}")
    @POST
    public Response changeUserPrivilege(@PathParam("id") int id, String userPrivilegeJSON, @Auth User user) {
        UserPrivilege userPrivilege = new Gson().fromJson(userPrivilegeJSON, UserPrivilege.class);
        return Body.buildResponse(
                Response.Status.OK,
                messageFactory.getMessageUtil(user.getLanguage()).get(USER_PRIVILEGE_CHANGED),
                userService.changeUserPrivilege(id, userPrivilege.getPrivilege())
        );
    }

    @Path("language/alter/")
    @POST
    public Response alterUserLanguage(@Auth User user, Language newLanguage) {
        return Body.buildResponse(
                Response.Status.OK,
                messageFactory.getMessageUtil(newLanguage).get(USER_LANGUAGE_CHANGED),
                userService.alterLanguage(user.getId(), newLanguage)
        );
    }

}
