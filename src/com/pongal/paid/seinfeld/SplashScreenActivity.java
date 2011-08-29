package com.pongal.paid.seinfeld;

import com.pongal.seinfeld.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.splashscreen);
	getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	TextView aboutContent = (TextView) findViewById(R.id.aboutContent);
	aboutContent
		.setText(Html
			.fromHtml("This application was inspired by Brad Issac's article "
				+ "<a href=\"http://lifehacker.com/#!281626/jerry-seinfelds-productivity-secret\">'Jerry Seinfeld's productivity secret'</a> "
				+ "<br><br> Thanks to Rajesh Babu for sharing this link"));
	aboutContent.setMovementMethod(LinkMovementMethod.getInstance());
	LinearLayout parent = (LinearLayout) findViewById(R.id.splash);
	parent.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		 finish();
	    }
	});
    }
}
