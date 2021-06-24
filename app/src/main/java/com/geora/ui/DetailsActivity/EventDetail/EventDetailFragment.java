package com.geora.ui.DetailsActivity.EventDetail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geora.R;
import com.geora.base.BaseFragment;
import com.geora.model.myactivity.EventData;
import com.geora.socket.SocketConstant;
import com.geora.ui.DetailsActivity.MyDetailsActivity;
import com.geora.util.ActivityUtils;
import com.geora.util.DateTimeUtils;
import com.geora.util.ImageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class EventDetailFragment extends BaseFragment {
    @BindView(R.id.tv_event_name)
    TextView tvEventName;

    @BindView(R.id.tv_event_place)
    TextView tvEventPlace;

    @BindView(R.id.iv_event_image)
    ImageView ivEventImage;

    @BindView(R.id.rv_form_data)
    RecyclerView rvFormData;

    @BindView(R.id.rl_form_detail)
    RelativeLayout rlFormDetails;

    @BindView(R.id.tv_event_date)
    TextView tvEventDate;

    @BindView(R.id.tv_timing_data)
    TextView tvTimingData;

    @BindView(R.id.tv_view_details)
    TextView tviewDetails;

    private Unbinder unbinder;
    private AppCompatActivity mActivity;
    private EventData eventData = new EventData();
    private List<FormDataValue> formDataValueList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = (AppCompatActivity) getActivity();
        Bundle bundle = ((MyDetailsActivity) Objects.requireNonNull(getActivity())).getBundle();
        eventData = bundle.getParcelable("object");
        settingViews();
        return view;
    }

    /**
     * Initializing views
     */
    private void settingViews() {
        checkEventRegistered();
        tvEventName.setText(eventData.getGetEvent().getGetCampaign().getCampTitle());
        tvEventPlace.setText(eventData.getGetEvent().getGetCampaign().getCampAddress());
        tvTimingData.append(DateTimeUtils.timeStampToEndCamp
                (eventData.getGetEvent().getGetCampaign().getCampStartDate().toString()) + " to " +
                DateTimeUtils.timeStampToEndCamp
                        (eventData.getGetEvent().getGetCampaign().getCampEndDate().toString()));
        tvEventDate.setText(DateTimeUtils.timeStampToEndCamp(eventData.getSubscriptionDate().toString()));

        ImageUtils.setImage(mActivity, eventData.getGetEvent().getGetCampaign().getGetDefaultImage().getImageUrl()
                , ivEventImage, getResources().getDrawable(R.drawable.placeholder_medium));
    }

    /**
     * check weather registered for event or not
     */

    private void checkEventRegistered() {
        if (eventData.getGetEvent().getRsvpType() == 2) {
            try {
                JSONObject jsonObject = new JSONObject(eventData.getSubscriptionData());
                Iterator<String> iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    FormDataValue formDataValue = new FormDataValue();
                    String key = iterator.next();
                    Object value = jsonObject.get(key);
                    formDataValue.setKey(key);
                    formDataValue.setValue(value.toString());
                    formDataValueList.add(formDataValue);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

            rlFormDetails.setVisibility(View.VISIBLE);
            setAdapter();
        }
    }

    /**
     * setting adapter for form data
     */
    private void setAdapter() {
        FormDataAdapter adpter = new FormDataAdapter(formDataValueList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFormData.setLayoutManager(layoutManager);
        rvFormData.setAdapter(adpter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick({R.id.tv_view_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_view_details:
                openDetailPage();
                break;
        }
    }

    private void openDetailPage() {
        if ((System.currentTimeMillis() / 1000) < eventData.getGetEvent().getGetCampaign().getCampEndDate()) {
            Bundle bundle = new Bundle();
            bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_ID, eventData.getGetEvent().getCampId().toString());
            bundle.putString(SocketConstant.SOCKEYKEYS.CAMP_TYPE, "4");
            bundle.putString(SocketConstant.SOCKEYKEYS.BEACON_ID, "");
            ActivityUtils.startDetailsActivity(mActivity, bundle);
        } else {
            showSnackBar(((MyDetailsActivity) mActivity).getParentView(), "Event is expired", true);
        }
    }
}
