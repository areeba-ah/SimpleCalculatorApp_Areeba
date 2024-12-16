package com.example.simplecalculatorapp_areeba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView title,inputText, displayResult, history, calculator_History;
    Button advance_basic;

    ImageButton info;

    private String input = "";
    private ArrayList<String> expression = new ArrayList<>(); // Stores numbers and operators

    private String currentNumber = "";

    boolean newCalculation = true;
    boolean switchToAdvance = true;

    private Calculator calculator; // Instance of Calculator


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputText = findViewById(R.id.display);
        displayResult = findViewById(R.id.resultDisplay);
        title = findViewById(R.id.title);
        history = findViewById(R.id.history);
        calculator_History = findViewById(R.id.calculatorHistory);

        //Image Button
        info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent informationActivity = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(informationActivity);
            }
        });

        //Button
        advance_basic = findViewById(R.id.advance_basic);
        advance_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchToAdvance){

                    if(calculator.calculationHistory.isEmpty()){
                        calculator_History.setText(R.string.no_history_available);
                    }

                    title.setText(R.string.my_calculator_advance);
                    advance_basic.setText(R.string.basic_calculator_with_out_history);
                    history.setText(R.string.history);
                    calculator_History.setVisibility(View.VISIBLE);
                    switchToAdvance = false;

                    history.setTextColor(getResources().getColor(R.color.splashScreen));
                }

                else{
                    title.setText(R.string.my_calculator_basic);
                    advance_basic.setText(R.string.advance_calculator_with_history);
                    history.setText(R.string.no_history);
                    calculator_History.setVisibility(View.GONE);
                    switchToAdvance = true;
                }
            }
        });
        //History Screen
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!switchToAdvance){
                    // Get the history formatted string
                    String history = calculator.formatHistory();

                    Intent historyActivity = new Intent(MainActivity.this, ViewFullHistory.class);

                    // Pass the history string using putExtra
                    historyActivity.putExtra("calculation_history", history);

                    startActivity(historyActivity);

                    Log.d("TEXT", "onClick: CLICKABLE TEXT");
                }
            }
        });

        // Initialize Calculator
        calculator = new Calculator();

        // Set up button click listeners
        setNumberButtonListeners();
        setOperatorButtonListeners();
    }

    private void setNumberButtonListeners() {
        int[] numberButtonIds = {
                R.id.zero, R.id.one, R.id.two, R.id.three,
                R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine
        };

        View.OnClickListener listener = view -> {

            Log.d("Expression", expression.toString());

            Button button = (Button) view;
            currentNumber += button.getText().toString();

            if(newCalculation){
                input = "";
                inputText.setText("");
                displayResult.setText("");
            }
                newCalculation = false;
                input += button.getText().toString();
                inputText.setText(input);

        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorButtonListeners() {
        int[] operatorButtonIds = {
                R.id.addition, R.id.subtract, R.id.multiply, R.id.divide, R.id.equal, R.id.ac
        };

        for (int id : operatorButtonIds) {
            findViewById(id).setOnClickListener(this::onOperatorClick);
        }
    }

    private void onOperatorClick(View view) {
        Button button = (Button) view;
        String clickedOperator = button.getText().toString();

        switch (clickedOperator) {
            case "AC":
                input = "";
                currentNumber = "";
                expression.clear();
                inputText.setText("0");
                displayResult.setText("0");
                calculator.calculationHistory.clear();
                calculator_History.setText(R.string.history_cleared_no_history_available);
                break;


            case "=":
                if (currentNumber.isEmpty()) {
                    displayResult.setText(R.string.error_incomplete_expression);
                } else {
                    expression.add(currentNumber); // Add the last number to the expression
                    currentNumber = "";
                    if(switchToAdvance){
                        double result = calculator.evaluateExpression(expression);
                        displayResult.setText(String.valueOf(result));
                    }
                    else {
                        double result = calculator.evaluateBodmasExpression(expression);
                        displayResult.setText(String.valueOf(result));

                        //History
                        String fullExpression = input + " = " + result; // Combine input and result
                        calculator.calculationHistory.add(fullExpression); // Add to history
                       // calculator_History.setText(calculator.formatHistory()); // Update history TextView
                        calculator_History.setText(calculator.getLastTwoValuesFormatted());
                    }

                    expression.clear(); // Clear for the next calculation
                    newCalculation = true;
                }
                break;

            default: // Handle operators
                if (!currentNumber.isEmpty()) {
                    expression.add(currentNumber); // Add the current number to the expression
                    currentNumber = "";
                    expression.add(clickedOperator); // Add operator to the expression
                    input += clickedOperator;
                    inputText.setText(input);
                } else if (!expression.isEmpty() && calculator.isOperator(expression.get(expression.size() - 1))) {
                    // Replace the last operator if there's no new number
                    expression.set(expression.size() - 1, clickedOperator);
                    input = input.substring(0, input.length() - 1) + clickedOperator;
                    inputText.setText(input);
                }
                break;
        }
    }
}
