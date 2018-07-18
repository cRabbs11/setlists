package com.example.evgeny.setlist_mobile;

public interface ThreaderInterface {
    void send(String method, String request);
    void get(String method, String result);
}
