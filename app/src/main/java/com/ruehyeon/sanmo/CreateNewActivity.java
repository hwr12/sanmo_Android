package com.ruehyeon.sanmo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.usermgmt.UserManagement;

import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.ruehyeon.sanmo.models.RuleDatabase;
import com.ruehyeon.sanmo.models.RulesEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateNewActivity extends AppCompatActivity {
    private SessionCallback sessionCallback;
    public static TextView textView;
    public static Dialog dialog;
    private Button dialogBtn;
    List<String> myNameList;
    List<String> myImageList;
    List<String> myUUIDList;
    TextView rule;
    TextView skin;
    CircleImageView pf_pic;
    TextView name;
    int viewholderPos;
    int rulePos;
    int skinPos;
    String p;
    String n;
    String id;
    TextInputEditText bp;
    TextInputEditText bp1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        bp = findViewById(R.id.bpinputText);
        bp1 = findViewById(R.id.bpinputText1);
        myNameList = new ArrayList<String>();
        myImageList = new ArrayList<String>();
        myUUIDList = new ArrayList<String>();
        pf_pic = findViewById(R.id.pf_pic);
        name = findViewById(R.id.counterpart_name);
        rule = findViewById(R.id.rule_textview);
        skin = findViewById(R.id.skin_textview);
        Intent bsIntent = new Intent(this, com.ruehyeon.sanmo.BottomSheetDialog.class);
        bsIntent.putExtra("type", 0);


        //fileBtn_real.setOnClickListener(view -> com.ruehyeon.sanhu.BottomSheetDialog.getInstance().show(getSupportFragmentManager(), "bottomSheet"));







        kakao_login();


        KakaoTalkService.getInstance()
                .requestProfile(new TalkResponseCallback<KakaoTalkProfile>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "???????????? ???????????? ??????");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "????????? ?????? ??????: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "???????????? ????????? ?????? ??????: " + errorResult);
                    }

                    @Override
                    public void onSuccess(KakaoTalkProfile result) {
                        Log.i("KAKAO_API", "???????????? ?????????: " + result.getNickName());
                        Log.i("KAKAO_API", "???????????? ??????????????????: " + result.getProfileImageUrl());
                    }
                });






// ?????? ??????





// ?????? ????????? ?????????
// ????????? ????????? ????????? ?????? ??????????????? [????????? ?????????] ??????


