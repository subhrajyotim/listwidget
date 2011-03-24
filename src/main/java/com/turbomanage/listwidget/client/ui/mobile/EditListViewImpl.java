package com.turbomanage.listwidget.client.ui.mobile;

import com.google.gwt.user.client.ui.Composite;

//public class EditListViewImpl extends Composite implements EditListView
//{
//	VerticalPanel vPanel = new VerticalPanel();
//	FlexTable t = new FlexTable();
//	Anchor back = new Anchor("Back");
//	Label listName = new Label();
//	Label listType = new Label();
//	TextBox newItemText = new TextBox();
//	private Presenter presenter;
//
//	public EditListViewImpl()
//	{
//		back.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				History.back();
//			}
//		});
//		vPanel.add(back);
//		vPanel.add(new Label("Edit List"));
//		vPanel.add(listName);
//		vPanel.add(listType);
//		vPanel.add(newItemText);
//		vPanel.add(t);
//		initWidget(vPanel);
//		newItemText.addKeyUpHandler(new KeyUpHandler()
//		{
//			@Override
//			public void onKeyUp(KeyUpEvent event)
//			{
//				if (KeyCodes.KEY_ENTER==event.getNativeKeyCode())
//				{
//					presenter.addItem(newItemText.getText());
//					// clear entry box
//					newItemText.setValue(null);
//				}
//			}
//		});
//	}
//
//	@Override
//	public void populateItems(List<ListItemProxy> items)
//	{
//		t.removeAllRows();
//		if (items==null) return;
//		int i = 0;
//		for (ListItemProxy item : items)
//		{
//			t.setText(i, 0, item.getItemText());
//			i++;
//		}
//	}
//
//	@Override
//	public void setListName(String name)
//	{
//		listName.setText(name);
//	}
//
//	@Override
//	public void setPresenter(Presenter p)
//	{
//		this.presenter = p;
//	}
//
//	@Override
//	public void setListType(ListType listType)
//	{
//		this.listType.setText(listType.toString());
//	}
//
//	@Override
//	public HasValue<String> getListName()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public HasValue<Date> dateCreated()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public HasData<ListItemProxy> getDataTable()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
