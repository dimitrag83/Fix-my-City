package com.eap.sdy;

import java.util.ArrayList;
import java.util.List;

public interface OnTaskCompleted {
		void onCodeCheckCompleted(String result);
		void onReportsReceived(ArrayList<Report> reports);
	}

