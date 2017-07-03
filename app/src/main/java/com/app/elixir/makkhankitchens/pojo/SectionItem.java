package com.app.elixir.makkhankitchens.pojo;

import com.app.elixir.makkhankitchens.interfac.Item;

/**
 * Created by Elixir on 19-Aug-2016.
 */
public class SectionItem implements Item {

    private final String title;

    public SectionItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}
