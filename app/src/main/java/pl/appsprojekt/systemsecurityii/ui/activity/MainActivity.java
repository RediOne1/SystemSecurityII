package pl.appsprojekt.systemsecurityii.ui.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.appsprojekt.systemsecurityii.MyApplication;
import pl.appsprojekt.systemsecurityii.R;
import pl.appsprojekt.systemsecurityii.presenter.MainPresenter;
import pl.appsprojekt.systemsecurityii.ui.dialog.JsonDialog;
import pl.appsprojekt.systemsecurityii.view.IMainView;

public class MainActivity extends AppCompatActivity implements IMainView {

	@Inject
	public MainPresenter presenter;
	@BindView(R.id.prover_btn)
	Button proverBtn;
	@BindView(R.id.verifier_btn)
	Button verifierBtn;
	@BindView(R.id.generate_world_btn)
	Button generateWorldBtn;
	@BindView(R.id.set_world)
	Button readJsonWorldBtn;
	@BindView(R.id.send_x)
	Button sendXBtn;
	@BindView(R.id.set_x)
	Button insertXBtn;
	@BindView(R.id.insert_c)
	Button insertCBtn;
	@BindView(R.id.send_s)
	Button sendSBtn;
	@BindView(R.id.send_c)
	Button sendCBtn;
	@BindView(R.id.verify)
	Button verifyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.getPresenterInjector().inject(this);
		ButterKnife.bind(this);

		presenter.attachView(this);
	}

	@Override
	public void onBackPressed() {
		presenter.onBackPressed();
	}

	@OnClick(R.id.prover_btn)
	public void onProverModeClick() {
		presenter.onModeSelected(MainPresenter.MODE_PROVER);
		verifierBtn.setEnabled(false);
	}

	@OnClick(R.id.verifier_btn)
	public void onVerifierModeClick() {
		presenter.onModeSelected(MainPresenter.MODE_VERIFIER);
		proverBtn.setEnabled(false);
	}

	@OnClick(R.id.generate_world_btn)
	public void onGenerateWorldClick(Button button) {
		presenter.generateWorld();
	}

	@OnClick(R.id.set_world)
	public void onReadWorldJsonClick() {
		JsonDialog dialog = JsonDialog.newInstance("Insert World JSON");
		dialog.setJsonInsertedListener(presenter::setWorld);
		dialog.show(getSupportFragmentManager(), "InsertWorldJSON");
	}

	@OnClick(R.id.send_x)
	public void onSendXClick() {
		presenter.sendX();
	}

	@OnClick(R.id.set_x)
	public void onSetX() {
		JsonDialog dialog = JsonDialog.newInstance("Insert X JSON");
		dialog.setJsonInsertedListener(presenter::insertX);
		dialog.show(getSupportFragmentManager(), "InsertX");
	}

	@OnClick(R.id.send_c)
	public void onSendCClick() {
		presenter.sendC();
	}

	@OnClick(R.id.insert_c)
	public void onInsertCClick() {
		JsonDialog dialogFragment = JsonDialog.newInstance("Insert C JSON");
		dialogFragment.setJsonInsertedListener(presenter::insertJsonC);
		dialogFragment.show(getSupportFragmentManager(), "InsertC");
	}

	@OnClick(R.id.send_s)
	public void onSendSClick() {
		presenter.sendS();
	}

	@OnClick(R.id.verify)
	public void onVerifyClick() {
		JsonDialog dialog = JsonDialog.newInstance("Insert S");
		dialog.setJsonInsertedListener(presenter::verify);
		dialog.show(getSupportFragmentManager(), "InsertS");
	}

	@Override
	public void showStage(@MainPresenter.Stage int stage) {
		switch (stage) {
			case MainPresenter.STAGE_NONE:
			case MainPresenter.STAGE_CHOOSE_MODE:
				proverBtn.setEnabled(true);
				verifierBtn.setEnabled(true);
				generateWorldBtn.setVisibility(View.GONE);
				readJsonWorldBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_GENERATE_WORLD:
				verifierBtn.setEnabled(false);
				generateWorldBtn.setEnabled(true);
				generateWorldBtn.setVisibility(View.VISIBLE);
				sendXBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_SEND_X:
				generateWorldBtn.setEnabled(false);
				sendXBtn.setEnabled(true);
				sendXBtn.setVisibility(View.VISIBLE);
				insertCBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_RECEIVE_C:
				sendXBtn.setEnabled(false);
				insertCBtn.setEnabled(true);
				insertCBtn.setVisibility(View.VISIBLE);
				sendSBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_SEND_S:
				sendSBtn.setVisibility(View.VISIBLE);
				insertCBtn.setEnabled(false);
				break;
			case MainPresenter.STAGE_SET_WORLD:
				proverBtn.setEnabled(false);
				readJsonWorldBtn.setVisibility(View.VISIBLE);
				break;
			case MainPresenter.STAGE_SET_X:
				insertXBtn.setEnabled(true);
				insertXBtn.setVisibility(View.VISIBLE);
				sendCBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_SEND_C:
				insertXBtn.setEnabled(false);
				sendCBtn.setEnabled(true);
				sendCBtn.setVisibility(View.VISIBLE);
				verifyBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_VERIFY:
				sendCBtn.setEnabled(false);
				verifyBtn.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public void showJson(String json) {
		Log.v(getClass().getSimpleName(), json);
		DialogFragment dialogFragment = JsonDialog.newInstance("Response", json);
		dialogFragment.show(getSupportFragmentManager(), "JsonDialog");
	}
}
