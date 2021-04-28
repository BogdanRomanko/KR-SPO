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
    private JInternalFrame lexer;
    private JInternalFrame lexerTable;
    private JInternalFrame poliz;

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
        desktopPane.add(getLexerFrame());
        desktopPane.add(getLexerTable());
        desktopPane.add(getPolizFrame());

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
        JMenu lexer = new JMenu("Лексический анализатор");
        JMenu lexerTable = new JMenu("Таблица лексического анализатора");
        JMenu poliz = new JMenu("ПОЛИЗ");

        /*
         * Подпункты пункта Текстовый редактор
         */
        JMenuItem viewEditor = new JMenuItem("Показать текстовый редактор");
        JMenuItem hideEditor = new JMenuItem("Скрыть текстовый редактор");
        hideEditor.setVisible(false);

        /*
         * Подпункты пункта Лексический анализатор
         */
        JMenuItem viewLexer = new JMenuItem("Показать лексический анализатор");
        JMenuItem hideLexer = new JMenuItem("Скрыть лексический анализатор");
        hideLexer.setVisible(false);

        /*
         * Подпункты пункта Таблица лексического анализатора
         */
        JMenuItem viewLexerTable = new JMenuItem("Показать таблицу лексического анализатора");
        JMenuItem hideLexerTable = new JMenuItem("Скрыть таблицу лексического анализатора");
        hideLexerTable.setVisible(false);

        /*
         * Подпункты пункта ПОЛИЗ
         */
        JMenuItem viewPoliz = new JMenuItem("Показать ПОЛИЗ");
        JMenuItem hidePoliz = new JMenuItem("Скрыть ПОЛИЗ");
        hidePoliz.setVisible(false);

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
        viewLexer.addActionListener(viewLexer());
        viewLexerTable.addActionListener(viewLexerTable());
        viewPoliz.addActionListener(viewPoliz());

        hideEditor.addActionListener(hideEditor());
        hideLexer.addActionListener(hideLexer());
        hideLexerTable.addActionListener(hideLexerTable());
        hidePoliz.addActionListener(hidePoliz());

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

        lexer.add(viewLexer);
        lexer.add(hideLexer);

        lexerTable.add(viewLexerTable);
        lexerTable.add(hideLexerTable);

        poliz.add(viewPoliz);
        poliz.add(hidePoliz);

        view.add(editor);
        view.add(new JSeparator());
        view.add(lexer);
        view.add(lexerTable);
        view.add(new JSeparator());
        view.add(poliz);

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

        /*
         * Добавляем текстовый редактор с подсчётом строк
         */
        editorTextArea = new JTextArea();
        JScrollPane pane1 = new JScrollPane(editorTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane1.setRowHeaderView(new TextLineNumber(editorTextArea));
        editor.add(pane1, BorderLayout.NORTH);



        return editor;
    }

    /*
     * Метод, возвращающий окно вывода лексического анализатора
     */
    private JInternalFrame getLexerFrame() {
        lexer = new JInternalFrame("Лексический анализатор", true, true, true, true);
        lexer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        lexer.setSize(450, 500);
        lexer.setMinimumSize(new Dimension(450, 500));

        lexer.addInternalFrameListener(viewAndHideLexer());

        return lexer;
    }

    /*
     * Метод, возвращающий окно вывода таблицы лексического анализатора
     */
    public JInternalFrame getLexerTable() {
        lexerTable = new JInternalFrame("Лексическая таблица", true, true, true, true);
        lexerTable.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        lexerTable.setSize(450, 500);
        lexerTable.setMinimumSize(new Dimension(450, 500));

        lexerTable.addInternalFrameListener(viewAndHideLexerTable());

        return lexerTable;
    }

    /*
     * Метод, возвращающий окно вывода перевода программы в ПОЛИЗ
     */
    public JInternalFrame getPolizFrame() {
        poliz = new JInternalFrame("ПОЛИЗ", true, true, true, true);
        poliz.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        poliz.setSize(450, 500);
        poliz.setMinimumSize(new Dimension(450, 500));

        poliz.addInternalFrameListener(viewAndHidePoliz());

        return poliz;
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
                if (fileChooser.showSaveDialog(UI.this) == JFileChooser.APPROVE_OPTION) {
                    /*
                     * Создаём файл с выбранным пользователем именем
                     * и добавляем расширение .scpp, если такого нет
                     */
                    File file = new File(fileChooser.getSelectedFile().getPath());
                    try {
                        if (!file.getName().endsWith(".scpp"))
                            file = new File(file.getPath() + ".scpp");

                        file.createNewFile();
                        fileName = file.getPath();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(UI.this, "Ошибка при создании файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }

                    JOptionPane.showMessageDialog(UI.this, "Файл успешно создан", "Новый файл", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                    JOptionPane.showMessageDialog(UI.this, "Ошибка при создании файла", "Ошибка", JOptionPane.ERROR_MESSAGE);

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

    /*
     * Обработчик вызова окна лексического анализатора
     */
    private ActionListener viewLexer(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(2)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(2)).getItem(1).setVisible(true);
                lexer.setVisible(true);
            }
        };
    }

    /*
     * Обработчик сокрытия окна лексического анализатора
     */
    private ActionListener hideLexer(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(2)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(2)).getItem(1).setVisible(false);
                lexer.setVisible(false);
            }
        };
    }

    /*
     * Обработчик вызова и сокрытия окна лексического анализатора
     */
    private InternalFrameListener viewAndHideLexer(){
        return new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(2)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(2)).getItem(1).setVisible(true);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(2)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(2)).getItem(1).setVisible(false);
            }
        };
    }

    /*
     * Обработчик вызова окна текстового таблицы лексического анализатора
     */
    private ActionListener viewLexerTable(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(3)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(3)).getItem(1).setVisible(true);
                lexerTable.setVisible(true);
            }
        };
    }

    /*
     * Обработчик сокрытия окна таблицы лексического анализатора
     */
    private ActionListener hideLexerTable(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(3)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(3)).getItem(1).setVisible(false);
                lexerTable.setVisible(false);
            }
        };
    }

    /*
     * Обработчик вызова и сокрытия окна таблицы лексического анализатора
     */
    private InternalFrameListener viewAndHideLexerTable(){
        return new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(3)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(3)).getItem(1).setVisible(true);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(3)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(3)).getItem(1).setVisible(false);
            }
        };
    }

    /*
     * Обработчик вызова окна ПОЛИЗа
     */
    private ActionListener viewPoliz(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(5)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(5)).getItem(1).setVisible(true);
                poliz.setVisible(true);
            }
        };
    }

    /*
     * Обработчик сокрытия окна ПОЛИЗа
     */
    private ActionListener hidePoliz(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(5)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(5)).getItem(1).setVisible(false);
                poliz.setVisible(false);
            }
        };
    }

    /*
     * Обработчик вызова и сокрытия окна ПОЛИЗа
     */
    private InternalFrameListener viewAndHidePoliz(){
        return new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(5)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(5)).getItem(1).setVisible(true);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(5)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(5)).getItem(1).setVisible(false);
            }
        };
    }

}