/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.listapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.actionbarcompat.ActionBarActivity;
import com.listapp.NewListDialog.NewListDialogListener;
import com.listapp.dao.NamedListDao;
import com.listapp.model.NamedList;

import java.util.List;

/**
 * Main activity - requests all lists from the server and provides a menu item
 * to invoke the accounts activity.
 */
public class ListappActivity extends ActionBarActivity implements OnItemClickListener,
        NewListDialogListener {
    /**
     * Tag for logging.
     */
    private static final String TAG = "ListappActivity";

    private static final int LOGIN = 0;

    /**
     * The current context.
     */
    private Context mContext = this;

    private ListwidgetAdapter adapter;

    private ListView listView;

    private final int LOADING_DIALOG = 1;
    private final int ERR_DIALOG = -1;
    private String errDialogMsg;

    private AsyncTask taskInProgress;

    private NamedListDao namedListDao;

    /**
     * Begins the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        ListwidgetApplication app = (ListwidgetApplication) getApplication();

        // Init data layer
        app = (ListwidgetApplication) getApplication();
        namedListDao = new NamedListDao(mContext);

        adapter = new ListwidgetAdapter(this);
        // setListAdapter(adapter);
        // listView = getListView();
        setContentView(R.layout.appview);
        // must layout first
        listView = (ListView) findViewById(R.id.main);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        // TODO force login to debug httpclient 
        login();
        
        // TODO two calls to setContentView?
        // moved from onResume
        // final SharedPreferences prefs = Util.getSharedPreferences(mContext);
        // String loggedInAccount = prefs.getString(Util.AUTH_COOKIE, null);
        setScreenContent(R.layout.appview);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN:
                setScreenContent(R.layout.appview);
//                final SharedPreferences prefs = Util.getSharedPreferences(mContext);
//                String loggedInAccount = prefs.getString(Util.AUTH_COOKIE, null);
//                if (loggedInAccount != null) {
//                    setScreenContent(R.layout.appview);
//                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // Invoke the Register activity
        menu.getItem(0).setIntent(new Intent(this, AccountsActivity.class));
        return true;
    }

    // Manage UI Screens
    private void fetchLists() {
        showDialog(LOADING_DIALOG);
        taskInProgress = new FetchListsTask(this).execute();
    }

    /**
     * Sets the screen content based on the screen id.
     */
    private void setScreenContent(int screenId) {
        // TODO DONT DO THIS!
        // setContentView(screenId);
        switch (screenId) {
            case R.layout.appview:
                fetchLists();
                break;
        }
    }

    public void setLists(List<NamedList> lists) {
        dismissDialog(LOADING_DIALOG);
        // adapter.setLists(lists);
        // Put into database
        if (lists == null)
            return;
        for (NamedList namedList : lists) {
            namedListDao.put(namedList);
        }
        adapter.reQuery();
    }

    public void login() {
        startActivityForResult(new Intent(this, AccountsActivity.class), LOGIN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_refresh:
                fetchLists();
                break;
            case R.id.action_new_list:
                showNewListDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNewListDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        NewListDialog newListDialog = new NewListDialog();
        newListDialog.show(fm, "new_list_dialog");
    }

    void onFinishNewListDialog(String newListName) {
        Toast.makeText(this, newListName, Toast.LENGTH_SHORT).show();
    }

    public void showError(String message) {
        dismissDialog(LOADING_DIALOG);
        errDialogMsg = message;
        showDialog(ERR_DIALOG);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Intent viewListIntent = new Intent(this, ViewListActivity.class);
        viewListIntent.putExtra("position", position);
        NamedList list = adapter.getItem(position);
        // TODO I do not like this, Sam-I-am
        // ListwidgetRequestFactory requestFactory =
        // Util.getRequestFactory(this,
        // ListwidgetRequestFactory.class);
        // String historyToken =
        // requestFactory.getHistoryToken(list.stableId());
        // TODO this is probably wrong
        viewListIntent.putExtra("listId", position);
        startActivity(viewListIntent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);

        switch (id) {
            case ERR_DIALOG:
                AlertDialog errDialog = new AlertDialog.Builder(this)
                        // See
                        // http://code.google.com/p/android/issues/detail?id=6489
                        .setMessage("")
                        .setCancelable(false)
                        .setNeutralButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();
                return errDialog;

            case LOADING_DIALOG:
                ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                // Listen for back button or cancel button in dialog itself
                progressDialog.setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (taskInProgress != null) {
                            // TODO why doesn't this stop the HTTP request?
                            taskInProgress.cancel(true);
                        }
                    }
                });
                return progressDialog;

            default:
                return null;
        }
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case ERR_DIALOG:
                ((AlertDialog) dialog).setMessage(errDialogMsg);
                break;

            default:
                break;
        }
    }

    @Override
    public void addList(String name) {
        // ContentValues namedListValues = new ContentValues();
        // namedListValues.put(NamedListTable.NAME, name);
        // this.getContentResolver().insert(Provider.NAMEDLIST_CONTENT_URI,
        // namedListValues);
        // NamedListProxy namedListProxy =
        // rf.namedListService().create(NamedListProxy.class);
        // namedListProxy.setName(name);
        // namedListDao.put(namedListProxy);
        // adapter.reQuery();
    }
}
