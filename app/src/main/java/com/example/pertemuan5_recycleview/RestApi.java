package com.example.pertemuan5_recycleview;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.pertemuan5_recycleview.databinding.ActivityRestApiBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RestApi extends AppCompatActivity implements
        View.OnClickListener{
    //declaration variable
    private ActivityRestApiBinding binding;
    String index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//setup view binding
        binding = ActivityRestApiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fetchButton.setOnClickListener(this);
    }
    //onclik button fetch
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fetch_button){
            index = binding.inputId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    //get data using api link
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://coinmap.org/api/v1/venues/")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }
    class DOTask extends AsyncTask<URL, Void, String>{
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            }catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //get data json
        public void parseJson(String data) throws JSONException{
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            }catch (JSONException e){
                e.printStackTrace();
            }

            JSONArray cityArray = jsonObject.getJSONArray("venues");

            for (int i =0; i <cityArray.length(); i++){
                JSONObject obj = cityArray.getJSONObject(i);
                String Sobj = obj.get("id").toString();
                if (Sobj.equals(index)){
                    String id = obj.get("id").toString();
                    binding.resultId.setText(id);
                    String latitude = obj.get("lat").toString();
                    binding.resultLatitude.setText(latitude);
                    String longitude = obj.get("lon").toString();
                    binding.resultLongitude.setText(longitude);
                    String kategori = obj.get("category").toString();
                    binding.resultCategory.setText(kategori);
                    String nama = obj.get("name").toString();
                    binding.resultName.setText(nama);
                    String geolokasi = obj.get("geolocation_degrees").toString();
                    binding.resultGeolocation.setText(geolokasi);
                    break;
                }
                else {
                    binding.resultName.setText("Not Found");
                }
            }
        }
    }
}