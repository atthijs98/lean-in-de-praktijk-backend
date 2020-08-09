package lean.resources;

import lean.util.MessageFactory.Language;
import lean.service.UserService;
import lean.util.MessageFactory.MessageFactory;
import lean.util.MessageFactory.MessageUtil;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

}
