package com.turbomanage.listwidget.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.turbomanage.listwidget.client.ui.widget.MessageWidget.MessageType;

public class MessageEvent extends GwtEvent<MessageEvent.MessageHandler>
{
	public interface MessageHandler extends EventHandler
	{
		void onShowMessage(String msg, MessageType type);
	}

	public static final GwtEvent.Type<MessageHandler> TYPE = new GwtEvent.Type<MessageEvent.MessageHandler>();
	private String msg;
	private MessageType msgType;

	public MessageEvent(String msg, MessageType msgType)
	{
		this.msg = msg;
		this.msgType = msgType;
	}
	
	@Override
	protected void dispatch(MessageHandler handler)
	{
		handler.onShowMessage(msg, msgType);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MessageHandler> getAssociatedType()
	{
		return TYPE;
	}

}
