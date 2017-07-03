package com.app.elixir.makkhankitchens.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.app.elixir.makkhankitchens.Model.CompanyModel;
import com.app.elixir.makkhankitchens.Model.CompanyModelArray;
import com.app.elixir.makkhankitchens.R;
import com.app.elixir.makkhankitchens.interfac.ActionBarTitleSetter;
import com.app.elixir.makkhankitchens.interfac.OnFragmentInteractionListener;
import com.app.elixir.makkhankitchens.mtplview.MtplLog;
import com.app.elixir.makkhankitchens.mtplview.MtplTextView;
import com.app.elixir.makkhankitchens.utils.CM;
import com.app.elixir.makkhankitchens.volly.OnVolleyHandler;
import com.app.elixir.makkhankitchens.volly.VolleyIntialization;
import com.app.elixir.makkhankitchens.volly.WebService;
import com.app.elixir.makkhankitchens.volly.WebServiceTag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragReturnPolicy extends Fragment implements View.OnClickListener {

    private static final String TAG = "FragReturnPolicy";
    Activity thisActivity;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog mProgressDialog;
    private ScrollView rootlayout;
    private MtplTextView mtplTextView;
    private WebView webview;
    private LinearLayout noInternetLayout;
    private LinearLayout internetLayout;
    private ArrayList<CompanyModelArray> companyModelArrays;


    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag_return_policy, container, false);
        thisActivity = getActivity();
        ((ActionBarTitleSetter) thisActivity).setTitle("Return Policy");

        setHasOptionsMenu(false);
        inIt(view);

        return view;
    }

    public void inIt(View view) {
        rootlayout = (ScrollView) view.findViewById(R.id.mainrerun);
        webview = (WebView) view.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        noInternetLayout = (LinearLayout) view.findViewById(R.id.noInternet);
        internetLayout = (LinearLayout) view.findViewById(R.id.Internet);

        if (CM.isInternetAvailable(getActivity())) {

            webCallReturnPolicy();
            noInternetLayout.setVisibility(View.GONE);
            internetLayout.setVisibility(View.VISIBLE);
        } else {
            noInternetLayout.setVisibility(View.VISIBLE);
            internetLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case android.R.id.home:
                Log.e("", "");
                break;

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.showDrawerToggle(false);
    }

    public void webCallReturnPolicy() {
        try {
            VolleyIntialization v = new VolleyIntialization(thisActivity, true, true);
            WebService.getResendForConpanyDate(v, new OnVolleyHandler() {
                @Override
                public void onVollySuccess(String response) {
                    if (thisActivity.isFinishing()) {
                        return;
                    }
                    MtplLog.i("WebCalls", response);
                    Log.e(TAG, response);
                    getResponseForReturnPolicy(response);

                }

                @Override
                public void onVollyError(String error) {
                    MtplLog.i("WebCalls", error);
                    if (CM.isInternetAvailable(thisActivity)) {
                        CM.showPopupCommonValidation(thisActivity, error, false);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getResponseForReturnPolicy(String response) {
        String strResponseStatus = CM.getValueFromJson(WebServiceTag.WEB_STATUS, response);
        if (strResponseStatus.equalsIgnoreCase(WebServiceTag.WEB_STATUSFAIL)) {
            CM.showPopupCommonValidation(thisActivity, CM.getValueFromJson(WebServiceTag.WEB_STATUS_ERRORTEXT, response), false);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            CompanyModel model_main = CM.JsonParse(new CompanyModel(), jsonObject.getString("ResponseObject"));
            companyModelArrays = model_main.companyModelArrays;
            thisActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webview.loadDataWithBaseURL("", companyModelArrays.get(0).cCompanyPolicies, "text/html", "UTF-8", "");
                }
            });

        } catch (Exception e) {
            CM.showPopupCommonValidation(thisActivity, e.getMessage(), false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        if (menu.findItem(R.id.notiCount) != null) {
            menu.findItem(R.id.notiCount).setVisible(false);
        }
        if (menu.findItem(R.id.profile) != null) {
            menu.findItem(R.id.profile).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // do whatever
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
