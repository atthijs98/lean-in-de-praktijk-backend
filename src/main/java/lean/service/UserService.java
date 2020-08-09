package lean.service;

import lean.LeanInDePraktijkApplication;
import lean.LeanInDePraktijkApplication;
import lean.core.JwtHelper;
import lean.db.UserDAO;
import lean.util.MessageFactory.Language;
import lean.util.MessageFactory.Message;
import lean.util.MessageFactory.MessageFactory;
import lean.util.MessageFactory.MessageUtil;
import lean.api.User;
import lean.core.Body;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private MessageFactory messageFactory;

    public UserService() {
        userDAO = LeanInDePraktijkApplication.jdbiConnection.onDemand(UserDAO.class);
        messageFactory = new MessageFactory();
        userDAO.createTable();
    }

    /**
     * Login functionality, it tries to login using the userDAO.
     * It gets the password from the database and compares it with the password received from the frontend.
     *
     * @param email Form Param that contains the email, received from the user resource
     * @param frontendPassword Form Param that contains the password, received from the user resource
     * @return a build response with a Status, a Message and if it succeeds the content
     */
    public User login(String email, String frontendPassword) throws BadRequestException {
        String databasePassword = userDAO.getPasswordByEmail(email);

        if (databasePassword == null) {
            throw new BadRequestException();
        }

        BCrypt.Result result = BCrypt.verifyer().verify(frontendPassword.toCharArray(), databasePassword);

        if (!result.validFormat || !result.verified) {
            throw new BadRequestException(
                    Body.buildResponse(
                            Response.Status.BAD_REQUEST,
                            messageFactory.getMessageUtil(Language.english).get(Message.INVALID_PASSWORD) + "<br>" +
                                    messageFactory.getMessageUtil(Language.dutch).get(Message.INVALID_PASSWORD),
                            null
                    )
            );
        } else {
            User user = userDAO.getUserByEmail(email);
            user.setAuthToken(JwtHelper.createAuthToken(user.getId()));
            return user;
        }
    }


    /**
     * Register functionality, it tries to make a new account using the userDAO.
     * The password is encrypted using BCrypt.
     *
     * @param email Form Param that contains the email, received from the user resource
     * @param name Form Param that contains the name, received from the user resource
     * @param password Form Param that contains the password, received from the user resource
     * @return a build response with a Status and a Message
     */
    public User register(String email, String name, String password) throws BadRequestException{
        if (isValidPassword(password)) {
            password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            try {
                UUID uuid = UUID.nameUUIDFromBytes(email.getBytes(StandardCharsets.UTF_8));
                int id = userDAO.registerUser(uuid.toString(), email, name, password);
                return userDAO.getUserFromId(id);

            } catch (UnableToExecuteStatementException e) {
                //do nothing
            }
        }
        throw new BadRequestException();
    }

    /**
     * Checks if the password is valid
     *
     * @param password given when registering a new user
     * @return boolean if the password length is greater or equal to 8
     */
    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }


    public List<User> getUsers() {
        return userDAO.getUsers();
    }

    public Response getUserFromId(User authUser, int id) {
        MessageUtil util = messageFactory.getMessageUtil(authUser.getLanguage());

        User user = userDAO.getUserFromId(id);

        if (authUser.getId() != id) {
            user.setEmail(null);
            user.setLanguage(null);
            user.setAuthToken(null);
        }

        return Body.buildResponse(
                Response.Status.OK,
                util.get(Message.USER_FROM_ID_RETRIEVED),
                user
        );
    }



    /**
     *
     * @param id
     * @param privilege
     * @return
     */
    public int changeUserPrivilege(int id, int privilege) {
        return userDAO.changeUserPrivilege(id, privilege);
    }

    public User alterLanguage(int id, Language newLanguage) {
        userDAO.alterLanguage(id, newLanguage.toString());
        return userDAO.getUserFromId(id);
    }

    /**
     * Checks if new password is diffrent from old password.
     * If so, update password.
     *
     * @param id               Path Param that contains
     * @param frontendPassword Form Param that contains the password, received from the user resource
     * @return a build response with a Status, a Message and if it succeeds the content
     */
    public int changeUserPassword(int id, String frontendPassword, User user) throws BadRequestException {
        MessageUtil util = messageFactory.getMessageUtil(user.getLanguage());
        String currentPassword = userDAO.getPasswordById(id);

        if (currentPassword == null) {
            throw new BadRequestException();
        }

        BCrypt.Result result = BCrypt.verifyer().verify(frontendPassword.toCharArray(), currentPassword);

        if (!result.validFormat) {
            throw new BadRequestException(
                    Body.buildResponse(
                            Response.Status.BAD_REQUEST,
                            util.get(Message.INVALID_PASSWORD),
                            null
                    )
            );
        } else if (result.verified) {
            throw new BadRequestException(
                    Body.buildResponse(
                            Response.Status.BAD_REQUEST,
                            util.get(Message.PASSWORD_NOT_NEW),
                            null
                    )
            );
        } else {
            String password = BCrypt.withDefaults().hashToString(12, frontendPassword.toCharArray());
            return userDAO.setNewPassword(password, id);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public int getPrivilegeOfUser(int id) {
        return userDAO.getPrivilegeById(id);
    }
}
