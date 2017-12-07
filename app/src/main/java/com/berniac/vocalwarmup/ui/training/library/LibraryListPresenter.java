package com.berniac.vocalwarmup.ui.training.library;

import com.berniac.vocalwarmup.model.HierarchyItem;
import com.berniac.vocalwarmup.ui.model.IWarmUpRepository;
import com.berniac.vocalwarmup.ui.training.ItemRowView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mikhail Lipkovich on 11/27/2017.
 */
public class LibraryListPresenter {

    private LibraryListView view;
    private IWarmUpRepository repository;
    private HierarchyItem[] itemsList;

    public LibraryListPresenter(IWarmUpRepository repository) {
        this.repository = repository;
        this.itemsList = repository.getItemsByHierarchy(Collections.EMPTY_LIST);
    }

    public void onAttach(LibraryListView view) {
        this.view = view;
    }

    public void onBindCategoryAtPosition(ItemRowView rowView, int position) {
        rowView.setTitle(itemsList[position].getName());
        rowView.setImage(itemsList[position].getImage());
    }

    public int getItemsCount() {
        return itemsList.length;
    }

    public void setCategories(List<Integer> categories) {
        itemsList = repository.getItemsByHierarchy(categories);
        view.reloadListItems();
    }

    public int getItemsType() {
        // TODO: itemsList.length = 0 should never happen
        if (itemsList.length > 0 && itemsList[0].getType() == HierarchyItem.ItemType.CATEGORY) {
            return view.getCategoriesViewType();
        }

        return view.getDrawsViewType();
    }


    public void onCategoryClicked(int clickedItem) {
        String clickedItemName = itemsList[clickedItem].getName();
        SelectedCategories.INSTANCE.goDownByHierarchy(clickedItem);
        view.switchToActivityWithCategory(clickedItemName);
    }
}
