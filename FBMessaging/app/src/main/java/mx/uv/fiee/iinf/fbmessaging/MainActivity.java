package mx.uv.fiee.iinf.fbmessaging;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class MainActivity  extends Activity {
    private LinkedList<Message> messages;
    private MessagesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        messages = new LinkedList<>();

        RecyclerView rvMessages = findViewById (R.id.rvMessages);
        rvMessages.setItemAnimator (new DefaultItemAnimator ());
        rvMessages.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));
        rvMessages.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));

        adapter = new MessagesAdapter ();
        rvMessages.setAdapter (adapter);
    }

    @Override
    protected void onResume() {
        super.onResume ();
        registerReceiver (receiver, new IntentFilter ("FBMessaging"));
    }

    @Override
    protected void onPause() {
        super.onPause ();
        unregisterReceiver (receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            String title = intent.getStringExtra ("MESSAGE_TITLE");
            String body = intent.getStringExtra  ("MESSAGE_BODY");

            Message m = new Message ();
            m.title = title;
            m.body = body;

            messages.add (m);
            adapter.notifyDataSetChanged ();
        }
    };

    class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesVH> {

        @NonNull
        @Override
        public MessagesAdapter.MessagesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.list_item, parent, false);
            return new MessagesVH (view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessagesAdapter.MessagesVH holder, int position) {
            Message m = messages.get (position);
            holder.tvTitle.setText (m.title);
            holder.tvBody.setText (m.body);
        }

        @Override
        public int getItemCount() {
            return messages.size ();
        }

        class MessagesVH extends RecyclerView.ViewHolder {
            TextView tvTitle;
            TextView tvBody;

            public MessagesVH(@NonNull View itemView) {
                super (itemView);
                tvTitle = itemView.findViewById (R.id.tvTitle);
                tvBody = itemView.findViewById (R.id.tvBody);
            }
        }

    }

}

class Message {
    public String title;
    public String body;
}