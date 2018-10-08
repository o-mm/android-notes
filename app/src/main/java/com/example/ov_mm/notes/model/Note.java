package com.example.ov_mm.notes.model;

import java.util.Date;

public class Note {

    private Long mId;

    private Date mDate;

    private String mTitle;

    private String mContent;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public static Note create(Long id, Date date, String title, String content) {
        Note note = new Note();
        note.mId = id;
        note.mDate = date;
        note.mTitle = title;
        note.mContent = content;
        return note;
    }
}
