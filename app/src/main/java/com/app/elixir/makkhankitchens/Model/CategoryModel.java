package com.app.elixir.makkhankitchens.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by NetSupport on 30-08-2016.
 */
public class CategoryModel {


    @SerializedName("CompanyProductCaregories")
    public ArrayList<CategoryModelArray> categoryModels = new ArrayList<CategoryModelArray>();
}
