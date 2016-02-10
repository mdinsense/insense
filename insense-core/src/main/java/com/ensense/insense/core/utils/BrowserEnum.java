package com.ensense.insense.core.utils;

public enum BrowserEnum {
	FIREFOX{
		@Override
		public int getBrowserTypeId(){
			return 1;
		}

		@Override
		public String getBrowserTypeName() {
			return "Firefox";
		}
	},
	CHROME{

		@Override
		public int getBrowserTypeId() {
			return 2;
		}

		@Override
		public String getBrowserTypeName() {
			return "chrome";
		}
		
	},
	SAFARI{

		@Override
		public int getBrowserTypeId() {
			return 4;
		}

		@Override
		public String getBrowserTypeName() {
			return "safari";
		}
		
	},
	IE{

		@Override
		public int getBrowserTypeId() {
			return 3;
		}

		@Override
		public String getBrowserTypeName() {
			return "IE";
		}
		
	};
	public abstract int getBrowserTypeId();
	public abstract String getBrowserTypeName();

	public BrowserEnum getBrowserForTypeId(int browserTypeId){
		switch (browserTypeId){
		case 1:
			return BrowserEnum.FIREFOX;
		case 2:
			return BrowserEnum.CHROME;
		case 3:
			return BrowserEnum.IE;
		case 4:
			return BrowserEnum.SAFARI;
		}
		return BrowserEnum.FIREFOX;
	}
}
