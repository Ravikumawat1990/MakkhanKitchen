package com.app.elixir.makkhankitchens.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NetSupport on 08-10-2016.
 */
public class OrderListArray {

    @SerializedName("nOrderID")
    public String nOrderID;
    @SerializedName("cOrderNo")
    public String cOrderNo;
    @SerializedName("cCustomerName")
    public String cCustomerName;
    @SerializedName("dOrderTime")
    public String dOrderTime;

    @SerializedName("dOrderDate")
    public String dOrderDate;
    @SerializedName("nAmount")
    public String nAmount;
    @SerializedName("nShippingAddressID")
    public String nShippingAddressID;
    @SerializedName("cAddress")
    public String cAddress;
    @SerializedName("cMobile")
    public String cMobile;
    @SerializedName("cOrderStatus")
    public String cOrderStatus;
}
