package com.example.apnabazzar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }

    private TextView AlreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private EditText email;
    private EditText fullName;
    private  EditText Password;
    private EditText confirmPassword;
    private Button signUpBtn;
    private ImageButton closeBtn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);
        AlreadyHaveAnAccount = view.findViewById(R.id.tv_already_have_an_acc);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);

        email= view.findViewById(R.id.sign_up_email);
        fullName=view.findViewById(R.id.sign_up_name);
        Password = view.findViewById(R.id.sign_up_pass);
        confirmPassword= view.findViewById(R.id.sign_up_confirmpass);

        closeBtn= view.findViewById(R.id.sign_up_close_btn);
        signUpBtn=view.findViewById(R.id.signup_btn);
        progressBar=view.findViewById(R.id.shipped_delivered_progress);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        if (disableCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }
        else {
            closeBtn.setVisibility(View.VISIBLE);
        }

        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SigninFragment());
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }
    private  void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId() , fragment);
        fragmentTransaction.commit();
    }

    private  void  checkInputs()
    {
        if (!TextUtils.isEmpty(email.getText()))
        {
            if (!TextUtils.isEmpty(fullName.getText()))
            {
                if (!TextUtils.isEmpty(Password.getText()) && Password.length() >= 8)
                {
                   if (!TextUtils.isEmpty(confirmPassword.getText()))
                   {
                       signUpBtn.setEnabled(true);
                       signUpBtn.setTextColor(Color.rgb(255,255,255));
                   }
                   else
                   {
                       signUpBtn.setEnabled(false);
                       signUpBtn.setTextColor(Color.argb(50,255,255,255));
                   }
                }
                else
                {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50,255,255,255));
                }
            }
            else
            {
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else
        {
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword()
    {
        if (email.getText().toString().matches(emailPattern))
        {
            if (Password.getText().toString().equals(confirmPassword.getText().toString()))
            {
                progressBar.setVisibility(View.VISIBLE);
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString() , Password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Map<String,Object> userdata = new HashMap<>();
                                    userdata.put("fullname" , fullName.getText().toString());
                                    userdata.put("email",email.getText().toString());
                                    userdata.put("profile","");

                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                                                .collection("USER_DATA");

                                                        //MAPS
                                                        Map<String,Object> wishlistMap = new HashMap<>();
                                                        wishlistMap.put("list_size" ,(long) 0);

                                                        Map<String,Object> ratingsMap = new HashMap<>();
                                                        ratingsMap.put("list_size" ,(long) 0);

                                                        Map<String,Object> cartMap = new HashMap<>();
                                                        cartMap.put("list_size" ,(long) 0);

                                                        Map<String,Object> myAddressesMap = new HashMap<>();
                                                        myAddressesMap.put("list_size" ,(long) 0);

                                                        Map<String,Object> notificationsMap = new HashMap<>();
                                                        notificationsMap.put("list_size" ,(long) 0);

                                                        //MAPS


                                                        final List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");
                                                        documentNames.add("MY_CART");
                                                        documentNames.add("MY_ADDRESSES");
                                                        documentNames.add("MY_NOTIFICATIONS");

                                                        List< Map<String,Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(wishlistMap);
                                                        documentFields.add(ratingsMap);
                                                        documentFields.add(cartMap);
                                                        documentFields.add(myAddressesMap);
                                                        documentFields.add(notificationsMap);

                                                        for (int x =0 ; x < documentNames.size(); x++){

                                                            final int finalX = x;
                                                            userDataReference.document(documentNames.get(x))
                                                                    .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        if (finalX == documentNames.size()-1) {
                                                                            mainIntent();
                                                                        }
                                                                    }
                                                                    else {
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        signUpBtn.setEnabled(true);
                                                                        signUpBtn.setTextColor(Color.rgb(255,255,255));
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(getActivity(), error ,Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                    else
                                                    {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error ,Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signUpBtn.setEnabled(true);
                                    signUpBtn.setTextColor(Color.rgb(255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error ,Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
            else
            {
                confirmPassword.setError("Password doesn't matched !");
            }
        }
        else
        {
            email.setError("Invalid Email ");
        }
    }

    private  void mainIntent()
    {
        if (disableCloseBtn) {
            disableCloseBtn = false;
        } else {
            Intent mainintent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainintent);
        }
        getActivity().finish();
    }
}