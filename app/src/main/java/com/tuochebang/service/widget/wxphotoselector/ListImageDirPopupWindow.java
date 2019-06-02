package com.tuochebang.service.widget.wxphotoselector;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.tuochebang.service.R;

import java.util.List;

public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder> {
    private OnImageDirSelected mImageDirSelected;
    private ListView mListDir;

    /* renamed from: com.tuochebang.user.widget.wxphotoselector.ListImageDirPopupWindow$2 */
    class C10802 implements OnItemClickListener {
        C10802() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (ListImageDirPopupWindow.this.mImageDirSelected != null) {
                ListImageDirPopupWindow.this.mImageDirSelected.selected((ImageFloder) ListImageDirPopupWindow.this.mDatas.get(position));
            }
        }
    }

    public interface OnImageDirSelected {
        void selected(ImageFloder imageFloder);
    }

    public ListImageDirPopupWindow(int width, int height, List<ImageFloder> datas, View convertView) {
        super(convertView, width, height, true, datas);
    }

    public void initViews() {
        this.mListDir = (ListView) findViewById(R.id.id_list_dir);
        this.mListDir.setAdapter(new CommonAdapter<ImageFloder>(this.context, this.mDatas, R.layout.list_dir_item) {
            public void convert(ViewHolder helper, ImageFloder item) {
                helper.setText(R.id.id_dir_item_name, item.getName());
                helper.setImageByUrl(R.id.id_dir_item_image, item.getFirstImagePath());
                helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
            }
        });
    }

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    public void initEvents() {
        this.mListDir.setOnItemClickListener(new C10802());
    }

    public void init() {
    }

    protected void beforeInitWeNeedSomeParams(Object... params) {
    }
}
