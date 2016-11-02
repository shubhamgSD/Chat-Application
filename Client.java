
import javax.swing.SwingUtilities;

public class Client {
	
	public static void main(String[] args)	throws Exception {
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				try {
				SwingContainerDemo ob = new SwingContainerDemo();
				ob.go();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				 
			}
		});
		
		
		
		
		
	}
	}
