package com.chauffeursync.models;

public class Role {
    private final String id;
    private final String title;

    public Role(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
