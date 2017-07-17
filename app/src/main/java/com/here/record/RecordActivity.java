package com.here.record;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.here.R;
import com.here.adapter.RecordAdapter;
import com.here.base.MvpActivity;
import com.here.bean.ImActivity;
import com.here.bean.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecordActivity extends MvpActivity<RecordPresenter> implements RecordContract {

    @Bind(R.id.tb_activity_record)
    Toolbar tbActivityRecord;
    @Bind(R.id.cv_activity_head)
    CircleImageView cvActivityHead;
    @Bind(R.id.tv_publish_count)
    TextView tvPublishCount;
    @Bind(R.id.tv_join_count)
    TextView tvJoinCount;
    @Bind(R.id.tv_my_publish)
    TextView tvMyPublish;
    @Bind(R.id.view_my_publish)
    View viewMyPublish;
    @Bind(R.id.rl_my_activity)
    RelativeLayout rlMyActivity;
    @Bind(R.id.tv_my_join)
    TextView tvMyJoin;
    @Bind(R.id.view_my_join)
    View viewMyJoin;
    @Bind(R.id.rl_my_join)
    RelativeLayout rlMyJoin;
    @Bind(R.id.rv_my_activity)
    RecyclerView rvMyActivity;

    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_activity_record);
        initHome();
        mvpPresenter.attachView(this);
        adapter = new RecordAdapter(null);
        rvMyActivity.setLayoutManager(new LinearLayoutManager(this));
        rvMyActivity.setAdapter(adapter);
        Glide.with(this)
                .load(BmobUser.getCurrentUser(User.class).getHeadImageUrl())
                .into(cvActivityHead);
        mvpPresenter.loadMyPublish();

    }

    @Override
    protected RecordPresenter createPresenter() {
        return new RecordPresenter();
    }

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void stopLoading() {
        dissmiss();
    }

    @Override
    public void loadFail(String error) {
        toastShow(error);
    }

    @Override
    public void LoadActivity(List<ImActivity> imActivities) {
        adapter.setNewData(imActivities);
    }

    @OnClick({R.id.rl_my_activity, R.id.tv_my_join})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_my_activity:
                viewMyPublish.setVisibility(View.VISIBLE);
                tvMyPublish.setTextColor(getResources().getColor(R.color.color_accent));
                tvMyJoin.setTextColor(getResources().getColor(R.color.share_text));
                viewMyJoin.setVisibility(View.GONE);
                mvpPresenter.loadMyPublish();
                break;
            case R.id.tv_my_join:
                viewMyPublish.setVisibility(View.GONE);
                tvMyPublish.setTextColor(getResources().getColor(R.color.share_text));
                tvMyJoin.setTextColor(getResources().getColor(R.color.color_accent));
                viewMyJoin.setVisibility(View.VISIBLE);
                mvpPresenter.loadMyJoin();
                break;
        }
    }
}
