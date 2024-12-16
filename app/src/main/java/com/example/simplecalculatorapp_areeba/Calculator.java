package com.example.simplecalculatorapp_areeba;
import android.app.Application;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calculator extends Application {

    TextView displayResult;
    public ArrayList<String> calculationHistory = new ArrayList<>();

    public boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("x") || str.equals("/");
    }

    public double evaluateExpression(ArrayList<String> expression) {
        double result = Double.parseDouble(expression.get(0)); // Start with the first number

        for (int i = 1; i < expression.size(); i += 2) { // Process pairs (operator, number)
            String operator = expression.get(i);
            double num = Double.parseDouble(expression.get(i + 1));
            switch (operator) {
                case "+":
                    result += num;
                    break;
                case "-":
                    result -= num;
                    break;
                case "x":
                    result *= num;
                    break;
                case "/":
                    result = (num != 0) ? result / num : 0; // Avoid division by zero
                    break;
            }
        }
        return result;
    }


    public double evaluateBodmasExpression(ArrayList<String> expression) {
        // Step 1: Handle Multiplication and Division First
        ArrayList<String> tempExpression = new ArrayList<>(expression);

        for (int i = 0; i < tempExpression.size(); i++) {
            if (tempExpression.get(i).equals("x") || tempExpression.get(i).equals("/")) {
                double num1 = Double.parseDouble(tempExpression.get(i - 1));
                double num2 = Double.parseDouble(tempExpression.get(i + 1));
                double result = 0;

                if (tempExpression.get(i).equals("x")) {
                    result = num1 * num2;
                } else if (tempExpression.get(i).equals("/")) {
                    if (num2 == 0) {
                        displayResult.setText(R.string.error_division_by_zero);
                        return 0;
                    }
                    result = num1 / num2;
                }

                // Replace the operator and numbers with the result
                tempExpression.set(i - 1, String.valueOf(result));
                tempExpression.remove(i); // Remove operator
                tempExpression.remove(i); // Remove second number
                i--; // Adjust index
            }
        }

        // Step 2: Handle Addition and Subtraction
        double result = Double.parseDouble(tempExpression.get(0));
        for (int i = 1; i < tempExpression.size(); i += 2) {
            String operator = tempExpression.get(i);
            double num = Double.parseDouble(tempExpression.get(i + 1));

            if (operator.equals("+")) {
                result += num;
            } else if (operator.equals("-")) {
                result -= num;
            }
        }

        return result;
    }

    public String formatHistory() {
        StringBuilder formattedHistory = new StringBuilder();
        for (String record : calculationHistory) {
            formattedHistory.append(record).append("\n");
        }
        return formattedHistory.toString();
    }

    public String getLastIndexValue() {
        if (!calculationHistory.isEmpty()) {
            return calculationHistory.get(calculationHistory.size() - 1);
        } else {
            return "No history available";
        }
    }

    public List<String> getLastThreeValues() {
        int historySize = calculationHistory.size();
        if (historySize == 0) {
            return Collections.emptyList(); // Return an empty list if there is no history.
        } else if (historySize <= 3) {
            return new ArrayList<>(calculationHistory); // Return all values if the size is 3 or less.
        } else {
            return calculationHistory.subList(historySize - 3, historySize); // Get the last 3 values.
        }
    }

    public String getLastTwoValuesFormatted() {
        List<String> lastTwo = getLastThreeValues();
        return String.join("\n", lastTwo);
    }

}
