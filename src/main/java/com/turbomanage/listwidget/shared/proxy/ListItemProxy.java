package com.turbomanage.listwidget.shared.proxy;

import java.util.Date;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.google.gwt.requestfactory.shared.ValueProxy;
import com.turbomanage.listwidget.domain.ListItem;

/**
 * Represents an item in a list
 *
 * @author turbomanage
 */
@ProxyFor(ListItem.class)
public interface ListItemProxy extends ValueProxy
{
	String getItemText();
	void setItemText(String itemText);
	Date getDateCreated();
}
