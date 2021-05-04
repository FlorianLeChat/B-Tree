package fr.miage.fsgbd;

import java.util.ArrayList;


/*
 * Classe de gestion des noeuds du b+Arbre
 * @author LAUGIER Vincent; COFFRE Jean-Denis
 */
public class Noeud<Type> implements java.io.Serializable {


    // Collection des Noeuds enfants du noeud
    public ArrayList<Noeud<Type>> fils = new ArrayList<Noeud<Type>>();

    // Collection des cl�s du noeud
    public ArrayList<Type> keys = new ArrayList<Type>();

    // Noeud Parent
    private Noeud<Type> parent;

    // Classe interfa�ant "Executable" et donc contenant une proc�dure de comparaison de <Type>
    private Executable compar;

    // Ordre de l'abre (u = nombre de cl�s maximum = 2m)
    private final int u;


    /* Constructeur de la classe noeud, qui permet l'ajout et la recherche d'�l�ment dans les branches
     * @param u Nombre de cl�s maximum du noeud
     * @param e Classe interfa�ant "Executable" et donc contenant une proc�dure de comparaison de <Type>
     * @param parent Nombre de cl�s minimum du noeud
     */
    public Noeud(int u, Executable e, Noeud<Type> parent) {
        this.u = u;
        compar = e;
        this.parent = parent;
    }

    public boolean compare(Type arg1, Type arg2) {
        return compar.execute(arg1, arg2);
    }

    public Noeud<Type> ajoutValeur(Type valeur) {

        return null;
    }

    /* Cherche une valeur dans la branche
     * @param valeur Valeur � rechercher dans la branche
     * @return Vrai si la valeur est trouv�e, sinon non
     */
    public Noeud<Type> contient(Type valeur) {
        Noeud<Type> retour = null;

        if (this.keys.contains(valeur) && (this.fils.isEmpty())) {
            retour = this;
        } else {
            Noeud<Type> trouve = null;
            int i = 0;

            while ((trouve == null) && (i < this.fils.size())) {
                trouve = this.fils.get(i).contient(valeur);
                i++;
            }

            retour = trouve;

        }
        return retour;
    }

    public Noeud<Type> choixNoeudAjout(Type valeur) {
        Noeud<Type> retour = null;

        if (this.fils.size() == 0) {
            retour = this;
        } else {
            int index = 0;

            boolean trouve = false;
            while (!trouve && (index < this.keys.size())) {
                trouve = compare(valeur, this.keys.get(index));
                if (!trouve)
                    index++;
            }

            retour = this.fils.get(index).choixNoeudAjout(valeur);
        }
        return retour;
    }

    public void afficheNoeud(boolean afficheSousNoeuds, int lvl) {

        StringBuilder dots = new StringBuilder();

        for (int i = 0; i < lvl; i++) {
            dots.append("..");
        }

        for (Type valeur : this.keys) {
            dots.append(valeur.toString()).append(" ");
        }

        System.out.println(dots);

        if (afficheSousNoeuds) {
            for (Noeud<Type> noeud : this.fils) {
                noeud.afficheNoeud(afficheSousNoeuds, lvl + 1);
            }
        }
    }


    private void insert(Type valeur) {
        int i = 0;
        while ((this.keys.size() > i) && compare(this.keys.get(i), valeur)) {
            i++;
        }
        this.keys.add(i, valeur);
    }


    /*
     * Algo d'ajout de donn�es dans l'arbre :
     *
     * On choisit un noeud appropri� en recherchant dans l'arbre l'endroit o� devrait se
     * situer la donn�e.
     * On ajoute la donn�e � ce noeud (qui peut ne pas �tre une feuille si l'ajout r�sulte du fait
     * qu'une donn�e m�diane d'un noeud fils vient de remonter)
     * Si la taille du noeud d�passe l'ordre de l'arbre, on trouve l'�l�ment m�dian,
     * on le remonte dans son parent (eventuellement on recr�e une racine), et on cr�e deux nouveaux noeuds
     * le premier avec tous les �l�ments dont la comparaison renvoie faux et le deuxieme tous les �l�ments
     * dont la comparaison renvoie true.
     * On ajoute les �ventuels noeuds fils de notre noeud aux nouveaux noeuds enfants
     * On raz la collection d'enfants de notre noeud et on y a ajoute nos deux nouveaux noeud gauche et droit
     * On renvoie la racine (potentiellement la nouvelle)
     *
     */

