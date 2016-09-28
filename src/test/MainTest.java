import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by jakefroeb on 9/28/16.
 */
public class MainTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }
    @Test
    public void testUser() throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn,"Alice");
        User user = Main.selectUser(conn,"Alice");
        conn.close();
        assertTrue(user != null);
    }

    @Test
    public void testAddingAndRetrievingItems()throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn,"Alice");
        Main.insertUser(conn,"Jake");
        Main.insertUser(conn,"Halley");
        User alice = Main.selectUser(conn,"Alice");
        User jake = Main.selectUser(conn,"Jake");
        User halley = Main.selectUser(conn,"Halley");
        Main.insertToDo(conn,alice.id,"todoITEM!");
        Main.insertToDo(conn,alice.id,"todoITEM2");
        Main.insertToDo(conn,jake.id,"todo");
        Main.insertToDo(conn,halley.id,"todo1");
        Main.insertToDo(conn,halley.id,"todo2");
        Main.insertToDo(conn,halley.id,"todo3");
        ArrayList<ToDoItem> testAliceItems = Main.selectToDos(conn,alice.id);
        assertTrue(testAliceItems.size() == 2);
        ArrayList<ToDoItem> testJakeItems = Main.selectToDos(conn,jake.id);
        assertTrue(testJakeItems.size() == 1);
        ArrayList<ToDoItem> testHalleyItems = Main.selectToDos(conn,halley.id);
        assertTrue(testHalleyItems.size()== 3);
    }

    public void testDeletingAndTogglingItem()throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn,"Alice");
        Main.insertUser(conn,"Jake");
        Main.insertUser(conn,"Halley");
        User alice = Main.selectUser(conn,"Alice");
        User jake = Main.selectUser(conn,"Jake");
        User halley = Main.selectUser(conn,"Halley");
        Main.insertToDo(conn,alice.id,"todoITEM!");
        Main.insertToDo(conn,alice.id,"todoITEM2");
        Main.insertToDo(conn,jake.id,"todo");
        Main.insertToDo(conn,halley.id,"todo1");
        Main.insertToDo(conn,halley.id,"todo2");
        Main.insertToDo(conn,halley.id,"todo3");



        ArrayList<ToDoItem> testAliceItems = Main.selectToDos(conn,alice.id);
        int aliceTestNum = 2;
        ArrayList<ToDoItem> testJakeItems = Main.selectToDos(conn,jake.id);
        int jakeTestNum = 1;
        ArrayList<ToDoItem> testHalleyItems = Main.selectToDos(conn,halley.id);
        int halleyTestNum=3;


        Main.toggleToDo(conn,testAliceItems.get(aliceTestNum).id);
        ArrayList<ToDoItem> testAliceItems2 = Main.selectToDos(conn,alice.id);
        assertTrue(testAliceItems.get(aliceTestNum).isDone);
        Main.toggleToDo(conn,testJakeItems.get(jakeTestNum).id);
        ArrayList<ToDoItem> testJakeItems2 = Main.selectToDos(conn,jake.id);
        assertTrue(testJakeItems2.get(jakeTestNum).isDone);
        Main.toggleToDo(conn,testHalleyItems.get(halleyTestNum).id);
        ArrayList<ToDoItem> testHalleyItems2 = Main.selectToDos(conn,jake.id);
        assertTrue(testHalleyItems2.get(halleyTestNum).isDone);


        Main.deleteItem(conn,testAliceItems.get(aliceTestNum).id);
        ArrayList<ToDoItem> testAliceItems3 = Main.selectToDos(conn,alice.id);
        assertTrue(testAliceItems3.size()==1);
        Main.deleteItem(conn,testJakeItems.get(jakeTestNum).id);
        ArrayList<ToDoItem> testJakeItems3 = Main.selectToDos(conn,jake.id);
        assertTrue(testJakeItems3.size() == 0);
        Main.deleteItem(conn,testHalleyItems.get(halleyTestNum).id);
        ArrayList<ToDoItem> testHalleyItems3 = Main.selectToDos(conn,jake.id);
        assertTrue(testHalleyItems3.size() == 2);




    }




}
