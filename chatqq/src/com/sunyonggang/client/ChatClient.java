package com.sunyonggang.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame
{
	public ChatClient(ClientConnection clientConnection)
	{
		this.clientConnection = clientConnection;
		initComponents();
	}
	private void initComponents()
	{
		jPanel1 = new JPanel();
		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();
		jTextField = new JTextField(20);
		jButton1 = new JButton();
		jButton2 = new JButton();
		jPanel2 = new JPanel();
		jScrollPane2 = new JScrollPane();
		jTextArea2 = new JTextArea();
		
		jPanel3 = new JPanel();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("chatting");
		setResizable(false);
		jPanel1.setBorder(BorderFactory.createTitledBorder("chatting message"));
		jPanel2.setBorder(BorderFactory.createTitledBorder("user list"));
		jTextArea1.setColumns(30);
		jTextArea1.setRows(25);
		
		jTextArea2.setColumns(20);
		jTextArea2.setRows(25);
		
		this.jTextArea1.setEditable(false);
		this.jTextArea2.setEditable(false);
		
		jPanel3.add(jTextField);
		jPanel3.add(jButton1);
		jPanel3.add(jButton2);
		
		jPanel1.setLayout(new BorderLayout());
		jPanel1.add(jScrollPane1, BorderLayout.NORTH);
		jPanel1.add(jPanel3, BorderLayout.SOUTH);
		
		jPanel2.add(jScrollPane2);
		
		jScrollPane1.setViewportView(jTextArea1);
		jScrollPane2.setViewportView(jTextArea2);
		
		jButton1.setText("send");
		jButton2.setText("clear");
		
		jButton1.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ChatClient.this.sendMessage(e);
			}
		});
		
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					ChatClient.this.clientConnection.sendMessage("client closed", "5");
				} catch (Exception e2)
				{
					e2.printStackTrace();
				}
			}
		});
		this.setLayout(new FlowLayout());
		this.getContentPane().add(jPanel1);
		this.getContentPane().add(jPanel2);
		
		this.pack();
		this.setVisible(true);
		
	}
	
	private void sendMessage(ActionEvent event)
	{
		String message = this.jTextField.getText();
		this.jTextField.setText("");
		this.clientConnection.sendMessage(message, "2");
	}
	
	public JTextArea getJTextArea1()
	{
		return jTextArea1;
	}
	public JTextArea getJTextArea2()
	{
		return jTextArea2;
	}
	private JButton jButton1;
	private JButton jButton2;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JTextArea jTextArea1;
	private JTextArea jTextArea2;
	private JTextField jTextField;
	
	private ClientConnection clientConnection;
	
}
