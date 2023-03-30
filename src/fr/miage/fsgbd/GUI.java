package fr.miage.fsgbd;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @author Galli Gregory, Mopolo Moke Gabriel
 */
public class GUI extends JFrame implements ActionListener
{
	TestInteger testInt = new TestInteger();
	BTreePlus bInt;

	private JButton buttonClean, buttonRemove, buttonLoad, buttonLoadIndex, buttonLoadRecherche, buttonLoadRechercheSeq, buttonSave, buttonAddMany, buttonAddItem, buttonRefresh;
	TextArea txtarea = new TextArea("Durée de la recherche");
	private JTextField txtNbreItem, txtFileRecherche, txtNbreSpecificItem, txtU, txtFile, txtFileIndex, removeSpecific;
	private final JTree tree = new JTree();

	public GUI()
	{
		super();
		build();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == buttonLoad || e.getSource() == buttonClean || e.getSource() == buttonSave || e.getSource() == buttonRefresh)
		{
			if (e.getSource() == buttonLoad)
			{
				BDeserializer<Integer> load = new BDeserializer<Integer>();
				bInt = load.getArbre(txtFile.getText());
			}
			else if (e.getSource() == buttonClean)
			{
				if (Integer.parseInt(txtU.getText()) >= 2)
					bInt = new BTreePlus(Integer.parseInt(txtU.getText()), testInt);
			}
			else if (e.getSource() == buttonSave)
			{
				new BSerializer(bInt, txtFile.getText());
			}
			else if (e.getSource() == buttonRefresh)
			{
				tree.updateUI();
			}
		}
		else
		{
			if (bInt == null)
				bInt = new BTreePlus(Integer.parseInt(txtU.getText()), testInt);

			if (e.getSource() == buttonAddMany)
			{
				for (int i = 0; i < Integer.parseInt(txtNbreItem.getText()); i++)
				{
					int firstnumber = (int) (Math.random() * 10 * Integer.parseInt(txtNbreItem.getText()));
					int secondnumber = (int) (Math.random() * 10 * Integer.parseInt(txtNbreItem.getText()));
					bInt.addValeur(firstnumber+","+secondnumber);
				}
			}
			else if (e.getSource() == buttonLoadIndex)
			{
				System.out.println(txtFileIndex.getText());
				File file = new File(txtFileIndex.getText());
				// Créer l'objet File Reader
				FileReader fr;
				try {
					fr = new FileReader(file);
					// Créer l'objet BufferedReader
					BufferedReader br = new BufferedReader(fr);
					String line;

					while ((line = br.readLine()) != null)
					{
						// ajoute la ligne au buffer
						String[] splt = line.split(",");

						if (splt.length > 1)
						{
							String res = splt[0] + "," + splt[1];
							bInt.addValeur(res);
						}
					}
					fr.close();
				}
				catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			else if (e.getSource() == buttonAddItem)
			{
				if (!bInt.addValeur(txtNbreSpecificItem.getText()))
					System.out.println("Tentative d'ajout d'une valeur existante : " + txtNbreSpecificItem.getText());

				txtNbreSpecificItem.setText(txtNbreSpecificItem.getText());
			}
			else if (e.getSource() == buttonRemove)
			{
				bInt.removeValeur(removeSpecific.getText());
			}
			else if (e.getSource() == buttonLoadRecherche)
			{
				long timestampMS = System.currentTimeMillis();
				boolean isNumeric =  txtFileRecherche.getText().trim().matches("[+-]?\\d*(\\.\\d+)?");
				Noeud<Integer> noeud=null;

				if (isNumeric)
				{
					noeud = bInt.searchValue(Integer.parseInt(txtFileRecherche.getText().trim()));
				}

				double timestampFinal = System.currentTimeMillis() - timestampMS;
				String txtres = "Recherche de l'arbre terminée en " + timestampFinal + "ms";

				if (noeud != null)
					txtres += "\nIndex trouvé dans le noeud : " + noeud.toString();
				else
					txtres += "\nIndex pas trouvé";

				txtarea.setText(txtres);
			}
			else if (e.getSource() == buttonLoadRechercheSeq)
			{
				File file = new File(txtFileIndex.getText());
				// Créer l'objet File Reader
				FileReader fr;

				try
				{
					fr = new FileReader(file);
					// Créer l'objet BufferedReader
					BufferedReader br = new BufferedReader(fr);
					String line;
					boolean trouve = false;
					long timestampMS = System.currentTimeMillis();
					String nbligne = "";
					while ((line = br.readLine()) != null && trouve == false)
					{
						// ajoute la ligne au buffer
						String[] splt = line.split(",");
						if (splt.length > 1) {
							if (splt[1].equals(txtFileRecherche.getText().trim())) {
								trouve = true;
								nbligne = splt[0];
							}
						}
					}

					double timestampFinal = System.currentTimeMillis() - timestampMS;
					String txtres = "Recherche de l'arbre terminée en " + timestampFinal + "ms";
					if (trouve)
						txtres += "\nIndex trouvé à la ligne: " + nbligne;
					else
						txtres += "\nIndex pas trouvé";
					txtarea.setText(txtres);
					fr.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		tree.setModel(new DefaultTreeModel(bInt.bArbreToJTree()));
		for (int i = 0; i < tree.getRowCount(); i++)
			tree.expandRow(i);

		tree.updateUI();
	}

	private void build() {
		setTitle("Indexation - B Arbre");
		setSize(760, 760);
		setLocationRelativeTo(this);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(buildContentPane());
	}

	private JPanel buildContentPane() {
		GridBagLayout gLayGlob = new GridBagLayout();

		JPanel pane1 = new JPanel();
		pane1.setLayout(gLayGlob);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 5, 2, 0);

		JLabel labelU = new JLabel("Nombre max de clés par noeud (2m): ");
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		pane1.add(labelU, c);

		txtU = new JTextField("4", 7);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 2;
		pane1.add(txtU, c);

		JLabel labelBetween = new JLabel("Nombre de clefs à ajouter:");
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		pane1.add(labelBetween, c);

		txtNbreItem = new JTextField("10000", 7);
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		pane1.add(txtNbreItem, c);

		buttonAddMany = new JButton("Ajouter n éléments aléatoires à l'arbre");
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 1;
		c.gridwidth = 2;
		pane1.add(buttonAddMany, c);

		JLabel labelSpecific = new JLabel("Ajouter une valeur spécifique:");
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(labelSpecific, c);

		txtNbreSpecificItem = new JTextField("10,50", 7);
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(txtNbreSpecificItem, c);

		buttonAddItem = new JButton("Ajouter l'élément");
		c.gridx = 2;
		c.gridy = 3;
		c.weightx = 1;
		c.gridwidth = 2;
		pane1.add(buttonAddItem, c);

		JLabel labelRemoveSpecific = new JLabel("Retirer une valeur spécifique:");
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(labelRemoveSpecific, c);

		removeSpecific = new JTextField("10,54", 7);
		c.gridx = 1;
		c.gridy = 4;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(removeSpecific, c);

		buttonRemove = new JButton("Supprimer l'élément n de l'arbre");
		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 1;
		c.gridwidth = 2;
		pane1.add(buttonRemove, c);

		JLabel labelFilename = new JLabel("Deserializer à partir de fichier : ");
		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(labelFilename, c);

		txtFile = new JTextField("arbre.abr", 7);
		c.gridx = 1;
		c.gridy = 5;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(txtFile, c);

		JLabel labelFileindex = new JLabel("Arbre à partir de fichier texte : ");
		c.gridx = 0;
		c.gridy = 6;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(labelFileindex, c);

		txtFileIndex = new JTextField("data.csv", 7);
		c.gridx = 1;
		c.gridy = 6;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(txtFileIndex, c);

		JLabel labelFileRecherche = new JLabel("Index à chercher (arbre ou fichier) : ");
		c.gridx = 0;
		c.gridy = 7;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(labelFileRecherche, c);

		txtFileRecherche = new JTextField("497429560", 7);
		c.gridx = 1;
		c.gridy = 7;
		c.weightx = 1;
		c.gridwidth = 1;
		pane1.add(txtFileRecherche, c);

		buttonSave = new JButton("Sauvegarder l'arbre");
		c.gridx = 2;
		c.gridy = 5;
		c.weightx = 0.5;
		c.gridwidth = 1;
		pane1.add(buttonSave, c);

		buttonLoad = new JButton("Deserializer l'arbre");
		c.gridx = 3;
		c.gridy = 5;
		c.weightx = 0.5;
		c.gridwidth = 1;
		pane1.add(buttonLoad, c);

		buttonLoadIndex = new JButton("Charger l'arbre du fichier texte");
		c.gridx = 2;
		c.gridy = 6;
		c.weightx = 1;
		c.gridwidth = 2;
		pane1.add(buttonLoadIndex, c);

		buttonLoadRecherche = new JButton("Recherche de l'arbre");
		c.gridx = 2;
		c.gridy = 7;
		c.weightx = 0.5;
		c.gridwidth = 1;
		pane1.add(buttonLoadRecherche, c);

		buttonLoadRechercheSeq = new JButton("Recherche du fichier");
		c.gridx = 3;
		c.gridy = 7;
		c.weightx = 0.5;
		c.gridwidth = 1;
		pane1.add(buttonLoadRechercheSeq, c);

		buttonClean = new JButton("Reset");
		c.gridx = 2;
		c.gridy = 8;
		c.weightx = 0.5;
		c.gridwidth = 1;
		pane1.add(buttonClean, c);

		buttonRefresh = new JButton("Refresh");
		c.gridx = 3;
		c.gridy = 8;
		c.weightx = 0.5;
		c.gridwidth = 1;
		pane1.add(buttonRefresh, c);

		JScrollPane scrollPane2 = new JScrollPane(txtarea);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		c.ipady = 50; // reset to default
		c.gridwidth = 2; // 2 columns wide
		c.gridx = 0;
		c.gridy = 8;
		pane1.add(scrollPane2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 400; // reset to default
		c.weighty = 1.0; // request any extra vertical space
		c.gridwidth = 4; // 2 columns wide
		c.gridx = 0;
		c.gridy = 9;

		JScrollPane scrollPane = new JScrollPane(tree);
		pane1.add(scrollPane, c);

		tree.setModel(new DefaultTreeModel(null));
		tree.updateUI();

		txtNbreItem.addActionListener(this);
		buttonAddItem.addActionListener(this);
		buttonAddMany.addActionListener(this);
		buttonLoad.addActionListener(this);
		buttonLoadIndex.addActionListener(this);
		buttonLoadRecherche.addActionListener(this);
		buttonLoadRechercheSeq.addActionListener(this);
		buttonSave.addActionListener(this);
		buttonRemove.addActionListener(this);
		buttonClean.addActionListener(this);
		buttonRefresh.addActionListener(this);

		return pane1;
	}
}