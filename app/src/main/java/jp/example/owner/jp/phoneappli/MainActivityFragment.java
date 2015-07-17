package jp.example.owner.jp.phoneappli;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_activity, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //1 count=文字数　2 editText=数字入力 3 デリートボタン
        final TextView textView_count = (TextView) getActivity().findViewById(R.id.textView);
        final EditText editText = (EditText) getActivity().findViewById(R.id.et_phone);
        final ImageButton btn_delete = (ImageButton) getActivity().findViewById(R.id.btn_delete);
        View img_phone = getActivity().findViewById(R.id.img_phone);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int txtLength = s.length();
                String co = getString(R.string.text_mojicount);
                textView_count.setText(String.valueOf(txtLength) + co);
                btn_delete.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //電話ボタン　インテント
        img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + editText.getText().toString()));
                startActivity(i);
            }
        });

        //デリートボタン(1文字削除)
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = editText.getText().toString();
                if (str.length() > 0) {
                    editText.setText(str.substring(0, str.length() - 1));
                    editText.setSelection(str.length() - 1);


                } else {
                    btn_delete.setVisibility(View.INVISIBLE);

                }


            }
        });
        btn_delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (editText.getText().length() != 0) {
                    editText.getText().clear();
                    Toast.makeText(getActivity(), R.string.text_message_ClearDone, Toast.LENGTH_SHORT).show();

                }
                return false;


            }


        });
        //数字ボタンを押したらエディットテキストに数字が入る実装

            View.OnClickListener button1= new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(;;) {
                        editText.setText("*");
                    }
                }
            };
            getActivity().findViewById(R.id.imv1).setOnClickListener(button1);
        }








}



