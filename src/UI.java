import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/*
 * Класс с визуальным интерфейсом пользователя
 */
public class UI extends JFrame {
    private String fileName = "";
    private String copy_text = "";

    private JInternalFrame editor;
    private JInternalFrame lexer;
    private JInternalFrame lexerTable;
    private JInternalFrame poliz;

    public Console console;

    private JScrollPane consolePanel;

    public JTextArea lexerArea = new JTextArea();
    public JTextArea lexerTableArea = new JTextArea();
    public JTextArea polizArea = new JTextArea();

    private JTextArea editorTextArea;

    /*
     * Инициализируем строки отображения информации для строки состояния
     */
    private final JLabel StatusL = new JLabel("Статус");
    private final JLabel StatusTime = new JLabel("");

    private JFrame helpFrame;

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
         * Устанавливаем панель инструментов для главного окна
         */
        add(getToolBar(), BorderLayout.NORTH);

        /*
         * Устанавливаем строку состояния
         */
        add(getStatusBar(), BorderLayout.SOUTH);

        /*
         * Делаем главное окно MDI
         */
        JDesktopPane desktopPane = new JDesktopPane();
        add(desktopPane);

        /*
         * Добавим дочерние окна
         */
        desktopPane.add(getEditorFrame());
        desktopPane.add(getLexerFrame());
        desktopPane.add(getLexerTable());
        desktopPane.add(getPolizFrame());

        /*
         * Переназначаем стандартный поток ввода данных на консоль
         */
        TextAreaStreamer ts = new TextAreaStreamer(console);
        console.addKeyListener(ts);
        System.setIn(ts);

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

        viewEditor.addActionListener(viewEditorAction());
        viewLexer.addActionListener(viewLexerAction());
        viewLexerTable.addActionListener(viewLexerTableAction());
        viewPoliz.addActionListener(viewPolizAction());

        hideEditor.addActionListener(hideEditorAction());
        hideLexer.addActionListener(hideLexerAction());
        hideLexerTable.addActionListener(hideLexerTableAction());
        hidePoliz.addActionListener(hidePolizAction());

        mExamples[0].addActionListener(viewExample1Action());
        mExamples[1].addActionListener(viewExample2Action());
        mExamples[2].addActionListener(viewExample3Action());
        mExamples[3].addActionListener(viewExample4Action());
        mExamples[4].addActionListener(viewExample5Action());

        help.addActionListener(viewHelpFrameAction());
        mAbout.addActionListener(infoAction());

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
     * Возвращает строку состояния для главного окна
     */
    private JPanel getStatusBar(){
        /*
         * Создание строки состояния с описанием состояния
         * и временем/датой. Создадим панель, куда добавим две текстовые
         * метки с датой/временем и названием открытого файла
         */
        JPanel StatusP = new JPanel();

        StatusP.setLayout(new BorderLayout());
        StatusP.add(StatusL,BorderLayout.WEST);
        StatusP.setPreferredSize(new Dimension(this.getWidth(),20));
        StatusP.setBorder(BorderFactory.createLineBorder(Color.black));
        StatusP.add(StatusTime,BorderLayout.EAST);

        /*
         * Создание таймера для ежесекундного обновления
         * даты/времени в строке состояния и его запуск
         */
        final Timer timer = new Timer( 100, this.timer);
        timer.start();

        return StatusP;
    }

    /*
     * Метод, возвращающий панель инструментов
     */
    private JToolBar getToolBar(){
        JToolBar toolBar = new JToolBar();

        toolBar.setSize(toolBar.getWidth(), 32);

        /*
         * массив с кнопками для панели инструментов
         */
        final JButton[] buttons = new JButton[8];

        // Массив с инонками для кнопок
        final ImageIcon[] icons = {
                new ImageIcon("./src/image/new-file.png"),
                new ImageIcon("./src/image/open-file.png"),
                new ImageIcon("./src/image/save.png"),
                new ImageIcon("./src/image/pencil.png"),
                new ImageIcon("./src/image/no-pencil.png"),
                new ImageIcon("./src/image/select-all-text.png"),
                new ImageIcon("./src/image/copy.png"),
                new ImageIcon("./src/image/paste.png")
        };

        // слушатель для массива с кнопками соотвественно предназначению кнопок
        final ActionListener[] listeners = {
                newFileAction(),
                openFileAction(),
                saveFileAction(),
                viewEditorAction(),
                hideEditorAction(),
                selectAlltextAction(),
                copyTextAction(),
                pasteTextAction()
        };

        // проходим по всем кнопкам панели инструментов
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(icons[i]);
            // добавляем кнопке соотвествующий ей слушатель
            buttons[i].addActionListener(listeners[i]);
            // добавляем кнопку в панель инструментов
            toolBar.add(buttons[i]);
        }

