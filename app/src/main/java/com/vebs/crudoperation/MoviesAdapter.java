package com.vebs.crudoperation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vebs.crudoperation.custum.SwipeRevealLayout;
import com.vebs.crudoperation.custum.ViewBinderHelper;
import com.vebs.crudoperation.model.Movie;

import java.util.List;

/**
 * Created by vaibhavi Rana on 14-10-2019.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    private OnOptionListener listener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public ImageView ivDelete;
        public SwipeRevealLayout swipeRevealLayout;
        public RelativeLayout rl;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            genre = view.findViewById(R.id.genre);
            year = view.findViewById(R.id.year);
            ivDelete = view.findViewById(R.id.iv_delete);
            swipeRevealLayout = view.findViewById(R.id.swipeRevealLayout);
            rl = view.findViewById(R.id.rl);
        }
    }

    public MoviesAdapter(List<Movie> moviesList, OnOptionListener listener) {
        this.moviesList = moviesList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Movie movie = moviesList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(movie.getId()));
        viewBinderHelper.setOpenOnlyOne(true);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteClick(movie.getId());
                }
            }
        });

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onUpdateClick(movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    interface OnOptionListener {
        void onDeleteClick(int id);
        void onUpdateClick(Movie movie);
    }
}
