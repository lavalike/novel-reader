package com.wangzhen.reader.ui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangzhen.adapter.base.RecyclerViewHolder;
import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.ItemFileBinding;
import com.wangzhen.reader.model.local.BookRepository;
import com.wangzhen.reader.utils.AppConfig;
import com.wangzhen.reader.utils.FileUtils;
import com.wangzhen.reader.utils.MD5Utils;
import com.wangzhen.reader.utils.StringUtils;

import java.io.File;
import java.util.HashMap;

/**
 * FileHolder
 * Created by wangzhen on 2023/4/12
 */
public class FileHolder extends RecyclerViewHolder<File> {
    private ItemFileBinding binding;

    private ImageView mIvIcon;
    private CheckBox mCbSelect;
    private TextView mTvName;
    private LinearLayout mLlBrief;
    private TextView mTvSize;
    private TextView mTvDate;
    private TextView mTvSubCount;

    private final HashMap<File, Boolean> mSelectedMap;

    public FileHolder(ViewGroup parent, HashMap<File, Boolean> selectedMap) {
        super(parent, R.layout.item_file);
        mSelectedMap = selectedMap;
    }

    @Override
    public void bind() {
        binding = ItemFileBinding.bind(itemView);
        mIvIcon = binding.fileIvIcon;
        mCbSelect = binding.fileCbSelect;
        mTvName = binding.fileTvName;
        mLlBrief = binding.fileLlBrief;
        mTvSize = binding.fileTvSize;
        mTvDate = binding.fileTvDate;
        mTvSubCount = binding.fileTvSubCount;

        //判断是文件还是文件夹
        if (mData.isDirectory()) {
            setFolder(mData);
        } else {
            setFile(mData);
        }
    }

    private void setFile(File file) {
        //选择
        String id = MD5Utils.strToMd5By16(file.getAbsolutePath());

        if (BookRepository.getInstance().getCollBook(id) != null) {
            mIvIcon.setImageResource(R.drawable.ic_file_loaded);
            mIvIcon.setVisibility(View.VISIBLE);
            mCbSelect.setVisibility(View.GONE);
        } else {
            boolean isSelected = mSelectedMap.get(file);
            mCbSelect.setChecked(isSelected);
            mIvIcon.setVisibility(View.GONE);
            mCbSelect.setVisibility(View.VISIBLE);
        }

        mLlBrief.setVisibility(View.VISIBLE);
        mTvSubCount.setVisibility(View.GONE);

        mTvName.setText(file.getName());
        mTvSize.setText(FileUtils.getFileSize(file.length()));
        mTvDate.setText(StringUtils.dateConvert(file.lastModified(), AppConfig.Format.FORMAT_FILE_DATE));
    }

    public void setFolder(File folder) {
        //图片
        mIvIcon.setVisibility(View.VISIBLE);
        mCbSelect.setVisibility(View.GONE);
        mIvIcon.setImageResource(R.drawable.ic_dir);
        //名字
        mTvName.setText(folder.getName());
        //介绍
        mLlBrief.setVisibility(View.GONE);
        mTvSubCount.setVisibility(View.VISIBLE);

        int count = 0;
        String[] list = folder.list();
        if (list != null) {
            count = list.length;
        }
        mTvSubCount.setText(itemView.getContext().getString(R.string.file_sub_count, count));
    }

}
