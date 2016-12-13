package pl.appsprojekt.systemsecurityii.ui.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import pl.appsprojekt.systemsecurityii.R;
import pl.appsprojekt.systemsecurityii.interfaces.IJsonInsertedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class JsonDialog extends DialogFragment {

	private static final String TITLE = "title";
	private static final String JSON = "json";
	private IJsonInsertedListener listener;

	public JsonDialog() {
		// Required empty public constructor
	}

	public static JsonDialog newInstance(String title) {
		Bundle arg = new Bundle();
		arg.putString(TITLE, title);
		JsonDialog dialog = new JsonDialog();
		dialog.setArguments(arg);
		return dialog;
	}

	public static JsonDialog newInstance(String title, String json) {
		Bundle arg = new Bundle();
		arg.putString(TITLE, title);
		arg.putString(JSON, json);
		JsonDialog dialog = new JsonDialog();
		dialog.setArguments(arg);
		return dialog;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle arg = getArguments();
		String json = arg.getString(JSON, "");
		String title = arg.getString(TITLE, "");
		try {
			json = new JSONObject(json).toString(4);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.dialog_json, null, false);
		EditText jsonInput = (EditText) view.findViewById(R.id.json_input);
		jsonInput.setText(json);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setView(view)
				.setTitle(title)
				.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
					if (listener != null)
						listener.onJsonInserted(jsonInput.getText().toString());
					dialogInterface.dismiss();
				})
				.setNeutralButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());

		return builder.create();
	}

	public void setJsonInsertedListener(IJsonInsertedListener listener) {
		this.listener = listener;
	}
}
