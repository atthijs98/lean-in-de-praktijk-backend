package lean.db;

import lean.db.mappers.UserMapper;
import lean.api.User;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * UserDAO, is used to execute sql queries on the database.
 *
 */
@RegisterRowMapper(UserMapper.class)
public interface UserDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS person ( " +
            "id SERIAL, " +
            "UUID CHAR(36) UNIQUE NOT NULL, " +
            "email VARCHAR(256) UNIQUE NOT NULL, " +
            "name VARCHAR(256) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "privilege INT DEFAULT 0, " +
            "PRIMARY KEY (id) " +
            ");")
    void createTable();

    @SqlQuery("SELECT * FROM person WHERE id = :id")
    User getUserFromId(@Bind("id") int id);

    @SqlUpdate("INSERT INTO person (UUID, email, name, password) VALUES (:uuid, :email, :name, :password)")
    @GetGeneratedKeys("id")
    int registerUser(@Bind("uuid") String uuid,
                     @Bind("email") String email,
                     @Bind("name") String name,
                     @Bind("password") String password);

    @SqlQuery("SELECT * FROM person ORDER BY id")
    List<User> getUsers();

    @SqlQuery("SELECT * FROM person WHERE email = :email")
    User getUserByEmail(@Bind("email") String email);

    @SqlQuery("SELECT password FROM person WHERE email = :email")
    String getPasswordByEmail(@Bind("email") String email);

    @SqlQuery("SELECT password FROM person WHERE id = :id")
    String getPasswordById(@Bind("id") int id);

    @SqlQuery("SELECT privilege FROM person WHERE id = :id")
    int getPrivilegeById(@Bind("id") int id);

    @SqlUpdate("UPDATE person SET privilege = :privilege WHERE id = :id")
    int changeUserPrivilege(@Bind("id") int id, @Bind("privilege") int privilege);

    @SqlUpdate("UPDATE person SET language = :language WHERE id = :id")
    int alterLanguage(@Bind("id") int id, @Bind("language") String language);

    @SqlUpdate("UPDATE person SET password = :password WHERE id = :id")
    int setNewPassword(@Bind("password") String password, @Bind("id") int id);
}

