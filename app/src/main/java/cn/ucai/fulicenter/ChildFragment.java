package cn.ucai.fulicenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/4/26.
 */

public class ChildFragment extends Fragment {
    ArrayList<CategoryChildBean> childList;
    int parentId = 344;
    String parentName = "";
    ICategoryModel model;
    @BindView(R.id.child_rv)
    RecyclerView childRv;
    ChildAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new CategoryModel();
        new WorkThread().start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showChildList(cn.ucai.fulicenter.ui.EventBus eventBus) {
        ArrayList<CategoryChildBean> list = eventBus.getChildList();
        if (list != null && list.size() > 0) {
            this.childList = list;
            if (adapter == null) {
                adapter = new ChildAdapter();
                childRv.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager manager = new GridLayoutManager(getContext(), I.COLUM_NUM);
        childRv.setLayoutManager(manager);
        childRv.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            model.loadChildData(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
                @Override
                public void onSuccess(CategoryChildBean[] result) {
                    final ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
                    Log.e("1111111111", list.toString());
                    cn.ucai.fulicenter.ui.EventBus eventBus = new cn.ucai.fulicenter.ui.EventBus();
                    eventBus.setChildList(list);
                    EventBus.getDefault().post(eventBus);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void loadChildList(CategoryGroupBean bean) {
        if (bean != null) {
            parentName = bean.getName();
            parentId = bean.getId();
            new WorkThread().start();
        }
    }

    class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

        @Override
        public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChildViewHolder(View.inflate(getContext(), R.layout.adapter_child, null));
        }

        @Override
        public void onBindViewHolder(ChildViewHolder holder, final int position) {
            holder.tvChild.setText(childList.get(position).getName());
            ImageLoader.downloadImg(getContext(), holder.ivChild, childList.get(position).getImageUrl());
            holder.cardCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoCategoryChild(getContext(),childList.get(position).getId(),
                            parentName,childList);
                }
            });
        }

        @Override
        public int getItemCount() {
            return childList != null ? childList.size() : 0;
        }

        class ChildViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.iv_child)
            ImageView ivChild;
            @BindView(R.id.tv_child)
            TextView tvChild;
            @BindView(R.id.card_category)
            CardView cardCategory;
            ChildViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        /*class ChildViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_child)
            ImageView ivChild;
            @BindView(R.id.tv_child)
            TextView tvChild;

            ChildViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

        }*/
    }
}
