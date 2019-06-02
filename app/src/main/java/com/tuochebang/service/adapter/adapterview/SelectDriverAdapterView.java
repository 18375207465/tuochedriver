package com.tuochebang.service.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;
import com.tuochebang.service.request.entity.Trailer;
import de.hdodenhof.circleimageview.CircleImageView;

public class SelectDriverAdapterView extends RelativeLayout {
    private onCheckListener mCheckListener;
    private ImageView mImg;
    private ImageView mImgIcon;
    private RadioButton mRbName;
    private Trailer mSelectFilterName;
    private TextView mTxtName;

    public interface onCheckListener {
        void checkListener();
    }

    /* renamed from: com.tuochebang.service.adapter.adapterview.SelectDriverAdapterView$1 */
    class C06511 implements OnCheckedChangeListener {
        C06511() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (SelectDriverAdapterView.this.mCheckListener != null) {
                SelectDriverAdapterView.this.mCheckListener.checkListener();
            }
        }
    }

    public SelectDriverAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public SelectDriverAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public SelectDriverAdapterView(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.adapter_view_driver_select, this);
        this.mRbName = (RadioButton) findViewById(R.id.pay_method_rb);
        this.mTxtName = (TextView) findViewById(R.id.txt_name_phone);
        this.mImg = (CircleImageView) findViewById(R.id.img_user);
    }

    public void refreshView(Trailer childType, int position) {
        this.mRbName.setOnCheckedChangeListener(new C06511());
        this.mTxtName.setText(childType.getNickName() + "(" + childType.getMobile() + ")");
        ImageLoader.getInstance().displayImage(childType.getPicture(), this.mImg);
        if (childType.getUserId() == this.mSelectFilterName.getUserId()) {
            this.mRbName.setChecked(true);
        } else {
            this.mRbName.setChecked(false);
        }
    }

    public void setSelectFilterName(Trailer selectFilterName) {
        this.mSelectFilterName = selectFilterName;
    }

    public void setOnCheckListener(onCheckListener listener) {
        this.mCheckListener = listener;
    }
}
