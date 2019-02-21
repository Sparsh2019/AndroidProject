package com.sparsh2k19.geekhub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

import static com.sparsh2k19.geekhub.Constants.client_secret;
import static com.sparsh2k19.geekhub.Constants.languages;
import static com.sparsh2k19.geekhub.Constants.server_url;

public class CodePlaygroundActivity extends AppCompatActivity {

    EditText codeView;
    EditText filename;
    TextView output;
    Button run;
    Spinner lang;

    Code code;

    DatabaseReference reference;
    FirebaseUser user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.codeplayground_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save:
                String key;
                if(code == null) {
                    key = reference.push().getKey();
                    code = new Code(key);
                } else {
                    key = code.getReferenceid();
                }
                code.setFilename(filename.getText().toString());
                code.setLang(lang.getSelectedItem().toString());
                code.setSource(codeView.getText().toString());
                reference.child(key).setValue(code);
                break;
            default:
                onBackPressed();

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_playground);

        codeView = findViewById(R.id.code_text);
        filename = findViewById(R.id.file_name);
        output = findViewById(R.id.output);
        run = findViewById(R.id.run);
        lang = findViewById(R.id.lang);

        reference = FirebaseDatabase.getInstance().getReference().child("code");
        user = FirebaseAuth.getInstance().getCurrentUser();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, languages);
        lang.setAdapter(adapter);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.setText("");

                RequestQueue requestQueue = Volley.newRequestQueue(CodePlaygroundActivity.this);

                StringRequest runRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        output.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    protected HashMap<String, String> getParams() {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("client_secret", client_secret);
                        params.put("source", codeView.getText().toString());
                        params.put("lang", lang.getSelectedItem().toString());
                        return params;
                    }
                };

                requestQueue.add(runRequest);
            }
        });
    }
}
