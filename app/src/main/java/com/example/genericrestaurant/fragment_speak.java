package com.example.genericrestaurant;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import ai.api.AIConfiguration;
//import ai.api.AIListener;
//import ai.api.model.AIError;
//import ai.api.model.AIResponse;
//import okhttp3.internal.Util;

import static android.app.Activity.RESULT_OK;

public class fragment_speak extends Fragment  {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<MenuCard> order = new ArrayList<>();
    TextView total_amount,date_user;

    Date currenttime = Calendar.getInstance().getTime();
    TextToSpeech voiceoutput;
    FloatingActionButton mic_float_button;
    List<ResponseMessage> responseMessageList = new ArrayList<>();
    RecyclerView Conversation;
    MessageAdapter messageAdapter;
    FloatingActionButton volume_button;
    int volume=1;
    public ResponseMessage message;





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
                    getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //order1 = new MenuCard("Chicken Burger", "Rs. 100", "Non-Veg.");
        //order.add(order1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);



       return inflater.inflate(R.layout.fragment_mic,null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mic_float_button = view.findViewById(R.id.mic_float_button);
        mic_float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        volume_button = view.findViewById(R.id.mute_float_button);

        if(savedInstanceState!=null)
            volume = savedInstanceState.getInt("volume");

        if(volume == 1)
        {
            volume_button.setImageResource(R.drawable.ic_volume_on_black_24dp);
        }

        else if(volume == 0)
        {
            volume_button.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }

        volume_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(volume == 1)
                {
                    volume = 0;
                    volume_button.setImageResource(R.drawable.ic_volume_off_black_24dp);
                }

                else if(volume == 0)
                {
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
                if(status == TextToSpeech.SUCCESS)
                {
                    int result =   voiceoutput.setLanguage(Locale.ENGLISH);                                //SETTING LANGUAGE TO ENGLISH
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e( "VOICE OUTPUT",  "Language not Supported");
                    }
                    else
                    {

                    }

                }

                else
                {
                    Log.e( "VOICE OUTPUT",  "Initialization failure");
                }

            }
        });

        //final AIConfiguration aiConfiguration;
        //aiConfiguration = new AIConfiguration("1348d95ad6aa4e119cec25a7973ba09f",AIConfiguration.SupportedLanguages.English);



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String string=result.get(0);

                    messageAdapter = new MessageAdapter(responseMessageList);
                    Conversation.setAdapter(messageAdapter);




                    message = new ResponseMessage(string ,true);
                    responseMessageList.add(message);
                    ResponseMessage message2 = null;
                    try {
                        message2 = new ResponseMessage(getText(message.getTextmessage()),false);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    responseMessageList.add(message2);
                    messageAdapter.notifyDataSetChanged();
                    if(!isMessageVisible())
                    {
                        Conversation.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                        if(volume == 1)
                            speak();
                    }


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order_speak_fragment",order);

                }
                break;
            }

        }
    }

    /*@Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }*/


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ArrayList<MenuCard> order);
    }

    public void speak()
    {
        int i = responseMessageList.size();
        String text = responseMessageList.get(i - 1).getTextmessage();
        voiceoutput.setPitch(1/10);
        voiceoutput.setSpeechRate(1/2);
        voiceoutput.speak(text,TextToSpeech.QUEUE_FLUSH,null);

    }


    public boolean isMessageVisible()
    {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) Conversation.getLayoutManager();
        int lastpos = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemcount = Conversation.getAdapter().getItemCount();
        return (lastpos>=itemcount);
    }

    @Override
    public void onDestroy()
    {
        if(voiceoutput != null)
        {
            voiceoutput.stop();
            voiceoutput.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString("test","test");
        outState.putInt("volume",volume);
        super.onSaveInstanceState(outState);
    }

    //Obtain response from Agent on DialogueFlow

    public String getText(String query) throws UnsupportedEncodingException
    {
        String message_server = null;
        String text = null;
        BufferedReader bufferedReader = null;

        try {

            URL url = new URL("https://api.api.ai/v1/query?v=20150910");

            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Authorization","Bearer 1348d95ad6aa4e119cec25a7973ba09f");
            connection.setRequestProperty("Content-Type","application/json");

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonParam = new JSONObject();
            jsonArray.put(query);
            jsonParam.put("query",jsonArray);
            jsonParam.put("lang","en");
            jsonParam.put("Session id","1234567890");

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            Log.d("Query","after converesion is" + jsonParam.toString());
            wr.write(jsonParam.toString());
            wr.flush();
            Log.d("Query","Json is" + jsonParam);


            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder Sb = new StringBuilder();
            String line =  null;

            text = Sb.toString();




            do{

                Sb.append(line + "\n");

            }while( (line = bufferedReader.readLine()) != null );

            JSONObject server_object = new JSONObject(text);
            JSONObject serverobject = server_object.getJSONObject("result");
            JSONObject fulfillment = null;
            if(serverobject.has("fulfillment")) {
                fulfillment = serverobject.getJSONObject("fulfillment");

                if(fulfillment.has("speech")){

                    message_server = fulfillment.optString("speech");

                }

            }


            return message_server;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}