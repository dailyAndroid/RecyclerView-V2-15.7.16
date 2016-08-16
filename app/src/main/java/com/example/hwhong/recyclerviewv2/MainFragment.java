package com.example.hwhong.recyclerviewv2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwhong on 7/14/16.
 */
public class MainFragment extends Fragment {
    public static final String TAG = "MyRecyclerList";
    private List<ListItems> listItemsList = new ArrayList<ListItems>();

    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;

    private int counter = 0;
    private String count;
    private String jsonSubreddit;
    private String after_id;
    private static final String gaming = "gaming";
    private static final String aww = "aww";
    private static final String pics = "pics";
    private static final String funny = "funny";
    private static final String food = "food";
    private static final String subredditUrl = "http://www.reddit.com/r/";
    private static final String jsonEnd = "/.json";
    private static final String qCount = "?count=";
    private static final String after = "&after=";

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = layoutInflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        updateList(MainActivity.fragSubreddit);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                int lastFirstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
                loadMore(jsonSubreddit);
            }
        });

        Button gamingButton = (Button) rootView.findViewById(R.id.gamingButton);
        gamingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fragSubreddit = gaming;
                reloadFragment();
            }
        });

        Button picsButton = (Button) rootView.findViewById(R.id.picsButton);
        picsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fragSubreddit = pics;
                reloadFragment();
            }
        });

        return rootView;
    }

    public void updateList(String subreddit) {

        // Set the counter to 0. This counter will be used to create new json urls
        // In the loadMore function we will increase this integer by 25
        counter = 0;

        // Create the reddit json url for parsing
        subreddit = subredditUrl + subreddit + jsonEnd;

        //declare the adapter and attach it to the recyclerview
        adapter = new MyRecyclerAdapter(getActivity(), listItemsList);
        recyclerView.setAdapter(adapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Clear the adapter because new data is being added from a new subreddit
        adapter.clearAdapter();

        showPD();

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());
                        hidePD();

                        // Parse json data.
                        // Declare the json objects that we need and then for loop through the children array.
                        // Do the json parse in a try catch block to catch the exceptions
                        try {
                            JSONObject data = response.getJSONObject("data");
                            after_id = data.getString("after");
                            JSONArray children = data.getJSONArray("children");

                            for (int i = 0; i < children.length(); i++) {

                                JSONObject post = children.getJSONObject(i).getJSONObject("data");
                                ListItems item = new ListItems();
                                item.setTitle(post.getString("title"));
                                item.setThumbnail(post.getString("thumbnail"));
                                item.setUrl(post.getString("url"));
                                item.setSubreddit(post.getString("subreddit"));
                                item.setAuthor(post.getString("author"));

                                jsonSubreddit = post.getString("subreddit");

                                listItemsList.add(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Update list by notifying the adapter of changes
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePD();
            }
        });

        queue.add(jsObjRequest);
    }

    public void loadMore(String subreddit) {

        // Set the counter to 0. This counter will be used to create new json urls
        // In the loadMore function we will increase this integer by 25
        counter = counter + 25;
        count = String.valueOf(counter);
        subreddit = jsonSubreddit;

        // Create the reddit json url for parsing
        subreddit = subredditUrl + subreddit + jsonEnd + qCount + after + after_id;

        //declare the adapter and attach it to the recyclerview
        adapter = new MyRecyclerAdapter(getActivity(), listItemsList);
        recyclerView.setAdapter(adapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        showPD();

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                hidePD();

                // Parse json data.
                // Declare the json objects that we need and then for loop through the children array.
                // Do the json parse in a try catch block to catch the exceptions
                try {
                    JSONObject data = response.getJSONObject("data");
                    after_id = data.getString("after");
                    JSONArray children = data.getJSONArray("children");

                    for (int i = 0; i < children.length(); i++) {

                        JSONObject post = children.getJSONObject(i).getJSONObject("data");
                        ListItems item = new ListItems();
                        item.setTitle(post.getString("title"));
                        item.setThumbnail(post.getString("thumbnail"));
                        item.setUrl(post.getString("url"));
                        item.setSubreddit(post.getString("subreddit"));
                        item.setAuthor(post.getString("author"));

                        jsonSubreddit = post.getString("subreddit");

                        listItemsList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePD();
            }
        });

        queue.add(jsObjRequest);
    }

    private void showPD() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    private void hidePD() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void funnyUpdate(View view) {
        updateList(funny);
    }

    public void foodUpdate(View view) {
        updateList(food);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePD();
    }

    public void reloadFragment() {
        Fragment fragment = new MainFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }
}
