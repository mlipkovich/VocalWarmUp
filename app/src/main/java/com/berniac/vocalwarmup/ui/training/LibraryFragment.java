package com.berniac.vocalwarmup.ui.training;

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
import com.berniac.vocalwarmup.ui.training.categories.CategoryAdapter;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
// TODO: Ugly. Think about better architecture
public class LibraryFragment extends Fragment {

    private LibraryPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        RecyclerView categoryList = (RecyclerView) view.findViewById(R.id.list_categories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        categoryList.setLayoutManager(layoutManager);
        categoryList.setItemAnimator(new DefaultItemAnimator());
        categoryList.setHasFixedSize(true);
        categoryList.setAdapter(new CategoryAdapter(presenter.getCategoryListPresenter()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(categoryList.getContext(), layoutManager.getOrientation());
        categoryList.addItemDecoration(dividerItemDecoration);

        presenter.onAttach(this);
        return view;
    }

    public void setPresenter(LibraryPresenter presenter) {
        this.presenter = presenter;
    }
}