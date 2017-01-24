package pl.appsprojekt.systemsecurityii.ui.activity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.appsprojekt.systemsecurityii.R;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.presenter.MainPresenter;
import pl.appsprojekt.systemsecurityii.state.ChooseProtocolState;
import pl.appsprojekt.systemsecurityii.ui.adapter.MessagesAdapter;
import pl.appsprojekt.systemsecurityii.view.INewMainView;

public class MainActivity extends AppCompatActivity implements INewMainView {

	@BindView(R.id.output_recyclerView)
	RecyclerView outputRecyclerView;

	@BindView(R.id.input_field)
	EditText inputField;

	private MainPresenter presenter;
	private List<Message> outputMessages;
	private MessagesAdapter adapter;

	public MainActivity() {
		presenter = new MainPresenter();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_main);
		ButterKnife.bind(this);

		outputMessages = new ArrayList<>();
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		adapter = new MessagesAdapter(outputMessages);
		outputRecyclerView.setLayoutManager(layoutManager);
		outputRecyclerView.setAdapter(adapter);

		presenter.attachView(this);
	}

	@Override
	public void showAvailableProtocols(ChooseProtocolState.PROTOCOL[] values) {
		String sb = getString(R.string.choose_one_protocol_of) + " " +
				TextUtils.join(", ", values);

		printOutputMessage(new Message(sb));
	}

	@Override
	public void showHint(@StringRes int hintRes) {
		inputField.setHint(hintRes);
	}

	@Override
	public void printOutputMessage(Message message) {
		Log.d("DEBUG_TAG", message.getContent());
		outputMessages.add(message);
		adapter.notifyItemInserted(outputMessages.size() - 1);
	}

	@Override
	public void cleatOutput() {
		int size = outputMessages.size();
		outputMessages.clear();
		adapter.notifyItemRangeRemoved(0, size);
	}

	@OnClick(R.id.send_button)
	public void onSendClick() {
		presenter.onUserInput(inputField.getText().toString());
		inputField.setText("");
	}
}
