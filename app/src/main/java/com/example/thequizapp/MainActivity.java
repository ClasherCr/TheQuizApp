package com.example.thequizapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thequizapp.databinding.ActivityMainBinding;
import com.example.thequizapp.model.Question;
import com.example.thequizapp.model.QuestionList;
import com.example.thequizapp.viewmodel.QuizViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    QuizViewModel quizViewModel;
    List<Question> questionsList;

    static int result = 0;
    static int totalQuestions = 0;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main
        );

        result = 0;
        totalQuestions = 0;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        quizViewModel = new ViewModelProvider(this)
                .get(QuizViewModel.class);
        DisplayFirstQuestion();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void DisplayFirstQuestion() {
        quizViewModel.getQuestionListLiveData().observe(this, new Observer<QuestionList>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(QuestionList questions) {
                questionsList = questions;

                binding.txtQuestion.setText("Question 1: " + questionsList.get(0).getQuestion());
                binding.radio1.setText(questions.get(0).getOption1());
                binding.radio2.setText(questions.get(0).getOption2());
                binding.radio3.setText(questions.get(0).getOption3());
                binding.radio4.setText(questions.get(0).getOption4());
            }
        });
        }

        @SuppressLint("SetTextI18n")
        public void DisplayNextQuestions(){
            int selectedOption = binding.radiogroup.getCheckedRadioButtonId();
            if (selectedOption != -1){
                RadioButton radioButton = findViewById(selectedOption);

                if ((questionsList.size() - i) > 0){
                    totalQuestions = questionsList.size();

                    if (radioButton.getText().toString().equals(questionsList.get(i).getCorrectOption()
                    )){
                        result++;
                        binding.txtQuestion.setText("Correct Answer : "+result);
                    }
                    if (i ==0){
                        i++;
                    }

                    binding.txtQuestion.setText("Question "+(i+1)+" : "+questionsList.get(i).getQuestion());

                    binding.radio1.setText(questionsList.get(i).getOption1());
                    binding.radio2.setText(questionsList.get(i).getOption2());
                    binding.radio3.setText(questionsList.get(i).getOption3());
                    binding.radio4.setText(questionsList.get(i).getOption4());


                    if (i == (questionsList.size() -1)){
                        binding.btnNext.setText("Finish");
                    }

                    binding.radiogroup.clearCheck();
                    i++;


                }else {
                    if (radioButton.getText().toString().equals(questionsList.get(i=1).getCorrectOption())){
                        result++;
                        binding.txtResult.setText("Correct Answer : "+result);
                    }
                }
            }else {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            }

        }
}