    public Noeud<Type> addValeur(Type nouvelleValeur) {
        Noeud<Type> racine = addValeur(nouvelleValeur, false);
        return racine;
    }

    public void addNoeud(Noeud<Type> noeud) {
        int i = 0;

        if (i == this.fils.size()) {
            this.fils.add(noeud);
        } else {
            while (((i < this.fils.size() && compare(this.fils.get(i).keys.get(this.fils.get(i).keys.size() - 1), noeud.keys.get(0)))))
                i++;
            this.fils.add(i, noeud);
        }
    }

    public boolean removeNoeud(Noeud<Type> noeud) {
        return fils.remove(noeud);
    }

    public Noeud<Type> removeValeur(Type valeur) {
        Noeud<Type> noeud, racine, noeud2 = this;
        Type eleMedian;
        int indexMedian;

        while (noeud2.parent != null)
            noeud2 = noeud2.parent;

        racine = noeud2;

        noeud = this.contient(valeur);
        if (noeud == null) {
            System.out.println("Tentative de suppression d'une valeur inexistante dans l'arbre : " + valeur);
            return racine;
        }

        int tailleListe = noeud.keys.size();

        System.out.println(noeud);

        if (!noeud.keys.contains(valeur)) {
//			if (tailleListe == u)
//			{
//				Noeud<Type> noeudGauche = new Noeud<Type>(u, compar, null);
//				Noeud<Type> noeudDroit = new Noeud<Type>(u, compar, null);
//
//				noeud.insert(nouvelleValeur);
//				tailleListe++;
//
//				if (tailleListe % 2 == 0) //pair
//					indexMedian = 1 + (tailleListe / 2);
//				else
//					indexMedian = (1 + tailleListe) / 2;
//
//				indexMedian--;
//
//				eleMedian = noeud.keys.get(indexMedian);
//
//				for(int i=0;i < indexMedian;i++)
//					noeudGauche.addValeur(noeud.keys.get(i));
//
//
//				if (!noeud.fils.isEmpty())
//				{
//					for(int i=indexMedian+1;i < tailleListe;i++)
//						noeudDroit.addValeur(noeud.keys.get(i));
//				}
//				else
//				{
//					for(int i=indexMedian;i < tailleListe;i++)
//						noeudDroit.addValeur(noeud.keys.get(i));
//				}
//
//				if (!noeud.fils.isEmpty())
//				{
//					indexMedian++;
//
//					for(int i=0;i < (indexMedian);i++)
//					{
//						noeudGauche.addNoeud(noeud.fils.get(i));
//						noeud.fils.get(i).parent = noeudGauche;
//					}
//
//
//					for(int i=(indexMedian);i < noeud.fils.size() ;i++)
//					{
//						noeudDroit.addNoeud(noeud.fils.get(i));
//						noeud.fils.get(i).parent = noeudDroit;
//					}
//				}
//
//				if (noeud.parent == null)
//				{
//					Noeud<Type> nouveauParent = new Noeud<Type>(u, compar, null);
//
//					nouveauParent.addNoeud(noeudGauche);
//					nouveauParent.addNoeud(noeudDroit);
//					noeudGauche.parent = nouveauParent;
//					noeudDroit.parent = nouveauParent;
//					nouveauParent.addValeur(eleMedian, true);
//
//					racine = nouveauParent;
//				}
//				else
//				{
//					noeud.parent.addNoeud(noeudGauche);
//					noeud.parent.addNoeud(noeudDroit);
//					noeud.parent.removeNoeud(noeud);
//					noeudGauche.parent = noeud.parent;
//					noeudDroit.parent = noeud.parent;
//					racine = noeud.parent.addValeur(eleMedian, true);
//				}
//
//			}
//			else
//				noeud.insert(nouvelleValeur);
        }

        return racine;
    }

