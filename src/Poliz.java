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
     * Метод, удаляющий запись на определённом индексе из ПОЛИЗа
     */
    public void remove(int index){
        poliz.remove(index);
    }

}