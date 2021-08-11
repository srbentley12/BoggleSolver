package com.example.boggle;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    List<String> dictionary;
    TextView boardTextView;
    Button boggleButton;
    ListView outputListView;
    EditText gridLettersEditText, widthEditText, heightEditText;
    ArrayList<String> output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize UI elements
        boardTextView = findViewById(R.id.board_tv);
        boggleButton = findViewById(R.id.boggle_button);
        outputListView = findViewById(R.id.output_listView);
        gridLettersEditText = findViewById(R.id.letters_edit_text);
        widthEditText = findViewById(R.id.width_edit_text);
        heightEditText = findViewById(R.id.height_edit_text);
        output = new ArrayList<>();

        //Initialize adapter for the output listview
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, output);
        outputListView.setAdapter(arrayAdapter);

        //Initialize dictionary words
        dictionary = new ArrayList<>();
        readDictionaryFile();


        //This button is the driver for the boggle class.
        boggleButton.setOnClickListener(new View.OnClickListener() {

            //First the output list is cleared.
            @Override
            public void onClick(View view) {
                output.clear();
                //Check to see if valid input
                if (widthEditText.getText() != null && heightEditText.getText() != null && gridLettersEditText.getText() != null) {
                    //Set width and height int for board dimensions
                    int width = Integer.parseInt(String.valueOf(widthEditText.getText()));
                    int height = Integer.parseInt(String.valueOf(heightEditText.getText()));

                    //Check it board letter input is valid
                    if (String.valueOf(gridLettersEditText.getText()).length() == width * height) {
                        //Run instance of boggle class
                        Boggle boggle = new Boggle();
                        boggle.setLegalWords(dictionary);
                        output.addAll(boggle.solveBoard(width, height, String.valueOf(gridLettersEditText.getText()).toLowerCase()));
                        boardTextView.setText(boggle.printBoard(boggle.board));
                    } else {
                        //Report to user that input is invalid
                        boardTextView.setText("Invalid Input!\nMust enter a width, and height for" +
                                                "\nfor the game board and" +
                                                "\nW * H amount of letters");
                    }
                } else {
                    //report to user that input is invalid
                    boardTextView.setText("Invalid Input!\nMust enter a width, height and letters.");
                }
                //Update listview items
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    //Helper method to read it a text file, line by line, as a word
    //dictionary. Text file should be in the app/src/main/assets/ directory
    private void readDictionaryFile() {
        BufferedReader reader;

        try {
            final InputStream file = getAssets().open("dictionary.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while (line != null) {
                dictionary.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}