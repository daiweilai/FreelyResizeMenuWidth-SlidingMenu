
package com.DavidDay.slidingmenulibrarydemo;
import java.util.ArrayList;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener{
	public static final String TAG = "MainActivity";
	private LocalActivityManager activitymanager;
	private LinearLayout layout_load;
    private LinearLayout columnTitleLayout;
    private ArrayList<String> array = new ArrayList<String>();
    private ImageView animImage;
    public static int currTabIndex;
	public static int lastTabIndex;
	private TranslateAnimation animation; 
	public static int mainToolCount,subToolCount;
	private long waitTime = 2000;
    private long touchTime = 0;
	public ContentActivity ca; 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		initSubView();
		initView();
	}

	/**
	 * 初始化控件
	 */	
	private void initView() {
		ca = (ContentActivity)getCurrentActivity();
		getLayoutInflater();
		animImage = (ImageView)findViewById(R.id.iv_start_tools_indicator);
		columnTitleLayout = (LinearLayout) findViewById(R.id.column_title_layout);
		defaultToolsLoad();	
	}
	
	private void defaultToolsLoad() {
		array.clear();
		String[] resource = this .getResources().getStringArray(R.array.default_tools);
		mainToolCount = resource.length;
		for(int i = 0 ; i<resource.length ; i++){
			String name = resource[i];
			array.add(name);
		}
		this.columnTitleLayout.removeAllViews();
		currTabIndex = 0;
		int i = 0;
		animImage.setBackgroundColor(getResources().getColor(R.color.mainColor));
		for(i=0 ; i<array.size() ; i++){
			String str = array.get(i);
			TextView columnTextView = new TextView(this);
			
			columnTextView.setText(str);
			columnTextView.setTag(i);			
			columnTextView.setWidth(getResources().getDimensionPixelOffset(R.dimen.tools_width));
			columnTextView.setHeight(getResources().getDimensionPixelOffset(R.dimen.tools_height));
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setTextAppearance(this , R.style.column_tx_style);
			columnTextView.setOnClickListener(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			columnTitleLayout.addView(columnTextView,params);	
		}
		((TextView) columnTitleLayout.getChildAt(0)).setTextColor(Color.WHITE);	
	}

	/**
	 * 初始化子activity
	 */
	private void initSubView() {
		 activitymanager = getLocalActivityManager();
		 layout_load = (LinearLayout) findViewById(R.id.LinearLayout_content);
		 Intent intent = new Intent(MainActivity.this, ContentActivity.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 Window w = activitymanager.startActivity("A",intent);
         View v = w.getDecorView();
         layout_load.removeAllViews();
         layout_load.setPadding(0, 0, 0, 0);
         layout_load.addView(v);
	}
	/**
	 * 左侧工具栏点击事件
	 */
	@Override
	public void onClick(View v) {
		if(!(((TextView) v).getText().toString().equals(""))){
				int k = (Integer)v.getTag();
				lastTabIndex = currTabIndex;
				currTabIndex = k;

			if (lastTabIndex != currTabIndex) {

				if (currTabIndex == array.size()) {
					return;
				}
				showAnimation();
				if(((TextView) v).getText().toString().equals("Close")){
					ca.onDefaultPressed();
				}
				else if(((TextView) v).getText().toString().equals("Menu1")){
					ca.onMenu1Pressed();
				}
				else if(((TextView) v).getText().toString().equals("Menu2")){
					ca.onMenu2Pressed();
				}
				else if(((TextView) v).getText().toString().equals("Menu3")){
					ca.onMenu3Pressed();
				}
			}
			else{
				if(((TextView) v).getText().toString().equals("Close")){
					ca.onDefaultPressed();
				}
				else if(((TextView) v).getText().toString().equals("Menu1")){
					ca.onMenu1Pressed();
				}
				else if(((TextView) v).getText().toString().equals("Menu2")){
					ca.onMenu2Pressed();
				}
				else if(((TextView) v).getText().toString().equals("Menu3")){
					ca.onMenu3Pressed();
				}

			}
		}
		
	}
	
	/**
	 * 工具栏的滑块滑动的动画效果
	 */
	private void showAnimation() {
		if (lastTabIndex == currTabIndex) {
			return;
		}
		else{
			int widgetItemHeight = ((TextView) columnTitleLayout.getChildAt(lastTabIndex)).getHeight();
			int fromY = lastTabIndex * widgetItemHeight;
			int toY = currTabIndex * widgetItemHeight;
			animation = new TranslateAnimation(0, 0, fromY, toY);
			animation.setDuration(200);
			animation.setFillAfter(true);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					((TextView) columnTitleLayout.getChildAt(lastTabIndex)).setTextColor(Color.BLACK);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					
					((TextView) columnTitleLayout.getChildAt(currTabIndex)).setTextColor(Color.WHITE);
					lastTabIndex = currTabIndex;
				}
			});
			
			animImage.startAnimation(animation);
			
		}
		
	}


	


	/**
	 * 恢复工具栏到初始化状态
	 */
	public void setCurrTabIndex(){

		currTabIndex = 0;
		showAnimation();
}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		ca.onKeyDown(keyCode, event);
		return false;
	}
	
	@Override
	public void onBackPressed() {
		//什么也不做 全部交给BackPressed()来做
	}

	/**
	 * 快速点击返回 退出应用
	 */
	public void BackPressed() {

		long currentTime = System.currentTimeMillis();
		if((currentTime-touchTime)>=waitTime) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		}else {
			finish();
		}
	}

}
