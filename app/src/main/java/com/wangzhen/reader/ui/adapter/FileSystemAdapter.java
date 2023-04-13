package com.wangzhen.reader.ui.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.adapter.RecyclerAdapter;
import com.wangzhen.reader.model.local.BookRepository;
import com.wangzhen.reader.ui.adapter.holder.FileHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangzhen on 17-5-27.
 */

public class FileSystemAdapter extends RecyclerAdapter<File> {
    private final HashMap<File, Boolean> mCheckMap = new HashMap<>();
    private int mCheckedCount = 0;

    public FileSystemAdapter(List<File> list) {
        super(list);
    }

    @Override
    public RecyclerView.ViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileHolder(parent, mCheckMap);
    }


    @Override
    public void setData(List<File> list) {
        mCheckMap.clear();
        for (File file : list) {
            mCheckMap.put(file, false);
        }
        super.setData(list);
    }

    public void removeItems(List<File> value) {
        //删除在HashMap中的文件
        for (File file : value) {
            mCheckMap.remove(file);
            //因为，能够被移除的文件，肯定是选中的
            --mCheckedCount;
        }
        //删除列表中的文件
        super.getDatas().removeAll(value);
    }

    //设置点击切换
    public void setCheckedItem(int pos) {
        File file = getDatas().get(pos);
        if (isFileLoaded(file.getAbsolutePath())) return;

        boolean isSelected = mCheckMap.get(file);
        if (isSelected) {
            mCheckMap.put(file, false);
            --mCheckedCount;
        } else {
            mCheckMap.put(file, true);
            ++mCheckedCount;
        }
        notifyDataSetChanged();
    }

    public void setCheckedAll(boolean isChecked) {
        Set<Map.Entry<File, Boolean>> entrys = mCheckMap.entrySet();
        mCheckedCount = 0;
        for (Map.Entry<File, Boolean> entry : entrys) {
            //必须是文件，必须没有被收藏
            if (entry.getKey().isFile() && !isFileLoaded(entry.getKey().getAbsolutePath())) {
                entry.setValue(isChecked);
                //如果选中，则增加点击的数量
                if (isChecked) {
                    ++mCheckedCount;
                }
            }
        }
        notifyDataSetChanged();
    }

    private boolean isFileLoaded(String path) {
        //如果是已加载的文件，则点击事件无效。
        return BookRepository.getInstance().getCollBookByPath(path) != null;
    }

    public int getCheckableCount() {
        List<File> files = getDatas();
        int count = 0;
        if (files != null) {
            for (File file : files) {
                if (!isFileLoaded(file.getAbsolutePath()) && file.isFile()) ++count;
            }
        }
        return count;
    }

    public boolean getItemIsChecked(int pos) {
        File file = getDatas().get(pos);
        return Boolean.TRUE.equals(mCheckMap.get(file));
    }

    public List<File> getCheckedFiles() {
        List<File> files = new ArrayList<>();
        Set<Map.Entry<File, Boolean>> entrys = mCheckMap.entrySet();
        for (Map.Entry<File, Boolean> entry : entrys) {
            if (entry.getValue()) {
                files.add(entry.getKey());
            }
        }
        return files;
    }

    public int getCheckedCount() {
        return mCheckedCount;
    }

    public HashMap<File, Boolean> getCheckMap() {
        return mCheckMap;
    }

}
