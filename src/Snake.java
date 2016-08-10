import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Snake extends JFrame
{
	Spielflaeche feld;
	boolean spielAktiv = false;
	boolean disko = false;
	int foodPos_x , foodPos_y;
	ArrayList<Schlange> schwaenze;
	String richtung = "Rechts"; 
	String letzterKurs = "Rechts";
	SchlangenBewegung ogog;
	int highscore, aktuellePunkte = 1;
	JLabel punktestand;
	JLabel highscoreLabel2;
	JLabel D, I, S, K, O, ausrufezeichen;
	
	
	class MenuLauscher implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String menupunkt;
			
			menupunkt = e.getActionCommand();
			
			if (menupunkt.equals("Start"))
			{
				if (spielAktiv == true)              // falls man beim Spielen neu startet. Die sleep-Pause, damit der
				{			             // Sek�nd�rthread das Ende vom Spiel auch mitkriegt und sich beendet
					spielAktiv = false;
		
					try
					{
						Thread.sleep(300);
		 			}
		 			catch(InterruptedException x)
		 			{}
		 			
		 			richtung = "Rechts"; 
					letzterKurs = "Rechts";
				}
				
				spielAktiv = true;
				richtung = "Rechts";
				punktestand.setForeground(Color.BLACK);
				
				for (int i = schwaenze.size()-1; i >= 1; i--)    // Snake-Array zur�cksetzen
				{						 //
    					schwaenze.remove(i);			 //
    				}						 //
    				schwaenze.get(0).snakePos_x = 140;		 //
				schwaenze.get(0).snakePos_y = 140;		 //
				
				foodPos_x = 20 * ((int)(Math.random() * 15));    // Futterposition neu ausw�rfeln
				foodPos_y = 20 * ((int)(Math.random() * 15));    //
				
				feld.repaint();
				ogog = new SchlangenBewegung();                  // neuer Bewegungsthread wird gestartet
				ogog.start();
				
			}
			
			if (menupunkt.equals("Disko-Modus"))
			{				
				if (disko == false)
				{
					disko = true;
				}
				else
				{
					disko = false;
				}
				
				diskomodus();
			}
			
			if (menupunkt.equals("Programm beenden"))
				System.exit(0);
		}
	}
	
	class ButtonLauscher implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String test = e.getActionCommand();
			
			switch(test)
			{ 
	    			case "Rechts":
	            			if (letzterKurs.equals("Links"))
						{
							return;
						}
	            			richtung = "Rechts";
	            			break;
	        		
	        		case "Links":
	        			if (letzterKurs.equals("Rechts"))
						{
							return;
						}
					richtung = "Links";
					break;
	    				
	        		case "Oben":
	        			if (letzterKurs.equals("Unten"))
						{
							return;
						}
	            			richtung = "Oben";
	            			break;
	        			
	        		case "Unten":
	        			if (letzterKurs.equals("Oben"))
						{
							return;
						}
	          			richtung = "Unten";
	          			break;	
	     		}
		}
	}
	
	
	public class PfeilTastenAktion extends AbstractAction       // Klasse wird im Konstruktor f�r Keybindings ben�tigt
	{
		String test;
		
		public PfeilTastenAktion(String taste)
		{
			test = taste;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			switch(test)
			{ 
	    			case "Rechts":
	            			if (letzterKurs.equals("Links"))
						{
							return;
						}
	            			richtung = "Rechts";
	            			break;
	        		
	        		case "Links":
	        			if (letzterKurs.equals("Rechts"))
						{
							return;
						}
					richtung = "Links";
					break;
	    				
	        		case "Oben":
	        			if (letzterKurs.equals("Unten"))
						{
							return;
						}
	            			richtung = "Oben";
	            			break;
	        			
	        		case "Unten":
	        			if (letzterKurs.equals("Oben"))
						{
							return;
						}
	          			richtung = "Unten";
	          			break;	
	     		}
     		}
     	}	
	
	class SchlangenBewegung extends Thread        // ist f�r die automatische Bewegung der Schlange verantwortlich
	{					      // ruft dabei auch Kollisionsabfrage etc. auf
		public void run()
		{
			while(spielAktiv == true)
			{
				switch(richtung)
				{
					case "Rechts": 
						letzterKurs = "Rechts";					
						schwanzKoordination();
						schwaenze.get(0).snakePos_x += 20;
						break;
						
					case "Links":
						letzterKurs = "Links";
						schwanzKoordination();
						schwaenze.get(0).snakePos_x -= 20;
						break;
					
					case "Oben":
						letzterKurs = "Oben";
						schwanzKoordination();
						schwaenze.get(0).snakePos_y -= 20;
						break;
					
					case "Unten":
						letzterKurs = "Unten";
						schwanzKoordination();
						schwaenze.get(0).snakePos_y += 20;
						break;
				}
				
				futterVerteilung();
				kollisionsCheck();
				diskomodus();
				feld.repaint();
				
			
				try
				{
					sleep(300);
		 		}
		 		catch(InterruptedException x)
		 		{}
			}
		 				 	
		}
	}
	
	public class Schlange  // Daraus wird das Array gebastelt, was dann als "Schlange" gemalt wird
	{
		private int snakePos_x, snakePos_y;
		
		Schlange(int x, int y)
		{
			snakePos_x = x;
			snakePos_y = y;
		}
	}
		
		
	// Verschiedene Methoden -----------------------------------------------------------------------------------------------------
	
	
	public void futterVerteilung() // Kollisionsabfrage mit dem Futterpunkt & darauffolgendes Wachsen der Schlange
	{
		if (schwaenze.get(0).snakePos_x == foodPos_x && schwaenze.get(0).snakePos_y == foodPos_y)
		{			
			foodPos_x = 20 * ((int)(Math.random() * 15));
			foodPos_y = 20 * ((int)(Math.random() * 15));
			
			while (istInSchlange() == true)
			{
				foodPos_x = 20 * ((int)(Math.random() * 15));
				foodPos_y = 20 * ((int)(Math.random() * 15));
			}
			
			schwaenze.add(new Schlange(400, 400));
			aktuellePunkte += 1;
			punktestand.setText(String.valueOf(aktuellePunkte));
			
		}				
	}
	
	public boolean istInSchlange() // Zum �berpr�fen, ob der neue Futterpunkt in der Schlange gespawnt ist
	{
		boolean ergebnis = false;
		
		for (int i = 0; i < schwaenze.size(); i++)
		{
			if (foodPos_x == schwaenze.get(i).snakePos_x && foodPos_y == schwaenze.get(i).snakePos_y)
			{
				ergebnis = true;
			}
		}
		
		return ergebnis;
	}
	
	public void kollisionsCheck() // �berpr�ft, ob man in eine Wand oder sich selbst gekracht ist
	{
		if (schwaenze.get(0).snakePos_x > 280 || schwaenze.get(0).snakePos_y > 280 || schwaenze.get(0).snakePos_x < 0 || schwaenze.get(0).snakePos_y < 0)
			{
				gameOver();
			}
		
		if (schwaenze.size() > 1)
		{
			for (int i = 1; i < schwaenze.size(); i++)
			{
				if (schwaenze.get(0).snakePos_x == schwaenze.get(i).snakePos_x && schwaenze.get(0).snakePos_y == schwaenze.get(i).snakePos_y)
				{
					gameOver();
				}
			}
		}
	}
		
	
	public void schwanzKoordination()  // Koordinaten des letzten Schwanzteils werden beim Bewegen an den vorletzten usw. durchgereicht
	{
		if (schwaenze.size() > 1)
		{
			for (int i = schwaenze.size()-1; i > 0; i--)
			{
				schwaenze.get(i).snakePos_x = schwaenze.get(i-1).snakePos_x;
				schwaenze.get(i).snakePos_y = schwaenze.get(i-1).snakePos_y;
			}
		}
	}
	
	public void diskomodus()
	{
		
		if (disko == true)
		{
			D.setForeground(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
			I.setForeground(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
			S.setForeground(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
			K.setForeground(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
			O.setForeground(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
			ausrufezeichen.setForeground(new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
		}
		else
		{
			D.setForeground(new Color(0,0,0,0));
			I.setForeground(new Color(0,0,0,0));
			S.setForeground(new Color(0,0,0,0));
			K.setForeground(new Color(0,0,0,0));
			O.setForeground(new Color(0,0,0,0));
			ausrufezeichen.setForeground(new Color(0,0,0,0));
		}
	}
						
	
		
	public void gameOver()  // selbsterkl�rend
	{
		JOptionPane.showMessageDialog(null, "Deine Schlange war " + aktuellePunkte + " Felder lang. \n Versuchs nochmal!", "Game Over", JOptionPane.INFORMATION_MESSAGE); 
		spielAktiv = false;
		if (highscore < aktuellePunkte)
		{
			highscore = aktuellePunkte;
			highscoreLabel2.setText(String.valueOf(highscore));
		}
		aktuellePunkte = 1;
		punktestand.setText(String.valueOf(aktuellePunkte));
		punktestand.setForeground(new Color(0,0,0,0));
		
		feld.repaint();
	}				
	
	// -------------------------------------------------------------------------------------------------------------------------
	
	
	
	Snake (String titel)
	{
		super(titel);
		
		schwaenze = new ArrayList<Schlange>();
		schwaenze.add(new Schlange(140,140));
		
		
		JMenuBar menuleiste = new JMenuBar();
		setJMenuBar(menuleiste);
			
		JMenu menu1 = new JMenu("Spiel...        ");
		JMenuItem item1 = new JMenuItem("Start");
		JMenuItem item2 = new JMenuItem("Disko-Modus");
		JMenuItem item3 = new JMenuItem("Programm beenden");
		
		D = new JLabel("D");
		I = new JLabel("I");
		S = new JLabel("S");
		K = new JLabel("K");
		O = new JLabel("O");
		ausrufezeichen = new JLabel("!          ");
		D.setForeground(new Color(0,0,0,0));
		I.setForeground(new Color(0,0,0,0));
		S.setForeground(new Color(0,0,0,0));
		K.setForeground(new Color(0,0,0,0));
		O.setForeground(new Color(0,0,0,0));
		ausrufezeichen.setForeground(new Color(0,0,0,0));
		
		JLabel highscoreLabel1 = new JLabel("Derzeitiger Highscore:   ");
		highscoreLabel2 = new JLabel("0");
		highscoreLabel2.setFont(new Font("Serif", Font.BOLD, 20)); 
		highscoreLabel2.setForeground(Color.RED);
		
		item1.addActionListener(new MenuLauscher());
		item2.addActionListener(new MenuLauscher());
		item3.addActionListener(new MenuLauscher());
		
		menu1.add(item1);
		menu1.add(item2);
		menu1.add(item3);
		menuleiste.add(menu1);
		menuleiste.add(D);
		menuleiste.add(I);
		menuleiste.add(S);
		menuleiste.add(K);
		menuleiste.add(O);
		menuleiste.add(ausrufezeichen);
		menuleiste.add(highscoreLabel1);
		menuleiste.add(highscoreLabel2);
		
		
		feld = new Spielflaeche();
		feld.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel buttonleiste = new JPanel();
		
		JButton rechts = new JButton(">");
		rechts.setActionCommand("Rechts");
		rechts.addActionListener(new ButtonLauscher());
		
		JButton links = new JButton("<");
		links.setActionCommand("Links");
		links.addActionListener(new ButtonLauscher());
		
		JButton oben = new JButton("^");
		oben.setActionCommand("Oben");
		oben.addActionListener(new ButtonLauscher());
		
		JButton unten = new JButton("v");
		unten.setActionCommand("Unten");
		unten.addActionListener(new ButtonLauscher());
		
		punktestand = new JLabel ("1", JLabel.CENTER);
		punktestand.setFont(new Font("Serif", Font.BOLD, 24)); 
		punktestand.setForeground(new Color(0,0,0,0));
		
		buttonleiste.setLayout(new GridLayout(3,3,5,5));
				
		buttonleiste.add(new JLabel(""));
		buttonleiste.add(oben);
		buttonleiste.add(new JLabel(""));
		buttonleiste.add(links);
		buttonleiste.add(punktestand);
		buttonleiste.add(rechts);
		buttonleiste.add(new JLabel(""));
		buttonleiste.add(unten);
		buttonleiste.add(new JLabel(""));
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		add(feld, c);
		
		c.gridx = 0;
		c.gridy = 3;
		add(buttonleiste, c);
	
		// KeyBindings - Damit die Pfeiltasten funktionieren, normale KeyListener funktionieren nicht konsistent wegen dem Fokus
		
		InputMap im = buttonleiste.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
    		ActionMap am = buttonleiste.getActionMap();

   		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RightArrow");
    		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LeftArrow");
    		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UpArrow");
    		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DownArrow");

    		am.put("RightArrow", new PfeilTastenAktion("Rechts"));
    		am.put("LeftArrow", new PfeilTastenAktion("Links"));
    		am.put("UpArrow", new PfeilTastenAktion("Oben"));
    		am.put("DownArrow", new PfeilTastenAktion("Unten"));
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);					
	}
	
	class Spielflaeche extends JPanel
	{
		Spielflaeche()
		{
			setBackground(Color.white);
			setForeground(Color.black);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);  // <- Wichtig! Ohne die Zeile gibt's in der Malfl�che bizarre Grafikbugs
			
			if (spielAktiv == false)  // wird erst gezeichnet, wenn ein Spiel gestartet wird
			{
				return;
			}
			
			else
			{			
				g.setColor(Color.orange);
     				g.fillOval(foodPos_x, foodPos_y, 20, 20);
        			g.setColor(Color.red);
        			g.drawOval(foodPos_x, foodPos_y, 20, 20);
        			
        			if (disko == false)
        			{
        				g.setColor(Color.blue);
					for (int i = 0; i < schwaenze.size(); i++)
					{
						g.fillRect(schwaenze.get(i).snakePos_x, schwaenze.get(i).snakePos_y, 20, 20);
					}
				}
				else 
				{
					int R, G, B;
					
					for (int i = 0; i < schwaenze.size(); i++)
					{
						R = (int)(Math.random()*256);
						G = (int)(Math.random()*256);
						B = (int)(Math.random()*256);
						
						g.setColor(new Color(R, G, B));
						g.fillRect(schwaenze.get(i).snakePos_x, schwaenze.get(i).snakePos_y, 20, 20);
					}
				}	
			}
		}
		
		public Dimension getMinimumSize()
		{
			return new Dimension(300,300);
		}
		
		public Dimension getPreferredSize()
		{
			return getMinimumSize();
		} 		
	}
		
	public static void main(String[] args)
	{
		Snake spieltest = new Snake("Marvin's Snake v0.6");
		spieltest.setVisible(true);
		spieltest.pack();
		spieltest.setResizable(false);
	}	
}
	
	