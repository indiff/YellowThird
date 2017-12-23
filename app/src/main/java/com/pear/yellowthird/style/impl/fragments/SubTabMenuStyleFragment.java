package com.pear.yellowthird.style.impl.fragments;

import android.support.v4.app.Fragment;

import com.pear.yellowthird.activitys.fragments.mainSubFragments.commonAbstract.CommonSubTabSubFragmentAbstract;
import com.pear.yellowthird.style.interfaces.StyleFragmentInterface;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;

import java.util.List;

/**
 * 创建子菜单的样式视图实现
 */

public class SubTabMenuStyleFragment implements StyleFragmentInterface<List<SubTabMenuStyleDataVo>>{

    @Override
    public Fragment created(List<SubTabMenuStyleDataVo> data) {
        StyleSubTabSubFragment fragment=StyleSubTabSubFragment.newInstance(StyleSubTabSubFragment.class);
        fragment.setMenuData(data);
        return fragment;
    }

    /**
     *
     * */
    public static class StyleSubTabSubFragment extends CommonSubTabSubFragmentAbstract {

        @Override
        public Fragment buildItem(int position, SubTabMenuStyleDataVo vo) {
            return null;
        }
    }

}
