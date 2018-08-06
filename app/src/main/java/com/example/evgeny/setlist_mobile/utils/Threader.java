package com.example.evgeny.setlist_mobile.utils;

import android.os.Bundle;

import com.example.evgeny.setlist_mobile.model.Artist;
import com.example.evgeny.setlist_mobile.model.Setlist;
import com.example.evgeny.setlist_mobile.net.SetlistConnect;

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
    public void getArtists(Bundle data, SetlistConnect.AnswerListener answerListener, CallbackArtists callbackArtists) {
        String request = "getArtists";
        SetlistConnect setlistConnect = new SetlistConnect(request, data, answerListener);
    }

    /**
     * получение списка сетлистов артиста
     */
    public void getSetlists(Bundle data, SetlistConnect.AnswerListener answerListener, CallbackSetlists callbackSetlists) {
        String request = "getSetlists";
        SetlistConnect setlistConnect = new SetlistConnect(request, data, answerListener);
    }
}
