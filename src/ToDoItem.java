/**
 * Created by jakefroeb on 9/28/16.
 */
public class ToDoItem {
    public int id;
    public int userId;
    public String text;
    public boolean isDone;

    public ToDoItem(int id, int userId, String text, boolean isDone){
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.isDone = isDone;
    }
}
