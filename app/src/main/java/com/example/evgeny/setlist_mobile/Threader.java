package com.example.evgeny.setlist_mobile;

public class Threader {

    static Threader threader;
    private ThreaderInterface threaderInterface;

    Thread thread;

    private Threader() {
        thread = new Thread();
    }

    public static Threader getInstance() {
        if (threader!=null) {
            return threader;
        } else {
            threader = new Threader();
            return threader;
        }
    }

    public void sign(ThreaderInterface threaderInterface) {
        this.threaderInterface = threaderInterface;
    }

    // Этот метод вызывается из главного потока GUI.
    public void newThread(Runnable runnable) {
        // Здесь трудоемкие задачи переносятся в дочерний поток.
        Thread thread = new Thread(null, runnable,
                "Background");
        thread.start();
    }
}
