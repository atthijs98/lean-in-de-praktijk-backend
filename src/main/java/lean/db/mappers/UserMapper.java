package lean.db.mappers;

import lean.api.User;
import lean.util.MessageFactory.Language;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper used by user dao, mapping the user object.
 */
public class UserMapper implements RowMapper<User> {

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("UUID"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getInt("privilege"),
                Language.valueOf(rs.getString("language")));
    }
}
