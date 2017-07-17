package com.example.iket.ecellapp2k17.sponsors.presenter;

import com.example.iket.ecellapp2k17.sponsors.model.SponsProvider;
import com.example.iket.ecellapp2k17.sponsors.view.OnSponsReceived;
import com.example.iket.ecellapp2k17.sponsors.view.SponsData;
import com.example.iket.ecellapp2k17.sponsors.view.SponsInterface;

import java.util.List;

/**
 * Created by samveg on 23/6/17.
 */

public class SponsPresenterImpl implements SponsPresenter {
    private SponsInterface sponsInterface;
    private SponsProvider sponsProvider;

    public SponsPresenterImpl(SponsInterface sponsInterface, SponsProvider sponsProvider) {
        this.sponsInterface = sponsInterface;
        this.sponsProvider = sponsProvider;
    }

    @Override
    public void requestSpons() {
        sponsInterface.showLoading(true);
        sponsProvider.reqSpons(new OnSponsReceived() {
            @Override
            public void onFailure() {
                sponsInterface.showLoading(false);
                sponsInterface.showMessage("Try again in some time");
            }

            @Override
            public void onSuccess(List<SponsData> sponsDataList) {
                sponsInterface.setData(sponsDataList);
                sponsInterface.showLoading(false);
            }
        });
    }
}