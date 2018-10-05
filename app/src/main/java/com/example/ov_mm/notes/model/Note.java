package com.example.ov_mm.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Note implements Parcelable {

    private Long id;

    private Date date;

    private String title;

    private String content;

    public static final Parcelable.Creator<Note> CREATOR = new Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel source) {
            Note note = new Note();
            note.id = (Long) source.readValue(null);
            note.title = (String) source.readValue(null);
            note.content = (String) source.readValue(null);
            note.date = (Date) source.readValue(null);
            return note;
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(content);
        dest.writeValue(date);
    }
}