package ru.lexp00.chat.server.core;

import ru.lexp00.chat.library.Library;
import ru.lexp00.chat.network.SocketThread;
import ru.lexp00.chat.network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {
    private String nickname;
    private boolean isAuthorized;
    private boolean isReconnected;
    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }


    public String getNickname() {
        return nickname;
    }

    public boolean isReconnected() {
        return isReconnected;
    }

    public void reconnect() {
        isReconnected = true;
        close();
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void authAccept(String nickname) {
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(Library.getAuthAccept(nickname));
    }

    public void authFail() {
        sendMessage(Library.getAuthDenied());
        close();
    }

    void msgFormatError(String msg) {
        sendMessage(Library.getMsgFormatError(msg));
        close();
    }
}
