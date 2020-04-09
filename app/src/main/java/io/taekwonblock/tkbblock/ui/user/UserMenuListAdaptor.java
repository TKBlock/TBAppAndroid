package io.taekwonblock.tkbblock.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.FirebaseLoginActivity;
import io.taekwonblock.tkbblock.LoginActivity;
import io.taekwonblock.tkbblock.R;

public class UserMenuListAdaptor extends RecyclerView.Adapter<UserMenuListAdaptor.ViewHolder> {

    List<String> menuItems;
    Context context;

    public UserMenuListAdaptor(Context context, List<String> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_menu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String title = menuItems.get(position);

        holder.tv_menuTitle.setText(title);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("POSITION", "" + position);

                switch (position) {
                    case 0: //Change Account Info
                        Intent modUserIntent = new Intent(context, ModifyUserActivity.class);
                        context.startActivity(modUserIntent);

                        break;
                    case 1: //Logout
                        Intent intent = new Intent(context, FirebaseLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        SharedPreferences sharedpreferences = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();

                        context.startActivity(intent);
                        ((Activity)context).finish();

                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView tv_menuTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.user_item_layout);
            tv_menuTitle = itemView.findViewById(R.id.user_item_menutitle);


        }
    }
}
