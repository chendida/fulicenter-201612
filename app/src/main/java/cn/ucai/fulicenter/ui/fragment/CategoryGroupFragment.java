package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.layout_tips)
    LinearLayout layoutTips;
    View loadView;
    View loadFail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_group, container, false);
        bind = ButterKnife.bind(this, view);
        //将loading布局解析到layoutTips上
        loadView = LayoutInflater.from(getContext()).inflate(R.layout.loading,
                layoutTips,false);
        loadFail = LayoutInflater.from(getContext()).inflate(R.layout.load_fail,
                layoutTips,false);
        layoutTips.addView(loadView);
        layoutTips.addView(loadFail);
        loadView.setVisibility(View.VISIBLE);
        loadFail.setVisibility(View.GONE);
//        showDialog(true,false);
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
        elvCategory.setGroupIndicator(null);
    }

    private void initData() {
        loadGroupData();
    }

    private void loadGroupData() {
        model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null && result.length > 0) {
                    L.e(TAG, "initData,result = " + result);
                    ArrayList<CategoryGroupBean> list = ConvertUtils.array2List(result);
                    mGroupList.clear();
                    mGroupList.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        loadChildData(list.get(i).getId(), i);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "initData,error = " + error);
                CommonUtils.showShortToast(error);
                showDialog(false,false);
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
                loadIndex++;
                if (result != null && result.length > 0) {
                    ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
                    mChildList.set(index, list);
                }
                //数据下载完成后，更新适配器
                if (loadIndex == mGroupList.size()) {
                    L.e(TAG, "loadData, = " + "success");
                    mAdapter.initData(mGroupList, mChildList);
                    showDialog(false,true);
                    /*loadView.setVisibility(View.GONE);
                    loadFail.setVisibility(View.GONE);
                    layoutTips.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "initData,error = " + error);
                CommonUtils.showShortToast(error);
                loadIndex++;
                //数据下载完成后，更新适配器
                if (loadIndex == mGroupList.size()) {
                    mAdapter.initData(mGroupList, mChildList);
                }
                showDialog(false,false);
            }
        });
    }


    @OnClick(R.id.layout_tips)
    public void loadAgain() {
        if (loadFail.getVisibility() == View.VISIBLE){
            loadGroupData();//重新下载数据
            showDialog(true,false);
        }
    }

    private void showDialog(boolean dialog, boolean success) {
        loadView.setVisibility(dialog?View.VISIBLE:View.GONE);
        if (dialog){
            loadFail.setVisibility(View.GONE);
            layoutTips.setVisibility(View.VISIBLE);
        }else {
            L.e(TAG, "loadData, = " + success);
            layoutTips.setVisibility(success?View.GONE:View.VISIBLE);
            loadFail.setVisibility(success?View.GONE:View.VISIBLE);
        }
    }
}