    public Noeud<Type> addValeur(Type nouvelleValeur, boolean force) {

        // Initialisation des variables
        Noeud<Type> noeud, racine = this;
        Type eleMedian;
        int indexMedian;

        // On remonte jusqu'� la racine � partir du noeud courant
        while (racine.parent != null)
            racine = racine.parent;

        // Si force = true, l'ajout se fera dans le noeud courant
        if (force)
            noeud = this;
        else // Sinon on va aller chercher le noeud ou l'on doit ajouter la nouvelle valeur
            noeud = this.choixNoeudAjout(nouvelleValeur);

        // On note le nombre de clef dans le noeud courant avant de commencer
        int tailleListe = noeud.keys.size();

        // On v�rifie que la valeur ne soit pas d�j� pr�sente dans l'arbre (juste au cas o�)
        if (!noeud.keys.contains(nouvelleValeur)) {

            // Si le nombre de clef du noeud courant est �gal au nom max d'�l�ments (2m)
            if (tailleListe == u) {


                // On cr�e deux nouveaux noeuds
                Noeud<Type> noeudGauche = new Noeud<Type>(u, compar, null);
                Noeud<Type> noeudDroit = new Noeud<Type>(u, compar, null);

                // On ins�re la valeur comme nouvelle clef du noeud courant
                noeud.insert(nouvelleValeur);
                tailleListe++;

                // On v�rifie le nombre de clefs dans le noeud courant pour savoir si on a une clef centrale ou si la m�diane se trouve entre deux clefs
                if (tailleListe % 2 == 0)
                    indexMedian = (tailleListe / 2);
                else
                    indexMedian = ((1 + tailleListe) / 2) - 1;

                // On r�cup�re la valeur centrale du noeud courant pour plus tard
                eleMedian = noeud.keys.get(indexMedian);

                // On utilise un appel r�cursif pour ajouter au noeud gauche, les clefs du noeud courant
                for (int i = 0; i < indexMedian; i++)
                    noeudGauche.addValeur(noeud.keys.get(i));

                // Puis on fait de m�me avec le noeud droit sans traiter la clef centrale si le noeud courant a des fils
                if (!noeud.fils.isEmpty()) {
                    for (int i = indexMedian + 1; i < tailleListe; i++)
                        noeudDroit.addValeur(noeud.keys.get(i));
                } else {
                    for (int i = indexMedian; i < tailleListe; i++)
                        noeudDroit.addValeur(noeud.keys.get(i));
                }

                // Ensuite, si le noeud courant a des fils
                if (!noeud.fils.isEmpty()) {
                    indexMedian++;

                    // On ajoute au noeud gauche les fils du noeud courant qui sont � gauche de la m�diane
                    for (int i = 0; i < (indexMedian); i++) {
                        noeudGauche.addNoeud(noeud.fils.get(i));
                        noeud.fils.get(i).parent = noeudGauche;
                    }

                    // Et on ajoute au noeud droit les fils du noeud courant qui sont sur la m�diane ou � droite de la m�diane
                    for (int i = (indexMedian); i < noeud.fils.size(); i++) {
                        noeudDroit.addNoeud(noeud.fils.get(i));
                        noeud.fils.get(i).parent = noeudDroit;
                    }
                }

                // Enfin, si le noeud courant est la racine
                if (noeud.parent == null) {
                    // On cr�e un nouveau noeud qui prendra sa place
                    Noeud<Type> nouveauParent = new Noeud<Type>(u, compar, null);

                    // Qui deviendra le parent des noeuds gauche et droit
                    nouveauParent.addNoeud(noeudGauche);
                    nouveauParent.addNoeud(noeudDroit);
                    noeudGauche.parent = nouveauParent;
                    noeudDroit.parent = nouveauParent;

                    // Et on rajoute dans les clefs du nouveau parent l'ancienne clef "centrale"
                    nouveauParent.addValeur(eleMedian, true);

                    // On modifie alors la racine pour faire de notre nouveau noeud, la racine de l'arbre
                    racine = nouveauParent;
                } else {
                    // Sinon, on ajoute les noeuds gauche et droit comme fils du parent du noeud courant (faisant des noeuds gauche et droit des fr�res du noeud courant)
                    noeud.parent.addNoeud(noeudGauche);
                    noeud.parent.addNoeud(noeudDroit);
                    noeudGauche.parent = noeud.parent;
                    noeudDroit.parent = noeud.parent;

                    // On retire le noeud courant des fils du parent ( les noeuds gauche et droit viennent le remplacer )
                    noeud.parent.removeNoeud(noeud);

                    // Et on fini par ajouter l'�l�ment m�dian laiss� de c�t� plus t�t au parent du noeud courant ( on remonte la clef dans le parent )
                    racine = noeud.parent.addValeur(eleMedian, true);
                }

            } else // Si le nombre de clefs dans le noeud n'est pas au max, on ajoute simplement la clef au noeud courant
                noeud.insert(nouvelleValeur);
        }

        return racine;
    }
}