// ?????? ??????????????? ???????????? ?????????




    }

    public void add_counterpart(View v) {
        lookUpFriend();

    }



    public void sendMessage(TemplateParams params){
        Log.d("KAKAO_API", "????????? ?????? ?????? " );


        List<String> uuids = new ArrayList<String>();
        uuids.add("1ufX797r3-bQ5cn7zPrN_c38zfrW49Hl0OfWRg");
        Log.d("KAKAO_API", "uuids: " + uuids.toString());
        //uuids.add("1584124181");
        KakaoTalkService.getInstance()
                .sendMessageToFriends(uuids, params, new TalkResponseCallback<MessageSendResponse>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "???????????? ???????????? ??????");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "????????? ?????? ??????: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "???????????? ????????? ??????: " + errorResult);
                    }

                    @Override
                    public void onSuccess(MessageSendResponse result) {
                        if (result.successfulReceiverUuids() != null) {
                            Log.i("KAKAO_API", "???????????? ????????? ??????");
                            Log.d("KAKAO_API", "????????? ????????? ??????: " + result.successfulReceiverUuids());
                        }
                        if (result.failureInfo() != null) {
                            Log.e("KAKAO_API", "?????? ??????????????? ?????? ????????? ??????");
                            for (MessageFailureInfo failureInfo : result.failureInfo()) {
                                Log.d("KAKAO_API", "code: " + failureInfo.code());
                                Log.d("KAKAO_API", "msg: " + failureInfo.msg());
                                Log.d("KAKAO_API", "failure_uuids: " + failureInfo.receiverUuids());
                            }
                        }
                    }
                });
    }



    public void showDialog(Activity activity){

        dialog = new Dialog(activity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_recycler);

        Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismissDialog();
            }
        });

        Button btndialog2 = (Button) dialog.findViewById(R.id.invite);
        btndialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                inviteFriend();
            }
        });



        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        AdapterRe adapterRe = new AdapterRe(CreateNewActivity.this, myNameList, myImageList, Glide.with(this));
        adapterRe.setOnItemClickListener(new AdapterRe.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Glide.with(getApplicationContext()).load(myImageList.get(position))
                     .centerCrop()
                     .placeholder(R.drawable.ic_action_name)
                     .into(pf_pic);

                name.setText(myNameList.get(position));
                viewholderPos = position;
                myNameList.get(position);


                dismissDialog();


               //p = myImageList.get(position);
                //n = myNameList.get(position);
                //id = myUUIDList.get(position);
                // TODO : ????????? ?????? ???????????? MainActivity?????? ??????.
            }
        }) ;
        recyclerView.setAdapter(adapterRe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        dialog.show();

    }

    public void dismissDialog() {

        dialog.dismiss();
    }

    public void kakao_login() {
        if (sessionCallback == null) {
            sessionCallback = new SessionCallback();
            Session.getCurrentSession().addCallback(sessionCallback);
        }
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);

    }



    public void sendImage(){
        File imageFile = new File("{?????? ????????? ?????? ??????}");
        KakaoLinkService.getInstance()
                .uploadImage(this, true,imageFile, new ResponseCallback<ImageUploadResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "????????? ????????? ??????: " + errorResult);
                    }

                    @Override
                    public void onSuccess(ImageUploadResponse result) {
                        Log.i("KAKAO_API", "????????? ????????? ??????");

                        Log.d("KAKAO_API", "URL: " + result.getOriginal().getUrl());

                        // TODO: ????????? ???????????? ????????? URL ??????
                    }
                })
        ;
    }

    public void lookUpFriend() {

        myNameList = new ArrayList<String>();
        myImageList = new ArrayList<String>();
        myUUIDList = new ArrayList<String>();
        AppFriendContext context =
                new AppFriendContext(false, 0, 100, "asc");

        KakaoTalkService.getInstance()
                .requestAppFriends(context, new TalkResponseCallback<AppFriendsResponse>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "???????????? ???????????? ??????");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "????????? ?????? ??????: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "?????? ?????? ??????: " + errorResult);
                    }

                    @Override
                    public void onSuccess(AppFriendsResponse result) {
                        Log.i("KAKAO_API", "?????? ?????? ??????" + result.getFriends().toString() + result.getTotalCount());


                        for (AppFriendInfo friend : result.getFriends()) {
                            Log.d("KAKAO_API", friend.toString() + "//" + friend.getUUID());
                            if (friend.getProfileThumbnailImage().length() == 0 ){
                                myNameList.add(friend.getProfileNickname());
                                myImageList.add("no Image");
                                myUUIDList.add(friend.getUUID());
                                Log.d("KAKAO_API1", myNameList.toString() + myImageList.toString() + myUUIDList.toString());
                            } else {
                                myNameList.add(friend.getProfileNickname());
                                myImageList.add(friend.getProfileThumbnailImage());
                                myUUIDList.add(friend.getUUID());
                                Log.d("KAKAO_API2", myNameList.toString() + myImageList.toString() + myUUIDList.toString());
                                Log.d("KAKAO_API2", myNameList.get(viewholderPos) + myImageList.toString() + myUUIDList.toString());
                            }

                            String uuid = friend.getUUID();// ????????? ?????? ??? ??????

                        }

                        showDialog(CreateNewActivity.this);
                    }
                });
    }

    public void inviteFriend(){

        LinkObject link = LinkObject.newBuilder()
                .setWebUrl("https://developers.kakao.com")
                .setMobileWebUrl("https://developers.kakao.com")
                .setAndroidExecutionParams("contentId=100")
                .build();
        TemplateParams params = TextTemplate.newBuilder("Text", link)
                .setButtonTitle("????????????")
                .build();

        KakaoLinkService.getInstance()
                .sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "??????????????? ?????? ??????: " + errorResult);
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        Log.i("KAKAO_API", "??????????????? ?????? ??????");

                        // ??????????????? ???????????? ??????????????? ?????? ?????? ???????????? ????????? ?????? ?????? ???????????? ?????? ???????????? ?????? ??? ????????????.
                        Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
                        Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());

                    }
                })
        ;
    }






    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {

            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "???????????? ????????? ??????????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"????????? ?????? ????????? ??????????????????: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"????????? ???????????????. ?????? ????????? ?????????: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //intent.putExtra("name", result.getNickname());
                    //intent.putExtra("profile", result.getProfileImagePath());
                    Log.d("??????????????????", result.toString());
                    SharedPreferences pref = getSharedPreferences("user-info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putLong("user_id",result.getId());
                    editor.apply();
                    //startActivity(intent);
                    //finish();
                    Toast.makeText(getApplicationContext(),"???????????? ???????????? ?????????????????????. ",Toast.LENGTH_SHORT).show();

                }

            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "????????? ?????? ????????? ??????????????????. ????????? ????????? ??????????????????: "+e.toString(), Toast.LENGTH_SHORT).show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }

    }

    public void create(View v) {
        create2();
    }


    public void create2() {
        SharedPreferences pref = getSharedPreferences("user-info", Activity.MODE_PRIVATE);
        Date date = new Date();


        RuleDatabase db = RuleDatabase.getRuleDatabase(getApplicationContext(), "user");

        Log.d("viewholderpos", Integer.toString(viewholderPos));
        db.rulesDao().insertNotification(new RulesEntity(skinPos , myImageList.get(viewholderPos), "????????????", rulePos, myNameList.get(viewholderPos), myUUIDList.get(viewholderPos), date, 1));

        finish();
    }

    public void InputInfo(View view) {
        switch (view.getId()) {
            case R.id.bpText :
                BottomSheetDialog dialog = new BottomSheetDialog(this, new com.ruehyeon.sanmo.BottomSheetDialog.BottomSheetDialogListener() {
                    @Override
                    public void exit(int data) {
                        Log.d("debug", String.valueOf(data) );
                        bp.setText(String.valueOf(data));
                    }
                }, 0);
                dialog.show(getSupportFragmentManager(), "bottomSheet");
                break;


            case R.id.bpText1 :
                BottomSheetDialog dialog1 = new BottomSheetDialog(this, new com.ruehyeon.sanmo.BottomSheetDialog.BottomSheetDialogListener() {
                    @Override
                    public void exit(int data) {
                        Log.d("debug", String.valueOf(data) );
                        bp.setText(String.valueOf(data));
                    }
                }, 1);
                dialog1.show(getSupportFragmentManager(), "bottomSheet");
                break;
        }

    }



}