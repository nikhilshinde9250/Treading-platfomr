package com.trade.service;

import com.trade.modal.Asset;
import com.trade.modal.Coin;
import com.trade.modal.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(User user, Coin coin, double quantity);

    Asset getAssetById(Long assetId) throws Exception;


    Asset getAssetByUserIdAndId(Long userId, Long assetId);

    List<Asset> getUsersAsset(Long userId);

    Asset  updateAsset(Long assetId, double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long assetId);
}
