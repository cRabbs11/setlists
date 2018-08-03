package com.example.evgeny.setlist_mobile.utils;

import android.os.Bundle;

import com.example.evgeny.setlist_mobile.model.Artist;
import com.example.evgeny.setlist_mobile.model.Setlist;
import com.example.evgeny.setlist_mobile.net.SetlistConnectNew;

import java.util.List;

public class Threader {

    Parser parser;
    static Threader threader;

    public interface CallbackArtists {
        void addArtists(List<Artist> artists);
        void error();
    }

    public interface CallbackSetlists {
        void addSetlists(List<Setlist> setlists);
        void error();
    }

    private Threader() {
        parser = Parser.getInstance();
    }

    public static Threader getInstance() {
        if (threader!=null) {
            return threader;
        } else {
            threader = new Threader();
            return threader;
        }
    }

    /**
     * получение списка артистов от сервера
     */
    public void getArtists(Bundle data, SetlistConnectNew.AnswerListener answerListener, CallbackArtists callbackArtists) {
        String request = "getArtists";
        SetlistConnectNew setlistConnectNew = new SetlistConnectNew(request, data, answerListener);
    }

    /**
     * получение списка сетлистов артиста
     */
    public void getSetlists(Bundle data, SetlistConnectNew.AnswerListener answerListener, CallbackSetlists callbackSetlists) {
        String request = "getSetlists";
        SetlistConnectNew setlistConnectNew = new SetlistConnectNew(request, data, answerListener);
    }
}
