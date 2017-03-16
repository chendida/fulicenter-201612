package cn.ucai.fulicenter.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryGroupFragment extends Fragment {
    private static final String TAG = CategoryGroupFragment.class.getSimpleName();
    @BindView(R.id.elvCategory)
    ExpandableListView elvCategory;
    Unbinder bind;
    ICategoryModel model;

    List<CategoryGroupBean> mGroupList;
    List<List<CategoryChildBean>> mChildList;
    CategoryAdapter mAdapter;
    public CategoryGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_group, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new CategoryModel();
        initView();
        initData();
    }

    private void initView() {
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(getContext(),mGroupList,mChildList);
        elvCategory.setAdapter(mAdapter);
    }

    private void initData() {
        loadGroupData();
    }

    private void loadGroupData() {
        model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null && result.length > 0){
                    L.e(TAG, "initData,result = " + result);
                    ArrayList<CategoryGroupBean>list = ConvertUtils.array2List(result);
                    mGroupList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "initData,error = " + error);
                CommonUtils.showShortToast(error);
            }
        });
    }

       @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    class CategoryAdapter extends BaseExpandableListAdapter {
        Context context;
        List<CategoryGroupBean> groupList;
        List<List<CategoryChildBean>> childList;

        public CategoryAdapter(Context context, List<CategoryGroupBean> groupList,
                               List<List<CategoryChildBean>> childList) {
            this.context = context;
            this.groupList = groupList;
            this.childList = childList;
        }

        @Override
        public int getGroupCount() {
            return groupList != null ? groupList.size() : 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childList != null && childList.get(groupPosition)
                    != null ? childList.get(groupPosition).size() : 0;
        }

        @Override
        public CategoryGroupBean getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public CategoryChildBean getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.categoty_group, null);
                holder = new GroupHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            ImageLoader.downloadImg(context, holder.ivGroup, getGroup(groupPosition).getImageUrl());
            holder.tvCategoryName.setText(getGroup(groupPosition).getName());
            if (isExpanded) {
                holder.ivExpand.setImageResource(R.drawable.arrow2_down);
                loadChildData(getGroup(groupPosition).getId(),groupPosition);
            } else {
                holder.ivExpand.setImageResource(R.drawable.arrow2_up);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder = null;
            if (convertView == null){
                convertView = View.inflate(context, R.layout.category_child, null);
                holder = new ChildHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (ChildHolder) convertView.getTag();
            }
            ImageLoader.downloadImg(context,holder.ivChild,getChild(groupPosition,childPosition)
                    .getImageUrl());
            holder.tvChildName.setText(getChild(groupPosition,childPosition).getName());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class GroupHolder {
            @BindView(R.id.ivGroup)
            ImageView ivGroup;
            @BindView(R.id.tvCategoryName)
            TextView tvCategoryName;
            @BindView(R.id.ivExpand)
            ImageView ivExpand;

            GroupHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        class ChildHolder {
            @BindView(R.id.ivChild)
            ImageView ivChild;
            @BindView(R.id.tvChildName)
            TextView tvChildName;

            ChildHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    private void loadChildData(int parentId, final int groupPosition) {
        model.loadChildData(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                if (result != null && result.length > 0){
                    ArrayList<CategoryChildBean>list = ConvertUtils.array2List(result);
                    for (int i = 0;i < mGroupList.size(); i ++){
                        mChildList.add(i,list);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "initData,error = " + error);
                CommonUtils.showShortToast(error);
            }
        });
    }


}
