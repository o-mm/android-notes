package com.example.ov_mm.notes.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.activity.ViewNotesFragment.OnListFragmentInteractionListener;
import com.example.ov_mm.notes.model.Note;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Note} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private final List<Note> notes;
    private final OnListFragmentInteractionListener onSelectListener;

    public NoteRecyclerViewAdapter(List<Note> items, OnListFragmentInteractionListener listener) {
        notes = items;
        onSelectListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Note note = notes.get(position);
        holder.titleView.setText(note.getTitle());
        holder.contentView.setText(note.getContent());
        holder.dateView.setText(DateFormat.format("yyyy-MM-dd HH:mm", note.getDate()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onSelectListener) {
                    onSelectListener.onListFragmentInteraction(note);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final TextView titleView;
        private final TextView contentView;
        private final TextView dateView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleView = view.findViewById(R.id.note_title);
            contentView = view.findViewById(R.id.note_content);
            dateView = view.findViewById(R.id.note_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}
