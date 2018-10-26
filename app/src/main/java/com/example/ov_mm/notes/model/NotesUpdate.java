package com.example.ov_mm.notes.model;

import java.util.Date;

public class NotesUpdate {

    private Long mId;
    private Long mVersion;
    private Date mDate;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getVersion() {
        return mVersion;
    }

    public void setVersion(Long version) {
        mVersion = version;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
