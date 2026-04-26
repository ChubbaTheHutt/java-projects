import java.util.ArrayDeque;

public class ActionMemory {

    private ArrayDeque undoArrayDeque;
    private ArrayDeque redoArrayDeque;
    private static final int ACTION_MEMORY_SIZE = 3;

    public ActionMemory() {
        undoArrayDeque = new ArrayDeque<String>(ACTION_MEMORY_SIZE);
        redoArrayDeque = new ArrayDeque<String>(ACTION_MEMORY_SIZE);       
    }

    public void redo(){
        //remove from redo add to undo
        String temp = redoArrayDeque.poll().toString();
        undoArrayDeque.addFirst(temp);
    }

    public void undo(){
        String temp = undoArrayDeque.poll().toString();
        redoArrayDeque.addFirst(temp);
    }

    public void pushAction(String s){
        //add to undo, clear redo
        if(!redoArrayDeque.isEmpty()){
            redoArrayDeque = new ArrayDeque(ACTION_MEMORY_SIZE);
        }
    }

    public void clearMemory(){
        undoArrayDeque = new ArrayDeque<String>(ACTION_MEMORY_SIZE);
        redoArrayDeque = new ArrayDeque<String>(ACTION_MEMORY_SIZE);
    }

    public boolean isEmpty(){
        boolean isempty = undoArrayDeque.isEmpty() && redoArrayDeque.isEmpty();
        return isempty;
    }    

}