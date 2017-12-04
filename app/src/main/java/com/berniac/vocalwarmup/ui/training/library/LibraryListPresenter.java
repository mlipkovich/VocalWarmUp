package com.berniac.vocalwarmup.ui.training.library;

import com.berniac.vocalwarmup.model.HierarchyItem;
import com.berniac.vocalwarmup.ui.model.IWarmUpRepository;
import com.berniac.vocalwarmup.ui.training.ItemRowView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/27/2017.
 */
public class LibraryListPresenter {

    private LibraryListView view;
    private IWarmUpRepository repository;
    private List<Integer> categoryIds = new LinkedList<>();
    private HierarchyItem[] itemsList;

    public LibraryListPresenter(IWarmUpRepository repository) {
        this.repository = repository;
        this.itemsList = repository.getItemsByHierarchy(categoryIds);
    }

    public void onAttach(LibraryListAdapter view) {
        this.view = view;
    }

    public void onBindCategoryAtPosition(ItemRowView rowView, int position) {
        rowView.setTitle(itemsList[position].getName());
        rowView.setImage(itemsList[position].getImage());
    }

    public int getItemsCount() {
        return itemsList.length;
    }

    public void onItemClicked(int clickedItemPosition) {
        goDownByHierarchy(clickedItemPosition);
    }

    private void goUpByHierarchy() {
        categoryIds.remove(categoryIds.size() - 1);
        itemsList = repository.getItemsByHierarchy(categoryIds);
        view.reloadListItems();
    }

    private void goDownByHierarchy(int categoryId) {
        categoryIds.add(categoryId);
        itemsList = repository.getItemsByHierarchy(categoryIds);
        view.reloadListItems();
    }

    public int getItemsType() {
        // TODO: itemsList.length = 0 should never happen
        if (itemsList.length > 0 && itemsList[0].getType() == HierarchyItem.ItemType.CATEGORY) {
            return view.getCategoriesViewType();
        }

        return view.getDrawsViewType();
    }

    public boolean onBackButtonClicked() {
        if (categoryIds.isEmpty()) {
            return false;
        }
        goUpByHierarchy();
        return true;
    }
}
