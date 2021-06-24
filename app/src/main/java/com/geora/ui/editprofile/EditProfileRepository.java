package com.geora.ui.editprofile;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.editprofile.EditProfileResponse;
import com.geora.model.request.User;

import java.util.HashMap;

import static com.geora.util.DateTimeUtils.bdayToTimeStamp;

public class EditProfileRepository {
    public String getName() {
        return DataManager.getInstance().getFullName();
    }

    public String getEmailId() {
        return DataManager.getInstance().getEmailId();
    }

    public String getMobileNumber() {
        return DataManager.getInstance().getMobileNo();
    }

    public String getDOB() {
        return DataManager.getInstance().getDateOfBirth();
    }

    public String isEmailIdVerified() {
        return DataManager.getInstance().isEmailIdVerified();
    }

    public void hitEditProfileApi(final RichMediatorLiveData<EditProfileResponse> mEditProfileLiveData, final User editProfileData) {
        DataManager.getInstance().hitEditProfileApi(createPayloadForEditProfile(editProfileData)).enqueue(new NetworkCallback<EditProfileResponse>() {
            @Override
            public void onSuccess(EditProfileResponse editProfileResponse) {
                mEditProfileLiveData.setValue(editProfileResponse);
                if (editProfileData.getIsNumberEdited()==0){
                  saveUpdatedProfileData(editProfileResponse);
                }else{
                  DataManager.getInstance().saveToken(editProfileResponse.getData().getToken());
                }

            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                mEditProfileLiveData.setFailure(failureResponse);

            }

            @Override
            public void onError(Throwable t) {
                mEditProfileLiveData.setError(t);

            }
        });
    }

    private void saveUpdatedProfileData(EditProfileResponse editProfileResponse) {
        DataManager.getInstance().saveUpdatedProfileData(editProfileResponse);
    }

    private HashMap<String,String> createPayloadForEditProfile(User editProfileData) {
        HashMap hashMap=new HashMap();
        if (editProfileData.getEmail()!=null)
            hashMap.put(AppConstantClass.APIConstant.NAME,editProfileData.getFullName());
        hashMap.put(AppConstantClass.APIConstant.ISNUMBEREDITED, editProfileData.getIsNumberEdited());

        hashMap.put(AppConstantClass.APIConstant.COUNTRYCODE,editProfileData.getCountryCode());
        if (editProfileData.getImage()!=null)
            hashMap.put(AppConstantClass.APIConstant.IMAGE,editProfileData.getImage());
        if (editProfileData.getDob()!=null)
            hashMap.put(AppConstantClass.APIConstant.DOB,bdayToTimeStamp(editProfileData.getDob()) + "");
        if (editProfileData.getPhone()!=null) {
            hashMap.put(AppConstantClass.APIConstant.PHONE, editProfileData.getPhone());
            if (editProfileData.getIsNumberEdited()==1)
            hashMap.put(AppConstantClass.APIConstant.CLIENTID, DataManager.getInstance().getClientId());
        }

   return hashMap;

    }

    public String getProfileImage() {
        return DataManager.getInstance().getProfileIMage();
    }

    public String getCountryCode() {
        return DataManager.getInstance().getCountryCode();
    }
}
