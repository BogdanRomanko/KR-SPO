import java.util.Stack;

/*
 * Класс для формирования ПОЛИЗа
 */
public class Poliz {

    /*
     * Поле стэк, хранящее весь ПОЛИЗ
     */
    private Stack<String> poliz;

    /*
     * Конструктор класса, инициализирующий стэк с ПОЛИЗом
     */
    public Poliz(){
        poliz = new Stack<>();
    }

    /*
     * Метод записи в ПОЛИЗ
     */
    public void toPoliz(String token){
        poliz.push(token + " - " + (poliz.size() + 1));
    }

    /*
     * Метод получения последней записи из ПОЛИЗа
     */
    public String getLastItem(){
        return poliz.pop();
    }

    /*
     * Метод получения значения на определённом
     * индексе из ПОЛИЗа
     */
    public String get(int index){
        return poliz.get(index);
    }

    /*
     * Метод, заменяющий на определённом индексе
     * запись на полученную
     */
    public void replace(int index, String token){
        poliz.remove(index);
        poliz.add(index, token);
    }

    /*
     * Метод, возвращающий индекс первого найденного совпадения
     * с записью, переданной в параметрах
     */
    public int find(String token) {
        for (int i = 0; i < getSize(); i++)
            if (poliz.indexOf(token + " - " + i) != -1)
                return i;
        return -1;
    }

    /*
     * Метод, удаляющий запись на определённом индексе
     */
    public void removeItemAt(int index){
        poliz.remove(index);
    }

    /*
     * Метод получения размера ПОЛИЗа
     */
    public int getSize(){
        return poliz.size() + 1;
    }

    /*
     * Метод, возвращающий все записи в
     * виде массива строк
     */
    public String[] getAll(){
        String[] all = new String[poliz.size()];
        for (int i = 0; i < poliz.size(); i++){
            all[i] = poliz.get(i);
        }
        return all;
    }

    /*
     * Метод, возвращающий количество меток TMP
     */
    public int TMPSize(){
        int count = 0;

        for (String s : (String[]) poliz.toArray()) {
            if (s.equals("TMP"+count))
                count++;
        }

        return count;
    }

    /*
     * Метод, заменяющий метки TMP на переданную запись
     */
    public void replaceTMP(String token){
        int index = poliz.indexOf("TMP" + TMPSize());
        if (index != -1) {
            poliz.remove(index);
            poliz.add(index, token);
        }
    }

}