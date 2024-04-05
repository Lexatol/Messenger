package ru.lexp00.chat.server.gui;


import ru.lexp00.chat.server.core.ChatServer;
import ru.lexp00.chat.server.core.ChatServerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, ChatServerListener {
    private static final int POS_X = 1000;
    private static final int POS_Y = 550;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private ChatServer chatServer = new ChatServer(this::onChatServerMessage);
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JPanel panel = new JPanel(new GridLayout(1,2 ));
    private final JTextArea log = new JTextArea();


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerGUI::new);
    }

    private ServerGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//закрываем не только окошко, но и выходим из программы
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);//устанавливаем границы и координаты
        setResizable(false);//размеры остаются не изменяемыми
        setTitle("Chat Server");//называем окошко
        setAlwaysOnTop(true);//окошко будет поверх всех окон
        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(log);

//        setLayout(new GridLayout(1, 2));//задаем размер
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);

        panel.add(btnStart);
        panel.add(btnStop);
        add(panel, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        setVisible(true);//видимость
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == btnStart) {
            chatServer.start(8189);
        } else if(src == btnStop) {
            chatServer.close();
        } else {
            throw new RuntimeException("Надо обработать эту кнопку, я такую не знаю " + src);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
        String msg;
        StackTraceElement [] ste = throwable.getStackTrace();
        msg = "Exception in Thread " + thread.getName() + " " +
                throwable.getClass().getCanonicalName() + " : " + throwable.getMessage() + "\n\t" + ste[0];
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    @Override
    public void onChatServerMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
