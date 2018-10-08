package com.example.ov_mm.notes.model;

import java.util.Date;

public class Note {

    private Long id;

    private Date date;

    private String title;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Note create(Long id, Date date, String title, String content) {
        Note note = new Note();
        note.id = id;
        note.date = date;
        note.title = title;
        note.content = content;
        return note;
    }
}