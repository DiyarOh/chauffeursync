package com.chauffeursync.controllers;

import com.chauffeursync.enums.ScreenType;
import com.chauffeursync.manager.ScreenManager;

public class StartController {

    private final ScreenManager manager;

    public StartController(ScreenManager manager) {
        this.manager = manager;
    }

    public void handleLogin() {
        manager.switchTo(ScreenType.LOGIN);
    }

    public void handleRegister() {
        manager.switchTo(ScreenType.REGISTER);
    }
}
