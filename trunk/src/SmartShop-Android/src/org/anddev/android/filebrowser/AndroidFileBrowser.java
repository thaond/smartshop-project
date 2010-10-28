package org.anddev.android.filebrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.anddev.android.filebrowser.iconifiedlist.IconifiedText;
import org.anddev.android.filebrowser.iconifiedlist.IconifiedTextListAdapter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;

/**
 * The source code is refered from andev.org
 * 
 * @modify VoMinhTam Some mini changes: - Add Callback when user want to choose
 *         file - Finish this activity in case user don't want to choose any
 *         picture - Filter choosed file
 */
public class AndroidFileBrowser extends ListActivity {

	private enum DISPLAYMODE {
		ABSOLUTE, RELATIVE;
	}

	protected static final int SUB_ACTIVITY_REQUEST_CODE = 1337;
	protected static final int RETURN = 1;

	private final DISPLAYMODE displayMode = DISPLAYMODE.RELATIVE;
	private List<IconifiedText> directoryEntries = new ArrayList<IconifiedText>();
	private File currentDirectory = new File("/");
	private String[] filter;

	// private FileChooserCallback callback;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// setTheme(android.R.style.Theme_Dark);

		// Get filter
		filter = getIntent().getStringArrayExtra(Global.FILTER_FILE);
		browseToRoot();
		this.setSelection(0);
	}

	// ===========================================================
	// Menu Creation & Reaction
	// ===========================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, RETURN, 0, getString(R.string.back));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case RETURN:
			setResult(RESULT_CANCELED);
			finish();
			break;

		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	// @Override
	// public boolean onMenuItemSelected(int featureId, Item item) {
	// switch (item.getId()) {
	// case 0:
	// startSubActivity(new Intent(this, DirNameInputActivity.class),
	// SUB_ACTIVITY_REQUEST_CODE);
	// return true;
	// }
	// return false;
	// }

	// ===========================================================
	// Reacting on started subactivities that return
	// ===========================================================
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode,
	// String data, Bundle extras) {
	// super.onActivityResult(requestCode, resultCode, data, extras);
	// // Here We identify the subActivity we starte
	// if (requestCode == SUB_ACTIVITY_REQUEST_CODE) {
	// // And show its result
	// try {
	// if (data.length() > 0) {
	// String dirName = this.currentDirectory.getPath() + "/"
	// + data + "/";
	// dirName = dirName.replace("//", "/");
	// File f = new File(dirName);
	// boolean success = f.mkdir();
	// if (!success)
	// showAlert("Error", "Could not create the directory: "
	// + data + "\nin: "
	// + this.currentDirectory.getPath(), "Ok", false);
	// }
	// } catch (Exception e) {
	// showAlert("Error", "Could not create the directory: " + data
	// + "\nin: " + this.currentDirectory.getPath(), "Ok",
	// false);
	// }
	// }
	// }

	/**
	 * This function browses to the root-directory of the file-system.
	 */
	private void browseToRoot() {
		browseTo(new File("/"));
	}

	/**
	 * This function browses up one level according to the field:
	 * currentDirectory
	 */
	private void upOneLevel() {
		if (this.currentDirectory.getParent() != null)
			this.browseTo(this.currentDirectory.getParentFile());
	}

	private void browseTo(final File aDirectory) {
		// On relative we display the full path in the title.
		if (this.displayMode == DISPLAYMODE.RELATIVE)
			this.setTitle(aDirectory.getAbsolutePath() + " :: "
					+ getString(R.string.app_name));
		if (aDirectory.isDirectory()) {
			this.currentDirectory = aDirectory;
			fill(aDirectory.listFiles());
		} else {
			OnClickListener okButtonListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// AndroidFileBrowser.this.openFile(aDirectory);
					// Filter here
					if (filter != null) {
						boolean isValid = false;
						for (String s : filter) {
							if (aDirectory.getName().endsWith(s)) {
								isValid = true;
								break;
							}
						}
						if (isValid) {
							// Call back to choose this file
							Intent intent = new Intent();
							intent.putExtra(Global.FILE_INTENT_ID, aDirectory);
							setResult(RESULT_OK, intent);
							finish();
						} else {
							Toast
									.makeText(
											AndroidFileBrowser.this,
											getString(R.string.can_not_choose_this_file),
											Toast.LENGTH_SHORT).show();
						}

					} else {
						// Call back to choose this file
						Intent intent = new Intent();
						intent.putExtra(Global.FILE_INTENT_ID, aDirectory);
						setResult(RESULT_OK, intent);
						finish();
					}
					// callback.getFilePath(aDirectory);
				}
			};
			OnClickListener cancelButtonListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// Do nothing ^^
				}
			};

			// Show an Alert with the ButtonListeners we created
			AlertDialog ad = new AlertDialog.Builder(this).setTitle(
					getString(R.string.notice)).setMessage(
					getString(R.string.do_you_want_to_choose_this_file))
					.setPositiveButton(getString(R.string.yes),
							okButtonListener).setNegativeButton(
							getString(R.string.no), cancelButtonListener)
					.create();
			ad.show();
		}
	}

	private void openFile(File aFile) {
		try {
			Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("file://" + aFile.getAbsolutePath()));
			// Intent myIntent = new Intent(android.content.Intent.VIEW_ACTION,
			// new ContentURI("file://" + aFile.getAbsolutePath()));
			startActivity(myIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fill(File[] files) {
		this.directoryEntries.clear();

		// Add the "." == "current directory"
		// this.directoryEntries.add(new IconifiedText(
		// getString(R.string.current_dir),
		// getResources().getDrawable(R.drawable.folder)));

		// and the ".." == 'Up one level'
		if (this.currentDirectory.getParent() != null)
			this.directoryEntries.add(new IconifiedText(
					getString(R.string.up_one_level), getResources()
							.getDrawable(R.drawable.uponelevel)));

		Drawable currentIcon = null;
		if (files != null) {
			for (File currentFile : files) {
				if (currentFile.isDirectory()) {
					currentIcon = getResources().getDrawable(R.drawable.folder);
				} else {
					String fileName = currentFile.getName();
					/*
					 * Determine the Icon to be used, depending on the
					 * FileEndings defined in: res/values/fileendings.xml.
					 */
					if (checkEndsWithInStringArray(fileName, getResources()
							.getStringArray(R.array.fileEndingImage))) {
						currentIcon = getResources().getDrawable(
								R.drawable.image);
					} else if (checkEndsWithInStringArray(fileName,
							getResources().getStringArray(
									R.array.fileEndingWebText))) {
						currentIcon = getResources().getDrawable(
								R.drawable.webtext);
					} else if (checkEndsWithInStringArray(fileName,
							getResources().getStringArray(
									R.array.fileEndingPackage))) {
						currentIcon = getResources().getDrawable(
								R.drawable.packed);
					} else if (checkEndsWithInStringArray(fileName,
							getResources().getStringArray(
									R.array.fileEndingAudio))) {
						currentIcon = getResources().getDrawable(
								R.drawable.audio);
					} else {
						currentIcon = getResources().getDrawable(
								R.drawable.text);
					}
				}
				switch (this.displayMode) {
				case ABSOLUTE:
					/* On absolute Mode, we show the full path */
					this.directoryEntries.add(new IconifiedText(currentFile
							.getPath(), currentIcon));
					break;
				case RELATIVE:
					/*
					 * On relative Mode, we have to cut the current-path at the
					 * beginning
					 */
					int currentPathStringLenght = this.currentDirectory
							.getAbsolutePath().length();
					this.directoryEntries.add(new IconifiedText(currentFile
							.getAbsolutePath().substring(
									currentPathStringLenght), currentIcon));

					break;
				}
			}
		}
		Collections.sort(this.directoryEntries);

		IconifiedTextListAdapter itla = new IconifiedTextListAdapter(this);
		itla.setListItems(this.directoryEntries);
		this.setListAdapter(itla);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		int selectionRowID = position;
		String selectedFileString = this.directoryEntries.get(selectionRowID)
				.getText();

		// TODO
		// if (selectedFileString.equals(getString(R.string.current_dir))) {
		// // Refresh
		// this.browseTo(this.currentDirectory);
		// } else

		if (selectedFileString.equals(getString(R.string.up_one_level))) {
			this.upOneLevel();
		} else {
			File clickedFile = null;
			switch (this.displayMode) {
			case RELATIVE:
				clickedFile = new File(this.currentDirectory.getAbsolutePath()
						+ this.directoryEntries.get(selectionRowID).getText());
				break;
			case ABSOLUTE:
				clickedFile = new File(this.directoryEntries
						.get(selectionRowID).getText());
				break;
			}
			if (clickedFile != null)
				this.browseTo(clickedFile);
		}
	}

	/**
	 * Checks whether checkItsEnd ends with one of the Strings from fileEndings
	 */
	private boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	public void setFilter(String[] filters) {
		this.filter = filters;
	}

	// public void setCallback(FileChooserCallback callback){
	// this.callback = callback;
	// }
}