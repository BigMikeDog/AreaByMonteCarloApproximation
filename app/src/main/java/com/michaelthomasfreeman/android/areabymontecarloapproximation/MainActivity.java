package com.michaelthomasfreeman.android.areabymontecarloapproximation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RadioButton xSquaredEquationRadio;
    RadioButton normalCurveEquationRadio;
    EditText lowerXBoundField;
    EditText upperXBoundField;
    EditText lowerYBoundField;
    EditText upperYBoundField;
    EditText numberSamplesField;
    TextView answerField;

    int equationSelected=2;
    double dart[]= new double[2];

    Toast noEquationSelectedToast;

    Random mRandom=new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xSquaredEquationRadio=findViewById(R.id.equationRadioNormalCurve);
        normalCurveEquationRadio=findViewById(R.id.equationRadioXSquared);
        lowerXBoundField=findViewById(R.id.x_lower_bound_field);
        upperXBoundField=findViewById(R.id.x_upper_bound_field);
        lowerYBoundField=findViewById(R.id.y_lower_bound_field);
        upperYBoundField=findViewById(R.id.y_upper_bound_field);
        numberSamplesField=findViewById(R.id.num_sample_points_field);
        answerField=findViewById(R.id.answerView);
    }

    public void xSquaredEquation(View view){
        equationSelected=0;
    }

    public void normalCurveEquation(View view){
        equationSelected=1;
    }

    public void approximateArea(View view){
        if(equationSelected==0||equationSelected==1){
            calculateArea();
        }else if(equationSelected==2){
            noEquationSelectedToast= Toast.makeText(getApplicationContext(),"You Didn't Select An Equation!", Toast.LENGTH_LONG);
            noEquationSelectedToast.show();
        }else{
            Log.d("Unexpected Value", "approximateArea: equationSelected is not 0, 1, or 2");
        }
    }

    public void calculateArea(){
        int samplesToTake=Integer.parseInt(numberSamplesField.getText().toString());

        double lowerXBound=Double.parseDouble(lowerXBoundField.getText().toString());
        double upperXBound=Double.parseDouble(upperXBoundField.getText().toString());
        double lowerYBound=Double.parseDouble(lowerYBoundField.getText().toString());
        double upperYBound=Double.parseDouble(upperYBoundField.getText().toString());

        int dartsInside=0;

        if (equationSelected==0){
            Log.d("debug", "calculateArea: X Squared running");
            for (int count=1;count<=samplesToTake;count++){
                throwDart(lowerXBound,upperXBound,lowerYBound,upperYBound);
                if (dart[1]<(dart[0]*dart[0])){dartsInside++;}
            }
        }else{
            for (int count=1;count<=samplesToTake;count++){
                throwDart(lowerXBound,upperXBound,lowerYBound,upperYBound);
                if (dart[1]<standardNormalCurve(dart[0])){dartsInside++;}
            }
        }
        double approxArea = ((upperXBound-lowerXBound)*(upperYBound-lowerYBound)*dartsInside)/samplesToTake;
        Log.d("debug", "calculateArea: Darts Inside: "+dartsInside);
        Log.d("debug", "calculateArea: Total Darts: "+samplesToTake);
        Log.d("debug", "calculateArea: X Range:"+(upperXBound-lowerXBound));
        Log.d("debug", "calculateArea: Y Range:"+(upperYBound-lowerYBound));
        Log.d("debug", "calculateArea: Area: "+approxArea);
        updateAnswerView(approxArea);
    }

    public double standardNormalCurve(double x){
        return Math.pow(Math.E,-Math.pow(x,2)/2)/Math.sqrt(2*Math.PI);
    }

    public void throwDart(double lowerXBound, double upperXBound, double lowerYBound, double upperYBound){
        dart[0]=lowerXBound+((upperXBound-lowerXBound)*mRandom.nextDouble());
        dart[1]=lowerYBound+((upperYBound-lowerYBound)*mRandom.nextDouble());
    }

    public void updateAnswerView(double area){
        answerField.setText(getString(R.string.answer_text, Double.toString(area)));
    }
}