package com.index.facturapp;


import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.index.facturapp.adapters.Adaptercatprod;
import com.index.facturapp.clasesextra.Categoria;
import com.index.facturapp.clasesextra.Producto;
import com.index.facturapp.dades.FacturaDB;

public class Managecatprod extends ActionBarActivity implements
		ActionBar.TabListener {

	private int selectedTab = 0;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onStop() {
		FacturaDB fdb = new FacturaDB(this);
		fdb.close();
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		FacturaDB fdb = new FacturaDB(this);
		fdb.close();
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managecatprod);
		// Set up the action bar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						selectedTab = position;
						
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.managecatprod, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.anadir2) {
			final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			if(selectedTab == 0){
				final View layout = inflater.inflate(R.layout.categoridialog, null);
				dialog.setView(layout);
				dialog.setPositiveButton("A�adir", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int id){
						EditText campocategoria = (EditText) layout.findViewById(R.id.nuevacategoria);
						String categoria = campocategoria.getText().toString();
						FacturaDB fdb = new FacturaDB(getBaseContext());
						Categoria cate = new Categoria();
						cate.setCategoria(categoria);
						//CatFragment fragment = (CatFragment) mSectionsPagerAdapter.getItem(selectedTab);
						CatFragment.addItem(categoria);
						cate.setId(CatFragment.ncategoria() - 1);
						fdb.createCategoria(cate);
					}
				});
				dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
				dialog.show();
			}
			else {
				final View layout2 = inflater.inflate(R.layout.productodialog, null);
				dialog.setView(layout2);
				Spinner campocat = (Spinner)layout2.findViewById(R.id.categoriaprod);
				FacturaDB fdb = new FacturaDB(getBaseContext());
				List<String> values = fdb.getCategorias();
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
				campocat.setAdapter(adapter);
				dialog.setPositiveButton("A�adir", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int id){
						EditText camponombre = (EditText) layout2.findViewById(R.id.nombreprod);
						EditText campoprecio = (EditText) layout2.findViewById(R.id.precioprod);
						Spinner campocat = (Spinner)layout2.findViewById(R.id.categoriaprod);
						FacturaDB fdb = new FacturaDB(getBaseContext());
						Producto prod = new Producto();
						prod.setNombre(camponombre.getText().toString());
						prod.setPrecio(Float.valueOf(campoprecio.getText().toString()));
						prod.setCategoria(fdb.getCategoria(campocat.getSelectedItem().toString()));
						ProdFragment.addItem(camponombre.getText().toString());
						fdb.createProducto(prod);
					}
				});
				dialog.show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			if(position == 0)
			return (Fragment)new CatFragment();
			else if(position == 1);
			return (Fragment)new ProdFragment();
			
			//return PlaceholderFragment.newInstance(position+1);
			
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Categorias";
			case 1:
				return "Productos";
			}
			return null;
		}
	}

	
	public static class CatFragment extends ListFragment {
		
		private static Adaptercatprod adapter;
		int pos;
		
		public CatFragment() {
			pos = 0;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(android.R.layout.list_content, container, false);
			return rootView;
		}

		@Override
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			final FacturaDB fdb = new FacturaDB(getActivity());
			List<String> values;
			values = fdb.getCategorias();
			adapter = new Adaptercatprod(getActivity(),values, "white", true);
			setListAdapter(adapter);
			getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
				@Override
		        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		                int position, long id) {
					pos = position;
					if (position == 0){
						Toast.makeText(getActivity(), "No se puede borrar", Toast.LENGTH_SHORT).show();
					}
					else {
						final int pos = position;
						final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
						String[] items = {"Eliminar", "Cancelar"};
						dialog.setTitle("Estas seguro de eliminar " + adapter.getItem(position) + "?");
						dialog.setItems(items, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0){
									String paborrar = adapter.getItem(pos);
									adapter.remove(paborrar);
									fdb.removeCategoria(paborrar);
								}
								else {
									dialog.dismiss();
								}
							}
						});
						dialog.show();
					}
					
					return true;
				}
			});
			
		}
		
		public static void addItem(String categoria){
			adapter.add(categoria);
		}
		
		public static int ncategoria (){
			return adapter.getCount();
		}

		
		
		
		
	}
	
	public static class ProdFragment extends ListFragment {
		
		private static Adaptercatprod adapter;
		
		public ProdFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(android.R.layout.list_content,
					container, false);
			return rootView;
		}
		
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			final FacturaDB fdb = new FacturaDB(getActivity());
			List<String> values;
			values = fdb.getProductos();
			adapter = new Adaptercatprod(getActivity(),values, "white", false);
			setListAdapter(adapter);
			getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
				@Override
		        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		                int position, long id) {
					final int pos = position;	
					final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
					String[] items = {"Editar", "Eliminar", "Cancelar"};
					dialog.setTitle("Que desea hacer con " + adapter.getItem(position) + "?");
					dialog.setItems(items, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							if (which == 0){
								final FacturaDB fdb = new FacturaDB(getActivity());
								final AlertDialog.Builder dialog2 = new AlertDialog.Builder(getActivity());
								LayoutInflater inflater = getActivity().getLayoutInflater();
								final View layout2 = inflater.inflate(R.layout.productodialog, null);
								dialog2.setView(layout2);
								Spinner campocat = (Spinner)layout2.findViewById(R.id.categoriaprod);
								List<String> values = fdb.getCategorias();
								ArrayAdapter<String> adapterspin = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, values);
								campocat.setAdapter(adapterspin);
								final String nprod = adapter.getItem(pos);
								Producto prod = fdb.getProducto(nprod);
								EditText camponombre = (EditText) layout2.findViewById(R.id.nombreprod);
								EditText campoprecio = (EditText) layout2.findViewById(R.id.precioprod);
								camponombre.setText(prod.getNombre());
								campoprecio.setText(String.valueOf(prod.getPrecio()));
								int posspin = adapterspin.getPosition(prod.getCategoria().getCategoria());
								campocat.setSelection(posspin);
								dialog2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										
									}
								});
								dialog2.setPositiveButton("Actualizar", new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog, int id){
										EditText camponombre = (EditText) layout2.findViewById(R.id.nombreprod);
										EditText campoprecio = (EditText) layout2.findViewById(R.id.precioprod);
										Spinner campocat = (Spinner)layout2.findViewById(R.id.categoriaprod);
										Producto prod = new Producto();
										prod.setNombre(camponombre.getText().toString());
										prod.setPrecio(Float.valueOf(campoprecio.getText().toString()));
										prod.setCategoria(fdb.getCategoria(campocat.getSelectedItem().toString()));
										ProdFragment.addItem(camponombre.getText().toString());
										fdb.updateProducto(prod);
									}
								});
								
								dialog2.show();
							}
							else if (which == 1){
								String paborrar = adapter.getItem(pos);
								adapter.remove(paborrar);
								fdb.removeProducto(paborrar);
							}
							else {
								dialog.dismiss();
							}
						}
					});
					dialog.show();
					return true;
				}
			});
		}
		
		public static void addItem(String producto){
			adapter.add(producto);
		}
	}

}
