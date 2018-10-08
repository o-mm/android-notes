package com.example.ov_mm.notes.bl;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ov_mm.notes.model.Note;

import java.util.Date;
import java.util.Objects;

public class ParcelableNote implements Parcelable {

    private final Note mNote;
    private boolean mChanged;

    ParcelableNote(Note note) {
        this.mNote = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mNote.getId());
        dest.writeValue(mNote.getTitle());
        dest.writeValue(mNote.getContent());
        dest.writeValue(mNote.getDate());
    }

    public String getTitle() {
        return mNote.getTitle();
    }

    public void setTitle(String title) {
        if (!Objects.equals(mNote.getTitle(), title)) {
            mNote.setTitle(title);
            mChanged = true;
        }
    }

    public String getContent() {
        return mNote.getContent();
    }

    public void setContent(String content) {
        if (!Objects.equals(mNote.getContent(), content)) {
            mNote.setContent(content);
            mChanged = true;
        }
    }

    public boolean isChanged() {
        return mChanged;
    }

    public Date getDate() {
        return mNote.getDate();
    }

    Note getNote() {
        return mNote;
    }

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
}
