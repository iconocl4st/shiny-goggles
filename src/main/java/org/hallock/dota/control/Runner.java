package org.hallock.dota.control;

import java.util.Timer;
import java.util.TimerTask;

public class Runner {
    Timer timer;

    Runner(Timer timer) {
        this.timer = timer;
    }

    public void start() {

    }

    public void stop() {

    }

    private static class HeartBeatTask extends TimerTask {

        @Override
        public void run() {

        }
    }

    private static class UpdatePicks extends TimerTask {

        @Override
        public void run() {

        }
    }
}
