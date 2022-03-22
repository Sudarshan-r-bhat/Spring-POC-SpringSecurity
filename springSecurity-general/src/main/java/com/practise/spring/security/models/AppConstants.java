package com.practise.spring.security.models;

public interface AppConstants {
	
	public interface OAUTH2 {
		
		public interface GOOGLE {
			public final String GOOGLE_CLIENT_ID = "******************";
			public final String GOOGLE_CLIENT_SECRET = "******************";
			public final String LOGIN_FAILURE_URL = "http://localhost:4200/login";
		}

	}
	public interface JWT {
		public final String SECRET_KEY = "narutoWillBecomeHookageOneDay!-BelieveIt!";
		
		
	}
}
