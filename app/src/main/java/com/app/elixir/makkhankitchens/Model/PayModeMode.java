package com.app.elixir.makkhankitchens.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by NetSupport on 28-12-2016.
 */

public class PayModeMode {

    @SerializedName("tblpaymentoptionList")
    public ArrayList<PayModeModeArray> payModeModeArrays = new ArrayList<PayModeModeArray>();
}
