package com.berniac.vocalwarmup.ui.training.patterns;

import com.berniac.vocalwarmup.ui.data.IWarmUpRepository;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public class PatternListPresenter {

    private IWarmUpRepository repository;

    public PatternListPresenter(IWarmUpRepository repository) {
        this.repository = repository;
    }

    public void onBindPatternAtPosition(PatternRowView rowView, int position) {
        rowView.setTitle(repository.getPatterns().get(position).getTitle());
        rowView.setImage(repository.getPatterns().get(position).getImageId());
        rowView.setButton(repository.getPatterns().get(position).getPlayButtonId());
    }

    public int getPatternsCount() {
        return repository.getPatternsCount();
    }
}
