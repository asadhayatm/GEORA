package com.geora.ui.addcard;

import android.util.Log;

import com.geora.base.NetworkCallback;
import com.geora.base.RichMediatorLiveData;
import com.geora.data.DataManager;
import com.geora.data.constants.AppConstantClass;
import com.geora.model.FailureResponse;
import com.geora.model.cardlist.CardListModel;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.geora.GeoraClass.getContext;


public class AddCardRepo {
    public void getStripeToken(Card card, final RichMediatorLiveData<CardListModel> addCardModelLiveData) {
        Stripe stripe = new Stripe(getContext(), AppConstantClass.stripeKeys.STRIPE_KEY);
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        hitAddCardApi(addCardModelLiveData, token.getId());
                        Log.e("", "");
                        // Send token to your server
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        addCardModelLiveData.setError(error);

                    }
                }
        );

    }

    private void hitAddCardApi(final RichMediatorLiveData<CardListModel> addCardModelLiveData, String tokrn) {

        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstantClass.APIConstant.TOKEN, tokrn);


        DataManager.getInstance().hitAddCardApi(params).enqueue(new NetworkCallback<CardListModel>() {
            @Override
            public void onSuccess(CardListModel userResponse) {
                //save data in preference
                addCardModelLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(FailureResponse failureResponse) {
                addCardModelLiveData.setFailure(failureResponse);
            }

            @Override
            public void onError(Throwable t) {
                addCardModelLiveData.setError(t);
            }
        });

    }
}
