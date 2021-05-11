package fr.miage.fsgbd;

import java.util.ArrayList;


/*
 * Classe de gestion des noeuds du b+Arbre
 * @author LAUGIER Vincent; COFFRE Jean-Denis
 * @author Galli Gregory, Mopolo Moke Gabriel
 */
public class Noeud<Type> implements java.io.Serializable {


    // Collection des Noeuds enfants du noeud courant
    public ArrayList<Noeud<Type>> fils = new ArrayList<Noeud<Type>>();

    // Collection des cl�s du noeud courant
    public ArrayList<Type> keys = new ArrayList<Type>();

    // Noeud Parent du noeud courant
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

    /**
     * Cherche une valeur dans la branche
     * @param valeur Valeur � rechercher dans la branche
     * @return le Noeud trouv� / null
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

    /**
     * Permet de trouver le noeud ou ajouter la valeur
     * @param valeur que l'on souhaite ins�rer
     * @return le Noeud choisi
     */
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

    /**
     * M�thode d'affichage pour le contenu d'un noeud
     * @param afficheSousNoeuds d�termine si l'on doit s'interesser aux sous arbres
     * @param lvl la profondeur
     */
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


    /**
     * Ins�re une clef dans le noeud courant
     * @param valeur � ajouter aux clefs du noeud courant
     */
    private void insert(Type valeur) {
        int i = 0;
        while ((this.keys.size() > i) && compare(this.keys.get(i), valeur)) {
            i++;
        }
        this.keys.add(i, valeur);
    }

    /**
     * Retire une clef dans le noeud courant
     * @param valeur � retirer des clefs du noeud courant
     */
    private void removeKey(Type valeur) {
        this.keys.remove(valeur);
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

    /**
     * Ajoute un noeud fils au noeud courant
     * @param noeud � ajouter
     */
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

    /**
     * Retire un fils au noeud courant
     * @param noeud � retirer
     * @return boolean
     */
    public boolean removeNoeud(Noeud<Type> noeud) {
        return fils.remove(noeud);
    }

    /**
     * Retire une clef au noeud courant
     * @param valeur � retirer
     * @return la <Noeud>racine</Noeud> de l'arbre
     */
    public Noeud<Type> removeValeur(Type valeur, boolean force) {
        System.out.println("removeValeur : "+valeur+", force : "+force);
        Noeud<Type> noeud, racine = this;
        Type eleMedian;
        int indexMedian;

        // On remonte jusqu'� la racine � partir du noeud courant
        while (racine.parent != null)
            racine = racine.parent;

        if (!force)
            noeud = this.contient(valeur);
        else noeud = this;

        if (noeud == null) {
            System.out.println("Tentative de suppression d'une valeur inexistante dans l'arbre : " + valeur);
            return racine;
        }
        else if (force && noeud.parent != null)
            noeud.parent.removeValeur(valeur, true);

        int tailleListe = noeud.keys.size();

        System.out.println(noeud);

        if (noeud.keys.contains(valeur)) {
            //Regroupement : Si le nombre de clef dans le noeud devient inf�rieur au minimum (m)
            System.out.println("tailleListe :"+tailleListe);
            System.out.println("u :"+u);

			if (tailleListe-1 < u/2)
			{
			    // On retire la clef du noeud courant
                noeud.removeKey(valeur);
                if (noeud.parent != null) {
                    // On retire le noeud courant de l'arbre
                    noeud.parent.removeNoeud(noeud);
                    // On r�attribue les clefs au parent
                    for (Type key : noeud.keys)
                        noeud.parent.addValeur(key);
                    // On rappelle la fonction pour effacer les �ventuelles traces de la clef effac�e dans les parents
                    noeud.parent.removeValeur(valeur, true);
                }

                // TODO : Si la clef effac�e dans un parent n'est pas associ� � une suppression d'un fils, il faut remplacer la clef dans le parent par la clef suivante du fils auquel on a retir� sa clef
			}
			else {
                noeud.removeKey(valeur);
                if (noeud.parent != null)
                    noeud.parent.removeValeur(valeur, true);
            }
        }
        else if (noeud.parent != null)
            noeud.parent.removeValeur(valeur, true);


        return racine;
    }

    /**
     * Ajoute une clef au noeud courant, ceci est une fonction r�cursive
     * @param nouvelleValeur � ajouter
     * @param force, bool�en sp�cificiant que l'on doit ajouter au noeud courant et non pas chercher l'endroit o� ins�rer la nouvelle valeur
     * @return la <Noeud>racine</Noeud> de l'arbre
     */
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
