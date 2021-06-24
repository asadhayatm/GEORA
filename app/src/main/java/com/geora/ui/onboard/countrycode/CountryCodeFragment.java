package com.geora.ui.onboard.countrycode;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.constants.AppConstants;
import com.geora.listeners.RecycleListener;
import com.geora.model.CountryCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CountryCodeFragment extends BaseFragment implements RecycleListener {

    @BindView(R.id.rv_country_code)
    RecyclerView rvCountryCode;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    private Unbinder unbinder;
    private List<CountryCode> countryCodeList;
    private List<CountryCode> filteredCountryCodeList;
    private String searchText = "";
    private CountryCodeAdapter adapter;
    private ICountryCodeHost mCountryCodeHost;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_country_code, container, false);
        unbinder = ButterKnife.bind(this, view);
        countryCodeList = new LinkedList<>();
        filteredCountryCodeList = new LinkedList<>();
        mContext = getContext();
        parseJsonData();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTextOInList(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void searchTextOInList(String s) {
        filteredCountryCodeList.clear();
        for (int i = 0; i < countryCodeList.size(); i++) {
            if (countryCodeList.get(i).getCountryName().toLowerCase().contains(s.toLowerCase())) {
                filteredCountryCodeList.add(countryCodeList.get(i));
            }
        }
        setUpRecyclerView(filteredCountryCodeList);
    }

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                mCountryCodeHost.closeCurrentFragment();
                break;
        }
    }

    /**
     * getting country data from the json file and storing in POJO
     */
    private void parseJsonData() {
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset());
            CountryCode countryCodeModel;
            for (int i = 0; i < array.length(); i++) {
                countryCodeModel = new CountryCode();
                JSONObject object = new JSONObject(array.getString(i));
                countryCodeModel.setCountryCode(object.optString(AppConstants.CountryConstants.COUNRTY_CODE));
                countryCodeModel.setCountryName(object.optString(AppConstants.CountryConstants.COUNRTY_ENGLISH_NAME));
                countryCodeModel.setCountryId(object.optString(AppConstants.CountryConstants.COUNRTY_ID));
                countryCodeList.add(countryCodeModel);
            }
            setUpRecyclerView(countryCodeList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // setting country code data into recyclerview
    private void setUpRecyclerView(List<CountryCode> countryCodeList) {
        List<CountryCode> newList = new LinkedList<>();
        newList.addAll(countryCodeList);
        adapter = new CountryCodeAdapter(newList, getContext());
        rvCountryCode.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
        rvCountryCode.setAdapter(adapter);
        adapter.setClickListener(this);

    }

    /*
     * fetching country data from the json file
     * */
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("country_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*
     * RecyclerView item click
     * */
    @Override
    public void onItemClicked(View view, String s, int pos) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.BundleConstants.COUNTRY_DATA, adapter.list.get(pos));
        mCountryCodeHost.closeCurrentFragmentWithData(bundle);

    }

    @Override
    public void onItemLongClicked(String s, int pos) {

    }


    public interface ICountryCodeHost {
        void closeCurrentFragmentWithData(Bundle bundle);

        void closeCurrentFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ICountryCodeHost) {
            mCountryCodeHost = (ICountryCodeHost) context;
        } else
            throw new IllegalStateException("Host must implement ICountryCodeHost");
    }

}
