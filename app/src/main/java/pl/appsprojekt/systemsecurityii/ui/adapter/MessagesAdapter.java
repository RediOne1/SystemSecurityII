package pl.appsprojekt.systemsecurityii.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.appsprojekt.systemsecurityii.R;
import pl.appsprojekt.systemsecurityii.model.Message;

/**
 * author:  redione1
 * date:    13.12.2016
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

	private final List<Message> messagesList;

	public MessagesAdapter(List<Message> messagesList) {
		this.messagesList = messagesList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.item_message, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Message message = messagesList.get(position);
		holder.textView.setText(message.getContent());

		if (!message.isFromMe())
			holder.cardView.setBackgroundResource(0);
	}

	@Override
	public int getItemCount() {
		return messagesList.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.item_message_cardView)
		CardView cardView;

		@BindView(R.id.item_message_content)
		TextView textView;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
