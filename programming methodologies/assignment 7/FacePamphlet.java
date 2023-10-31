/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {

	private FacePamphletCanvas canvas;
	private FacePamphletDatabase database;
	private FacePamphletProfile currentProfile;
	
	private JTextField nameField;
	private JTextField statusField;
	private JTextField pictureField;
	private JTextField friendField;
	
	private JButton addName;
	private JButton deleteName;
	private JButton lookupName;
	
	private JButton changeStatus;
	private JButton changePicture;
	private JButton addFriend;
	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		canvas = new FacePamphletCanvas();
		add(canvas);
		database = new FacePamphletDatabase();
		currentProfile = null;
		
//------------------------NORTH SIDE--------------------------//
		add(new JLabel("Name"), NORTH);
		
		nameField = new JTextField(TEXT_FIELD_SIZE);
		add(nameField, NORTH);
		
		addName = new JButton("Add");
		add(addName, NORTH);
		
		deleteName = new JButton("Delete");
		add(deleteName, NORTH);
		
		lookupName = new JButton("Lookup");
		add(lookupName, NORTH);
//------------------------WEST SIDE---------------------------//
		statusField = new JTextField(TEXT_FIELD_SIZE);
		add(statusField, WEST);
		changeStatus = new JButton("Change Status");
		add(changeStatus, WEST);
		
		add(new JLabel(EMPTY_LABEL_TEXT), WEST); 
		
		pictureField = new JTextField(TEXT_FIELD_SIZE);
		add(pictureField, WEST);
		changePicture = new JButton("Change Picture");
		add(changePicture, WEST);
		
		add(new JLabel(EMPTY_LABEL_TEXT), WEST); 
		
		friendField = new JTextField(TEXT_FIELD_SIZE);
		add(friendField, WEST);
		addFriend = new JButton("Add Friend");
		add(addFriend, WEST);
		
		// Adding action listeners to appropriate GUI objects
		addName.addActionListener(this);
		deleteName.addActionListener(this);
		lookupName.addActionListener(this);
		statusField.addActionListener(this);
		changeStatus.addActionListener(this);
		pictureField.addActionListener(this);
		changePicture.addActionListener(this);
		friendField.addActionListener(this);
		addFriend.addActionListener(this);
    }
    public void run() {
    	println("Program has initialized successfully");
    }
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
    	// Add a new profile with entered name
		if (e.getSource() == addName && !nameField.getText().isEmpty()) {
			String name = nameField.getText();
			canvas.removeAll();
			if (!database.containsProfile(name)) {
				FacePamphletProfile profile = new FacePamphletProfile(name);
				database.addProfile(profile);
				canvas.showMessage("Added a new profile with name: "+ profile.getName());
			}
			else {
				canvas.showMessage("Profile already exists");
			}
			currentProfile = database.getProfile(name);
			
			canvas.displayProfile(currentProfile);
		}
		
		// Delete profile with the name from the database
		if (e.getSource() == deleteName && !nameField.getText().isEmpty()) {
			String name = nameField.getText();
			canvas.removeAll();
			if (database.containsProfile(name)) {
				database.deleteProfile(name);
				canvas.showMessage("Profile deleted: " + name);
			}
			else {
				canvas.showMessage("Couldn't find a profile with that name");
			}
			
			currentProfile = null;
		}
		
		// Display the profile with the entered name
		if (e.getSource() == lookupName && !nameField.getText().isEmpty()) {
			String name = nameField.getText();
			canvas.removeAll();
			if (database.containsProfile(name)) {
				FacePamphletProfile profile = database.getProfile(name);
				canvas.showMessage("Looking up: "+ profile.getName());
				currentProfile = profile;
				canvas.displayProfile(currentProfile);
			}
			else {
				canvas.showMessage("Couldn't find a profile with that name");
				currentProfile = null;
				
			}
		}
		
		// Change status
		if ((e.getSource() == statusField || e.getSource() == changeStatus) && !statusField.getText().isEmpty()) {
			canvas.removeAll();
			if (currentProfile != null) {
				String status = statusField.getText();
				currentProfile.setStatus(status);
				println(currentProfile.toString());
				
				database.deleteProfile(currentProfile.getName());
				database.addProfile(currentProfile);
				
				
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Changed status to: " + currentProfile.getStatus());
			}
			else {
				canvas.showMessage("Choose a profile");
			}
		}
		
		// Change picture
		if ((e.getSource() == pictureField || e.getSource() == changePicture) && !pictureField.getText().isEmpty()) {
			canvas.removeAll();
			if (currentProfile != null) {
				GImage image = null;
				try { 
					image = new GImage(pictureField.getText());
					currentProfile.setImage(image);
					println(currentProfile.toString());
					
					database.deleteProfile(currentProfile.getName());
					database.addProfile(currentProfile);
					
					canvas.showMessage("Changed profile picture");
					canvas.displayProfile(currentProfile);
				} catch (ErrorException ex) { 
					// Code that is executed if the filename cannot be opened. 
				}
			}
			else {
				canvas.showMessage("Choose a profile");
			}
		}
		
		// Add a friend to current profile
		if ((e.getSource() == friendField || e.getSource() == addFriend) && !friendField.getText().isEmpty()) {
			canvas.removeAll();
			if (currentProfile != null) {
				String friendName = friendField.getText();
				
				    if (currentProfile.getFriends().contains(friendName) || currentProfile.getName().equals(friendName)) {
				    	canvas.showMessage("Already has the profile as a friend");
				    }
				    else {
				    	if (database.containsProfile(friendName)) {
							currentProfile.addFriend(friendName);
							FacePamphletProfile friendProfile = database.getProfile(friendName);
							friendProfile.addFriend(currentProfile.getName());
							
							database.deleteProfile(friendName);
							database.addProfile(friendProfile);
							
							database.deleteProfile(currentProfile.getName());
							database.addProfile(currentProfile);
							
							
							canvas.displayProfile(currentProfile);
							canvas.showMessage("Added a friend to this profile: "+ friendName);
						}
						else {
							canvas.showMessage("Profile doesn't exist with that name");
						}
				    }
			}
			else {
				canvas.showMessage("Choose a profile");
			}
		}
	}

}
