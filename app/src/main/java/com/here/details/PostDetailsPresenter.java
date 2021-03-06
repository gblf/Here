package com.here.details;

import android.text.TextUtils;

import com.here.base.BasePresenter;
import com.here.bean.Comment;
import com.here.bean.User;
import com.here.util.CommentUtil;
import com.here.util.LikeUtil;
import com.here.util.UserUtil;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/16 11:24
 */

public class PostDetailsPresenter  extends BasePresenter<PostDetailsContract>{

    private String publishId;

    public void loadData(){
        if (mvpView.getType().equals("mood")){
            mvpView.setMood(mvpView.getMood());
            publishId = mvpView.getMood().getObjectId();
        }else {
            mvpView.setAppointment(mvpView.getAppointment());
            publishId = mvpView.getAppointment().getObjectId();
        }
        loadComment();
    }


    public void loadComment(){
        mvpView.showLoading();
        CommentUtil.queryCommentOfPost(publishId, new CommentUtil.OnQueryCommentListener() {
            @Override
            public void success(List<Comment> comments) {
                if (mvpView != null){
                    mvpView.stopLoading();
                    mvpView.loadComment(comments);
                }
            }

            @Override
            public void fail(String error) {
                if (mvpView != null){
                    mvpView.stopLoading();
                    mvpView.loadFail(error);
                }

            }
        });
    }



    public void comment(){
        if (TextUtils.isEmpty(mvpView.getCommentMessage())){
            return;
        }else {
            mvpView.showLoading();
            final Comment comment = new Comment();
            comment.setUser(BmobUser.getCurrentUser(User.class));
            comment.setPublish(publishId);
            comment.setContent(mvpView.getCommentMessage());
            CommentUtil.uploadComment(comment, new UserUtil.OnDealListener() {
                @Override
                public void success() {
                    if (mvpView !=null){
                        mvpView.stopLoading();
                        mvpView.commentSuccess(comment);
                    }
                }

                @Override
                public void fail(String error) {
                    if (mvpView != null){
                        mvpView.stopLoading();
                        mvpView.commentFail(error);
                    }

                }
            });
        }
    }

}
