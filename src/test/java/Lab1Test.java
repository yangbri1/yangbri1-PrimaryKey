
import Util.ConnectionUtil;
import Util.FileUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.fail;

public class Lab1Test {


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
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql2 = "INSERT into Artist (id, name) VALUES (1,'Peter Gabriel')";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.executeUpdate();
        } catch (SQLException e) {
            Assert.fail("There was an issue inserting a record into your table: "+e.getMessage());
        }
    }

    /**
     * Testing the the records of the table are unique.
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

            String sql = "INSERT into Artist (id, name) VALUES (1,'Peter Gabriel');";
            String sql2 = "INSERT into Artist (id, name) VALUES (1,'Kate Bush');";

            PreparedStatement ps = connection.prepareStatement(sql + sql2);

            ps.executeUpdate();
            fail("Primary Key constraint not implemented due to unique constraint not being enforced");

        } catch (SQLException e) {
            if(!(e.getMessage().substring(0, "Unique index or primary key violation".length()).equals("Unique index or primary key violation"))){
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
            String sql = "DROP TABLE Artist;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }
}
