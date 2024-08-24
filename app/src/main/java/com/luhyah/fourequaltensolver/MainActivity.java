package com.luhyah.fourequaltensolver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class MainActivity extends AppCompatActivity {

    private TextView Solution;
    private CheckBox UseBracket;
    private Button Solve;
    private EditText FourNumbers;
    private EditText Symbols;

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
        Solution = findViewById(R.id.Output);
        UseBracket = findViewById(R.id.UseBracket);
        Solve = findViewById(R.id.Solve);
        FourNumbers = findViewById(R.id.FourNumbers);
        Symbols = findViewById(R.id.Symbols);

    }

    @Override
    protected void onStart() {
        super.onStart();


        Solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FourNumbersString = FourNumbers.getText().toString();
                String SymbolsString = Symbols.getText().toString();
                boolean A = UseBracket.isChecked();
                FetchAnswers(FourNumbersString,SymbolsString,A,Solution);

                    Log.e("FourNumString", FourNumbersString);
                Log.e("SymbolsString", SymbolsString);

            }
        });
    }

    private void FetchAnswers(String fNS, String symbols, boolean isChecked, TextView textView) {
        ArrayList<Character> numArrayListChar = new ArrayList<>();

        for(char num: fNS.toCharArray()){
            numArrayListChar.add(num);
        }
        ArrayList<Character> symbolList = new ArrayList<>();
        for(char symb: symbols.toCharArray()){
            symbolList.add(symb);
        }
        ArrayList<ArrayList<Character>> symbolListCombinations = combine(symbolList);
        ArrayList<ArrayList<Character>> fourNumberListPermutations = permute(numArrayListChar);

        ArrayList<String> Literals = new ArrayList<>();
        if(isChecked) {
            for (ArrayList<Character> num : fourNumberListPermutations) {
                for (ArrayList<Character> op : symbolListCombinations) {
                    String expr = String.valueOf(num.get(0)) + String.valueOf(op.get(0)) + String.valueOf(num.get(1)) + String.valueOf(op.get(1)) + String.valueOf(num.get(2)) + String.valueOf(op.get(2)) + String.valueOf(num.get(3));
                    Literals.add(expr);
                    expr = "(" + String.valueOf(num.get(0)) + String.valueOf(op.get(0)) + String.valueOf(num.get(1))+ ")" + String.valueOf(op.get(1)) + String.valueOf(num.get(2)) + String.valueOf(op.get(2)) + String.valueOf(num.get(3));
                    Literals.add(expr);
                    expr = "(" + String.valueOf(num.get(0)) + String.valueOf(op.get(0)) + String.valueOf(num.get(1)) + String.valueOf(op.get(1)) + String.valueOf(num.get(2)) + ")"+ String.valueOf(op.get(2)) + String.valueOf(num.get(3));
                    Literals.add(expr);
                    expr = String.valueOf(num.get(0)) + String.valueOf(op.get(0)) +"(" +  String.valueOf(num.get(1)) + String.valueOf(op.get(1)) + String.valueOf(num.get(2)) + ")"+ String.valueOf(op.get(2)) + String.valueOf(num.get(3));
                    Literals.add(expr);
                    expr = String.valueOf(num.get(0)) + String.valueOf(op.get(0)) +"(" +  String.valueOf(num.get(1)) + String.valueOf(op.get(1)) + String.valueOf(num.get(2)) + String.valueOf(op.get(2)) + String.valueOf(num.get(3))+ ")";
                    Literals.add(expr);
                    expr = String.valueOf(num.get(0)) + String.valueOf(op.get(0)) +  String.valueOf(num.get(1)) + String.valueOf(op.get(1)) +"(" + String.valueOf(num.get(2)) + String.valueOf(op.get(2)) + String.valueOf(num.get(3))+ ")";
                    Literals.add(expr);
                }
            }
        }
        else{
            for (ArrayList<Character> num : fourNumberListPermutations) {
                for (ArrayList<Character> op : symbolListCombinations) {
                    if (num.size() < 4 || op.size() < 3) {
                       // Log.e("MainActivity", num.toString());
                        continue;
                    }

                    String expr = String.valueOf(num.get(0)) + String.valueOf(op.get(0)) + String.valueOf(num.get(1)) + String.valueOf(op.get(1)) + String.valueOf(num.get(2)) + String.valueOf(op.get(2)) + String.valueOf(num.get(3));
                    Literals.add(expr);
                }

            }
        }
        //ArrayList<String> eq10 = new ArrayList<>();
        Expression e;
        StringBuilder eq10 = new StringBuilder();
        for(int i = 0; i< Literals.size(); i++){
            e = new ExpressionBuilder(Literals.get(i)).build();
           try {
               double result = e.evaluate();
               if(result == 10.0){
                   eq10.append(Literals.get(i)).append(" = 10").append("\n");
               }
           }
           catch (ArithmeticException ignored){

           }

        }
        textView.setText(eq10.toString());

    }

    //SYMBOL COMBINATIONS
    public static ArrayList<ArrayList<Character>> combine(ArrayList<Character> ops) {

        int freq = 3;
        int length = ops.size();
        int loopAmt = (int) Math.pow(length, freq);

        Character[] nOps = ops.toArray(new Character[ops.size()]);

        Character[][] nOutput = new Character[loopAmt][freq];

        for (int i = 0; i < loopAmt; i++) {
            nOutput[i][0] = nOps[i % nOps.length];
        }

        for (int i = 1; i < freq; i++) {
            for (int j = 0; j < loopAmt; j++) {
                nOutput[j][i] = nOps[(j / (int) Math.pow(nOps.length, i)) % nOps.length];
            }
        }


        ArrayList<ArrayList<Character>> output = new ArrayList<>();
        for (Character[] row : nOutput) {
            ArrayList<Character> rowList = new ArrayList<>(Arrays.asList(row));
            output.add(rowList);
        }


        return output;
    }

    //Number Permutations
    public static ArrayList<ArrayList<Character>> permute(ArrayList<Character> nums) {
        ArrayList<ArrayList<Character>> result = new ArrayList<>();

        // Sort the input to handle duplicates correctly
        Collections.sort(nums);

        // Call the backtrack function to generate permutations
        backtrack(result, nums, new ArrayList<>(), new boolean[nums.size()]);

        return result;
    }

    // Recursive backtracking function to generate permutations
    public static void backtrack(ArrayList<ArrayList<Character>> result, ArrayList<Character> nums, ArrayList<Character> tempList, boolean[] used) {
        if (tempList.size() == nums.size()) {
            // If the tempList has the same size as the original list, a permutation is complete, so add it to the result.
            result.add(new ArrayList<>(tempList));
        } else {
            for (int i = 0; i < nums.size(); i++) {
                if (used[i] || (i > 0 && nums.get(i) == nums.get(i - 1) && !used[i - 1])) {
                    // Skip elements already used or handle duplicate elements in a specific position.
                    continue;
                }
                tempList.add(nums.get(i)); // Add the current element to the tempList.
                used[i] = true;
                backtrack(result, nums, tempList, used); // Recursively generate permutations.
                tempList.remove(tempList.size() - 1); // Remove the last added element to backtrack.
                used[i] = false;
            }
        }
    }
}