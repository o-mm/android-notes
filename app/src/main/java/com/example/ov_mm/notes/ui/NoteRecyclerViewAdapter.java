package com.example.ov_mm.notes.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.repository.NoteWrapper;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link NoteWrapper} and makes a call to the
 * specified {@link ViewNotesFragment.OnListItemInteractionListener}.
 */
public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    @Nullable
    private List<NoteWrapper> mNotes;
    @NonNull
    private final ViewNotesFragment.OnListItemInteractionListener mOnSelectListener;

    public NoteRecyclerViewAdapter(@NonNull ViewNotesFragment.OnListItemInteractionListener listener) {
        mOnSelectListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final NoteWrapper note = getNotes().get(position);
        holder.mTitleView.setText(note.getTitle());
        holder.mContentView.setText(note.getContent());
        holder.mDateView.setText(DateFormat.format("yyyy-MM-dd HH:mm", note.getDate()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectListener.onListItemInteraction(note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getNotes().size();
    }

    @NonNull
    public List<NoteWrapper> getNotes() {
        if (mNotes == null) {
            mNotes = Collections.emptyList();
        }
        return mNotes;
    }

    public void updateData(List<NoteWrapper> notes) {
        mNotes = notes;
    }

    public interface NoteListSupplier {

        @NonNull
        List<NoteWrapper> getNotes();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mTitleView;
        private final TextView mContentView;
        private final TextView mDateView;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            mTitleView = view.findViewById(R.id.view_note_title);
            mContentView = view.findViewById(R.id.view_note_content);
            mDateView = view.findViewById(R.id.view_note_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
