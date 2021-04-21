import java.util.Arrays;
import java.util.Stack;

public class Poliz {

    private Stack<String> poliz;

    public Poliz(){
        poliz = new Stack<>();
    }

    public void toPoliz(String token){
        poliz.push(token + " - " + (poliz.size() + 1));
    }

    public String getLastItem(){
        return poliz.pop();
    }

    public String get(int index){
        return poliz.get(index);
    }

    public void replace(int index, String token){
        poliz.remove(index);
        poliz.add(index, token);
    }

    public int find(String token){
        return poliz.indexOf(token);
    }

    public void removeItemAt(int index){
        poliz.remove(index);
    }

    public int getSize(){
        return poliz.size() + 1;
    }

    public String[] getAll(){
        String[] all = new String[poliz.size()];
        for (int i = 0; i < poliz.size(); i++){
            all[i] = poliz.get(i);
        }
        return all;
    }

    public int TMPSize(){
        int count = 0;

        for (String s : (String[]) poliz.toArray()) {
            if (s.equals("TMP"+count))
                count++;
        }

        return count;
    }

    public void replaceTMP(String token){
        int index = poliz.indexOf("TMP" + TMPSize());
        if (index != -1) {
            poliz.remove(index);
            poliz.add(index, token);
        }
    }

}