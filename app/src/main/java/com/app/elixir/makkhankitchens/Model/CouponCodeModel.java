package com.app.elixir.makkhankitchens.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by NetSupport on 02-09-2016.
 */
public class CouponCodeModel {
    @SerializedName("data")
    public ArrayList<CouponCodeModelArray> couponCodeModelArrays = new ArrayList<CouponCodeModelArray>();
}
