package com.geora.ui.home.detailpage.rsvp;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.changred.FundraisedChangeModel;
import com.geora.socket.SocketConstant;
import com.geora.ui.home.HomeActivity;
import com.geora.ui.home.detailpage.FormAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RSVPActivity extends BaseActivity {

    @BindView(R.id.main)
    RelativeLayout main;
    @BindView(R.id.rl_form)
    RecyclerView rvForm;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Context mContext;
    private String formData = "";
    private String[] dataList;
    private Unbinder unbinder;
    private RSVPViewModel mRsvpViewModel;
    private FormAdapter adapter;
    private String eventId, busniussId;
    private int campId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        mContext = this;
        tvTitle.setText("RSVP Form");
        formData = getIntent().getStringExtra(SocketConstant.SOCKEYKEYS.DATA);
        eventId = getIntent().getStringExtra(AppConstantClass.APIConstant.EVENTID);
        busniussId = getIntent().getStringExtra(AppConstantClass.APIConstant.BUSNISS_USER_IS);
        campId = getIntent().getIntExtra(AppConstantClass.APIConstant.CAMP_ID,0);
        dataList = formData.split(",");
        setupAdapter();

        setObserver();
    }

    private void setObserver() {
        mRsvpViewModel = ViewModelProviders.of(this).get(RSVPViewModel.class);
        mRsvpViewModel.setGenericListeners(getErrorObserver(), getFailureResponseObserver());
        mRsvpViewModel.getFundRaisedCharhedLiveData().
                observe(this, new Observer<FundraisedChangeModel>() {
                    @Override
                    public void onChanged(@Nullable final FundraisedChangeModel cardListModel) {
                        hideProgressDialog();
                        Intent intent = new Intent(RSVPActivity.this, HomeActivity.class);
                        intent.putExtra(AppConstantClass.FROM, "Event");
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_rsvpform;
    }

    private void setupAdapter() {
        adapter = new FormAdapter(dataList, mContext);
        rvForm.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvForm.setAdapter(adapter);
        rvForm.getAdapter().notifyDataSetChanged();
    }

    @OnClick({R.id.iv_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishAfterTransition();
                break;
            case R.id.tv_submit:
                if (isInternetAvailable())
                    getDataFromFields();
                else showSnackBar(main, getString(R.string.no_internet), true);

                break;
        }
    }

    private void getDataFromFields() {
//        HashMap<Integer, EditText> data = adapter.getHashMap();
        showProgressDialog();
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < adapter.getHashMap().size(); i++) {
            try {
                jsonObject.put(adapter.getHashMap().get(i).get(i).getHint().toString(), adapter.getHashMap().get(i).get(i).getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mRsvpViewModel.hitRSVPData(jsonObject.toString(), eventId, busniussId, campId);

    }

    @Override
    protected void onFailure(FailureResponse failureResponse) {
        super.onFailure(failureResponse);
        hideProgressDialog();
    }
}