        buttons[4].setVisible(false);

        return toolBar;
    }

    /*
     * Метод, возвращающий окно текстового редактора
     */
    private JInternalFrame getEditorFrame() {
        editor = new JInternalFrame("Текстовый редактор", true, true, true, true);
        editor.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        editor.setSize(500, 350);
        editor.setMinimumSize(new Dimension(500, 350));
        editor.addInternalFrameListener(viewAndHideEditor());
        editor.setLayout(new BorderLayout());

        /*
         * Добавляем текстовый редактор с подсчётом строк
         */
        editorTextArea = new JTextArea();
        editorTextArea.setComponentPopupMenu(getPopup());
        JScrollPane pane1 = new JScrollPane(editorTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane1.setRowHeaderView(new TextLineNumber(editorTextArea));
        pane1.setPreferredSize(new Dimension(200, (int) Math.round(editor.getSize().height * 0.75)));
        pane1.setSize(new Dimension(200, (int) Math.round(editor.getSize().height * 0.75)));
        editor.add(pane1, BorderLayout.CENTER);

        /*
         * Добавляем консоль в окно текстового редактора
         */
        console = new Console();
        consolePanel = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        consolePanel.setPreferredSize(new Dimension(200, (int) Math.round(editor.getSize().height * 0.35)));
        consolePanel.setSize(new Dimension(200, (int) Math.round(editor.getSize().height * 0.35)));

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(new Label("Консоль"), BorderLayout.NORTH);
        bottom.add(consolePanel, BorderLayout.SOUTH);

        editor.add(bottom, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JButton buttonRun = new JButton("Запустить");

        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                console.clear();
                lexerArea.setText("");
                lexerTableArea.setText("");
                polizArea.setText("");

                InterpreterThread interpreterThread = new InterpreterThread("interpreter", editorTextArea.getText(), UI.this);
                interpreterThread.start();

            }
        });

        editor.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                consolePanel.setPreferredSize(new Dimension(200, (int) Math.round(editor.getSize().height * 0.35)));
                consolePanel.setSize(new Dimension(200, (int) Math.round(editor.getSize().height * 0.35)));
                editor.repaint();
            }
        });

        menuBar.add(buttonRun);
        editor.setJMenuBar(menuBar);

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

        JScrollPane pane = new JScrollPane(lexerArea);
        lexer.add(pane);

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

        JScrollPane pane = new JScrollPane(lexerTableArea);
        lexerTable.add(pane);

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

        JScrollPane pane = new JScrollPane(polizArea);
        poliz.add(pane);

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
                if (fileChooser.showOpenDialog(UI.this) == JFileChooser.APPROVE_OPTION) {
                    fileName = fileChooser.getSelectedFile().getPath();
                    try {
                        editorTextArea.setText(new String(Files.readAllBytes(Paths.get(fileName))));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(UI.this, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                    StatusL.setText(fileName);
                }
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
                if (fileChooser.showSaveDialog(UI.this) != JFileChooser.APPROVE_OPTION)
                    JOptionPane.showMessageDialog(UI.this, "Ошибка при создании файла", "Ошибка", JOptionPane.ERROR_MESSAGE);

                File file = new File(fileChooser.getSelectedFile().getPath());
                try {

                    if (!file.getName().endsWith(".scpp"))
                        file = new File(file.getPath() + ".scpp");

                    if (file.createNewFile())
                        JOptionPane.showMessageDialog(UI.this, "Файл успешно сохранён", "Сохранение", JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(UI.this, "Ошибка при сохранении файла", "Ошибка", JOptionPane.ERROR_MESSAGE);

                    PrintWriter writer = new PrintWriter(file.getPath());
                    writer.print(editorTextArea.getText());
                    writer.close();
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
                        StatusL.setText(fileName);
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
     * Обработчик выделения всего текста в текстовом редакторе
     */
    private ActionListener selectAlltextAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorTextArea.select(0, editorTextArea.getText().length());
                editor.repaint();
            }
        };
    }

    /*
     * Обработчик копирования текста в буфер обмена
     */
    private ActionListener copyTextAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy_text = editorTextArea.getSelectedText();
            }
        };
    }

    /*
     * Обработчик вставки текста из буфера обмена
     */
    private ActionListener pasteTextAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorTextArea.replaceSelection(copy_text);
            }
        };
    }

    /*
     * Обработчик вызова окна текстового редактора
     */
    private ActionListener viewEditorAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(0).setVisible(false);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(1).setVisible(true);
                (((JToolBar) UI.this.getContentPane().getComponents()[0]).getComponent(3)).setVisible(false);
                (((JToolBar) UI.this.getContentPane().getComponents()[0]).getComponent(4)).setVisible(true);
                editor.setVisible(true);
            }
        };
    }

    /*
     * Обработчик сокрытия окна текстового редактора
     */
    private ActionListener hideEditorAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(1).setVisible(false);
                (((JToolBar) UI.this.getContentPane().getComponents()[0]).getComponent(3)).setVisible(true);
                (((JToolBar) UI.this.getContentPane().getComponents()[0]).getComponent(4)).setVisible(false);
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
                (((JToolBar) UI.this.getContentPane().getComponents()[0]).getComponent(3)).setVisible(false);
                (((JToolBar) UI.this.getContentPane().getComponents()[0]).getComponent(4)).setVisible(true);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(0).setVisible(true);
                ((JMenu) UI.this.getJMenuBar().getMenu(1).getItem(0)).getItem(1).setVisible(false);
                (((JToolBar) UI.this.getContentPane().getComponents()[0]).getComponent(3)).setVisible(true);
                (((JToolBar) UI.this.getContentPane().getComponents()[0]).getComponent(4)).setVisible(false);
            }
        };
    }

    /*
     * Обработчик вызова окна лексического анализатора
     */
    private ActionListener viewLexerAction(){
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
    private ActionListener hideLexerAction(){
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
    private ActionListener viewLexerTableAction(){
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
    private ActionListener hideLexerTableAction(){
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
    private ActionListener viewPolizAction(){
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
    private ActionListener hidePolizAction(){
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

    /*
     * Обработчик выбора примера 1 в меню Примеры
     */
    private ActionListener viewExample1Action(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorTextArea.setText(
                        "int main(){\n" +
                        "\n" +
                        "    int a = 0;\n" +
                        "    int b = cin();\n" +
                        "    double c = 10.14;\n" +
                        "    double d = cin();\n" +
                        "    string A = \"Переменная String\";\n" +
                        "    string B = cin();\n" +
                        "\n" +
                        "    return 0;\n" +
                        "}"
                );
            }
        };
    }

    /*
     * Обработчик выбора примера 2 в меню Примеры
     */
    private ActionListener viewExample2Action(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorTextArea.setText(
                        "    int main(){\n" +
                        "        \n" +
                        "        int a = 5;\n" +
                        "        int b = cin();\n" +
                        "        if (a < b){\n" +
                        "            cout(\"a < b\");\n" +
                        "        } else {\n" +
                        "            cout(\"a > b\");\n" +
                        "        }\n" +
                        "        \n" +
                        "        return 0;\n" +
                        "    }"
                );
            }
        };
    }

    /*
     * Обработчик выбора примера 3 в меню Примеры
     */
    private ActionListener viewExample3Action(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorTextArea.setText(
                                "int main(){\n" +
                                "    cout(\"Перед циклом 1\");\n" +
                                "    for (int i = 0; i < 5; i = i + 1){\n" +
                                "        cout(\"Внутри цикла 1\");\n" +
                                "        for (int i = 0; i < 5; i = i + 1){\n" +
                                "            cout(\"Внутри цикла 2\");\n" +
                                "        }\n" +
                                "    }\n" +
                                "    cout(\"После цикла 1\");\n" +
                                "\n" +
                                "    return 0;\n" +
                                "}"
                );
            }
        };
    }

    /*
     * Обработчик выбора примера 4 в меню Примеры
     */
    private ActionListener viewExample4Action() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorTextArea.setText(
                        "int main(){\n" +
                        "    int a = 5;\n" +
                        "    m:\n" +
                        "    int b = cin();\n" +
                        "    if (a > b){\n" +
                        "       goto m;\n" +
                        "    }\n" +
                        "    return 0;\n" +
                        "}"
                );
            }
        };
    }

    /*
     * Обработчик выбора примера 5 в меню Примеры
     */
    private ActionListener viewExample5Action(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorTextArea.setText(
                        "int main(){\n" +
                        "\n" +
                        "    int a = 0;\n" +
                        "    int b = 5;\n" +
                        "    int r = a + b;\n" +
                        "\n" +
                        "    double c = 10.14;\n" +
                        "    double d = 11.25;\n" +
                        "    double f = c + d;\n" +
                        "\n" +
                        "    string D = \" Переменная String!\";\n" +
                        "    string E = D + \"Переменная String 2!\";\n" +
                        "\n" +
                        "    return 0;\n" +
                        "}"
                );
            }
        };
    }

    /*
     * Слушатель для таймера, изменяющий дату и время в строке состояния
     */
    private ActionListener timer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            /*
             * Получаем дату и устанавливаем её в текстовую метку панели состояния
             */
            Date date = new Date();
            StatusTime.setText(date.toString());
        }
    };

    /*
     * Метод, возвращающий окно со справкой по работе с программой
     */
    private JFrame getHelpFrame() throws IOException {
        helpFrame = new JFrame("Справка");
        helpFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        helpFrame.add(new JScrollPane(new JLabel(new String(Files.readAllBytes(Paths.get("./src/html-files/help.html"))))));

        helpFrame.setSize(new Dimension(1050,720));
        helpFrame.setVisible(true);

        return helpFrame;
    }

    /*
     * Обработчик вызова окна со справкой
     */
    private ActionListener viewHelpFrameAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    getHelpFrame();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    /*
     * Метод, возвращающий контекстное меню для текстового редактора
     */
    private JPopupMenu getPopup() {
        final JPopupMenu popup = new JPopupMenu();

        final JMenuItem newFile = new JMenuItem("Новый");
        final JMenuItem openFile = new JMenuItem("Открыть");
        final JMenuItem save = new JMenuItem("Сохранить");

        final JMenuItem selectAll = new JMenuItem("Выделить всё");
        final JMenuItem copy = new JMenuItem("Копировать");
        final JMenuItem paste = new JMenuItem("Вставить");

        newFile.addActionListener(newFileAction());
        openFile.addActionListener(openFileAction());
        save.addActionListener(saveFileAction());
        selectAll.addActionListener(selectAlltextAction());
        copy.addActionListener(copyTextAction());
        paste.addActionListener(pasteTextAction());

        popup.add(newFile);
        popup.add(openFile);
        popup.add(save);
        popup.add(new JSeparator());
        popup.add(selectAll);
        popup.add(copy);
        popup.add(paste);

        return popup;
    }

    /*
     * Обработчик вызова информации о программе
     */
    private ActionListener infoAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JPanel panel = new JPanel();
                final GridBagConstraints gbc = new GridBagConstraints();
                final GridBagLayout gbl = new GridBagLayout();
                panel.setLayout(gbl);
                final Font font = new Font("Verdana", Font.PLAIN, 12);
                final JLabel jLabel = new JLabel("<html><font face=’verdana’ size = 5><p align='center'>Курсовая работа по СПО</p></html>");
                jLabel.setVerticalAlignment(JLabel.TOP);
                panel.add(jLabel, gbc);
                gbc.gridy = 1;

                final JLabel jLabel1 = new JLabel();
                String s = "<html><font face=’verdana’ size = 4>"+"<br><p align='center'>Студента группы СКС-18<br>"+"Романко Б. А.<br>"+"На тему:<br>"+"\"Small C++\"</p><br><br><br><br></html>";
                jLabel1.setText(s);
                jLabel1.setVerticalAlignment(JLabel.CENTER);
                panel.add(jLabel1, gbc);
                gbc.gridy = 2;

                final JLabel jLabel2 = new JLabel("<html><font face=’verdana’ size = 4><p align='center'>Алчевск, 2021</p></html>");
                jLabel2.setVerticalAlignment(JLabel.BOTTOM);
                panel.add(jLabel2, gbc);
                gbc.gridy = 0;

                JOptionPane.showMessageDialog(null, panel, "О программе", JOptionPane.INFORMATION_MESSAGE);
            }
        };
    }

}