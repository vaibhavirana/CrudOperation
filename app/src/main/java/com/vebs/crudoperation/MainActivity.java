package com.vebs.crudoperation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vebs.crudoperation.database.MovieDatabase;
import com.vebs.crudoperation.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private FloatingActionButton fabAdd;
    private MovieDatabase movieDatabase;
    private SwipeRefreshLayout swipe;
    private ImageView ivDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.rv_movie);
        fabAdd = findViewById(R.id.fab_add);

        movieDatabase = new MovieDatabase(this);
        mAdapter = new MoviesAdapter(movieList, new MoviesAdapter.OnOptionListener() {
            @Override
            public void onDeleteClick(final int id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure to delete this movie?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (movieDatabase.deleteMovie(id)) {
                            prepareMovieData();
                            mAdapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(MainActivity.this, "Something went wrong. please try again later!..", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setCancelable(false);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }

            @Override
            public void onUpdateClick(Movie movie) {
                showInsertDialog(movie);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        prepareMovieData();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareMovieData();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInsertDialog(null);
            }
        });

    }

    private void showInsertDialog(final Movie mv) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_movie);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        final EditText etTitle = dialog.findViewById(R.id.etTitle);
        final EditText etGenre = dialog.findViewById(R.id.etGenre);
        final EditText etYear = dialog.findViewById(R.id.etYear);

        if (mv != null) {
            etTitle.setText(mv.getTitle());
            etGenre.setText(mv.getGenre());
            etYear.setText(mv.getYear());
        }
        Button dialogButton = dialog.findViewById(R.id.btnSubmit);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTitle.getText().toString().trim().length() <= 0) {
                    Toast.makeText(MainActivity.this, "please enter title", Toast.LENGTH_SHORT).show();
                } else if (etGenre.getText().toString().trim().length() <= 0) {
                    Toast.makeText(MainActivity.this, "please enter genre", Toast.LENGTH_SHORT).show();
                } else if (etYear.getText().toString().trim().length() <= 0) {
                    Toast.makeText(MainActivity.this, "please enter year", Toast.LENGTH_SHORT).show();
                } else {
                    if (mv != null) {
                        boolean isupdated = movieDatabase.updateMovie(new Movie(mv.getId(), etTitle.getText().toString().trim(),
                                etGenre.getText().toString().trim(), etYear.getText().toString().trim()));
                        if (isupdated)
                            prepareMovieData();
                    } else {
                        boolean isInserted = movieDatabase.insertData(new Movie(etTitle.getText().toString().trim(),
                                etGenre.getText().toString().trim(), etYear.getText().toString().trim()));
                        if (isInserted)
                            prepareMovieData();
                    }
                    dialog.dismiss();
                }
            }
        });

        Button dialogCancelButton = dialog.findViewById(R.id.btnCancel);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void prepareMovieData() {
        movieList.clear();
        movieList.addAll(movieDatabase.getMovieList());
        swipe.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }
}
