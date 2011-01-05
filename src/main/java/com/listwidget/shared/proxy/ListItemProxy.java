package com.listwidget.shared.proxy;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.google.gwt.requestfactory.shared.ValueProxy;
import com.listwidget.domain.ListItem;

/**
 * Represents an item in a list
 *
 * @author drfibonacci
 */
@ProxyFor(value = ListItem.class)
public interface ListItemProxy extends ValueProxy
{
	String getItemText();
	void setItemText(String itemText);
}
