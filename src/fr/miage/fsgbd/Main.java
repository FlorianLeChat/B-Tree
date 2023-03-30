package fr.miage.fsgbd;

public class Main
{
	public static void main(String args[])
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				GUI fenetre = new GUI();
				fenetre.setVisible(true);
			}
		});
	}
}