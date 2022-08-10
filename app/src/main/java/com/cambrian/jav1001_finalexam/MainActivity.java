package com.cambrian.jav1001_finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView currentValueTextView, previousValueTextView;
    private Button rollButton;
    private Spinner diceSpinner;
    private DiceViewModel viewModel;
    private ListView historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise view model
        viewModel = new DiceViewModel();
        loadDiceData();
        // Checks if data is null or empty other wise sets initial data to view model dices object
        if (viewModel.dices == null || viewModel.dices.size() == 0) {
            viewModel.addStaticDices();
        }
        setupViews();
    }

    /**
     * Save data to shared preferences for all the dices
     * @params: nothing
     * @return: nothing
     */
    private void saveDiceData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences for dice", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(viewModel.dices);
        editor.putString("Dice List", json);
        editor.apply();
    }

    /**
     * Get data from shared preferences for all the dices and set to view model dices object
     * @params: nothing
     * @return: nothing
     */
    private void loadDiceData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences for dice", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Dice List", null);
        Type type = new TypeToken<ArrayList<Dice>>(){}.getType();
        viewModel.dices = gson.fromJson(json, type);
    }

    /**
     * Setup all the views such as spinnner, roll button, text views and list view
     * @params: nothing
     * @return: nothing
     */
    private void setupViews() {
        diceSpinner = findViewById(R.id.idDiceSpinnerView);
        rollButton = findViewById(R.id.idRollMeButton);
        currentValueTextView = findViewById(R.id.idCurrentValueTextView);
        previousValueTextView = findViewById(R.id.idPreviousValueTextView);
        historyListView = findViewById(R.id.idHistoryListView);

        setupSpinnerAdapter();
        setupListeners();
        setupHistoryListView();
    }

    /**
     * Setup adapter for history list view
     * @params: nothing
     * @return: nothing
     */
    private void setupHistoryListView() {
        ArrayAdapter<String> arr = new ArrayAdapter<String>(this, R.layout.history_list_view_item, R.id.idHistoryListViewTextView, viewModel.previousRounds);
        historyListView.setAdapter(arr);
    }

    /**
     * Setup adapter for Spinner and attach names of dice to it
     * @params: nothing
     * @return: nothing
     */
    private void setupSpinnerAdapter() {
        ArrayAdapter fromArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, viewModel.getDiceNames());
        fromArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diceSpinner.setAdapter(fromArrayAdapter);
    }

    /**
     * Setup listeners for Spinner and Roll button
     * @params: nothing
     * @return: nothing
     */
    private void setupListeners() {
        diceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (adapterView.getId() == R.id.idDiceSpinnerView) {
                    if (position == viewModel.dices.size() - 1) {
                        showAddNewDiceActivity();
                    } else {
                        viewModel.setSelectedDice(position);
                    }
                    reset();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = viewModel.getSelectedDice().roll();
                String currentValue = currentValueTextView.getText().toString();
                // Check if current value is empty or not
                if (currentValue != "-") {
                    previousValueTextView.setText(currentValue);
                }
                currentValueTextView.setText(value);
                // Set roll button title according to isRollAgain boolean
                if (!viewModel.isRollAgain) {
                    viewModel.isRollAgain = true;
                    rollButton.setText("ROLL AGAIN");
                }
                viewModel.addPreviousRound(currentValueTextView.getText().toString());
                setupHistoryListView();
            }
        });
    }

    /**
     * Resets data when new dice selected from the spinner
     * @params: nothing
     * @return: nothing
     */
    private void reset() {
        viewModel.isRollAgain = false;
        rollButton.setText("ROLL ME");
        previousValueTextView.setText("-");
        currentValueTextView.setText("-");
    }

    /**
     * Creates a dialog with form to add custom dice for user
     * Provides two fields one for Name and other sides
     * Checks if field is empty or not or if dice is already present or not
     * @params: nothing
     * @return: nothing
     */
    private void showAddNewDiceActivity() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_add_new_dice);

        EditText diceNameTextView = dialog.findViewById(R.id.idNewDiceNameTextView);
        EditText sidesTextView = dialog.findViewById(R.id.idNewDiceSidesTextView);
        Button saveButton = dialog.findViewById(R.id.idSaveNewDiceButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dice dice;
                String diceName = diceNameTextView.getText().toString();
                String diceSides = sidesTextView.getText().toString();
                if (!diceName.isEmpty() && !diceSides.isEmpty()) {
                    int sides = Integer.valueOf(diceSides);
                    if (sides < 2) {
                        makeToast("Please enter sides more than 1");
                    } else if (viewModel.checkIfSidesExists(sides)) {
                        makeToast("This dice is already present");
                    } else {
                        dice = new Dice(diceName, Integer.valueOf(diceSides));
                        viewModel.dices.add(viewModel.dices.size()-1, dice);
                        setupSpinnerAdapter();
                        diceSpinner.setSelection(viewModel.dices.size()-2);
                        saveDiceData();
                        dialog.dismiss();
                    }
                } else {
                    makeToast( "Please Enter Dice " + (diceName.isEmpty() ? "Name" : "Sides"));
                }
            }
        });

        dialog.show();
    }

    /**
     * Creates Toast for warnings or errors
     * @params: message - Custom message to show to user
     * @return: nothing
     */
    private void makeToast(String message) {
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}