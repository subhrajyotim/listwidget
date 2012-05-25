
package com.listapp.model;

import android.content.ContentValues;

import android.database.Cursor;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

// http://stackoverflow.com/questions/5455014/ignoring-new-fields-on-json-objects-using-jackson
@JsonIgnoreProperties(ignoreUnknown = true)
public class NamedList extends ModelBase {

    public interface Columns extends ModelBase.Columns {
        String NAME = "name";
    }

    public String name;

    public NamedList() {
        super();
        // Empty constructor for Jackson
    }
    
    public NamedList(Cursor c) {
        super(c);
        this.name = c.getString(c.getColumnIndexOrThrow(Columns.NAME));
    }

    @Override
    public ContentValues getEditableValues() {
        ContentValues values = super.getEditableValues();
        values.put(Columns.NAME, name);
        return values;
    }

}
