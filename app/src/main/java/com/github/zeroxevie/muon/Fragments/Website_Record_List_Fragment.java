package com.github.zeroxevie.muon.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.zeroxevie.muon.Adapters.PlatformRecordsRecyclerViewAdapter;
import com.github.zeroxevie.muon.DMBS.DBManager;
import com.github.zeroxevie.muon.Objects.PlatformRecord;
import com.github.zeroxevie.muon.R;
import io.realm.RealmResults;

public class Website_Record_List_Fragment extends Fragment
{
    private DBManager dbManager;
    private Context thisContext;
    private RecyclerView recyclerView;
    private RealmResults<PlatformRecord> recordsToDisplay = null;
    PlatformRecordsRecyclerViewAdapter recyclerViewAdapter;
    private TextView emptyView;

    public Website_Record_List_Fragment()
    {
        // Required empty public constructor
    }

    public static Website_Record_List_Fragment newInstance()
    {
        Website_Record_List_Fragment fragment = new Website_Record_List_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.searchButton);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {
                recyclerViewAdapter.getWebsiteFilter().filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerViewAdapter.getWebsiteFilter().filter(query);

                return false;
            }

        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_website_record_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisContext = view.getContext();
        dbManager = new DBManager(thisContext);
        recyclerView = view.findViewById(R.id.websiteRecordListView);
        emptyView = view.findViewById(R.id.empty_view);

        recordsToDisplay = dbManager.getRealm().where(PlatformRecord.class).equalTo("platform_type", "Website").sort("platform_title").findAll();

        recyclerViewAdapter = new PlatformRecordsRecyclerViewAdapter(recordsToDisplay, thisContext);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(thisContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
