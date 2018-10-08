package com.example.ov_mm.notes.activity.bl;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ov_mm.notes.model.Note;

import java.util.Date;
import java.util.Objects;

public class ParcelableNote implements Parcelable {

    private final Note note;
    private boolean changed;

    public static final Parcelable.Creator<ParcelableNote> CREATOR = new Creator<ParcelableNote>() {

        @Override
        public ParcelableNote createFromParcel(Parcel source) {
            Note note = new Note();
            note.setId((Long) source.readValue(null));
            note.setTitle((String) source.readValue(null));
            note.setContent((String) source.readValue(null));
            note.setDate((Date) source.readValue(null));
            return new ParcelableNote(note);
        }

        @Override
        public ParcelableNote[] newArray(int size) {
            return new ParcelableNote[size];
        }
    };

    public ParcelableNote(Note note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(note.getId());
        dest.writeValue(note.getTitle());
        dest.writeValue(note.getContent());
        dest.writeValue(note.getDate());
    }

    public Note getNote() {
        return note;
    }

    public String getTitle() {
        return note.getTitle();
    }

    public void setTitle(String title) {
        if (!Objects.equals(note.getTitle(), title)) {
            note.setTitle(title);
            changed = true;
        }
    }

    public String getContent() {
        return note.getContent();
    }

    public void setContent(String content) {
        if (!Objects.equals(note.getContent(), content)) {
            note.setContent(content);
            changed = true;
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public Date getDate() {
        return note.getDate();
    }
}
