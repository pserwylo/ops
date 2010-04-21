import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Main extends JFrame implements KeyListener, ActionListener
{

	private JLabel nBackLabel, focusLabel;
	private NBack nback;
	private String userId;
	
	public Main( String userId )
	{
		this.userId = userId;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.CENTER ) );
		
		this.nBackLabel = new JLabel( "NBack" );
		this.nBackLabel.setFont( new Font( Font.SERIF, Font.BOLD, 50 ) );
		this.nBackLabel.setHorizontalAlignment( JLabel.CENTER );
		panel.add( this.nBackLabel );
		
		this.focusLabel = new JLabel( "" );
		this.focusLabel.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 100 ) );
		this.focusLabel.setHorizontalAlignment( JLabel.CENTER );
		panel.add( this.focusLabel );
		
		this.addKeyListener( this );
		this.setLayout( new GridBagLayout() );
		this.add( panel, new GridBagConstraints() );
		this.setSize( 800, 300 );
		this.setVisible( true );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		this.nback = new NBack();
		this.nback.addActionListener( this );
		this.nback.start();
	}
	
	public static void main( String[] args )
	{
		String userId;
		do
		{
			userId = JOptionPane.showInputDialog( "Please enter the participant code.", "A1" );
		} while ( userId.trim().length() == 0 );
		
		new Main( userId );
	}
	
	
	public void actionPerformed( ActionEvent e )
	{
		if ( e.getID() == NBack.ACTION_TICK )
		{
			this.nBackLabel.setText( this.nback.getCurrentNumber() + "" );
			this.focusLabel.setText( "" );
		}
		else if ( e.getID() == NBack.ACTION_FOCUS )
		{
			this.nBackLabel.setText( "" );
			this.focusLabel.setText( "+" );
		}
		else if ( e.getID() == NBack.ACTION_COMPLETE )
		{
			this.nback.saveResults( this.userId );
			System.exit( 1 );
		}
	}
	

	@Override
	public void keyPressed( KeyEvent e ) 
	{
		// If is target
		if ( e.getKeyCode() == KeyEvent.VK_ENTER )
		{
			this.nback.submitResult( true );
		}
		// If isn't target
		else if ( e.getKeyCode() == KeyEvent.VK_SPACE )
		{
			this.nback.submitResult( false );
		}
		else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
		{
			this.nback.saveResults( this.userId );
			System.exit( 1 );
		}
	}

	@Override
	public void keyReleased( KeyEvent e ) 
	{
		
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}

}
