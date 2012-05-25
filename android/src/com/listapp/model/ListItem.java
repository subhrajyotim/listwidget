package com.listapp.model;

import android.content.ContentValues;
import android.database.Cursor;

public class ListItem extends ModelBase {
    
    private String itemText;

    public ListItem(Cursor c) {
        super(c);
        this.itemText = c.getString(c.getColumnIndexOrThrow("itemText"));
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    @Override
    public ContentValues getEditableValues() {
        ContentValues cv = super.getEditableValues();
        cv.put("itemText", this.itemText);
        return cv;
    }
    
}
