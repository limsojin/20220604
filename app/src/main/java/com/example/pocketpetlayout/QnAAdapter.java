package com.example.pocketpetlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QnAAdapter extends RecyclerView.Adapter<QnAAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView qna_image;
        TextView txt_main;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qna_image = (ImageView) itemView.findViewById(R.id.QnA_image);
            txt_main = (TextView) itemView.findViewById(R.id.txt_main);
        }
    }

    private ArrayList<QnAItem> mQnAList = null;

    public QnAAdapter(ArrayList<QnAItem> mList) {
        this.mQnAList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_question_item, parent, false);
        QnAAdapter.ViewHolder vh = new QnAAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull QnAAdapter.ViewHolder holder, int position) {
        QnAItem item = mQnAList.get(position);

        holder.qna_image.setImageResource(R.drawable.ic_launcher_background); // 사진 없어서 기본 파일로 이미지 띄움
        holder.txt_main.setText(item.getMainText());
    }

    @Override
    public int getItemCount() {
        return mQnAList.size();
    } //아이템 사이즈도 지정하기
}
