package com.fwj.mobilesafe.fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fwj.mobilesafe.R;

@SuppressLint("NewApi")
public class SDFragment extends Fragment {
	// 初始化fragement 布局
	private TextView tv_scanner;
	private ProgressBar pb_process;
	private FragmentActivity activity;
	private List<CacheInfo> cacheInfos;
	private ListView lv_cache;
	private PackageManager manager;
	private Button btn_all;
	private CacheAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("onCreateView");

		return inflater.inflate(R.layout.fragment_cache, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//  1 
		cacheInfos = new ArrayList<SDFragment.CacheInfo>();
		activity = getActivity();
		tv_scanner = (TextView) activity.findViewById(R.id.tv_scanner);
		pb_process = (ProgressBar) activity.findViewById(R.id.pb_process);
		lv_cache = (ListView) activity.findViewById(R.id.lv_cache);
		btn_all = (Button) activity.findViewById(R.id.btn_all);
		
		// 5 
		btn_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Class<?> loadClass;
				try {
					loadClass = SDFragment.class.getClassLoader().loadClass(
							"android.content.pm.PackageManager");
					Method declaredMethod = loadClass
					.getDeclaredMethod("freeStorageAndNotify", Long.TYPE,
							IPackageDataObserver.class);
					
					declaredMethod.invoke(manager, Long.MAX_VALUE,new MyIPackageDataObserver());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//2 
		scanner();
	}

	//   5.1 清理全部的回调方法
	private class MyIPackageDataObserver extends IPackageDataObserver.Stub{
		// 清理所有缓存成功 回调方法   异步线程中
		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					cacheInfos.clear();
					adapter.notifyDataSetChanged();
					btn_all.setVisibility(View.INVISIBLE);
				}
			});
			
			
		}
		
	}

	//  4.0 缓存的实体对象
	private class CacheInfo {
		private String packageName;
		private String cache;

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getCache() {
			return cache;
		}

		public void setCache(String cache) {
			this.cache = cache;
		}

		@Override
		public String toString() {
			return "CacheInfo [packageName=" + packageName + ", cache=" + cache
					+ "]";
		}

	}
	// 3 
	IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
		// 获取到缓存信息完成的时候会调用
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cacheSize = pStats.cacheSize;
			// long codeSize=pStats.codeSize;
			// long dataSize=pStats.dataSize;
			String packageName = pStats.packageName;
			String cache = Formatter.formatFileSize(activity, cacheSize);
			// String code=Formatter.formatFileSize(activity, codeSize);
			// String data=Formatter.formatFileSize(activity, dataSize);
			if (cacheSize > 0) {
				CacheInfo info = new CacheInfo();
				info.setCache(cache);
				info.setPackageName(packageName);
				cacheInfos.add(info);
			}

		}

	};
	// 2 扫描 
	private void scanner() {
		tv_scanner.setText("正在初始化8核扫描引擎....");
		new Thread() {

			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				manager = activity.getPackageManager();
				List<PackageInfo> installedPackages = manager
						.getInstalledPackages(0);
				pb_process.setMax(installedPackages.size());
				int count = 0;
				for (PackageInfo info : installedPackages) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String packageName = info.packageName;
					final String label = info.applicationInfo
							.loadLabel(manager).toString();
					// manager.getPackageSizeInfo(packageName,mStatsObserver);
					// 反射了packageManager
					try {
						// 3 
						Class<?> loadClass = CacheFragment.class
								.getClassLoader().loadClass(
										"android.content.pm.PackageManager");
						Method declaredMethod = loadClass.getDeclaredMethod(
								"getPackageSizeInfo", String.class,
								IPackageStatsObserver.class);
						declaredMethod.invoke(manager, packageName,
								mStatsObserver);
					} catch (Exception e) {
						e.printStackTrace();
					}

					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							tv_scanner.setText("正在扫描" + label);
						}
					});

					// System.out.println(label+"..."+digest);

					count++;
					pb_process.setProgress(count);
				}
				// 扫描完成
				activity.runOnUiThread(new Runnable() {

					
					@Override
					public void run() {
						
						if (cacheInfos.size() < 1) {
							tv_scanner.setText("您的手机没有任何缓存");
						} else {
							// 4.0
							btn_all.setVisibility(View.VISIBLE);
							tv_scanner.setText("您的手机发现了缓存");
							adapter = new CacheAdapter();
							lv_cache.setAdapter(adapter);

							lv_cache.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									CacheInfo cacheInfo = cacheInfos
											.get(position);
									String packageName = cacheInfo
											.getPackageName();
									Intent intent = new Intent();
									intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
									intent.setData(Uri.parse("package:"
											+ packageName));
									startActivity(intent);
								}
							});
						}
					}
				});
			}
		}.start();
	}
	// 4.1
	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_cache_size;
	}
	//4.1
	private class CacheAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cacheInfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CacheInfo cacheInfo = cacheInfos.get(position);
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(activity, R.layout.item_cache, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_cache_size = (TextView) view
						.findViewById(R.id.tv_cache_size);
			}
			String packageName = cacheInfo.getPackageName();
			ApplicationInfo applicationInfo;
			try {
				applicationInfo = manager.getApplicationInfo(packageName, 0);
				Drawable loadIcon = applicationInfo.loadIcon(manager);
				holder.iv_icon.setImageDrawable(loadIcon);
				String loadLabel = applicationInfo.loadLabel(manager)
						.toString();
				holder.tv_name.setText(loadLabel);
				holder.tv_cache_size.setText(cacheInfo.getCache());

			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}



}
