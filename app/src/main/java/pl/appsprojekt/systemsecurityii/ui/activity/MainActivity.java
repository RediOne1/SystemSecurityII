package pl.appsprojekt.systemsecurityii.ui.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.appsprojekt.systemsecurityii.R;
import pl.appsprojekt.systemsecurityii.presenter.MainPresenter;
import pl.appsprojekt.systemsecurityii.ui.dialog.JsonDialog;
import pl.appsprojekt.systemsecurityii.view.IMainView;

public class MainActivity extends AppCompatActivity implements IMainView {

	public MainPresenter presenter;
	@BindView(R.id.prover_btn)
	Button proverBtn;
	@BindView(R.id.verifier_btn)
	Button verifierBtn;
	@BindView(R.id.generate_world_btn)
	Button generateWorldBtn;
	@BindView(R.id.set_world)
	Button readJsonWorldBtn;
	@BindView(R.id.generate_sign)
	Button generateSignBtn;
	@BindView(R.id.set_sign)
	Button setSignBtn;
	@BindView(R.id.verify)
	Button verifyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		presenter = new MainPresenter();
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

	@OnClick(R.id.generate_sign)
	public void onGenerateSignClick() {
		presenter.generateSign();
	}

	@OnClick(R.id.set_sign)
	public void onSetSign() {
		JsonDialog dialog = JsonDialog.newInstance("Insert Signature JSON");
		dialog.setJsonInsertedListener(presenter::setSign);
		dialog.show(getSupportFragmentManager(), "Insert sign");
	}

	@OnClick(R.id.verify)
	public void onVerifyClick() {
		presenter.verify();
	}

	@Override
	public void showStage(int stage) {
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
				generateSignBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_GENERATE_SIGN:
				generateWorldBtn.setEnabled(false);
				generateSignBtn.setEnabled(true);
				generateSignBtn.setVisibility(View.VISIBLE);
				break;
			case MainPresenter.STAGE_SET_WORLD:
				proverBtn.setEnabled(false);
				readJsonWorldBtn.setVisibility(View.VISIBLE);
				setSignBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_SET_SIGN:
				readJsonWorldBtn.setEnabled(false);
				setSignBtn.setVisibility(View.VISIBLE);
				verifierBtn.setVisibility(View.GONE);
				break;
			case MainPresenter.STAGE_VERIFY:
				setSignBtn.setEnabled(false);
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
