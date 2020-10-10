package net.thedanpage.game.world.map.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.thedanpage.game.framework.gamestate.WorldState;
import net.thedanpage.game.world.map.Map;

public class SaveSystem {

	private static final FileNameExtensionFilter filter = new FileNameExtensionFilter("Game Save Files", "gam");

	private static boolean currentlySaving = false;

	public static void saveWorldToFilePrompt() {
		currentlySaving = true;

		// parent component of the dialog
		JFrame parentFrame = new JFrame();

		JFileChooser fileChooser = new JFileChooser() {
			private static final long serialVersionUID = 1L;

			@Override
			public void approveSelection() {
				File selectedFile = getSelectedFile();
				if (selectedFile.exists() && getDialogType() == JFileChooser.SAVE_DIALOG) {
					int result = JOptionPane.showConfirmDialog(this, "Do you want to overwrite?", "File already exists",
							JOptionPane.YES_NO_OPTION);
					if (result != JOptionPane.YES_OPTION) {
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};
		fileChooser.setDialogTitle("Save world");
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int userSelection = fileChooser.showSaveDialog(parentFrame);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();

			if (!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".gam")) {
				fileToSave = new File(fileChooser.getSelectedFile() + ".gam");
			}

			try {
				FileOutputStream f = new FileOutputStream(new File(fileToSave.getAbsolutePath()));
				ObjectOutputStream o = new ObjectOutputStream(f);

				o.writeObject(WorldState.getMap());

				o.close();
				f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Saving is finished
		currentlySaving = false;

	}

	public static void loadWorldFromFilePrompt() throws Exception {

		// parent component of the dialog
		JFrame parentFrame = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Load world");

		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileFilter() {

			public String getDescription() {
				return "Game Save Files (*.gam)";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".gam");
				}
			}
		});

		int result = fileChooser.showOpenDialog(parentFrame);
		if (result == JFileChooser.APPROVE_OPTION) {
			;
			FileInputStream fi = new FileInputStream(fileChooser.getSelectedFile());
			ObjectInputStream oi = new ObjectInputStream(fi);

			WorldState.loadMap((Map) oi.readObject());

			oi.close();
			fi.close();
		}

	}
	
	public static void newMap() {
		String response = JOptionPane.showInputDialog(null, "Seed:", "Create new world", JOptionPane.INFORMATION_MESSAGE);
		if (response != null) {
			WorldState.newMap(response);
		}
	}

	public static boolean isCurrentlySaving() {
		return currentlySaving;
	}

}
