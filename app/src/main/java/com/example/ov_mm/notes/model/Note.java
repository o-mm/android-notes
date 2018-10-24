package com.example.ov_mm.notes.model;

import java.util.Date;

public class Note {

    private Long mId;

    private String mGuid;

    private Date mDate;

    private String mTitle;

    private String mContent;

    private boolean deleted;

    private boolean mSynced;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public String getGuid() {
        return mGuid;
    }

    public void setGuid(String guid) {
        this.mGuid = guid;
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

    public boolean isSynced() {
        return mSynced;
    }

    public void setSynced(boolean synced) {
        this.mSynced = synced;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
