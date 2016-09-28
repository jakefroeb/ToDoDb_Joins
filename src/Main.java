import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by jakefroeb on 9/28/16.
 */
public class Main {
    public static boolean loggedIn=false;



        public static void main(String[] args) throws SQLException {
            Server.createWebServer().start();
            Connection conn = DriverManager.getConnection("jdbc:h2:./main");
            Scanner scanner = new Scanner(System.in);

            createTables(conn);

            while(true) {
                    System.out.println("Please enter your username");
                    String userName = scanner.nextLine();
                    User user = selectUser(conn, userName);
                    loggedIn = true;
                    if (user == null) {
                        insertUser(conn, userName);
                        user = selectUser(conn, userName);
                        loggedIn = true;
                    }


                while (loggedIn) {
                    System.out.println("1. Create to-do item");
                    System.out.println("2. Toggle to-do item");
                    System.out.println("3. List to-do items");
                    System.out.println("4. Delete item");
                    System.out.println("5. Logout");

                    String option = scanner.nextLine();
                    if (option.equals("1")) {
                        System.out.println("Enter your to-do item:");
                        String text = scanner.nextLine();
                        insertToDo(conn, user.id, text);
                    } else if (option.equals("2")) {
                        System.out.println("Enter the number of the item you want to toggle:");
                        int itemNum = Integer.valueOf(scanner.nextLine());
                        ArrayList<ToDoItem> todos = selectToDos(conn, user.id);
                        toggleToDo(conn, todos.get(itemNum-1).id);
                    } else if (option.equals("3")) {
                        ArrayList<ToDoItem> todos = selectToDos(conn, user.id);
                        for (ToDoItem item : todos) {
                            String checkbox = "[ ] ";
                            if (item.isDone) {
                                checkbox = "[x] ";
                            }
                            System.out.println(checkbox + (todos.indexOf(item)+1) + ". " + item.text);

                        }
                    }else if (option.equals("4")){
                        System.out.println("Enter the number of the item you want to delete");
                        int itemNum = Integer.valueOf(scanner.nextLine());
                        ArrayList<ToDoItem> todos = selectToDos(conn, user.id);
                        deleteItem(conn,todos.get(itemNum-1).id);

                    } else if (option.equals("5")) {
                        loggedIn = false;
                    } else {
                        System.out.println("Invalid option");
                    }
                }
            }
        }

    public static void createTables(Connection conn)throws SQLException{
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS items (id IDENTITY, user_id INT, text VARCHAR, is_done BOOLEAN)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY , user_name VARCHAR)");
    }

    public static User selectUser(Connection conn, String userName)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE user_name = ?");
        stmt.setString(1,userName);
        ResultSet results = stmt.executeQuery();
        if(results.next()){
            int id = results.getInt("id");
            return new User(id,userName);
        }
        return null;
    }

    public static void insertUser(Connection conn, String userName)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES(null, ?)");
        stmt.setString(1,userName);
        stmt.execute();
    }


    public static void insertToDo(Connection conn, int userId, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO items VALUES(null,?,?,false)");
        stmt.setInt(1,userId);
        stmt.setString(2,text);
        stmt.execute();
    }

    public static ArrayList<ToDoItem> selectToDos(Connection conn, int userId)throws SQLException{
        ArrayList<ToDoItem> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items INNER JOIN users ON items.user_id = users.id WHERE user_id = ?");
        stmt.setInt(1,userId);
        ResultSet results = stmt.executeQuery();
        while(results.next()){
            int id = results.getInt("items.id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean("is_done");
            items.add(new ToDoItem(id, userId, text,isDone));
        }
        return items;
    }
    public static void toggleToDo(Connection conn, int id)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("UPDATE items SET is_done = NOT is_done WHERE id = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }
    public static void deleteItem(Connection conn, int id)throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE id = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }

}


