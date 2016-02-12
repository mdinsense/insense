package com.ensense.insense.data.utils;

public enum UIFunctionalityType {
	FUNCTIONAL_TESTING(1, "Functional Testing"),
	AKAMAI_TESTING(2, "Akamai Check"),
	ANALYTICS_TESTING(3, "Analytics Testing"),
	BROKENLINKS(4, "Brokenlinks Check"),
	WEBSERVICE_TESTING(5, "Webservice Testing"),
	OPRA_RESET(6, "OPRA Reset"),
	MVC_OPRA_RESET(7, "MVC OPRA Reset"),
	FIND_TEXT_IMAGE(8, "Find Text/Image");;

	private final int functionalityTypeId;
    private final String functionalityName;
    
    UIFunctionalityType(int functionalityTypeId, String functionalityName) {
        this.functionalityTypeId = functionalityTypeId;
        this.functionalityName = functionalityName;
    }

	public int getFunctionalityTypeId() {
		return functionalityTypeId;
	}

	public String getFunctionalityName() {
		return functionalityName;
	}
	
	public static UIFunctionalityType getUIFunctionalityType(int functionalityTypeId) {
        for (UIFunctionalityType uIFunctionalityType : UIFunctionalityType.values()) {
            if (uIFunctionalityType.getFunctionalityTypeId() == functionalityTypeId) {
                return uIFunctionalityType;
            }
        }
        // throw an IllegalArgumentException or return null
        throw new IllegalArgumentException("The given functionalityTypeId doesn't match any UIFunctionalityType.");
    }
	
	public static String getUIFunctionalityTypeName(int functionalityTypeId) {
        for (UIFunctionalityType uIFunctionalityType : UIFunctionalityType.values()) {
            if (uIFunctionalityType.getFunctionalityTypeId() == functionalityTypeId) {
                return uIFunctionalityType.functionalityName;
            }
        }
        // throw an IllegalArgumentException or return null
        throw new IllegalArgumentException("The given functionalityTypeId doesn't match with any UIFunctionalityType.");
    }
}
