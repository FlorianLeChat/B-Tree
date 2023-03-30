package fr.miage.fsgbd;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Galli Gregory, Mopolo Moke Gabriel
 * @param <Type>
 */
public class BTreePlus<Type> implements java.io.Serializable
{
	private Noeud<Type> racine;

	public BTreePlus(int u, Executable<Integer> e)
	{
		racine = new Noeud<Type>(u, e, null);
	}

	public void afficheArbre()
	{
		racine.afficheNoeud(true, 0);
	}

	/**
	 * Méthode récursive permettant de récupérer tous les noeuds
	 *
	 * @return DefaultMutableTreeNode
	 */
	public DefaultMutableTreeNode bArbreToJTree()
	{
		return bArbreToJTree(racine);
	}

	private DefaultMutableTreeNode bArbreToJTree(Noeud<Type> root)
	{
		StringBuilder txt = new StringBuilder();

		for (Integer key : root.keys)
			txt.append(key.toString()).append(" ");

		DefaultMutableTreeNode racine2 = new DefaultMutableTreeNode(txt.toString(), true);

		for (Noeud<Type> fil : root.fils)
			racine2.add(bArbreToJTree(fil));

		return racine2;
	}

	public boolean addValeur(String valeur)
	{
		System.out.println("Ajout de la valeur : " + valeur.toString());

		if (racine.contient(Integer.parseInt(valeur.split(",")[1])) == null)
		{
			Noeud<Type> newRacine = racine.addValeur(valeur);

			if (racine != newRacine)
				racine = newRacine;

			return true;
		}

		return false;
	}

	public void removeValeur(String valeur)
	{
		System.out.println("Retrait de la valeur : " + valeur.toString());

		int valueInt = Integer.parseInt(valeur.split(",")[1]);

		if (racine.contient(valueInt) == null)
		{
			Noeud<Type> newRacine = racine.removeValeur(valueInt, false);

			if (racine != newRacine)
				racine = newRacine;
		}
	}

	public Noeud<Type> searchValue(Integer valeur)
	{
		return racine.contient(valeur);
	}
}