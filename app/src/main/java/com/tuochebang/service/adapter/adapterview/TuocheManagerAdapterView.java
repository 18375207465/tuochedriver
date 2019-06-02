package com.tuochebang.service.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.service.R;
import com.tuochebang.service.app.MyApplication;
import com.tuochebang.service.constant.AppConstant.BroadCastAction;
import com.tuochebang.service.request.base.ServerUrl;
import com.tuochebang.service.request.entity.Trailer;
import com.tuochebang.service.request.task.DeleteDriverRequest;
import com.tuochebang.service.ui.user.UserTuocheManagerActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import de.hdodenhof.circleimageview.CircleImageView;

public class TuocheManagerAdapterView extends RelativeLayout {
    private Context mContext;
    private CircleImageView mImgHeader;
    private TextView mTxtDelete;
    private TextView mTxtNamePhone;
    private TextView mTxtType;

    /* renamed from: com.tuochebang.service.adapter.adapterview.TuocheManagerAdapterView$1 */
    class C06521 implements OnResponseListener<Object> {
        C06521() {
        }

        public void onStart(int what) {
            ((UserTuocheManagerActivity) TuocheManagerAdapterView.this.mContext).showCommonProgreessDialog("请稍后...");
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ToastUtil.showMessage(MyApplication.getInstance(), "删除成功");
                BroadCastUtil.sendBroadCast(TuocheManagerAdapterView.this.mContext, BroadCastAction.DRIVER_CHANGE);
            }
        }

        public void onFailed(int what, Response<Object> response) {
        }

        public void onFinish(int what) {
            ((UserTuocheManagerActivity) TuocheManagerAdapterView.this.mContext).dismissCommonProgressDialog();
        }
    }

    public TuocheManagerAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public TuocheManagerAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TuocheManagerAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_tuoche_manager_item, this);
        this.mTxtNamePhone = (TextView) view.findViewById(R.id.txt_name_phone);
        this.mTxtType = (TextView) view.findViewById(R.id.txt_type);
        this.mTxtDelete = (TextView) view.findViewById(R.id.txt_btn_delete);
        this.mImgHeader = (CircleImageView) view.findViewById(R.id.img_user);
    }

    private void httpDeleteDriver(Trailer info) {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new DeleteDriverRequest(ServerUrl.getInst().DELETE_DRIVER_URL(), RequestMethod.POST, String.valueOf(info.getUserId())), new C06521());
    }

    public void refreshView(final Trailer info) {
        this.mTxtNamePhone.setText(info.getNickName() + "(" + info.getMobile() + ")");
        ImageLoader.getInstance().displayImage(info.getPicture(), this.mImgHeader);
        this.mTxtDelete.setOnClickListener(new OnClickListener() {

            /* renamed from: com.tuochebang.service.adapter.adapterview.TuocheManagerAdapterView$2$1 */
            class C06531 implements CommonNoticeDialog.DialogButtonInterface {
                C06531() {
                }

                public void onDialogButtonClick(CommonNoticeDialog.DialogResult dialogResult) {
                    if (dialogResult == CommonNoticeDialog.DialogResult.Yes) {
                        TuocheManagerAdapterView.this.httpDeleteDriver(info);
                    }
                }
            }

            public void onClick(View v) {
                ((UserTuocheManagerActivity) TuocheManagerAdapterView.this.mContext).showChoiceDialog("提示", "是否删除该司机?", new C06531());
            }
        });
    }
}
