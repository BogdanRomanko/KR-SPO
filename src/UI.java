import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


/*
 * Класс с визуальным интерфейсом пользователя
 */
public class UI extends JFrame {
    private String fileName = "";
    private JInternalFrame editor;
    private JTextArea editorTextArea;

    /*
     * Констуктор создания визуального интерфейса пользователя
     */
    public UI(){
        /*
         * Первичная настройка главного окна визуального интерфейса
         */
        super("Small C++");
        setSize(980, 720);
        setMinimumSize(new Dimension(500, 200));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*
         * Устанавливаем меню для главного окна
         */
        setJMenuBar(getMenu());

        /*
         * Делаем главное окно MDI
         */
        JDesktopPane desktopPane = new JDesktopPane();
        add(desktopPane);

        desktopPane.add(getEditorFrame());

        setVisible(true);
    }

    /*
     * Возвращает главное меню для главного окна
     */
    private JMenuBar getMenu(){
        JMenuBar menuBar = new JMenuBar();

        /*
         * Главные пункты меню
         */
        JMenu file = new JMenu("Файл");
        JMenu view = new JMenu("Вид");
        JMenu examples = new JMenu("Примеры");
        JMenu about = new JMenu("Справка");

        /*
         * Подпункты пункта меню Файл
         */
        JMenuItem newFile = new JMenuItem("Новый");
        JMenuItem openFile = new JMenuItem("Открыть");
        JMenuItem save = new JMenuItem("Сохранить");
        JMenuItem exit = new JMenuItem("Выход");

        /*
         * Подпункты пункта меню Вид
         */
        JMenu editor = new JMenu("Текстовый редактор");

        /*
         * Подпункты пункта Текстовый редактор
         */
        JMenuItem viewEditor = new JMenuItem("Показать текстовый редактор");
        JMenuItem hideEditor = new JMenuItem("Скрыть текстовый редактор");
        hideEditor.setVisible(false);

        /*
         * Подпункты пункта меню Примеры
         */
        JMenuItem[] mExamples = new JMenuItem[5];
        for (int i = 0; i < mExamples.length; i++) {
            mExamples[i] = new JMenuItem("Пример " + (i + 1));
        }

        /*
         * Подпункты пункта меню Справка
         */
        JMenuItem mAbout = new JMenuItem("О программе");
        JMenuItem help = new JMenuItem("Справка");

        /*
         * Устанавливаем слушатели для пунктов меню
         */
        exit.addActionListener(exitAction());
        openFile.addActionListener(openFileAction());
        save.addActionListener(saveFileAction());
        newFile.addActionListener(newFileAction());

        viewEditor.addActionListener(viewEditor());
        hideEditor.addActionListener(hideEditor());

        /*
         * Добавляем все подпункты в пункты меню
         */
        file.add(newFile);
        file.add(openFile);
        file.add(new JSeparator());
        file.add(save);
        file.add(new JSeparator());
        file.add(exit);


        editor.add(viewEditor);
        editor.add(hideEditor);
        view.add(editor);

        for (JMenuItem item : mExamples)
            examples.add(item);

        about.add(mAbout);
        about.add(new JSeparator());
        about.add(help);

        /*
         * Добавляем все пункты меню в строку меню
         */
        menuBar.add(file);
        menuBar.add(view);
        menuBar.add(examples);
        menuBar.add(about);

        return menuBar;
    }

    /*
     * Метод, возвращающий окно текстового редактора
     */
    private JInternalFrame getEditorFrame(){
        editor = new JInternalFrame("Текстовый редактор", true, true, true, true);
        editor.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        editor.setSize(500, 350);
        editor.setMinimumSize(new Dimension(500, 350));
        editor.addInternalFrameListener(viewAndHideEditor());

        editorTextArea = new JTextArea();
        JScrollPane pane1 = new JScrollPane(editorTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane1.setRowHeaderView(new TextLineNumber(editorTextArea));
        editor.add(pane1);

        return editor;
    }

    /*
     * Обработчик выхода из приложения
     */
    private ActionListener exitAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
    }

    /*
     * Обработчик вызова диалога выбора файла
     */
    private ActionListener openFileAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выберите файл для открытия");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                /*
                 * Настраиваем фильтр выбора файла
                 */
                fileChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory())
                            return true;

                        return f.getName().endsWith(".scpp");
                    }

                    @Override
                    public String getDescription() {
                        return "Файлы Small C++ (*.scpp)";
                    }
                });
                if (fileChooser.showOpenDialog(UI.this) == JFileChooser.APPROVE_OPTION)
                    fileName = fileChooser.getSelectedFile().getPath();
                else
                    JOptionPane.showMessageDialog(UI.this, "Неверно выбранный файл", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    /*
     * Обработчик вызова диалога сохранения файла
     */
    private ActionListener saveFileAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выберите место сохранения файла");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                /*
                 * Настраиваем фильтр выбора файла
                 */
                fileChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory())
                            return true;

                        return f.getName().endsWith(".scpp");
                    }

                    @Override
                    public String getDescription() {
                        return "Файлы Small C++ (*.scpp)";
                    }
                });
                if (fileChooser.showSaveDialog(UI.this) == JFileChooser.APPROVE_OPTION)
                    JOptionPane.showMessageDialog(UI.this, "Файл успешно сохранён", "Сохранение", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(UI.this, "Ошибка при сохранении файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
                File file = new File(fileChooser.getSelectedFile().getPath());
                try {

                    if (!file.getName().endsWith(".scpp"))
                        file = new File(file.getPath() + ".scpp");

                    file.createNewFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(UI.this, "Ошибка при сохранении файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }

    /*
     * Обработчик вызова диалога сохранения файла
     */
    private ActionListener newFileAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выберите место создания файла");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                /*
                 * Настраиваем фильтр выбора файла
                 */
                fileChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory())
                            return true;

                        return f.getName().endsWith(".scpp");
                    }

                    @Override
                    public String getDescription() {
                        return "Файлы Small C++ (*.scpp)";
                    }
                });
                if (fileChooser.showSaveDialog(UI.this) == JFileChooser.APPROVE_OPTION)
                    JOptionPane.showMessageDialog(UI.this, "Файл успешно создан", "Новый файл", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(UI.this, "Ошибка при создании файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
                File file = new File(fileChooser.getSelectedFile().getPath());
                try {

                    if (!file.getName().endsWith(".scpp"))
                        file = new File(file.getPath() + ".scpp");

                    file.createNewFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(UI.this, "Ошибка при создании файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }

    /*
     * Обработчик вызова окна текстового редактора
     */
    private ActionListener viewEditor(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(1).setVisible(true);
                editor.setVisible(true);
            }
        };
    }

    /*
     * Обработчик сокрытия окна текстового редактора
     */
    private ActionListener hideEditor(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(1).setVisible(false);
                editor.setVisible(false);
            }
        };
    }

    /*
     * Обработчик вызова и сокрытия окна текстового редактора
     */
    private InternalFrameListener viewAndHideEditor(){
        return new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(1).setVisible(true);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(1).setVisible(false);
            }
        };
    }

}
