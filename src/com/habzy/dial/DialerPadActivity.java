/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.habzy.dial;

import android.app.Activity;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;

public class DialerPadActivity extends Activity implements OnClickListener,
        OnKeyListener, OnLongClickListener, TextWatcher
{
    
    private static final String TAG = "DialerPadActivity";
    
    /**
     * View (usually FrameLayout) containing mDigits field. This can be null, in
     * which mDigits isn't enclosed by the container.
     */
    private EditText mDigits;
    
    private View mDelete;
    
    
    private View mDialpad;
    
    private View mAdditionalButtonsRow;
    
    private View mSearchButton;
    
    private View mDialButton;
    
//    private ListView mDialpadChooser;
    
    private boolean mEnableDail = false;
    
    private String mLastNumber;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialpad_whole);
        
        initViews();
    }
    
    private void initViews()
    {
        // Load up the resources for the text field.
        mDigits = (EditText) findViewById(R.id.digits);
        mDigits.setKeyListener(DialerKeyListener.getInstance());
        mDigits.setOnClickListener(this);
        mDigits.setOnKeyListener(this);
        mDigits.setOnLongClickListener(this);
        mDigits.addTextChangedListener(this);
        
        // PhoneNumberFormatter.setPhoneNumberFormattingTextWatcher(DialerPadActivity.this,
        // mDigits);
        
        // Check for the presence of the keypad
        View oneButton = findViewById(R.id.one);
        if (oneButton != null)
        {
            setupKeypad();
        }
        
        mAdditionalButtonsRow = findViewById(R.id.dialpadAdditionalButtons);
        
        mSearchButton = mAdditionalButtonsRow.findViewById(R.id.searchButton);
        if (mSearchButton != null)
        {
            mSearchButton.setOnClickListener(this);
        }
        
        // Check whether we should show the onscreen "Dial" button.
        mDialButton = mAdditionalButtonsRow.findViewById(R.id.dialButton);
        mDialButton.setOnClickListener(this);
        
        mDialButton.setEnabled(mEnableDail); 
        
        mDelete = mAdditionalButtonsRow.findViewById(R.id.deleteButton);
        mDelete.setOnClickListener(this);
        mDelete.setOnLongClickListener(this);
        
        mDialpad = findViewById(R.id.dialpad); // This is null in landscape
                                               // mode.
        
        // In landscape we put the keyboard in phone mode.
        if (null == mDialpad)
        {
            mDigits.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        }
        else
        {
            mDigits.setCursorVisible(false);
        }
        
        // Set up the "dialpad chooser" UI; see showDialpadChooser().
//        mDialpadChooser = (ListView) findViewById(R.id.dialpadChooser);
//        mDialpadChooser.setOnItemClickListener(this);
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.one:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_1);
                keyPressed(KeyEvent.KEYCODE_1);
                return;
            }
            case R.id.two:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_2);
                keyPressed(KeyEvent.KEYCODE_2);
                return;
            }
            case R.id.three:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_3);
                keyPressed(KeyEvent.KEYCODE_3);
                return;
            }
            case R.id.four:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_4);
                keyPressed(KeyEvent.KEYCODE_4);
                return;
            }
            case R.id.five:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_5);
                keyPressed(KeyEvent.KEYCODE_5);
                return;
            }
            case R.id.six:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_6);
                keyPressed(KeyEvent.KEYCODE_6);
                return;
            }
            case R.id.seven:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_7);
                keyPressed(KeyEvent.KEYCODE_7);
                return;
            }
            case R.id.eight:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_8);
                keyPressed(KeyEvent.KEYCODE_8);
                return;
            }
            case R.id.nine:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_9);
                keyPressed(KeyEvent.KEYCODE_9);
                return;
            }
            case R.id.zero:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_0);
                keyPressed(KeyEvent.KEYCODE_0);
                return;
            }
            case R.id.pound:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_P);
                keyPressed(KeyEvent.KEYCODE_POUND);
                return;
            }
            case R.id.star:
            {
                Utils.playTone(this, ToneGenerator.TONE_DTMF_S);
                keyPressed(KeyEvent.KEYCODE_STAR);
                return;
            }
            case R.id.deleteButton:
            {
                keyPressed(KeyEvent.KEYCODE_DEL);
                return;
            }
            case R.id.dialButton:
            {
                placeCall();
                return;
            }
            case R.id.searchButton:
            {
                // if (mListener != null) {
                // mListener.onSearchButtonPressed();
                // }
                return;
            }
            case R.id.digits:
            {
                if (!isDigitsEmpty())
                {
                    mDigits.setCursorVisible(true);
                }
                return;
            }
        }
        
    }
    
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after)
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void afterTextChanged(Editable input)
    {
        if (isDigitsEmpty())
        {
            mDigits.setCursorVisible(false);
        }
        
        updateDialAndDeleteButtonEnabledState();
        
    }
    
    @Override
    public boolean onLongClick(View view)
    {
        final Editable digits = mDigits.getText();
        int id = view.getId();
        switch (id) {
            case R.id.deleteButton: {
                digits.clear();
                // TODO: The framework forgets to clear the pressed
                // status of disabled button. Until this is fixed,
                // clear manually the pressed status. b/2133127
                mDelete.setPressed(false);
                return true;
            }
            case R.id.zero: {
                keyPressed(KeyEvent.KEYCODE_PLUS);
                return true;
            }
            case R.id.digits: {
                // Right now EditText does not show the "paste" option when cursor is not visible.
                // To show that, make the cursor visible, and return false, letting the EditText
                // show the option by itself.
                mDigits.setCursorVisible(true);
                return false;
            }
        }
        return false;
    }
    
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event)
    {
        switch (view.getId()) {
            case R.id.digits:
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    placeCall();
                    return true;
                }
                break;
        }
        return false;
    }
    
    private void setupKeypad()
    {
        // Setup the listeners for the buttons
        View view = findViewById(R.id.one);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);
        findViewById(R.id.star).setOnClickListener(this);
        
        view = findViewById(R.id.zero);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        
        findViewById(R.id.pound).setOnClickListener(this);
    }
    
    private void keyPressed(int keyCode)
    {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        mDigits.onKeyDown(keyCode, event);
    }
    
    /**
     * @return true if the widget with the phone number digits is empty.
     */
    private boolean isDigitsEmpty()
    {
        return mDigits.length() == 0;
    }
    
    /**
     * place the call, but check to make sure it is a viable number.
     */
    private void placeCall()
    {
        mLastNumber = mDigits.getText().toString();
        Log.d(TAG, "EEEEEEE placing call to " + mLastNumber);
        
        // don't place the call if it is not a valid number
        if (mLastNumber == null || !TextUtils.isGraphic(mLastNumber))
        {
            // There is no number entered.
            Utils.playTone(this, ToneGenerator.TONE_PROP_NACK);
            return;
        }
        
        // send a call.
        Log.d(TAG, "Start a call!");
        
        TelephonyManager telephoneManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        int state = telephoneManager.getCallState();
        Log.d(TAG, "current call state:" + state);
        if (TelephonyManager.CALL_STATE_IDLE != state)
        {
            Log.w(TAG, "cs call is running, can't make a ps call!");
            return;
        }
        
        // SipManager.getInstance().makeCall(mLastNumber);
        mDigits.getText().delete(0, mDigits.getText().length());
        
    }
    
    /**
     * Update the enabledness of the "Dial" and "Backspace" buttons if
     * applicable.
     */
    private void updateDialAndDeleteButtonEnabledState()
    {
        final boolean digitsNotEmpty = !isDigitsEmpty();
        
        mDialButton.setEnabled(digitsNotEmpty);
        mDelete.setEnabled(digitsNotEmpty);
    }
    
}