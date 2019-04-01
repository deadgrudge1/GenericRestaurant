package com.example.genericrestaurant;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

//import ai.api.AIConfiguration;
//import ai.api.AIListener;
//import ai.api.model.AIError;
//import ai.api.model.AIResponse;
//import okhttp3.internal.Util;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class fragment_speak extends Fragment {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<MenuCard> order = new ArrayList<>();

    TextToSpeech voiceoutput;
    FloatingActionButton mic_float_button;
    List<ResponseMessage> responseMessageList = new ArrayList<>();
    RecyclerView Conversation;
    MessageAdapter messageAdapter;
    FloatingActionButton volume_button, offline_micbutton;
    int volume = 1;
    public ResponseMessage message;
    SessionsClient sessionsClient;
    SessionName session;
    private String uuid = UUID.randomUUID().toString();
    DatabaseHelper databaseHelper;
    ArrayList<String> response_food_list;

    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        databaseHelper = new DatabaseHelper(getActivity());
        //ChatBot Initialized.
        initV2Chatbot();

        return inflater.inflate(R.layout.fragment_mic, null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mic_float_button = view.findViewById(R.id.mic_float_button2);
        offline_micbutton = view.findViewById(R.id.mic_float_button);
        offline_micbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
        mic_float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }


        });
        volume_button = view.findViewById(R.id.mute_float_button);

        if (savedInstanceState != null)
            volume = savedInstanceState.getInt("volume");
        if (volume == 1) {
            volume_button.setImageResource(R.drawable.ic_volume_on_black_24dp);
        } else if (volume == 0) {
            volume_button.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
        volume_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume == 1) {
                    volume = 0;
                    volume_button.setImageResource(R.drawable.ic_volume_off_black_24dp);
                } else if (volume == 0) {
                    volume = 1;
                    volume_button.setImageResource(R.drawable.ic_volume_on_black_24dp);
                }
            }
        });

        messageAdapter = new MessageAdapter(responseMessageList);
        Conversation = view.findViewById(R.id.converse);
        Conversation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Conversation.setAdapter(messageAdapter);

        voiceoutput = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
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
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String string = result.get(0);
                    if (string.contains("burger")) {
                        databaseHelper.insertCartItem(databaseHelper.getWritableDatabase(), 1, 2);
                    }

                    messageAdapter = new MessageAdapter(responseMessageList);
                    Conversation.setAdapter(messageAdapter);
                    message = new ResponseMessage(string, true);
                    responseMessageList.add(message);
                    sendMessage(message);
                    messageAdapter.notifyDataSetChanged();


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order_speak_fragment", order);

                }
                break;
            }

        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ArrayList<MenuCard> order);
    }

    public void speak() {
        int i = responseMessageList.size();
        String text = responseMessageList.get(i - 1).getTextmessage();
        voiceoutput.setPitch(1 / 10);
        voiceoutput.setSpeechRate(1 / 2);
        voiceoutput.speak(text, TextToSpeech.QUEUE_FLUSH, null);

    }


    public boolean isMessageVisible() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) Conversation.getLayoutManager();
        int lastpos = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemcount = Conversation.getAdapter().getItemCount();
        return (lastpos >= itemcount);
    }

    @Override
    public void onDestroy() {
        if (voiceoutput != null) {
            voiceoutput.stop();
            voiceoutput.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString("test", "test");
        outState.putInt("volume", volume);
        super.onSaveInstanceState(outState);
    }

    //Obtain response from Agent on DialogueFlow

    private void initV2Chatbot() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.agent_credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            Log.d("Credentials", "Credentials are " + credentials);
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();
            Log.d("projectId", " This is  " + projectId);
            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            Log.d("settingsBuilder ", " This is  " + settingsBuilder);
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            Log.d("sessionSettings ", " This is  " + sessionsSettings);
            sessionsClient = SessionsClient.create(sessionsSettings);
            Log.d("Client of Session ", " This is  " + sessionsClient);
            session = SessionName.of(projectId, uuid);
            Log.d("Name of Session ", " This is  " + session + "\t UUID = " + uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callbackV2(DetectIntentResponse response) {
        String Foods = "";
        if (response != null) {
            // process aiResponse here
            String botReply = response.getQueryResult().getFulfillmentText();
            Log.d("Bot Reply", "V2 Bot Reply: " + botReply);
            ResponseMessage message_server = new ResponseMessage(botReply, false);
            responseMessageList.add(message_server);
            messageAdapter.notifyDataSetChanged();

            if (!isMessageVisible()) {
                Conversation.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                if (volume == 1)
                    speak();
            }

            response_food_list = new ArrayList<>();

            Struct struct = response.getQueryResult().getParameters();
            try {
                Value number = struct.getFieldsOrThrow("number");
                String Quantity = String.valueOf(number.getListValue().getValuesList().get(0).getNumberValue());

                Value Topping = struct.getFieldsOrThrow("Toppings");
                String Toppings = String.valueOf(Topping.getStringValue());

                Value Food = struct.getFieldsOrThrow("Food");
                Foods = Food.getListValue().getValuesList().get(0).getStringValue();

                Log.d("Quantity is:", String.valueOf(Quantity));
                Log.d("Toppings is:", String.valueOf(Toppings));
                Log.d("Foods is:", String.valueOf(Foods));

                Log.d("Collection Response", "Collection : " + struct.toString());
            } catch (Exception e) {
            }
        } else {
            Log.d("Bot Reply", "Bot Reply is Null");
            ResponseMessage message_server = new ResponseMessage("Didn't receive a " +
                    response + "from API.AI. \nCheck your net connection and try again.", false);
            responseMessageList.add(message_server);
            messageAdapter.notifyDataSetChanged();

            if (!isMessageVisible()) {
                Conversation.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                if (volume == 1)
                    speak();
            }
        }
    }

    public void sendMessage(ResponseMessage query_client) {
        String msg = query_client.getTextmessage();
        Log.d("sendMessage-322", "This is Query" + msg);
        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("en-US")).build();
        new RequestJava(fragment_speak.this, session, sessionsClient, queryInput).execute();
    }


}