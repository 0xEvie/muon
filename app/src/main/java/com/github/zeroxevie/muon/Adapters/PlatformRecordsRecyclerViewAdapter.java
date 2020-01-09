package com.github.zeroxevie.muon.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.zeroxevie.muon.Activities.View_Platform_Record_Activity;
import com.github.zeroxevie.muon.DMBS.DBManager;
import com.github.zeroxevie.muon.Objects.PlatformRecord;
import com.github.zeroxevie.muon.R;
import io.realm.Case;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class PlatformRecordsRecyclerViewAdapter extends RealmRecyclerViewAdapter<PlatformRecord, PlatformRecordsRecyclerViewAdapter.MyViewHolder>
{
    DBManager dbManager;
    Context thisContext;

    public PlatformRecordsRecyclerViewAdapter(RealmResults<PlatformRecord> data, Context thisContext)
    {
        super(data, true);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_platform_record, parent, false);
        thisContext = parent.getContext();
        dbManager = new DBManager(thisContext);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PlatformRecord obj = getItem(position);

        holder.title.setText(obj.getPlatform_title());
        holder.favouriteButton.setChecked(obj.isFavourite());

        View.OnClickListener deleteWebsiteListener = new View.OnClickListener(){

            public void onClick(View v) {

                PlatformRecord platformRecordToDelete = obj;
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(thisContext, android.R.style.Theme_Material_Dialog);

                builder.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.deletePlatformRecord(obj);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }};

        View.OnClickListener favouriteButtonListener = new View.OnClickListener(){

            public void onClick(View v) {
                PlatformRecord platformRecord = obj;
                holder.favouriteButton.setChecked(holder.favouriteButton.isChecked());
                dbManager.setFavourite(platformRecord);
            }};

        View.OnClickListener listItemClickListener = new View.OnClickListener(){

            public void onClick(View v) {
                PlatformRecord platformRecordToView = obj;

                Intent viewIntent = new Intent(v.getContext(), View_Platform_Record_Activity.class);
                viewIntent.putExtra("id", platformRecordToView.getId());
                v.getContext().startActivity(viewIntent);
            }};

        holder.favouriteButton.setOnClickListener(favouriteButtonListener);
        holder.deleteButton.setOnClickListener(deleteWebsiteListener);
        holder.itemView.setOnClickListener(listItemClickListener);

    }

    //Favourite Filter
    public void filterFavouriteData(String searchText)
    {
        if (searchText != null | !searchText.equals(""))
        {
            updateData(dbManager.getRealm().where(PlatformRecord.class).equalTo("favourite", true).contains("platform_title", searchText, Case.INSENSITIVE)
                    .sort("platform_title")
                    .findAll());
        } else {
            updateData(dbManager.getRealm().where(PlatformRecord.class).equalTo("favourite", true).sort("platform_title").findAll());

        }
    }

    public Filter getFavouriteFilter() {
        return new FavouriteFilter(this);
    }

    private class FavouriteFilter extends Filter {
        private final PlatformRecordsRecyclerViewAdapter adapter;

        private FavouriteFilter(PlatformRecordsRecyclerViewAdapter platformAdapter) {
            this.adapter = platformAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence searchText, FilterResults results) {
            adapter.filterFavouriteData(searchText.toString());
        }
    }

    //Favourite Filter

    //Website Filter
    public void filterWebsiteData(String searchText)
    {
        if (searchText != null | !searchText.equals(""))
        {
            updateData(dbManager.getRealm().where(PlatformRecord.class).equalTo("platform_type", "Website")
                    .contains("platform_title", searchText, Case.INSENSITIVE)
                    .sort("platform_title")
                    .findAll());
        } else {
            updateData(dbManager.getRealm().where(PlatformRecord.class).equalTo("platform_type", "Website").sort("platform_title").findAll());
        }
    }

    public Filter getWebsiteFilter() {
        return new WebsiteFilter(this);
    }
    
    private class WebsiteFilter extends Filter {
        private final PlatformRecordsRecyclerViewAdapter adapter;

        private WebsiteFilter(PlatformRecordsRecyclerViewAdapter platformAdapter) {
            this.adapter = platformAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence searchText, FilterResults results) {
            adapter.filterWebsiteData(searchText.toString());
        }
    }

    //WebsiteFilter

    //Application Filter
    public void filterApplicationData(String searchText)
    {
        if (searchText != null | !searchText.equals(""))
        {
            updateData(dbManager.getRealm().where(PlatformRecord.class).equalTo("platform_type", "Application")
                    .contains("platform_title", searchText, Case.INSENSITIVE)
                    .sort("platform_title")
                    .findAll());
            dbManager.getRealm().where(PlatformRecord.class).equalTo("favourite", true).sort("platform_title").findAll();

        } else {
            updateData(dbManager.getRealm().where(PlatformRecord.class).equalTo("platform_type", "Application").sort("platform_title").findAll());
        }
    }

    public Filter getApplicationFilter() {
        return new ApplicationFilter(this);
    }

    private class ApplicationFilter extends Filter {
        private final PlatformRecordsRecyclerViewAdapter adapter;

        private ApplicationFilter(PlatformRecordsRecyclerViewAdapter platformAdapter) {
            this.adapter = platformAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence searchText, FilterResults results) {
            adapter.filterApplicationData(searchText.toString());
        }
    }

    //Application Filter//

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton deleteButton;
        public ToggleButton favouriteButton;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.platformRecordTitle);
            deleteButton = view.findViewById(R.id.deleteButton);
            favouriteButton = view.findViewById(R.id.favouriteButton);
        }
    }


}
