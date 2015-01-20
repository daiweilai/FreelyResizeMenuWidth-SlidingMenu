


package com.DavidDay.slidingmenulibrarydemo;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

public class ContentActivity extends SlidingFragmentActivity  {
	private SlidingMenu sm;
	protected static boolean isOpen , is1 , is2 , is3 ,  is4;
	private MainActivity ma;
	public static ProgressDialog mDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		initSlidingMenu(savedInstanceState);
	}

	/**
	 * 初始化侧滑栏
	 * @param savedInstanceState
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setOnClosedListener(new OnClosedListener() {
				
				@Override
				public void onClosed() {
					MainActivity s =(MainActivity)getParent();
					s.setCurrTabIndex();
					isOpen = false;	
				}
			});
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);	
		} else {
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		sm = getSlidingMenu();
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.0f);
		
		isOpen = false;
		is3 = false;
		is1 = false;
		is2 = false;
		is4 = false;
	}

	public void onDefaultPressed(){
		
		if(isOpen){
			isOpen = false;
			is3 = false;
			is1 = true;
			is2 = false;
			is4 = false;
			toggle();
		}	
	}
	
	public void onMenu1Pressed(){
		if(isOpen){
			if(is2){
				toggle();
				isOpen = false;
				is2 = false;				
			}
			else{
				if(is1){
					SetSlidemenuOffset(0);
					switchMenu(new MenuFragment() , 2);

				}
				else{
					SetSlidemenuOffset(0);
					switchMenu(new MenuFragment() , 1);
				}
				isOpen = true;
				is3 = false;
				is1 = false;
				is2 = true;
				is4 = false;
			}
			
			
			
		}
		else{
			SetSlidemenuOffset(0);
			switchMenu(new MenuFragment(),0);
			isOpen = true;
			is2 = true;
			is3 = false;
			is1 = false;
			is4 = false;
			toggle();
		}
		
	}

	public void onMenu2Pressed(){
		if(isOpen){
			if(is3){
				toggle();
				isOpen = false;
				is3 = false;				
			}
			else{
				if(is1||is2){
					SetSlidemenuOffset(1);
					switchMenu(new MenuFragment() , 2);

				}
				else{
					SetSlidemenuOffset(1);
					switchMenu(new MenuFragment() , 1);
				}
				isOpen = true;
				is3 = true;
				is1 = false;
				is2 = false;
				is4 = false;
			}
			
			
			
		}
		else{
			SetSlidemenuOffset(1);
			switchMenu(new MenuFragment(),0);
			toggle();
			isOpen = true;
			is2 = false;
			is3 = true;
			is1 = false;
			is4 = false;
		}
	}

	public void onMenu3Pressed(){
		if(isOpen){
			if(is4){
				toggle();
				isOpen = false;
				is4 = false;				
			}
			else{
				
				SetSlidemenuOffset(2);
				switchMenu(new MenuFragment() , 2);
				isOpen = true;
				is3 = false;
				is1 = false;
				is2 = false;
				is4 = true;
			}	
		}
		else{
			SetSlidemenuOffset(2);
			switchMenu(new MenuFragment(),0);
			toggle();
			isOpen = true;
			is2 = false;
			is3 = false;
			is1 = false;
			is4 = true;
		}
	}
	
	/**
	 * 切换侧滑菜单内的内容
	 * @param fragment 菜单中具体功能的提供者
	 * @param mode 切换模式，主要区别是提供切换动画效果 0：默认模式 不具有动画效果 ；1：从小往上滑动切换 ； 2：从上往下滑动切换
	 */
	private void switchMenu(final Fragment fragment ,int mode) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		switch (mode) {
		case 0:
			ft.replace(R.id.menu_frame, fragment).commit();
			
			break;
		case 1:
			
//			ft.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_down,0,0);
			ft.replace(R.id.menu_frame, fragment).commit();
			
			break;
		case 2:
			
//			ft.setCustomAnimations(R.anim.push_up_out, R.anim.push_up_out,0,0);
			ft.replace(R.id.menu_frame, fragment).commit();
			
			break;

		default:
			
			break;
		}	
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
				is3 = false;
				is1 = false;
				is2 = false;
				is4 = false;
				if(isOpen){
					toggle();
					isOpen = false;
				}
				else{	
					ma.BackPressed();
				}
				return true;
			}
		if(keyCode == KeyEvent.KEYCODE_HOME){
			finish();	
		}
		return true;	
	}
	
	/**
	 * 动态调整slidingmenu的宽度
	 * @param mode 0为0dp 1为500dp  2为350dp
	 */
	public void SetSlidemenuOffset(int mode){
		 switch(mode){
		 case 0:
			 sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_200dp);
			 break;
		 case 1:
			 sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_350dp);	 
			 break; 
		 case 2:
			 sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_500dp);	 
			 break;  
			 
		 default:
			 sm.setBehindOffsetRes(R.dimen.slidingmenu_offset_200dp);
			 break;
		 }
	 }
	
	

	
}
