package de.meisterfuu.animexx.utils;

import java.util.ArrayList;
import java.util.List;

import org.scribe.model.OAuthRequest;

import oauth.signpost.OAuth;


public class PostBodyFactory {
	List<ValuePair> nameValuePairs;
	
	public PostBodyFactory(){
		nameValuePairs = new ArrayList<ValuePair>();
	}
	
	public PostBodyFactory putValue(String name, String value){
		nameValuePairs.add(new ValuePair(name, value));
		return this;
	}
	
	public void build(OAuthRequest request){
		for(ValuePair value: nameValuePairs){
				request.addBodyParameter(value.getName(), value.getValue());
		}
	}

	static class ValuePair {
		String name, value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}
		
		public String getEncodedValue() {
			return OAuth.percentEncode(value);
		}

		public void setValue(String value) {
			this.value = value;
		}

		public ValuePair(String name, String value) {
			super();
			if(name == null || value == null) throw new NullPointerException(name+":::"+value);
			this.name = name;
			this.value = value;
		}
		
		
	}
	
}
