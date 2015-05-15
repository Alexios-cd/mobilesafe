package com.fwj.mobilesafe.view;

import com.fwj.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleView extends RelativeLayout implements OnClickListener {
	private TextView tv_title_left;
	private TextView tv_title_centre;
	private TextView tv_title_right;

	private String namespace = "http://schemas.android.com/apk/res/com.fwj.mobilesafe";

	public TitleBarClickListener onclickListener;
	private String lefttext;
	private String centretext;
	private String righttext;
	private int left_l_icon;
	private int left_r_icon;
	private int centre_l_icon;
	private int centre_r_icon;
	private int right_l_icon;
	private int right_r_icon;

	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		lefttext = attrs.getAttributeValue(namespace, "lefttext");
		centretext = attrs.getAttributeValue(namespace, "centretext");
		righttext = attrs.getAttributeValue(namespace, "righttext");
		left_l_icon = attrs.getAttributeResourceValue(namespace, "left_l_icon",
				0);
		left_r_icon = attrs.getAttributeResourceValue(namespace, "left_r_icon",
				0);
		centre_l_icon = attrs.getAttributeResourceValue(namespace,
				"centre_l_icon", 0);
		centre_r_icon = attrs.getAttributeResourceValue(namespace,
				"centre_r_icon", 0);
		right_l_icon = attrs.getAttributeResourceValue(namespace,
				"right_l_icon", 0);
		right_r_icon = attrs.getAttributeResourceValue(namespace,
				"right_r_icon", 0);
		initView(context);

	}

	public TitleView(Context context) {
		super(context);
		initView(context);
	}

	public void initView(Context context) {
		View view = View.inflate(context, R.layout.view_title, this);
		tv_title_left = (TextView) view.findViewById(R.id.tv_title_left);
		tv_title_centre = (TextView) view.findViewById(R.id.tv_title_centre);
		tv_title_right = (TextView) view.findViewById(R.id.tv_title_right);

		tv_title_left.setOnClickListener(this);
		tv_title_centre.setOnClickListener(this);
		tv_title_right.setOnClickListener(this);
		initData();
	}

	private void initData() {
		setLeftText(lefttext, left_l_icon, left_r_icon);
		setCentreText(centretext, centre_l_icon, centre_r_icon);
		setRightText(righttext, right_l_icon, right_r_icon);

	}

	public interface TitleBarClickListener {
		public void onLeftTitleClicked();

		public void onCenterTitleClicked();

		public void onRightTitleClicked();
	}

	public void setOnTitleClicked(TitleBarClickListener onclickListener) {
		this.onclickListener = onclickListener;
	}

	public void setLeftText(String leftText) {
		setLeftText(leftText, 0, 0);
	}

	public void setCentreText(String centreText) {
		setCentreText(centreText, 0, 0);
	}

	public void setRightText(String rightText) {
		setRightText(rightText, 0, 0);
	}

	public void setLeftText(String leftText, int leftid, int rightid) {
		if (0 != leftid && 0 != rightid)
			tv_title_left.setGravity(Gravity.CENTER);
		else if (0 != leftid)
			tv_title_left.setGravity(Gravity.LEFT + Gravity.CENTER_VERTICAL);
		else if (0 != rightid)
			tv_title_left.setGravity(Gravity.RIGHT + Gravity.CENTER_VERTICAL);
		tv_title_left.setCompoundDrawablesWithIntrinsicBounds(leftid, 0,
				rightid, 0);
		tv_title_left.setText(leftText);

	}

	public void setCentreText(String centreText, int leftid, int rightid) {
		if (0 != leftid && 0 != rightid)
			tv_title_centre.setGravity(Gravity.CENTER);
		else if (0 != leftid)
			tv_title_centre.setGravity(Gravity.LEFT + Gravity.CENTER_VERTICAL);
		else if (0 != rightid)
			tv_title_centre.setGravity(Gravity.RIGHT + Gravity.CENTER_VERTICAL);
		tv_title_centre.setCompoundDrawablesWithIntrinsicBounds(leftid, 0,
				rightid, 0);
		tv_title_centre.setText(centreText);
	}

	public void setRightText(String rightText, int leftid, int rightid) {
		if (0 != leftid && 0 != rightid)
			tv_title_right.setGravity(Gravity.CENTER);
		else if (0 != leftid)
			tv_title_right.setGravity(Gravity.LEFT + Gravity.CENTER_VERTICAL);
		else if (0 != rightid)
			tv_title_right.setGravity(Gravity.RIGHT + Gravity.CENTER_VERTICAL);
		tv_title_right.setCompoundDrawablesWithIntrinsicBounds(leftid, 0,
				rightid, 0);
		tv_title_right.setText(rightText);
	}

	public String getLefttext() {
		return lefttext;
	}

	public String getCentretext() {
		return centretext;
	}

	public String getRighttext() {
		return righttext;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_left:
			if (null != onclickListener) {
				onclickListener.onLeftTitleClicked();
			}
			break;
		case R.id.tv_title_centre:
			if (null != onclickListener) {
				onclickListener.onCenterTitleClicked();
			}
			break;
		case R.id.tv_title_right:
			if (null != onclickListener) {
				onclickListener.onRightTitleClicked();
			}
			break;

		default:
			break;
		}

	}

}
