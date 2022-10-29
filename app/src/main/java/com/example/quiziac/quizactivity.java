package com.example.quiziac;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quiziac.databinding.ActivityQuizactivityBinding;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class quizactivity extends AppCompatActivity {

    ActivityQuizactivityBinding binding;

    ArrayList<Question> questions;

    int index = 0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswer = 0;
    ImageView bookmark;
    String FeaturesId,catId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questions = new ArrayList<>();

        database = FirebaseFirestore.getInstance();

        catId = getIntent().getStringExtra("catId");
        FeaturesId = getIntent().getStringExtra("FeaturesId");

        //bookmark = findViewById(R.id.bookmark);


        Random random = new Random();
        final int rand = random.nextInt(30); //bound show total no of questions available in db

        database.collection("categories")
                .document(catId).collection("featuresList")
                .document(FeaturesId).collection("questions")
                .whereGreaterThanOrEqualTo("index",rand)
                        .orderBy("index").limit(8).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if(queryDocumentSnapshots.getDocuments().size()<8)
                            {
                                database.collection("categories")
                                        .document(catId).collection("featuresList")
                                        .document(FeaturesId).collection("questions")
                                        .whereLessThanOrEqualTo("index",rand)
                                        .orderBy("index").limit(8).get().addOnSuccessListener(queryDocumentSnapshots1 -> {

                                                for(DocumentSnapshot snapshot: queryDocumentSnapshots1){
                                                    Question question = snapshot.toObject(Question.class);
                                                    questions.add(question);
                                                }
                                            setNextQuestion();
                                        });
                            }
                            else{
                                for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                                    Question question = snapshot.toObject(Question.class);
                                    questions.add(question);
                                }
                                setNextQuestion();
                            }
                        });
        resetTimer();

//        bookmark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //addToBookmark();
//            }
//        });

    }
    void resetTimer(){
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void showAnswer(){
        if(question.getAnswer().equals(binding.option1.getText().toString())){
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else if(question.getAnswer().equals(binding.option2.getText().toString())){
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else if(question.getAnswer().equals(binding.option3.getText().toString())){
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else if(question.getAnswer().equals(binding.option4.getText().toString())){
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
    }

    @SuppressLint("DefaultLocale")
    void setNextQuestion(){
        //resetTimer();
        if(timer !=null)
            timer.cancel();

        assert timer != null;
        timer.start();
        if(index < questions.size()){
            binding.questionCounter.setText(String.format("%d/%d",(index+1),questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
//            if(question.getBookmarked())
//            {
//                bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
//            }
//            else
//            {
//                bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
//            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())){
            correctAnswer++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else
        {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view){
        switch (view.getId()){
            case R.id.option1:
            case R.id.option2:
            case R.id.option3:
            case R.id.option4:
                if(timer !=null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;

            case R.id.next:
                reset();
                if(index < (questions.size()-1)){
                    index++;
                    setNextQuestion();//resetTimer();

                }
                else
                {
                    //Toast.makeText(this, "Quiz finished", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(quizactivity.this,ResultActivity.class);
                    intent.putExtra("correct",correctAnswer);
                    intent.putExtra("total",questions.size());
                    startActivity(intent);
                }
                break;

        }
    }
    public void onClickBack(View view) {
        super.onBackPressed();
    }



//    public void addToBookmark()
//    {
//        if(question.getBookmarked())
//        {
//            //remove from bookmark list
//            question.setBookmarked(false);
////            database.collection("categories")
////                    .document(catId).collection("featuresList")
////                    .document(FeaturesId).collection("questions")
////                    .document(FirebaseAuth.getInstance().getUid()).update("bookmarked",false);
//            bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
//        }
//        else
//        {
//            //add to bookmark list
//            question.setBookmarked(true);
////            database.collection("categories")
////                    .document(catId).collection("featuresList")
////                    .document(FeaturesId).collection("questions")
////                    .document(FirebaseAuth.getInstance().getUid()).update("bookmarked",true);
//            bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
//        }
//    }
}