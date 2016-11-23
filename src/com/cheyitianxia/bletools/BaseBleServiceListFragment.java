package com.cheyitianxia.bletools;

import java.util.ArrayList;
import java.util.List;

import com.cytx.model.CharacterModel;
import com.cytx.model.ServiceModel;
import com.cytx.view.CharacterView;
import com.cytx.view.ServiceView;
import com.cytx.view.CharacterView.OnEnableClick;
import com.mt.mtbletools.R;

import android.bluetooth.BluetoothGattCharacteristic;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class BaseBleServiceListFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (view != null) {
			return view;
		}
		view = inflater.inflate(R.layout.fragment_basebleserviceslist, container, false);

		initView();

		return view;
	}
	
	// 初始化控件
	private ExpandableListView service_list_view;
	private List<ServiceModel> grounps;// = new ArrayList<ServiceModel>();
	private List<List<CharacterModel>> childs;// = new ArrayList<List<CharacterModel>>();
	private void initView(){
		service_list_view = (ExpandableListView) view.findViewById(R.id.service_list_view);
		if(grounps == null){
			grounps = new ArrayList<ServiceModel>();
		}
		if(childs == null){
			childs = new ArrayList<List<CharacterModel>>();
		}
		
		service_list_view.setAdapter(service_list_adapter);
		
		service_list_view.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				onClick(groupPosition, childPosition);
				return false;
			}
		});
	}
	
	// 适配器
	private ExpandableListAdapter service_list_adapter = new ExpandableListAdapter() {
		
		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onGroupExpanded(int groupPosition) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onGroupCollapsed(int groupPosition) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = new ServiceView(getContext());
			}

			((ServiceView) convertView).setDatas(getGroup(groupPosition));
			return convertView;
		}
		
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		
		@Override
		public int getGroupCount() {
			return grounps.size();
		}
		
		@Override
		public ServiceModel getGroup(int groupPosition) {
			return grounps.get(groupPosition);
		}
		
		@Override
		public long getCombinedGroupId(long groupId) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public long getCombinedChildId(long groupId, long childId) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			return childs.get(groupPosition).size();
		}
		
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			if (null == convertView) {
				convertView = new CharacterView(getContext());
			}
			CharacterView view = (CharacterView) convertView;
			final CharacterModel childmodel = getChild(groupPosition, childPosition);
			view.setDatas(childmodel);
			view.setEnableClick(new OnEnableClick() {
				
				@Override
				public void onEnable(boolean enable) {
					onEnableClick(childmodel.getCharact(), enable);
				}
			});
			return convertView;
		}
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}
		
		@Override
		public CharacterModel getChild(int groupPosition, int childPosition) {
			return childs.get(groupPosition).get(childPosition);
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}
	};
	
	// 按键监听
	protected void onClick(int grounpid, int childid){
		
	}
	
	// 使能监听
	protected void onEnableClick(BluetoothGattCharacteristic charact, boolean enable){
		
	}
	
	// 设置数据
	public void setDatas(List<ServiceModel> grounps, List<List<CharacterModel>> childs){
		this.grounps = grounps;
		this.childs = childs;
	}
}
