package a6.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import a6.calculator.model.*;
import a6.calculator.controller.Operation;

public class MainActivity extends AppCompatActivity {

    private enum DisplayState { INPUT, STACK, ERROR }
    private DisplayState displayState = DisplayState.INPUT;

    private StackCalculator calc;
    private TextView stackView;
    private TextView displayView;
    private StringBuilder inputBuilder = new StringBuilder("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calc = new StackCalculator();
        stackView = findViewById(R.id.stack);
        displayView = findViewById(R.id.display);

        setupDigitButtons();
        setupOperatorButtons();
        setupEnterButton();
        setupClearButton();
        setupPlusMinusButton();
        updateDisplay();
    }

    private void setupDigitButtons() {
        int[] ids = {R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine};
        for (int i = 0; i < ids.length; i++) {
            int finalI = i;
            Button btn = findViewById(ids[i]);
            btn.setOnClickListener(v -> {
                if (displayState != DisplayState.INPUT) {
                    inputBuilder.setLength(0);
                    inputBuilder.append("0");
                    displayState = DisplayState.INPUT;
                }
                if (inputBuilder.toString().equals("0")) {
                    inputBuilder.setLength(0);
                }
                inputBuilder.append(finalI);
                updateDisplay();
            });
        }
    }

    private void setupPlusMinusButton() {
        Button btn = findViewById(R.id.plusminus);
        btn.setOnClickListener(v -> {
            if (displayState != DisplayState.INPUT) {
                inputBuilder.setLength(0);
                inputBuilder.append("-0");
                displayState = DisplayState.INPUT;
            } else {
                if (inputBuilder.length() > 0 && inputBuilder.charAt(0) == '-') {
                    inputBuilder.deleteCharAt(0);
                } else {
                    inputBuilder.insert(0, "-");
                }
            }
            updateDisplay();
        });
    }

    private void setupEnterButton() {
        Button btn = findViewById(R.id.enter);
        btn.setOnClickListener(v -> {
            if (displayState == DisplayState.INPUT || displayState == DisplayState.STACK) {
                try {
                    int num = Integer.parseInt(inputBuilder.toString());
                    calc.push(num);
                    displayState = DisplayState.STACK;
                    inputBuilder = new StringBuilder(Integer.toString(num));
                    updateDisplay();
                } catch (NumberFormatException e) {
                    showError("Overflow");
                }
            }
        });
    }

    private void setupClearButton() {
        Button btn = findViewById(R.id.clear);
        btn.setOnClickListener(v -> {
            calc.clear();
            inputBuilder.setLength(0);
            inputBuilder.append("0");
            displayState = DisplayState.INPUT;
            updateDisplay();
        });
    }

    private void setupOperatorButtons() {
        Button addBtn = findViewById(R.id.add);
        Button subBtn = findViewById(R.id.sub);
        Button mulBtn = findViewById(R.id.mul);
        Button divBtn = findViewById(R.id.div);

        addBtn.setOnClickListener(v -> performOperation(() -> calc.add()));
        subBtn.setOnClickListener(v -> performOperation(() -> calc.subtract()));
        mulBtn.setOnClickListener(v -> performOperation(() -> calc.multiply()));
        divBtn.setOnClickListener(v -> performOperation(() -> calc.divide()));
    }

    private void performOperation(Operation op) {
        if (displayState == DisplayState.INPUT) {
            try {
                int num = Integer.parseInt(inputBuilder.toString());
                calc.push(num);
                displayState = DisplayState.STACK;
            } catch (NumberFormatException e) {
                showError("Overflow");
                return;
            }
        }

        try {
            op.apply();
            inputBuilder = new StringBuilder(Integer.toString(calc.peek()));
            displayState = DisplayState.STACK;
            updateDisplay();
        } catch (NotEnoughArgumentsException e) {
            showError("Not enough args");
        } catch (OverflowException e) {
            showError("Overflow");
        } catch (DivisionByZeroException e) {
            showError("Division by 0");
        }
    }

    private void updateDisplay() {
        String text = inputBuilder.toString();
        if (displayState == DisplayState.INPUT) text += "_";
        displayView.setText(text);

        StringBuilder stackText = new StringBuilder();
        for (int i = calc.size() - 1; i >= 0; i--) {
            stackText.append(calc.get(i)).append(" ");
        }
        stackView.setText(stackText.toString().trim());
    }

    private void showError(String msg) {
        displayView.setText(msg);
        displayState = DisplayState.ERROR;
    }
}
