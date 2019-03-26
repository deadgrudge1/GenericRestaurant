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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class fragment_speak extends Fragment {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<MenuCard> order = new ArrayList<>();
    TextView total_amount;

    TextToSpeech voiceoutput;
    FloatingActionButton mic_float_button;
    List<ResponseMessage> responseMessageList = new ArrayList<>();
    RecyclerView Conversation;
    MessageAdapter messageAdapter;


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mic_float_button = view.findViewById(R.id.mic_float_button);
        mic_float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
        messageAdapter = new MessageAdapter(responseMessageList);
        Conversation = view.findViewById(R.id.conversation);
        Conversation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Conversation.setAdapter(messageAdapter);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String string=result.get(0);

                    responseMessageList.add(new ResponseMessage(string,true));
                    responseMessageList.add(new ResponseMessage("This is bot.",false));

                    //order.add(new MenuCard(string,"Rs. 50","Veg",0));
                    messageAdapter = new MessageAdapter(responseMessageList);
                    Conversation.setAdapter(messageAdapter);
                    speak();


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order_speak_fragment",order);

                }
                break;
            }

        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ArrayList<MenuCard> order);
    }

    public void speak()
    {
        String text = responseMessageList.get(responseMessageList.size()-1).getTextmessage();
        voiceoutput.setPitch(1/10);
        voiceoutput.setSpeechRate(1/2);
        voiceoutput.speak(text,TextToSpeech.QUEUE_FLUSH,null);

    }

}