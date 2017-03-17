package cn.ucai.fulicenter.ui.fragment;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ExpandableListView;


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
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;

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
    int loadIndex = 0;
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
        initData();
        initView();
    }

    private void initView() {
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(getContext());
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
                    mGroupList.clear();
                    mGroupList.addAll(list);
                    for (int i = 0;i < list.size();i ++){
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        loadChildData(list.get(i).getId(),i);
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

       @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    private void loadChildData(int parentId, final int index) {
        model.loadChildData(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                loadIndex ++;
                if (result != null && result.length > 0){
                    ArrayList<CategoryChildBean>list = ConvertUtils.array2List(result);
                    mChildList.set(index,list);
                }
                //数据下载完成后，更新适配器
                if (loadIndex == mGroupList.size()){
                    mAdapter.initData(mGroupList,mChildList);
                }
            }
            @Override
            public void onError(String error) {
                L.e(TAG, "initData,error = " + error);
                CommonUtils.showShortToast(error);
                loadIndex ++;
                //数据下载完成后，更新适配器
                if (loadIndex == mGroupList.size()){
                    mAdapter.initData(mGroupList,mChildList);
                }
            }
        });
    }


}
