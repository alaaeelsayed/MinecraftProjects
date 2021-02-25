package me.headshot.ineedjesus.tasks;

import me.headshot.ineedjesus.INeedJesus;
import me.headshot.ineedjesus.util.JesusUtil;

public class TalkRunnable implements Runnable {
    private INeedJesus plugin;

    public TalkRunnable(INeedJesus plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (JesusUtil.getJesus() != null)
            JesusUtil.getJesus().sendRandomMessageWithVoice(plugin);
    }
}