package com.example.evgeny.setlist_mobile;

import com.example.evgeny.setlist_mobile.model.BaseModel;

public interface SelectBottomMenuListener {
    public <T extends BaseModel> void setMenuItem  (String item, T object);
}
