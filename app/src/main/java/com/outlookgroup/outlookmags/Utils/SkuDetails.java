/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.outlookgroup.outlookmags.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app product's listing details.
 */
public class SkuDetails implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public SkuDetails createFromParcel(Parcel in) {
            return new SkuDetails(in);
        }

        public SkuDetails[] newArray(int size) {
            return new SkuDetails[size];
        }
    };
    String mItemType;
    String mSku;
    String mType;
    String mPrice;
    String mTitle;
    String mDescription;
    String mJson;

    public SkuDetails(String jsonSkuDetails) throws JSONException {
        this(IabHelper.ITEM_TYPE_INAPP, jsonSkuDetails);
    }

    public SkuDetails(String itemType, String jsonSkuDetails) throws JSONException {
        mItemType = itemType;
        mJson = jsonSkuDetails;
        JSONObject o = new JSONObject(mJson);
        mSku = o.optString("productId");
        mType = o.optString("type");
        mPrice = o.optString("price");
        mTitle = o.optString("title");
        mDescription = o.optString("description");
    }

    public SkuDetails(Parcel in) {
        mItemType = in.readString();
        mSku = in.readString();
        mType = in.readString();
        mPrice = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mJson = in.readString();
    }

    public String getSku() {
        return mSku;
    }

    public String getType() {
        return mType;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public String toString() {
        return "SkuDetails:" + mJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mItemType);
        parcel.writeString(mSku);
        parcel.writeString(mType);
        parcel.writeString(mPrice);
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mJson);
    }
}
