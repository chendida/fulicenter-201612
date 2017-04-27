package cn.ucai.fulicenter.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {
    ICategoryModel model;
    List<CategoryGroupBean> groupList;
    GroupAdapter adapter;
    @BindView(R.id.list_item_name)
    RecyclerView listItemName;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_name_list, container, false);
        ButterKnife.bind(this, view);
        adapter = new GroupAdapter();
        listItemName.setLayoutManager(new LinearLayoutManager(getContext()));
        listItemName.setHasFixedSize(true);
        listItemName.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showCategoryGroup(cn.ucai.fulicenter.ui.EventBus eventBus) {
        List<CategoryGroupBean> list = eventBus.getGroupList();
        if (list != null && list.size() > 0) {
            this.groupList = list;
            listItemName.setAdapter(adapter);
        }
    }

    class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupNameViewHolder> {

        @Override
        public GroupNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GroupNameViewHolder(View.inflate(getContext(), R.layout.item_group_name, null));
        }

        @Override
        public void onBindViewHolder(GroupNameViewHolder holder, int position) {
            final CategoryGroupBean bean = groupList.get(position);
            holder.groupName.setText(bean.getName());
            holder.groupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(bean);
                }
            });
        }

        @Override
        public int getItemCount() {
            return groupList!=null?groupList.size():0;
        }

        class GroupNameViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.group_name)
            TextView groupName;

            GroupNameViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new CategoryModel();
        new WorkThread().start();
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
            model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
                @Override
                public void onSuccess(CategoryGroupBean[] result) {
                    List<CategoryGroupBean> list = ConvertUtils.array2List(result);
                    Log.e("1111111111", list.toString());
                    cn.ucai.fulicenter.ui.EventBus eventBus = new cn.ucai.fulicenter.ui.EventBus();
                    eventBus.setGroupList(list);
                    EventBus.getDefault().post(eventBus);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

}
