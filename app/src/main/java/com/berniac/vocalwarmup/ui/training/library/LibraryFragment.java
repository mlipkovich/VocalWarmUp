package com.berniac.vocalwarmup.ui.training.library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berniac.vocalwarmup.R;
import com.berniac.vocalwarmup.ui.ResourcesProvider;
import com.berniac.vocalwarmup.ui.model.WarmUpRepository;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public class LibraryFragment extends Fragment {

    private LibraryListPresenter libraryListPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        RecyclerView libraryItemsList = (RecyclerView) view.findViewById(R.id.list_library_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        libraryItemsList.setLayoutManager(layoutManager);
        libraryItemsList.setItemAnimator(new DefaultItemAnimator());
        libraryItemsList.setHasFixedSize(true);

        libraryListPresenter = new LibraryListPresenter();
        LibraryListView listView = new LibraryListAdapter(libraryListPresenter, getContext());

        libraryItemsList.setAdapter(listView);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(libraryItemsList.getContext(), layoutManager.getOrientation());
        libraryItemsList.addItemDecoration(dividerItemDecoration);

        return view;
    }

    public LibraryListPresenter getPresenter() {
        return libraryListPresenter;
    }
}