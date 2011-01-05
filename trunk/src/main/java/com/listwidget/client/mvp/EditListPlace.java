package com.listwidget.client.mvp;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class EditListPlace extends Place
{

	private String token;

	public EditListPlace(String token)
	{
		this.token = token;
	}

	public String getToken()
	{
		return token;
	}

	public static class Tokenizer implements PlaceTokenizer<EditListPlace>
	{
		public EditListPlace getPlace(String token)
		{
			return new EditListPlace(token);
		}

		public String getToken(EditListPlace place)
		{
			return place.getToken();
		}
	}

}