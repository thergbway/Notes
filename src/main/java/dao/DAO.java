package dao;

import loggingUtils.Loggers;
import serverConfiguration.ServerConfiguration;
import webServer.MethodInvokeResult;
import webServer.dto.Note;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DAO {
    private static Connection connection;

    static {
        try {
            Loggers.DB.info("Data Access Object initializing");
            Class.forName(((String) ServerConfiguration.getProperty("jdbcDriverName")));
            String url = ((String) ServerConfiguration.getProperty("connectionToDB"));
            String user = ((String) ServerConfiguration.getProperty("dbUser"));
            String password = ((String) ServerConfiguration.getProperty("dbPassword"));
            connection = DriverManager.getConnection(url, user, password);

            Loggers.DB.info("Data Access Object was initialized");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Invalidates existing tokens for login and creates a new one
     *
     * @param login    login
     * @param password password
     * @return new token or null if login or password wrong
     */
    public static String createToken(String login, String password) {
        String newToken = null;
        try {
            Statement st = connection.createStatement();
            String sql = "select id from users where login='" + login + "' and password='" + password + "';";
            Loggers.DB.info("sql=" + sql);
            ResultSet rs = st.executeQuery(sql);
            Integer userId = null;
            if (rs.next())
                userId = rs.getInt("id");
            else return null;

            sql = "delete from tokens where user_id='" + userId + "';";
            Loggers.DB.info("sql=" + sql);
            st.executeUpdate(sql);

            newToken = "" + new Long(System.currentTimeMillis()).hashCode() + "_" + System.currentTimeMillis() + "_" + userId;
            Timestamp validUntilDate = new Timestamp(System.currentTimeMillis() + 31104000000L);
            sql = "insert into tokens(user_id, valid_until_date, token) values ('" + userId + "','" + validUntilDate + "','" + newToken + "');";
            Loggers.DB.info("sql=" + sql);
            st.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return newToken;
    }

    public static MethodInvokeResult.ResultCodes signUp(String login, String password, String firstName, String lastName) {
        if (isLoginExists(login))
            return MethodInvokeResult.ResultCodes.USER_EXISTS_ERROR;

        if (login.equals("") || password.equals("") || firstName.equals("") || lastName.equals(""))
            return MethodInvokeResult.ResultCodes.INVALID_PARAMS_ERROR;

        String sql = "insert into users(login, password, first_name, last_name) values " +
                "('" + login + "','" + password + "','" + firstName + "','" + lastName + "');";
        Loggers.DB.info("sql = " + sql);

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return MethodInvokeResult.ResultCodes.SUCCESS;
    }

    public static Integer[] getAllNoteIds(Integer user_id) {
        try {
            String sql = "select id from notes where user_id='" + user_id + "';";
            Loggers.DB.info(sql);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Integer> ids = new LinkedList<>();
            while (rs.next())
                ids.add(rs.getInt("id"));

            return ids.toArray(new Integer[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getUserIdByToken(String token) {
        try {
            String sql = "select user_id from tokens where token='" + token + "'";
            Loggers.DB.info("sql = " + sql);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            return rs.getInt("user_id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Note getNoteById(Integer id) {
        try {
            String sql = "select id, creation_time, change_time, content, title from notes where id='" + id + "';";
            Loggers.DB.info(sql);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();

            Timestamp creation_time = rs.getTimestamp("creation_time");
            Timestamp change_time = rs.getTimestamp("change_time");
            String content = rs.getString("content");
            String title = rs.getString("title");

            sql = "SELECT text FROM tags LEFT JOIN notesTags ON tags.id=notesTags.tag_id WHERE notesTags.note_id='" + id + "';";
            Loggers.DB.info("sql = " + sql);
            StringBuilder tagsSb = new StringBuilder();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                tagsSb.append(rs.getString("text"));
                tagsSb.append(" ");
            }
            String tags = tagsSb.toString();

            Note note = new Note(id, creation_time, change_time, content, title, tags);
            return note;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNoteExistingAndBelongsToUser(Integer noteId, Integer userId) {
        try {
            String sql = "select id from notes where id='" + noteId + "' and user_id='" + userId + "';";
            Loggers.DB.info(sql);

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodInvokeResult.ResultCodes isValidToken(String token) {
        try {
            String sql = "select user_id from tokens where token='" + token + "'";
            Loggers.DB.info("sql = " + sql);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (!rs.next())
                return MethodInvokeResult.ResultCodes.INVALID_TOKEN_ERROR;
            else
                return MethodInvokeResult.ResultCodes.SUCCESS;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isLoginExists(String login) {
        try {
            Statement st = connection.createStatement();
            String sql = "select login from users where login = '" + login + "';";
            Loggers.DB.info("sql = " + sql);
            ResultSet rs = st.executeQuery(sql);

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer updateNote(Note note) {
        try {
            String sql1 = "UPDATE notes set creation_time = ?, change_time = ?, content = ?, title = ? where id=?;";
            String sql2 = "delete from notesTags where note_id= " + note.getId();
            Loggers.DB.info(sql1);
            Loggers.DB.info(sql2);

            Statement st = connection.createStatement();
            PreparedStatement pst = connection.prepareStatement(sql1);
            pst.setTimestamp(1, note.getCreationTime());
            pst.setTimestamp(2, note.getChangeTime());
            pst.setString(3, note.getContent());
            pst.setString(4, note.getTitle());
            pst.setInt(5, note.getId());
            pst.executeUpdate();
            st.executeUpdate(sql2);

            String[] tags = note.getTags().split(" ");
            for (String tag : tags) {
                String sql3 = "select id from tags where text='" + tag + "'";
                Loggers.DB.info(sql3);
                ResultSet rs = st.executeQuery(sql3);
                if (rs.next()) {
                    int tagId = rs.getInt("id");
                    String sql4 = "insert into notesTags(note_id, tag_id) values (" +
                            note.getId() + ", " + tagId + ");";
                    Loggers.DB.info(sql4);
                    st.executeUpdate(sql4);
                } else {
                    String sql5 = "insert into tags(text) values ('" + tag + "');";
                    String sql6 = "select id from tags where text='" + tag + "'";
                    Loggers.DB.info(sql5);
                    Loggers.DB.info(sql6);

                    st.executeUpdate(sql5);
                    rs = st.executeQuery(sql6);
                    rs.next();
                    int tagId = rs.getInt("id");

                    String sql7 = "insert into notesTags(note_id, tag_id) VALUES (" +
                            note.getId() + ", " + tagId + ");";
                    Loggers.DB.info(sql7);
                    st.executeUpdate(sql7);
                }
            }

            return note.getId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer saveNewNote(Note note, Integer userId) {
        int noteId = 0;
        try {
            String sql1 = "INSERT INTO notes(creation_time, change_time, content, user_id, title) VALUES "
                    + "(?, ?, ?, ?, ?);";
            Loggers.DB.info(sql1);
            PreparedStatement pst = connection.prepareStatement(sql1);
            pst.setTimestamp(1, note.getCreationTime());
            pst.setTimestamp(2, note.getChangeTime());
            pst.setString(3, note.getContent());
            pst.setInt(4, userId);
            pst.setString(5, note.getTitle());

            pst.executeUpdate();

            String sql2 = "SELECT id FROM notes WHERE user_id=? AND title=? AND content=? AND creation_time=? AND change_time=?;";
            Loggers.DB.info(sql2);
            pst = connection.prepareStatement(sql2);
            pst.setInt(1, userId);
            pst.setString(2, note.getTitle());
            pst.setString(3, note.getContent());
            pst.setTimestamp(4, note.getCreationTime());
            pst.setTimestamp(5, note.getChangeTime());
            ResultSet rs = pst.executeQuery();
            rs.next();
            noteId = rs.getInt("id");

            String[] tags = note.getTags().split(" ");
            for (String tag : tags) {
                String sql3 = "select id from tags where text='" + tag + "'";
                Loggers.DB.info(sql3);
                Statement st = connection.createStatement();
                rs = st.executeQuery(sql3);
                if (rs.next()) {
                    int tagId = rs.getInt("id");
                    String sql4 = "insert into notesTags(note_id, tag_id) values (" +
                            noteId + ", " + tagId + ");";
                    Loggers.DB.info(sql4);
                    st.executeUpdate(sql4);
                } else {
                    String sql5 = "insert into tags(text) values ('" + tag + "');";
                    String sql6 = "select id from tags where text='" + tag + "'";
                    Loggers.DB.info(sql5);
                    Loggers.DB.info(sql6);

                    st.executeUpdate(sql5);
                    rs = st.executeQuery(sql6);
                    rs.next();
                    int tagId = rs.getInt("id");

                    String sql7 = "insert into notesTags(note_id, tag_id) VALUES (" +
                            noteId + ", " + tagId + ");";
                    Loggers.DB.info(sql7);
                    st.executeUpdate(sql7);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return noteId;
    }

    public static Integer[] getAllNotesByTagAndBelongsToUser(String tag, Integer userId) {
        try {
            String sql = "select id from notes right join (select note_id from notesTags left join tags on notesTags.tag_id=tags.id where tags.text='" + tag +
                    "') as nt on notes.id=nt.note_id where notes.user_id=" + userId + ";";
            Loggers.DB.info(sql);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            List<Integer> noteIds = new LinkedList<>();
            while (rs.next()) {
                noteIds.add(rs.getInt("id"));
            }

            return noteIds.toArray(new Integer[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteNoteById(Integer userId, Integer noteId) {
        try {
            String sql1 = "select id from notes where user_id=" + userId + " and id=" + noteId;
            Loggers.DB.info(sql1);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql1);
            if(!rs.next())
                return false;

            String sql2 = "delete from notesTags where note_id=" + noteId;
            String sql3 = "delete from notes where id=" + noteId;
            Loggers.DB.info(sql2);
            Loggers.DB.info(sql3);
            st.executeUpdate(sql2);
            st.executeUpdate(sql3);

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}