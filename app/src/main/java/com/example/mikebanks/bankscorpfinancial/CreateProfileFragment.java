package com.example.mikebanks.bankscorpfinancial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.mikebanks.bankscorpfinancial.Model.Profile;
import com.example.mikebanks.bankscorpfinancial.Model.db.ApplicationDB;

import java.util.ArrayList;

public class CreateProfileFragment extends Fragment {

    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtCountry;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtPasswordConfirm;

    String regularExpression="^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!])[A-Za-z\\d@$!]{8,}$";

    public CreateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Create Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_profile, container, false);

        edtFirstName = rootView.findViewById(R.id.edt_first_name);
        edtLastName = rootView.findViewById(R.id.edt_last_name);
        edtCountry = rootView.findViewById(R.id.edt_country);
        edtUsername = rootView.findViewById(R.id.edt_username);
        edtPassword = rootView.findViewById(R.id.edt_password);
        edtPasswordConfirm = rootView.findViewById(R.id.edt_password_confirm);
        final Button btnCreateAccount = rootView.findViewById(R.id.btn_create_account);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

                {

                    String username=edtUsername.getText().toString();
                    String password=edtPassword.getText().toString();
                    if(validatePassword(password))
                    {

                        Bundle bundle = new Bundle();
                        bundle.putString("", username);
                        bundle.putString("", password);
                        createProfile();

                    }
                    else {
                        Toast.makeText(btnCreateAccount.getContext(), "Password must have atleast 1caps,1symbol,1number", Toast.LENGTH_LONG).show();
                    }
                    }


        });

        ((LaunchActivity) getActivity()).showUpButton();

        return rootView;
    }

    /**
     * method used to create an account
     */
    public boolean validatePassword(String password)
    {
        Pattern pattern= Pattern.compile(regularExpression);
        Matcher matcher=pattern.matcher(password);
        return matcher.matches();
    }
    private void createProfile() {

        ApplicationDB applicationDb = new ApplicationDB( getActivity().getApplicationContext());
        ArrayList<Profile> profiles = applicationDb.getAllProfiles();
        boolean usernameTaken = false;

        for (int iProfile = 0; iProfile < profiles.size(); iProfile++) {
            if (edtUsername.getText().toString().equals(profiles.get(iProfile).getUsername())) {
                usernameTaken = true;
            }
        }

        if (edtFirstName.getText().toString().equals("") || edtLastName.getText().toString().equals("") || edtCountry.getText().toString().equals("") ||
                edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("") || edtPasswordConfirm.getText().toString().equals("")) {
            Toast.makeText(getActivity(), R.string.fields_blank, Toast.LENGTH_SHORT).show();
        }

        else if (!(edtPassword.getText().toString()).equals(edtPasswordConfirm.getText().toString())) {
            Toast.makeText(getActivity(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
        }
        else if (usernameTaken) {
            Toast.makeText(getActivity(), "A User has already taken that username", Toast.LENGTH_SHORT).show();
        }
        else {
            Profile userProfile = new Profile(edtFirstName.getText().toString(), edtLastName.getText().toString(), edtCountry.getText().toString(),
                    edtUsername.getText().toString(), edtPassword.getText().toString());

            applicationDb.saveNewProfile(userProfile);

            Bundle bundle = new Bundle();
            bundle.putString("Username", userProfile.getUsername());
            bundle.putString("Password", userProfile.getPassword());

            ((LaunchActivity) getActivity()).profileCreated(bundle);

        }
    }
}
