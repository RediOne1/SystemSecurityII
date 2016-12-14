package pl.appsprojekt.systemsecurityii.view;

import android.support.annotation.StringRes;

import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.state.ChooseProtocolState;

/**
 * author:  redione1
 * date:    13.12.2016
 */

public interface INewMainView extends IView {
	void showAvailableProtocols(ChooseProtocolState.PROTOCOL[] values);

	void showHint(@StringRes int hintRes);

	void printOutputMessage(Message message);

	void cleatOutput();
}
