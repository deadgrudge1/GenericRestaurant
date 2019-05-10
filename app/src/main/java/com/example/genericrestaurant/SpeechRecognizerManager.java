package com.example.genericrestaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class SpeechRecognizerManager{


    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";
    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "jarvis";
    private static final String KEY_SHUT = "";
    private edu.cmu.pocketsphinx.SpeechRecognizer mPocketSphinxRecognizer;
    private static final String TAG = SpeechRecognizerManager.class.getSimpleName();
    private Context mContext;
    TextToSpeech voiceoutput;
    List<ResponseMessage> responseMessageList = new ArrayList<>();
    MessageAdapter messageAdapter;
    FragmentSpeakListener fragmentSpeakListener;



    public SpeechRecognizerManager(Context context) {
        this.mContext = context;

        initPockerSphinx();
        voiceoutput = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = voiceoutput.setLanguage(Locale.ENGLISH);                                //SETTING LANGUAGE TO ENGLISH
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("VOICE OUTPUT", "Language not Supported");
                    } else {
                    }
                } else {
                    Log.e("VOICE OUTPUT", "Initialization failure");
                }
            }
        });

        messageAdapter = new MessageAdapter(responseMessageList);
    }

    public void sphinx_listen()
    {
        mPocketSphinxRecognizer.startListening(KWS_SEARCH);
    }

    @SuppressLint("StaticFieldLeak")
    private void initPockerSphinx() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(mContext);

                    //Performs the synchronization of assets in the application and external storage
                    File assetDir = assets.syncAssets();


                    //Creates a new speech recognizer builder with default configuration
                    SpeechRecognizerSetup speechRecognizerSetup = SpeechRecognizerSetup.defaultSetup();

                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));

                    // Threshold to tune for keyphrase to balance between false alarms and misses
                    speechRecognizerSetup.setKeywordThreshold(1e-45f);

                    speechRecognizerSetup.setFloat("-vad_threshold", 4.0);

                    //Creates a new SpeechRecognizer object based on previous set up.
                    mPocketSphinxRecognizer = speechRecognizerSetup.getRecognizer();

                    // Create keyword-activation search.
                    mPocketSphinxRecognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

                    // Create your custom grammar-based search
                    File menuGrammar = new File(assetDir, "menu.list");
                    mPocketSphinxRecognizer.addKeywordSearch(MENU_SEARCH, menuGrammar);
                    //mPocketSphinxRecognizer.addGrammarSearch(KWS_SEARCH, menuGrammar);

                    mPocketSphinxRecognizer.addListener(new PocketSphinxRecognitionListener());
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(mContext, "Failed to init pocketSphinxRecognizer ", Toast.LENGTH_SHORT).show();
                } else {
                    restartSearch(KWS_SEARCH);
                }
            }
        }.execute();

    }


    private void restartSearch(String searchName) {
        mPocketSphinxRecognizer.stop();
        if (searchName.equals(KWS_SEARCH))
            mPocketSphinxRecognizer.startListening(searchName);
        else {
            mPocketSphinxRecognizer.startListening(MENU_SEARCH, 5000);
            /*TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mPocketSphinxRecognizer.startListening(KWS_SEARCH);
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask,5000); */


            //mPocketSphinxRecognizer.startListening(searchName);
        }

    }



    protected class PocketSphinxRecognitionListener implements edu.cmu.pocketsphinx.RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
        }


        /**
         * In partial result we get quick updates about current hypothesis. In
         * keyword spotting mode we can react here, in other modes we need to wait
         * for final result in onResult.
         */
        @Override
        public void onPartialResult(Hypothesis hypothesis) {
            if (hypothesis == null)
                return;


            String text = hypothesis.getHypstr();




            if (text.contains(KEYPHRASE)) {
                Toast.makeText(mContext,"You said:"+text,Toast.LENGTH_SHORT).show();
                voice_out("Yes User");
                restartSearch(MENU_SEARCH);
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        restartSearch(KWS_SEARCH);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask,5000);


                //mPocketSphinxRecognizer.cancel();
                //MainActivity.getInstance().findViewById(R.id.btnSpeak).performClick();
                //restartSearch(KWS_SEARCH);
                }

            else if (text.contains("food")) {
                Toast.makeText(mContext, "You said:" + text, Toast.LENGTH_SHORT).show();
                //System.exit(1);
                Fragment fragment = MainActivity.getInstance().fragment_speak;
                fragment_speak fragment_speak = (fragment_speak)fragment;
                //Fragment fragment = new fragment_speak();
                fragmentSpeakListener = ((fragment_speak) fragment).fragmentSpeakListener;

                MainActivity.getInstance().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment,"Speak")
                        .commit();
                try {
                    //Thread.sleep(500);
                    //fragmentSpeakListener.voice_input_listener();
                    fragment_speak.mic_float_button.performClick();
                    //fragment_speak.promptSpeechInput();
                }
                catch (Exception e) {}
                MainActivity.getInstance().navigation.setSelectedItemId(R.id.navigation_Microphone);
                voice_out("What would you like?");


                //listener.voice_input_listener();
                restartSearch(KWS_SEARCH);
            }

            else if (text.contains("menu")) {
                Toast.makeText(mContext, "You said:" + text, Toast.LENGTH_SHORT).show();
                //System.exit(1);

                MainActivity.getInstance().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.getInstance().fragment_menu,"Menu")
                        .commit();
                voice_out("Here you go");
                MainActivity.getInstance().navigation.setSelectedItemId(R.id.navigation_home);
                restartSearch(KWS_SEARCH);
            }

            else if (text.contains("cart")) {
                Toast.makeText(mContext, "You said:" + text, Toast.LENGTH_SHORT).show();
                //System.exit(1);

                MainActivity.getInstance().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainActivity.getInstance().fragment_cart,"Cart")
                        .commit();

                voice_out("Would you like to place the order?");
                MainActivity.getInstance().navigation.setSelectedItemId(R.id.navigation_dashboard);
                restartSearch(KWS_SEARCH);
            }

            else if (text.contains("waiter")) {
                Toast.makeText(mContext, "You said:" + text, Toast.LENGTH_SHORT).show();
                //System.exit(1);

                //listener.voice_input_listener();
                voice_out("What would you like me to do?");
                MainActivity.getInstance().navigation.setSelectedItemId(R.id.navigation_Microphone);
                restartSearch(KWS_SEARCH);
            }

        }

        @Override
        public void onResult(Hypothesis hypothesis) {
            if (hypothesis == null)
                return;

            String text = hypothesis.getHypstr();
            //Toast.makeText(mContext, "Result:" + text, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onEndOfSpeech() {
        }

        public void onError(Exception error) {
        }

        @Override
        public void onTimeout() {
        }



    }

    public void speak() {
        int i = responseMessageList.size();
        String text = responseMessageList.get(i - 1).getTextmessage();
        voiceoutput.setPitch(1 / 10);
        voiceoutput.setSpeechRate(1 / 2);
        voiceoutput.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void voice_out(String out)
    {
        ResponseMessage message = new ResponseMessage(out, true);
        responseMessageList.add(message);
        messageAdapter = new MessageAdapter(responseMessageList);
        messageAdapter.notifyDataSetChanged();
        speak();
    }


}