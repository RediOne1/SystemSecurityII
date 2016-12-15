package pl.appsprojekt.systemsecurityii.presenter;

import pl.appsprojekt.systemsecurityii.view.IView;

/**
 * Created by redione1 on 02.11.2016.
 */

public interface Presenter<T extends IView> {

    void attachView(T view);
}
