package com.example.roboticcar;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;


public class Notification_dataAdaptor extends FirebaseRecyclerAdapter<
        notification_data, Notification_dataAdaptor.NotificationdataViewholder> {

    public Notification_dataAdaptor(
            @NonNull FirebaseRecyclerOptions<notification_data> options)
    {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull NotificationdataViewholder holder,
                     int position, @NonNull notification_data model)
    {

        holder.Data.setText(model.getData());

        holder.DateTime.setText(model.getDateTime());

    }


    @NonNull
    @Override
    public NotificationdataViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_data, parent, false);
        return new NotificationdataViewholder(view);
    }

    static class NotificationdataViewholder
            extends RecyclerView.ViewHolder {
        TextView Data, DateTime;
        public NotificationdataViewholder(@NonNull View itemView)
        {
            super(itemView);

            Data = itemView.findViewById(R.id.notification_alert_msg);
            DateTime = itemView.findViewById(R.id.notification_alert_time);

        }
    }
}
