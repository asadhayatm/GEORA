package com.geora.ui.addcard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.geora.R;
import com.geora.base.RichMediatorLiveData;
import com.geora.constants.AppConstants;
import com.geora.model.CardModel;
import com.geora.model.FailureResponse;
import com.geora.model.cardlist.CardListModel;
import com.geora.util.ResourceUtils;
import com.stripe.android.model.Card;

public class AddCardViewModel extends ViewModel {
    private RichMediatorLiveData<CardListModel> mAddCardModelLiveData;
    private Observer<FailureResponse> mFailureObserver;
    private Observer<Throwable> mErrorObserver;
    private MutableLiveData<FailureResponse> mValidateLiveData;
    private AddCardRepo mAddCardRepo = new AddCardRepo();
    private Card card;

    public void setGenericListeners(Observer<Throwable> errorObserver,
                                    Observer<FailureResponse> failureObserver) {
        this.mErrorObserver = errorObserver;
        this.mFailureObserver = failureObserver;

        initLiveData();
    }

    private void initLiveData() {
        if (mAddCardModelLiveData == null) {
            mAddCardModelLiveData = new RichMediatorLiveData<CardListModel>() {
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

        if (mValidateLiveData == null) {
            mValidateLiveData = new MutableLiveData<>();
        }
    }

    /**
     * This method gives the live data object
     *
     * @return {@link #mAddCardModelLiveData}
     */
    public RichMediatorLiveData<CardListModel> getAddCardModelLiveData() {
        return mAddCardModelLiveData;
    }


    /**
     * Method to check validation
     *
     * @param cardModel
     * @return
     */
    private boolean checkValidation(CardModel cardModel, boolean isInternetAvailable) {
        card = onClickSomething(cardModel.getCardno(), cardModel.getMonth(), cardModel.getYear(), cardModel.getCvv());
        card.validateNumber();
        card.validateCVC();
        if (cardModel.getCardno().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.CODE_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_card_no)));
            return false;
        } else if (!card.validateCard()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_CARD, ResourceUtils.getInstance().getString(R.string.enter_valid_card_no)
            ));
            return false;
        } else if (cardModel.getDate().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.DOB_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_expiry_date)
            ));
            return false;
        } else if (cardModel.getCvv().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.OTP_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_cvv)
            ));
            return false;
        } else if (!card.validateCVC()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.INVALID_CVV, ResourceUtils.getInstance().getString(R.string.enter_valid_cvv)
            ));
            return false;
        } else if (!isInternetAvailable) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.NO_INTERNET, ResourceUtils.getInstance().getString(R.string.no_internet_connection)
            ));
            return false;
        } else if (cardModel.getName().isEmpty()) {
            mValidateLiveData.setValue(new FailureResponse(
                    AppConstants.UIVALIDATIONS.NAME_EMPTY, ResourceUtils.getInstance().getString(R.string.enter_name)));
            return false;
        }
        return true;
    }

    public Card onClickSomething(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        Card card = new Card(
                cardNumber,
                cardExpMonth,
                cardExpYear,
                cardCVC
        );
        return card;
    }

    /**
     * This method gives the validation live data object to {@link AddCardActivity}
     *
     * @return {@link #mValidateLiveData}
     */
    public MutableLiveData<FailureResponse> getValidationLiveData() {
        return mValidateLiveData;
    }

    public void submutClicked(CardModel cardModel, boolean internetAvailable) {
        if (checkValidation(cardModel, internetAvailable)) {
            mAddCardRepo.getStripeToken(card, mAddCardModelLiveData);
        }
    }
}
