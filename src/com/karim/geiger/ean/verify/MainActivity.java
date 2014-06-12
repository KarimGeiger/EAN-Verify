package com.karim.geiger.ean.verify;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText etEAN, etCountry, etCompany, etArticle, etCheck, etCheckCalc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Assign elements to variables.
		etEAN = (EditText) findViewById(R.id.etEAN);
		etCountry = (EditText) findViewById(R.id.etCountry);
		etCompany = (EditText) findViewById(R.id.etCompany);
		etArticle = (EditText) findViewById(R.id.etArticle);
		etCheck = (EditText) findViewById(R.id.etCheck);
		etCheckCalc = (EditText) findViewById(R.id.etCheckCalc);

		resetAll();

		// TextChangedListener triggers verifyEAN every time the user has typed.
		etEAN.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				verifyEAN();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
	}

	/**
	 * Reset all fields for default state. This is required if the user deletes
	 * the EAN.
	 */
	private void resetAll() {
		etCountry.setText("");
		etCompany.setText("");
		etArticle.setText("");
		etCheck.setText("");
		etCheckCalc.setText("");
		etCheckCalc.setTextColor(Color.BLUE);
	}

	/**
	 * Verify EAN and fill in all fields that can be filled in.
	 */
	private void verifyEAN() {
		resetAll();
		String ean = etEAN.getText().toString();

		// Execute as many operations as possible. Breaks at some place if the
		// EAN isn't long enough. But that's okay because this method gets
		// called every time the user releases a key.
		try {
			etCountry.setText(ean.subSequence(0, 3));
			etCompany.setText(ean.subSequence(3, 7));
			etArticle.setText(ean.subSequence(7, 12));

			// Create checsum or output error if EAN is too long.
			if (ean.length() <= 13)
				etCheckCalc.setText(generateChecksum(ean).toString());
			else
				etCheckCalc.setText(R.string.error);

			etCheck.setText(ean.subSequence(12, 13));
			setChecksumColor();
		} catch (IndexOutOfBoundsException e) {
			// It's okay. Just run as many operations as possible.
		}
	}

	/**
	 * Generate checksum for 13 digit EAN.
	 * 
	 * @param ean
	 *            13 digit EAN.
	 * @return Check digit.
	 */
	private static Integer generateChecksum(String ean) {
		char[] eanArr = ean.toCharArray();
		int sum = 0;

		for (int i = 0; i < 12; i++)
			sum += (eanArr[i] - '0') * (1 + i % 2 * 2);

		return (10 - sum % 10) % 10;
	}

	/**
	 * Set color of etCheckCalc according to entered text. Red if the text does
	 * not match, otherwise blue.
	 */
	private void setChecksumColor() {
		if (etCheck.getText().toString().equals(etCheckCalc.getText().toString()))
			etCheckCalc.setTextColor(Color.BLUE);
		else
			etCheckCalc.setTextColor(Color.RED);
	}
}
