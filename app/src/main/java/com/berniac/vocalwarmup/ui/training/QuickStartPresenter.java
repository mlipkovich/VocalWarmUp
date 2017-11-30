package com.berniac.vocalwarmup.ui.training;

import com.berniac.vocalwarmup.ui.data.IWarmUpRepository;
import com.berniac.vocalwarmup.ui.data.WarmUpRepository;
import com.berniac.vocalwarmup.ui.training.patterns.PatternListPresenter;

/**
 * Created by Mikhail Lipkovich on 11/28/2017.
 */
public class QuickStartPresenter {

    private QuickStartFragment view;
    private IWarmUpRepository warmUpRepository = new WarmUpRepository();

    public void onAttach(QuickStartFragment view) {
        this.view = view;
    }

    public PatternListPresenter getPatternListPresenter() {
        return new PatternListPresenter(warmUpRepository);
    }
}
