package com.app.elixir.makkhankitchens.Fragment;

import com.app.elixir.makkhankitchens.Model.PastOrderModelArray;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by NetSupport on 08-10-2016.
 */
public class PastOrderModel {

    @SerializedName("PastOrderList")
    public ArrayList<PastOrderModelArray> pastOrderModelArrays = new ArrayList<PastOrderModelArray>();
}
