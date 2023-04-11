package com.wangzhen.reader.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.FragmentFileCategoryBinding;
import com.wangzhen.reader.model.local.BookRepository;
import com.wangzhen.reader.ui.adapter.FileSystemAdapter;
import com.wangzhen.reader.utils.FileStack;
import com.wangzhen.reader.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangzhen on 17-5-27.
 */

public class FileCategoryFragment extends BaseFileFragment {
    private FragmentFileCategoryBinding binding;
    TextView mTvPath;
    TextView mTvBackLast;
    RecyclerView mRvContent;

    private FileStack mFileStack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFileCategoryBinding.inflate(inflater);
        mTvPath = binding.fileCategoryTvPath;
        mTvBackLast = binding.fileCategoryTvBackLast;
        mRvContent = binding.fileCategoryRvContent;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpAdapter();

        mTvBackLast.setOnClickListener((v) -> {
            FileStack.FileSnapshot snapshot = mFileStack.pop();
            int oldScrollOffset = mRvContent.computeHorizontalScrollOffset();
            if (snapshot == null) return;
            mTvPath.setText(snapshot.filePath);
            mAdapter.refreshItems(snapshot.files);
            mRvContent.scrollBy(0, snapshot.scrollOffset - oldScrollOffset);
            //反馈
            if (mListener != null) {
                mListener.onCategoryChanged();
            }
        });

        mFileStack = new FileStack();
        File root = Environment.getExternalStorageDirectory();
        toggleFileTree(root);
    }

    private void setUpAdapter() {
        mAdapter = new FileSystemAdapter();
        mAdapter.setOnItemClickListener((v, pos) -> {
            File file = mAdapter.getItem(pos);
            if (file.isDirectory()) {
                //保存当前信息。
                FileStack.FileSnapshot snapshot = new FileStack.FileSnapshot();
                snapshot.filePath = mTvPath.getText().toString();
                snapshot.files = new ArrayList<File>(mAdapter.getItems());
                snapshot.scrollOffset = mRvContent.computeVerticalScrollOffset();
                mFileStack.push(snapshot);
                //切换下一个文件
                toggleFileTree(file);
            } else {

                //如果是已加载的文件，则点击事件无效。
                String id = mAdapter.getItem(pos).getAbsolutePath();
                if (BookRepository.getInstance().getCollBook(id) != null) {
                    return;
                }
                //点击选中
                mAdapter.setCheckedItem(pos);
                //反馈
                if (mListener != null) {
                    mListener.onItemCheckedChange(mAdapter.getItemIsChecked(pos));
                }
            }
        });
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.setAdapter(mAdapter);
    }

    private void toggleFileTree(File file) {
        //路径名
        mTvPath.setText(getString(R.string.file_path, file.getPath()));
        //获取数据
        File[] files = file.listFiles(new SimpleFileFilter());
        //转换成List
        List<File> rootFiles = Arrays.asList(files);
        //排序
        Collections.sort(rootFiles, new FileComparator());
        //加入
        mAdapter.refreshItems(rootFiles);
        //反馈
        if (mListener != null) {
            mListener.onCategoryChanged();
        }
    }

    @Override
    public int getFileCount() {
        int count = 0;
        Set<Map.Entry<File, Boolean>> entrys = mAdapter.getCheckMap().entrySet();
        for (Map.Entry<File, Boolean> entry : entrys) {
            if (!entry.getKey().isDirectory()) {
                ++count;
            }
        }
        return count;
    }

    public class FileComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            }
            if (o2.isDirectory() && o1.isFile()) {
                return 1;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public static class SimpleFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if (pathname.getName().startsWith(".")) {
                return false;
            }
            //文件夹内部数量为0
            if (pathname.isDirectory() && pathname.list().length == 0) {
                return false;
            }

            /**
             * 现在只支持TXT文件的显示
             */
            //文件内容为空,或者不以txt为开头
            if (!pathname.isDirectory() && (pathname.length() == 0 || !pathname.getName().endsWith(FileUtils.SUFFIX_TXT))) {
                return false;
            }
            return true;
        }
    }
}
