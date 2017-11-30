package com.berniac.vocalwarmup.ui.training.categories;

import com.berniac.vocalwarmup.ui.data.IWarmUpRepository;

/**
 * Created by Mikhail Lipkovich on 11/27/2017.
 */
public class CategoryListPresenter {

    private IWarmUpRepository repository;

    public CategoryListPresenter(IWarmUpRepository repository) {
        this.repository = repository;
    }

    public void onBindCategoryAtPosition(CategoryRowView rowView, int position) {
        rowView.setTitle(repository.getCategories().get(position).getTitle());
        rowView.setImage(repository.getCategories().get(position).getImageId());
    }

    public int getCategoriesCount() {
        return repository.getCategoriesCount();
    }
}
