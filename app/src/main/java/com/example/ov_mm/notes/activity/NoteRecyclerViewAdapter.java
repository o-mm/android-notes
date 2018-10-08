package com.example.ov_mm.notes.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.ParcelableNote;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ParcelableNote} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    @NonNull
    private final NotesSupplier mNotesSupplier;
    @Nullable
    private List<ParcelableNote> mNotes;
    @NonNull
    private final OnListFragmentInteractionListener mOnSelectListener;

    public NoteRecyclerViewAdapter(@NonNull NotesSupplier notesSupplier,
                                   @NonNull OnListFragmentInteractionListener listener) {
        this.mNotesSupplier = notesSupplier;
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
        final ParcelableNote note = getNotes().get(position);
        holder.mTitleView.setText(note.getTitle());
        holder.mContentView.setText(note.getContent());
        holder.mDateView.setText(DateFormat.format("yyyy-MM-dd HH:mm", note.getDate()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSelectListener.onListFragmentInteraction(note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getNotes().size();
    }

    @NonNull
    public List<ParcelableNote> getNotes() {
        if (mNotes == null) {
            mNotes = mNotesSupplier.getNotes();
        }
        return mNotes;
    }

    public void resetData() {
        mNotes = null;
    }

    public interface NotesSupplier {

        List<ParcelableNote> getNotes();
    }

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(ParcelableNote note);
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
