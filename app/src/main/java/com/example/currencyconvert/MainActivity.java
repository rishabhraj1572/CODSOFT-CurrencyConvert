package com.example.currencyconvert;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        HashMap codeMap = new HashMap<String,String>();
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray jsonArrays= new JSONArray(readjson());

            for (int i =0 ; i<jsonArrays.length(); i++){
                JSONObject c_code = jsonArrays.getJSONObject(i);
                String country_code = c_code.getString("code");
                String countryname = c_code.getString("countryname");
                String curr_name = c_code.getString("name");
                String symbol1 = c_code.getString("symbol");
                String  symbol = String.valueOf(Html.fromHtml(symbol1, Html.FROM_HTML_MODE_LEGACY));
                list.add(symbol+" - "+country_code+"("+curr_name+")"+", "+countryname);
                codeMap.put(symbol+" - "+country_code+"("+curr_name+")"+", "+countryname,country_code);
            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        AutoCompleteTextView fromCurr_txt = findViewById(R.id.srcCurr);
        String fr = fromCurr_txt.getText().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line, // Built-in layout for suggestions
                list
        );

        // Set the adapter for AutoCompleteTextView
        fromCurr_txt.setAdapter(adapter);
        AutoCompleteTextView toCurr_txt = findViewById(R.id.toCurr);
        String tooo= toCurr_txt.getText().toString();
        toCurr_txt.setAdapter(adapter);

        EditText fromAmt_txt = findViewById(R.id.srcAmt);

        TextView finalAmt = findViewById(R.id.finalAmt);
        Button convertBtn = findViewById(R.id.button);

        convertBtn.setOnClickListener(v -> {
            try{
                String fromCurr = (String) codeMap.get(fromCurr_txt.getText().toString());
                String from = fromCurr.toLowerCase();
                String toCurr = (String) codeMap.get(toCurr_txt.getText().toString());
                String to = toCurr.toLowerCase();
                String url = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/"+from+"/"+to+".json";
                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        //.addHeader("","")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    String json;
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            json = response.body().string();
                            System.out.println(json);
                            try {
                                if(json!=null){
                                    JSONObject jsonObject = new JSONObject(json);
                                    double inrValue = jsonObject.getDouble(to);
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    try{
                                        int amt = Integer.parseInt(fromAmt_txt.getText().toString());
                                        double total = amt*inrValue;
                                        String formattedInrValue = decimalFormat.format(total);
                                        finalAmt.setText(formattedInrValue);
                                    }catch (Exception e){
                                        String formattedInrValue = decimalFormat.format(inrValue);
                                        finalAmt.setText(formattedInrValue);
                                        e.printStackTrace();
                                    }


                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                });
            }catch (Exception e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private String readjson() {
            String json = "[\n" +
                    "  {\n" +
                    "    \"code\": \"AFN\",\n" +
                    "    \"countryname\": \"Afghanistan\",\n" +
                    "    \"name\": \"Afghanistan Afghani\",\n" +
                    "    \"symbol\": \"&#1547;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"ARS\",\n" +
                    "    \"countryname\": \"Argentina\",\n" +
                    "    \"name\": \"Argentine Peso\",\n" +
                    "    \"symbol\": \"&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"AWG\",\n" +
                    "    \"countryname\": \"Aruba\",\n" +
                    "    \"name\": \"Aruban florin\",\n" +
                    "    \"symbol\": \"&#402;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"AUD\",\n" +
                    "    \"countryname\": \"Australia\",\n" +
                    "    \"name\": \"Australian Dollar\",\n" +
                    "    \"symbol\": \"&#65;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"AZN\",\n" +
                    "    \"countryname\": \"Azerbaijan\",\n" +
                    "    \"name\": \"Azerbaijani Manat\",\n" +
                    "    \"symbol\": \"&#8380;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BSD\",\n" +
                    "    \"countryname\": \"The Bahamas\",\n" +
                    "    \"name\": \"Bahamas Dollar\",\n" +
                    "    \"symbol\": \"&#66;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BBD\",\n" +
                    "    \"countryname\": \"Barbados\",\n" +
                    "    \"name\": \"Barbados Dollar\",\n" +
                    "    \"symbol\": \"&#66;&#100;&#115;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BDT\",\n" +
                    "    \"countryname\": \"People's Republic of Bangladesh\",\n" +
                    "    \"name\": \"Bangladeshi taka\",\n" +
                    "    \"symbol\": \"&#2547;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BYN\",\n" +
                    "    \"countryname\": \"Belarus\",\n" +
                    "    \"name\": \"Belarus Ruble\",\n" +
                    "    \"symbol\": \"&#66;&#114;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BZD\",\n" +
                    "    \"countryname\": \"Belize\",\n" +
                    "    \"name\": \"Belize Dollar\",\n" +
                    "    \"symbol\": \"&#66;&#90;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BMD\",\n" +
                    "    \"countryname\": \"British Overseas Territory of Bermuda\",\n" +
                    "    \"name\": \"Bermudian Dollar\",\n" +
                    "    \"symbol\": \"&#66;&#68;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BOP\",\n" +
                    "    \"countryname\": \"Bolivia\",\n" +
                    "    \"name\": \"Boliviano\",\n" +
                    "    \"symbol\": \"&#66;&#115;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BAM\",\n" +
                    "    \"countryname\": \"Bosnia and Herzegovina\",\n" +
                    "    \"name\": \"Bosnia-Herzegovina Convertible Marka\",\n" +
                    "    \"symbol\": \"&#75;&#77;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BWP\",\n" +
                    "    \"countryname\": \"Botswana\",\n" +
                    "    \"name\": \"Botswana pula\",\n" +
                    "    \"symbol\": \"&#80;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BGN\",\n" +
                    "    \"countryname\": \"Bulgaria\",\n" +
                    "    \"name\": \"Bulgarian lev\",\n" +
                    "    \"symbol\": \"&#1083;&#1074;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BRL\",\n" +
                    "    \"countryname\": \"Brazil\",\n" +
                    "    \"name\": \"Brazilian real\",\n" +
                    "    \"symbol\": \"&#82;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"BND\",\n" +
                    "    \"countryname\": \"Sultanate of Brunei\",\n" +
                    "    \"name\": \"Brunei dollar\",\n" +
                    "    \"symbol\": \"&#66;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"KHR\",\n" +
                    "    \"countryname\": \"Cambodia\",\n" +
                    "    \"name\": \"Cambodian riel\",\n" +
                    "    \"symbol\": \"&#6107;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"CAD\",\n" +
                    "    \"countryname\": \"Canada\",\n" +
                    "    \"name\": \"Canadian dollar\",\n" +
                    "    \"symbol\": \"&#67;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"KYD\",\n" +
                    "    \"countryname\": \"Cayman Islands\",\n" +
                    "    \"name\": \"Cayman Islands dollar\",\n" +
                    "    \"symbol\": \"&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"CLP\",\n" +
                    "    \"countryname\": \"Chile\",\n" +
                    "    \"name\": \"Chilean peso\",\n" +
                    "    \"symbol\": \"&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"CNY\",\n" +
                    "    \"countryname\": \"China\",\n" +
                    "    \"name\": \"Chinese Yuan Renminbi\",\n" +
                    "    \"symbol\": \"&#165;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"COP\",\n" +
                    "    \"countryname\": \"Colombia\",\n" +
                    "    \"name\": \"Colombian peso\",\n" +
                    "    \"symbol\": \"&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"CRC\",\n" +
                    "    \"countryname\": \"Costa Rica\",\n" +
                    "    \"name\": \"Costa Rican colón\",\n" +
                    "    \"symbol\": \"&#8353;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"HRK\",\n" +
                    "    \"countryname\": \"Croatia\",\n" +
                    "    \"name\": \"Croatian kuna\",\n" +
                    "    \"symbol\": \"&#107;&#110;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"CUP\",\n" +
                    "    \"countryname\": \"Cuba\",\n" +
                    "    \"name\": \"Cuban peso\",\n" +
                    "    \"symbol\": \"&#8369;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"CZK\",\n" +
                    "    \"countryname\": \"Czech Republic\",\n" +
                    "    \"name\": \"Czech koruna\",\n" +
                    "    \"symbol\": \"&#75;&#269;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"DKK\",\n" +
                    "    \"countryname\": \"Denmark, Greenland, and the Faroe Islands\",\n" +
                    "    \"name\": \"Danish krone\",\n" +
                    "    \"symbol\": \"&#107;&#114;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"DOP\",\n" +
                    "    \"countryname\": \"Dominican Republic\",\n" +
                    "    \"name\": \"Dominican peso\",\n" +
                    "    \"symbol\": \"&#82;&#68;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"XCD\",\n" +
                    "    \"countryname\": \"Antigua and Barbuda, Commonwealth of Dominica, Grenada, Montserrat, St. Kitts and Nevis, Saint Lucia and St. Vincent and the Grenadines\",\n" +
                    "    \"name\": \"Eastern Caribbean dollar\",\n" +
                    "    \"symbol\": \"&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"EGP\",\n" +
                    "    \"countryname\": \"Egypt\",\n" +
                    "    \"name\": \"Egyptian pound\",\n" +
                    "    \"symbol\": \"&#163;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SVC\",\n" +
                    "    \"countryname\": \"El Salvador\",\n" +
                    "    \"name\": \"Salvadoran colón\",\n" +
                    "    \"symbol\": \"&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"EEK\",\n" +
                    "    \"countryname\": \"Estonia\",\n" +
                    "    \"name\": \"Estonian kroon\",\n" +
                    "    \"symbol\": \"&#75;&#114;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"EUR\",\n" +
                    "    \"countryname\": \"European Union, Italy, Belgium, Bulgaria, Croatia, Cyprus, Czechia, Denmark, Estonia, Finland, France, Germany, Greece, Hungary, Ireland, Latvia, Lithuania, Luxembourg, Malta, Netherlands, Poland, Portugal, Romania, Slovakia, Slovenia, Spain, Sweden\",\n" +
                    "    \"name\": \"Euro\",\n" +
                    "    \"symbol\": \"&#8364;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"FKP\",\n" +
                    "    \"countryname\": \"Falkland Islands\",\n" +
                    "    \"name\": \"Falkland Islands (Malvinas) Pound\",\n" +
                    "    \"symbol\": \"&#70;&#75;&#163;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"FJD\",\n" +
                    "    \"countryname\": \"Fiji\",\n" +
                    "    \"name\": \"Fijian dollar\",\n" +
                    "    \"symbol\": \"&#70;&#74;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"GHC\",\n" +
                    "    \"countryname\": \"Ghana\",\n" +
                    "    \"name\": \"Ghanaian cedi\",\n" +
                    "    \"symbol\": \"&#71;&#72;&#162;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"GIP\",\n" +
                    "    \"countryname\": \"Gibraltar\",\n" +
                    "    \"name\": \"Gibraltar pound\",\n" +
                    "    \"symbol\": \"&#163;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"GTQ\",\n" +
                    "    \"countryname\": \"Guatemala\",\n" +
                    "    \"name\": \"Guatemalan quetzal\",\n" +
                    "    \"symbol\": \"&#81;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"GGP\",\n" +
                    "    \"countryname\": \"Guernsey\",\n" +
                    "    \"name\": \"Guernsey pound\",\n" +
                    "    \"symbol\": \"&#81;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"GYD\",\n" +
                    "    \"countryname\": \"Guyana\",\n" +
                    "    \"name\": \"Guyanese dollar\",\n" +
                    "    \"symbol\": \"&#71;&#89;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"HNL\",\n" +
                    "    \"countryname\": \"Honduras\",\n" +
                    "    \"name\": \"Honduran lempira\",\n" +
                    "    \"symbol\": \"&#76;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"HKD\",\n" +
                    "    \"countryname\": \"Hong Kong\",\n" +
                    "    \"name\": \"Hong Kong dollar\",\n" +
                    "    \"symbol\": \"&#72;&#75;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"HUF\",\n" +
                    "    \"countryname\": \"Hungary\",\n" +
                    "    \"name\": \"Hungarian forint\",\n" +
                    "    \"symbol\": \"&#70;&#116;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"ISK\",\n" +
                    "    \"countryname\": \"Iceland\",\n" +
                    "    \"name\": \"Icelandic króna\",\n" +
                    "    \"symbol\": \"&#237;&#107;&#114;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"INR\",\n" +
                    "    \"countryname\": \"India\",\n" +
                    "    \"name\": \"Indian rupee\",\n" +
                    "    \"symbol\": \"&#8377;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"IDR\",\n" +
                    "    \"countryname\": \"Indonesia\",\n" +
                    "    \"name\": \"Indonesian rupiah\",\n" +
                    "    \"symbol\": \"&#82;&#112;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"IRR\",\n" +
                    "    \"countryname\": \"Iran\",\n" +
                    "    \"name\": \"Iranian rial\",\n" +
                    "    \"symbol\": \"&#65020;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"IMP\",\n" +
                    "    \"countryname\": \"Isle of Man\",\n" +
                    "    \"name\": \"Manx pound\",\n" +
                    "    \"symbol\": \"&#163;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"ILS\",\n" +
                    "    \"countryname\": \"Israel\",\n" +
                    "    \"name\": \"Israeli Shekel\",\n" +
                    "    \"symbol\": \"&#8362;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"JMD\",\n" +
                    "    \"countryname\": \"Jamaica\",\n" +
                    "    \"name\": \"Jamaican dollar\",\n" +
                    "    \"symbol\": \"&#74;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"JPY\",\n" +
                    "    \"countryname\": \"Japan\",\n" +
                    "    \"name\": \"Japanese yen\",\n" +
                    "    \"symbol\": \"&#165;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"JEP\",\n" +
                    "    \"countryname\": \"Jersey\",\n" +
                    "    \"name\": \"Jersey pound\",\n" +
                    "    \"symbol\": \"&#163;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"KZT\",\n" +
                    "    \"countryname\": \"Kazakhstan\",\n" +
                    "    \"name\": \"Kazakhstani tenge\",\n" +
                    "    \"symbol\": \"&#8376;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"KPW\",\n" +
                    "    \"countryname\": \"North Korea\",\n" +
                    "    \"name\": \"North Korean won\",\n" +
                    "    \"symbol\": \"&#8361;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"KPW\",\n" +
                    "    \"countryname\": \"South Korea\",\n" +
                    "    \"name\": \"South Korean won\",\n" +
                    "    \"symbol\": \"&#8361;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"KGS\",\n" +
                    "    \"countryname\": \"Kyrgyz Republic\",\n" +
                    "    \"name\": \"Kyrgyzstani som\",\n" +
                    "    \"symbol\": \"&#1083;&#1074;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"LAK\",\n" +
                    "    \"countryname\": \"Laos\",\n" +
                    "    \"name\": \"Lao kip\",\n" +
                    "    \"symbol\": \"&#8365;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"LAK\",\n" +
                    "    \"countryname\": \"Laos\",\n" +
                    "    \"name\": \"Latvian lats\",\n" +
                    "    \"symbol\": \"&#8364;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"LVL\",\n" +
                    "    \"countryname\": \"Laos\",\n" +
                    "    \"name\": \"Latvian lats\",\n" +
                    "    \"symbol\": \"&#8364;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"LBP\",\n" +
                    "    \"countryname\": \"Lebanon\",\n" +
                    "    \"name\": \"Lebanese pound\",\n" +
                    "    \"symbol\": \"&#76;&#163;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"LRD\",\n" +
                    "    \"countryname\": \"Liberia\",\n" +
                    "    \"name\": \"Liberian dollar\",\n" +
                    "    \"symbol\": \"&#76;&#68;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"LTL\",\n" +
                    "    \"countryname\": \"Lithuania\",\n" +
                    "    \"name\": \"Lithuanian litas\",\n" +
                    "    \"symbol\": \"&#8364;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"MKD\",\n" +
                    "    \"countryname\": \"North Macedonia\",\n" +
                    "    \"name\": \"Macedonian denar\",\n" +
                    "    \"symbol\": \"&#1076;&#1077;&#1085;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"MYR\",\n" +
                    "    \"countryname\": \"Malaysia\",\n" +
                    "    \"name\": \"Malaysian ringgit\",\n" +
                    "    \"symbol\": \"&#82;&#77;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"MUR\",\n" +
                    "    \"countryname\": \"Mauritius\",\n" +
                    "    \"name\": \"Mauritian rupee\",\n" +
                    "    \"symbol\": \"&#82;&#115;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"MXN\",\n" +
                    "    \"countryname\": \"Mexico\",\n" +
                    "    \"name\": \"Mexican peso\",\n" +
                    "    \"symbol\": \"&#77;&#101;&#120;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"MNT\",\n" +
                    "    \"countryname\": \"Mongolia\",\n" +
                    "    \"name\": \"Mongolian tögrög\",\n" +
                    "    \"symbol\": \"&#8366;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"MZN\",\n" +
                    "    \"countryname\": \"Mozambique\",\n" +
                    "    \"name\": \"Mozambican metical\",\n" +
                    "    \"symbol\": \"&#77;&#84;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"NAD\",\n" +
                    "    \"countryname\": \"Namibia\",\n" +
                    "    \"name\": \"Namibian dollar\",\n" +
                    "    \"symbol\": \"&#78;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"NPR\",\n" +
                    "    \"countryname\": \"Nepal\",\n" +
                    "    \"name\": \"Nepalese rupee\",\n" +
                    "    \"symbol\": \"&#82;&#115;&#46;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"ANG\",\n" +
                    "    \"countryname\": \"Curaçao and Sint Maarten\",\n" +
                    "    \"name\": \"Netherlands Antillean guilder\",\n" +
                    "    \"symbol\": \"&#402;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"NZD\",\n" +
                    "    \"countryname\": \"New Zealand\",\n" +
                    "    \"name\": \"New Zealand dollar\",\n" +
                    "    \"symbol\": \"&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"NIO\",\n" +
                    "    \"countryname\": \"Nicaragua\",\n" +
                    "    \"name\": \"Nicaraguan córdoba\",\n" +
                    "    \"symbol\": \"&#67;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"NGN\",\n" +
                    "    \"countryname\": \"Nigeria\",\n" +
                    "    \"name\": \"Nigerian naira\",\n" +
                    "    \"symbol\": \"&#8358;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"NOK\",\n" +
                    "    \"countryname\": \"Norway\",\n" +
                    "    \"name\": \"Norwegian krone\",\n" +
                    "    \"symbol\": \"&#107;&#114;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"OMR\",\n" +
                    "    \"countryname\": \"Oman\",\n" +
                    "    \"name\": \"Omani rial\",\n" +
                    "    \"symbol\": \"&#65020;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"PKR\",\n" +
                    "    \"countryname\": \"Pakistan\",\n" +
                    "    \"name\": \"Pakistani rupee\",\n" +
                    "    \"symbol\": \"&#82;&#115;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"PAB\",\n" +
                    "    \"countryname\": \"Panama\",\n" +
                    "    \"name\": \"Panamanian balboa\",\n" +
                    "    \"symbol\": \"&#66;&#47;&#46;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"PYG\",\n" +
                    "    \"countryname\": \"Paraguay\",\n" +
                    "    \"name\": \"Paraguayan Guaraní\",\n" +
                    "    \"symbol\": \"&#8370;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"PEN\",\n" +
                    "    \"countryname\": \"Peru\",\n" +
                    "    \"name\": \"Sol\",\n" +
                    "    \"symbol\": \"&#83;&#47;&#46;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"PHP\",\n" +
                    "    \"countryname\": \"Philippines\",\n" +
                    "    \"name\": \"Philippine peso\",\n" +
                    "    \"symbol\": \"&#8369;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"PLN\",\n" +
                    "    \"countryname\": \"Poland\",\n" +
                    "    \"name\": \"Polish złoty\",\n" +
                    "    \"symbol\": \"&#122;&#322;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"QAR\",\n" +
                    "    \"countryname\": \"State of Qatar\",\n" +
                    "    \"name\": \"Qatari Riyal\",\n" +
                    "    \"symbol\": \"&#65020;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"RON\",\n" +
                    "    \"countryname\": \"Romania\",\n" +
                    "    \"name\": \"Romanian leu (Leu românesc)\",\n" +
                    "    \"symbol\": \"&#76;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"RUB\",\n" +
                    "    \"countryname\": \"Russia\",\n" +
                    "    \"name\": \"Russian ruble\",\n" +
                    "    \"symbol\": \"&#8381;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SHP\",\n" +
                    "    \"countryname\": \"Saint Helena\",\n" +
                    "    \"name\": \"Saint Helena pound\",\n" +
                    "    \"symbol\": \"&#163;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SAR\",\n" +
                    "    \"countryname\": \"Saudi Arabia\",\n" +
                    "    \"name\": \"Saudi riyal\",\n" +
                    "    \"symbol\": \"&#65020;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"RSD\",\n" +
                    "    \"countryname\": \"Serbia\",\n" +
                    "    \"name\": \"Serbian dinar\",\n" +
                    "    \"symbol\": \"&#100;&#105;&#110;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SCR\",\n" +
                    "    \"countryname\": \"Seychelles\",\n" +
                    "    \"name\": \"Seychellois rupee\",\n" +
                    "    \"symbol\": \"&#82;&#115;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SGD\",\n" +
                    "    \"countryname\": \"Singapore\",\n" +
                    "    \"name\": \"Singapore dollar\",\n" +
                    "    \"symbol\": \"&#83;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SBD\",\n" +
                    "    \"countryname\": \"Solomon Islands\",\n" +
                    "    \"name\": \"Solomon Islands dollar\",\n" +
                    "    \"symbol\": \"&#83;&#73;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SOS\",\n" +
                    "    \"countryname\": \"Somalia\",\n" +
                    "    \"name\": \"Somali shilling\",\n" +
                    "    \"symbol\": \"&#83;&#104;&#46;&#83;&#111;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"ZAR\",\n" +
                    "    \"countryname\": \"South Africa\",\n" +
                    "    \"name\": \"South African rand\",\n" +
                    "    \"symbol\": \"&#82;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"LKR\",\n" +
                    "    \"countryname\": \"Sri Lanka\",\n" +
                    "    \"name\": \"Sri Lankan rupee\",\n" +
                    "    \"symbol\": \"&#82;&#115;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SEK\",\n" +
                    "    \"countryname\": \"Sweden\",\n" +
                    "    \"name\": \"Swedish krona\",\n" +
                    "    \"symbol\": \"&#107;&#114;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"CHF\",\n" +
                    "    \"countryname\": \"Switzerland\",\n" +
                    "    \"name\": \"Swiss franc\",\n" +
                    "    \"symbol\": \"&#67;&#72;&#102;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SRD\",\n" +
                    "    \"countryname\": \"Suriname\",\n" +
                    "    \"name\": \"Suriname Dollar\",\n" +
                    "    \"symbol\": \"&#83;&#114;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"SYP\",\n" +
                    "    \"countryname\": \"Syria\",\n" +
                    "    \"name\": \"Syrian pound\",\n" +
                    "    \"symbol\": \"&#163;&#83;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"TWD\",\n" +
                    "    \"countryname\": \"Taiwan\",\n" +
                    "    \"name\": \"New Taiwan dollar\",\n" +
                    "    \"symbol\": \"&#78;&#84;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"THB\",\n" +
                    "    \"countryname\": \"Thailand\",\n" +
                    "    \"name\": \"Thai baht\",\n" +
                    "    \"symbol\": \"&#3647;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"TTD\",\n" +
                    "    \"countryname\": \"Trinidad and Tobago\",\n" +
                    "    \"name\": \"Trinidad and Tobago dollar\",\n" +
                    "    \"symbol\": \"&#84;&#84;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"TRY\",\n" +
                    "    \"countryname\": \"Turkey\",\n" +
                    "    \"name\": \"Turkey Lira\",\n" +
                    "    \"symbol\": \"&#8378;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"TVD\",\n" +
                    "    \"countryname\": \"Tuvalu\",\n" +
                    "    \"name\": \"Tuvaluan dollar\",\n" +
                    "    \"symbol\": \"&#84;&#86;&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"UAH\",\n" +
                    "    \"countryname\": \"Ukraine\",\n" +
                    "    \"name\": \"Ukrainian hryvnia\",\n" +
                    "    \"symbol\": \"&#8372;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"GBP\",\n" +
                    "    \"countryname\": \"United Kingdom\",\n" +
                    "    \"name\": \"Pound sterling\",\n" +
                    "    \"symbol\": \"&#163;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"UGX\",\n" +
                    "    \"countryname\": \"Uganda\",\n" +
                    "    \"name\": \"Ugandan shilling\",\n" +
                    "    \"symbol\": \"&#85;&#83;&#104;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"USD\",\n" +
                    "    \"countryname\": \"United States\",\n" +
                    "    \"name\": \"United States dollar\",\n" +
                    "    \"symbol\": \"&#36;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"UYU\",\n" +
                    "    \"countryname\": \"Uruguay\",\n" +
                    "    \"name\": \"Uruguayan peso\",\n" +
                    "    \"symbol\": \"&#36;&#85;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"UZS\",\n" +
                    "    \"countryname\": \"Uzbekistan\",\n" +
                    "    \"name\": \"Uzbekistani som\",\n" +
                    "    \"symbol\": \"&#1083;&#1074;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"VEF\",\n" +
                    "    \"countryname\": \"Venezuela\",\n" +
                    "    \"name\": \"Venezuelan bolívar\",\n" +
                    "    \"symbol\": \"&#66;&#115;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"VND\",\n" +
                    "    \"countryname\": \"Vietnam\",\n" +
                    "    \"name\": \"Vietnamese đồng\",\n" +
                    "    \"symbol\": \"&#8363;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"YER\",\n" +
                    "    \"countryname\": \"Yemen\",\n" +
                    "    \"name\": \"Yemeni rial\",\n" +
                    "    \"symbol\": \"&#65020;\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"code\": \"ZWD\",\n" +
                    "    \"countryname\": \"Zimbabwe\",\n" +
                    "    \"name\": \"Zimbabwean dollar\",\n" +
                    "    \"symbol\": \"&#90;&#36;\"\n" +
                    "  }\n" +
                    "]\n";

            return json;
    }
}