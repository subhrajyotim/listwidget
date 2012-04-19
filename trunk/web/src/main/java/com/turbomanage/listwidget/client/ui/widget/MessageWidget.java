package com.turbomanage.listwidget.client.ui.widget;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.turbomanage.listwidget.client.event.MessageEvent;
import com.turbomanage.listwidget.client.event.MessageEvent.MessageHandler;

public class MessageWidget extends Composite
{
	private final EventBus eventBus;

	public static enum MessageType 
	{
		INFO("info"), 
		WARN("warn");
		
		private String styleName;

		MessageType(String styleName)
		{
			this.styleName = styleName;
		}

		public String getStyleName()
		{
			return styleName;
		}
	};
	
	// TODO can replace with div element?
	private FlowPanel messageDiv = new FlowPanel();
	private Label messageText = new Label();

	public MessageWidget()
	{
		// TODO For GWT Designer
		eventBus = null;
	}
	
	public MessageWidget(EventBus bus)
	{
		this.eventBus = bus;
		
		messageText.addStyleName("info");
		messageDiv.add(messageText);
		messageDiv.getElement().setId("msgBox");
		initWidget(messageDiv);
		
		eventBus.addHandler(MessageEvent.TYPE, new MessageHandler()
		{
			public void onShowMessage(String msg, MessageType type)
			{
				flashMessage(msg, type);
			}
		});
	}

	public void flashMessage(String msg, MessageType type)
	{
		messageText.setStyleName(type.getStyleName());
		messageText.setText(msg);
//		final Fade fade = new Fade(messageText.getElement());
//		fade.reset();
//		fade.addEffectCompletedHandler(new EffectCompletedHandler()
//		{
//			@Override
//			public void onEffectCompleted(EffectCompletedEvent event)
//			{
//				messageText.setText(null);
//				fade.remove();
//			}
//		});
//		fade.play(3000);
	}

	public Widget asWidget()
	{
		return messageDiv;
	}
}