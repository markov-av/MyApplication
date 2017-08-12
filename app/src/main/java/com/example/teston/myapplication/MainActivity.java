package com.example.teston.myapplication;

import android.app.*;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity{

    public String [] scope = new String[]{VKScope.GROUPS, VKScope.FRIENDS, VKScope.STATS, VKScope.MESSAGES};
    private ListView listView;
    private Button showMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        VKSdk.login(this, scope);
        showMessage = (Button) findViewById(R.id.showMessage);
        showMessage.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {

                   final VKRequest request = VKApi.messages().get(VKParameters.from(VKApiConst.COUNT, 11));

                       request.executeWithListener(new VKRequest.VKRequestListener() {
                           @Override
                           public void onComplete(VKResponse response) {
                               super.onComplete(response);

                               VKApiGetMessagesResponse getMessagesResponse = (VKApiGetMessagesResponse) response.parsedModel;

                               VKList <VKApiMessage> list = getMessagesResponse.items;
                               ArrayList<String> arrayList = new ArrayList<>();

                               for (VKApiMessage msg : list){
                                   arrayList.add(msg.body);
                               }
                               ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                                       android.R.layout. simple_dropdown_item_1line, arrayList);

                               listView.setAdapter(arrayAdapter);
                           }
                       });


               }
          });


        /*String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println(Arrays.asList(fingerprints));*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                //listView = (ListView) findViewById(R.id.listView);


                //final VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id"));
                final VKRequest request1 = VKApi.groups().get(VKParameters.from(VKApiConst.FIELDS, "name"));
                /*final VKRequest request2 = VKApi.groups().get(VKParameters.from(VKApiConst.COUNT, 10));*/

                request1.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList list = (VKList) response.parsedModel;
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout. simple_dropdown_item_1line, list);

                        listView.setAdapter(arrayAdapter);
                    }
                });
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
