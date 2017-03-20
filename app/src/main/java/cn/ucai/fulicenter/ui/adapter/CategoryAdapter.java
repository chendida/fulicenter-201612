package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.fragment.CategoryGroupFragment;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/17.
 */

public class CategoryAdapter extends BaseExpandableListAdapter{
    Context context;
    List<CategoryGroupBean> groupList;
    ArrayList<ArrayList<CategoryChildBean>> childList;

    public CategoryAdapter(Context context) {
        this.context = context;
        this.groupList = new ArrayList<>();
        this.childList = new ArrayList<>();
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
        //绑定数据
        holder.bind(groupPosition,isExpanded);
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
        //绑定数据
        holder.bind(groupPosition,childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(List<CategoryGroupBean> mGroupList, ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        groupList.addAll(mGroupList);
        childList.addAll(mChildList);
        notifyDataSetChanged();
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

        public void bind(int groupPosition, boolean isExpanded) {
            ImageLoader.downloadImg(context, ivGroup, getGroup(groupPosition).getImageUrl());
            tvCategoryName.setText(getGroup(groupPosition).getName());
            ivExpand.setImageResource(isExpanded?R.mipmap.expand_off:R.mipmap.expand_on);
        }
    }

    class ChildHolder {
        @BindView(R.id.ivChild)
        ImageView ivChild;
        @BindView(R.id.tvChildName)
        TextView tvChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout mLayoutCategoryChild;

        ChildHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(final int groupPosition, int childPosition) {
            final CategoryChildBean categoryChildBean = getChild(groupPosition, childPosition);
            if (categoryChildBean != null) {
                ImageLoader.downloadImg(context, ivChild, categoryChildBean.getImageUrl());
                tvChildName.setText(categoryChildBean.getName());
                mLayoutCategoryChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MFGT.gotoCategoryChild(context,categoryChildBean.getId(),
                                getGroup(groupPosition).getName(),childList.get(groupPosition));
                    }
                });
            }
        }
    }
}
