package com.mli.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mli.model.CardTypeModel;
import com.mli.model.Coverage;

public enum CardType {
	
    YESPRIVATE("Yes Private"),
    YESFIRSTEXCLUSIVE("Yes First Exclusive"),
    YESFIRSTPREFERRED("Yes First Preferred"),
    YESPREMIA("Yes Premia"),
    YESPROSPERITYEDGE("Yes Prosperity Edge"),
    YESPROSPERITYREWARD("Yes Prosperity Rewards & Cashback");

    private final String value;
	
	CardType(String value){
		this.value = value;
	}
	
	public static final List<CardTypeModel> lookup = new ArrayList<CardTypeModel>();
	
	static {
		lookup.add(new CardTypeModel(YESPRIVATE.getValue(), Arrays.asList(new Coverage("Rs. 50 Lakhs", "12400"),new Coverage("Rs. 30 Lakhs", "7440"))));
		lookup.add(new CardTypeModel(YESFIRSTEXCLUSIVE.getValue(), Arrays.asList(new Coverage("Rs. 30 Lakhs", "7440"),new Coverage("Rs. 20 Lakhs", "4960"))));
		lookup.add(new CardTypeModel(YESFIRSTPREFERRED.getValue(), Arrays.asList(new Coverage("Rs. 30 Lakhs", "7440"),new Coverage("Rs. 20 Lakhs", "4960"))));
		lookup.add(new CardTypeModel(YESPREMIA.getValue(), Arrays.asList(new Coverage("Rs. 15 Lakhs", "3720"),new Coverage("Rs. 10 Lakhs", "2480"))));
		lookup.add(new CardTypeModel(YESPROSPERITYEDGE.getValue(), Arrays.asList(new Coverage("Rs. 15 Lakhs", "3720"),new Coverage("Rs. 10 Lakhs", "2480"))));
		lookup.add(new CardTypeModel(YESPROSPERITYREWARD.getValue(), Arrays.asList(new Coverage("Rs. 15 Lakhs", "3720"),new Coverage("Rs. 10 Lakhs", "2480"))));
	}

	public String getValue() {
		return value;
	}
}
