package pl.appsprojekt.systemsecurityii.view;

import pl.appsprojekt.systemsecurityii.presenter.MainPresenter;

/**
 * Created by redione1 on 02.11.2016.
 */

public interface IMainView extends IView {

	void showStage(int stage);

	void showJson(String json);
}
