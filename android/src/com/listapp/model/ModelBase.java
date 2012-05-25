
package com.listapp.model;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class ModelBase {

    public interface Columns {
        String ID = "_id";
        String VERSION = "version";
        String LAST_MOD = "lastMod";
        String LAST_SYNC = "lastSync";
        String[] BASE_COLUMNS = {
                ID, VERSION, LAST_MOD, LAST_SYNC
        };
    }

    public long id;
    public int version;
    public long lastMod; // ms since epoch
    public long lastSync; // ms since epoch

    public ModelBase (Cursor c) {
        this.id = c.getLong(c.getColumnIndexOrThrow(Columns.ID));
        this.version = c.getInt(c.getColumnIndexOrThrow(Columns.VERSION));
        this.lastMod = c.getLong(c.getColumnIndexOrThrow(Columns.LAST_MOD));
        this.lastSync = c.getLong(c.getColumnIndexOrThrow(Columns.LAST_SYNC));
    }

    public ModelBase() {
        // Empty ctor for Jackson
    }

    protected String[] allColumns(String... colName) {
        int baseLen = Columns.BASE_COLUMNS.length;
        String[] allColumns = new String[baseLen + colName.length];
        for (int i = 0; i < colName.length; i++) {
            allColumns[baseLen + i] = colName[i];
        }
        return allColumns;
    }

    public ContentValues getEditableValues() {
        ContentValues cv = new ContentValues();
        cv.put(Columns.ID, this.id);
        cv.put(Columns.VERSION, this.version);
        cv.put(Columns.LAST_MOD, this.lastMod);
        cv.put(Columns.LAST_SYNC, this.lastSync);
        return cv;
    }

}
