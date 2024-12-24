
import Util.ConnectionUtil;
import Util.FileUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.fail;

public class Lab2Test {
    /**
     * The @After annotation runs after every test so that way we drop the tables to avoid conflicts in future tests
     */
    @After
    public void afterEach(){
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DROP TABLE Artist;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    /**
     * To test that the table exists, we are attempting to insert a row into the table and if table does not exist, the test will fail.
     */
    @Test
    public void createTableSurrogateKeyTest(){

        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = FileUtil.parseSQLFile("src/main/lab1.sql");
            if (sql.isEmpty()) {
                Assert.fail("No sql statement written in lab1.sql.");
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }catch (SQLException e) {
            Assert.fail("There was an issue running your SQL statement in lab1.sql: "+e.getMessage());
        }
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = FileUtil.parseSQLFile("src/main/lab2.sql");
            if (sql.isEmpty()) {
                Assert.fail("No sql statement written in lab2.sql.");
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }catch (SQLException e) {
            Assert.fail("There was an issue running your SQL statement in lab2.sql: "+e.getMessage());
        }
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO Artist (id, name) VALUES (3, 'Kate Bush')";
            String sql2 = "INSERT into Album (id, title, artist_fk) VALUES (3,'The Dreaming', 3)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.executeUpdate();
        } catch (SQLException e) {
            Assert.fail("There was an issue inserting a record into your table: "+e.getMessage());
        }
    }

    /**
     * Testing the the records of the table are foreign keys.
     */
    @Test
    public void TestPrimaryKeyUniqueConstraint(){

        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = FileUtil.parseSQLFile("src/main/lab1.sql");
            if (sql.isEmpty()) {
                Assert.fail("No sql statement written in lab1.sql.");
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }catch (SQLException e) {
            Assert.fail("There was an issue running your SQL statement in lab1.sql: "+e.getMessage());
        }
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = FileUtil.parseSQLFile("src/main/lab2.sql");
            if (sql.isEmpty()) {
                Assert.fail("No sql statement written in lab2.sql.");
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }catch (SQLException e) {
            Assert.fail("There was an issue running your SQL statement in lab2.sql: "+e.getMessage());
        }
        try {

            Connection connection = ConnectionUtil.getConnection();

            String sql = "INSERT into Album (id, title, artist_fk) VALUES (3,'Some other album', 4);";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.executeUpdate();
            Assert.fail("Referential integrity not enforced, fkey points to non-existent pkey");

        } catch (SQLException e) {
            if(!(e.getMessage().contains("Referential integrity constraint violation:"))){
                Assert.fail("There was an issue executing insert statements:"+ e.getMessage());
            }
        }
    }
    /**
     * The @After annotation runs after every test so that way we drop the tables to avoid conflicts in future tests
     */
    @After
    public void cleanup(){
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DROP TABLE IF EXISTS Album;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {

        }
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DROP TABLE IF EXISTS Artist;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {

        }

    }
}
