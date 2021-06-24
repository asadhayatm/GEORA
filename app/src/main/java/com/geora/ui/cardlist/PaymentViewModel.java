package com.geora.ui.cardlist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.base.RichMediatorLiveData;
import com.geora.model.ChargeAmountModel;
import com.geora.model.FailureResponse;
import com.geora.model.ResetPasswordModel;
import com.geora.model.cardlist.CardListModel;
import com.geora.model.changred.FundraisedChangeModel;
import com.geora.model.chargeresponse.ChargeResponse;


public class PaymentViewModel extends ViewModel {
    private RichMediatorLiveData<CardListModel> mCardListLiveData;
    private RichMediatorLiveData<FundraisedChangeModel> mFundRaisedChangedLiveData;
    private RichMediatorLiveData<ChargeResponse> mChargeResponseLiveData;
    private RichMediatorLiveData<ResetPasswordModel> mDefaultCardLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private PaymentRepo mPaymentRepo = new PaymentRepo();

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mCardListLiveData == null) {
            mCardListLiveData = new RichMediatorLiveData<CardListModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }
        if (mDefaultCardLiveData == null) {
            mDefaultCardLiveData = new RichMediatorLiveData<ResetPasswordModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }
        if (mFundRaisedChangedLiveData == null) {
            mFundRaisedChangedLiveData = new RichMediatorLiveData<FundraisedChangeModel>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }
        if (mChargeResponseLiveData == null) {
            mChargeResponseLiveData = new RichMediatorLiveData<ChargeResponse>() {
                @Override
                protected Observer<FailureResponse> getFailureObserver() {
                    return mFailureObserver;
                }

                @Override
                protected Observer<Throwable> getErrorObserver() {
                    return mErrorObserver;
                }
            };
        }

    }

    /**
     * This method gives the  live data object
     *
     * @return {@link #mCardListLiveData}
     */
    public RichMediatorLiveData<CardListModel> getCardListLiveData() {
        return mCardListLiveData;
    }

    public RichMediatorLiveData<FundraisedChangeModel> getFundRaisedCharhedLiveData() {
        return mFundRaisedChangedLiveData;
    }

    public RichMediatorLiveData<ChargeResponse> getChargeResponseLiveData() {
        return mChargeResponseLiveData;
    }

    /**
     * This method gives the  live data object
     *
     * @return {@link #mDefaultCardLiveData}
     */
    public RichMediatorLiveData<ResetPasswordModel> getDefaultCardLiveData() {
        return mDefaultCardLiveData;
    }

    public void hitGetListingApi(boolean isInternet, int limt) {
        if (isInternet) {
            mPaymentRepo.hitCardList(mCardListLiveData, limt);
        }
    }

    public void hitDegaultCardApi(String id, boolean isDelete) {
        if (isDelete)
            mPaymentRepo.hitDeleteCardApi(mDefaultCardLiveData, id);
        else
            mPaymentRepo.hitDefaultCardApi(mDefaultCardLiveData, id);
    }

    public void hitChargeData(ChargeAmountModel chargeAmountModel) {
        if (chargeAmountModel.getFundId() != null)
            mPaymentRepo.hitChargeApiForFund(mFundRaisedChangedLiveData,chargeAmountModel);
        else
            mPaymentRepo.hitChargeSaleAPI(mChargeResponseLiveData,chargeAmountModel);
    }
}