/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	private GLabel name;
	private GImage image;
	private GLabel status;
	private GLabel message;
	private GLabel friend;
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		message = new GLabel(msg);
		message.setFont(MESSAGE_FONT);
		add(message, getWidth() / 2 - message.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		name = new GLabel(profile.getName());
		name.setFont(PROFILE_NAME_FONT);
		name.setColor(Color.BLUE);
		add(name,LEFT_MARGIN, TOP_MARGIN * 2);
		
		if (profile.getImage() == null) {
			GRect rect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(rect, LEFT_MARGIN, TOP_MARGIN * 2 + IMAGE_MARGIN);
			
			GLabel label = new GLabel("No Image");
			label.setFont(PROFILE_IMAGE_FONT);
			add(label,LEFT_MARGIN + IMAGE_WIDTH / 2 - label.getWidth() / 2,  TOP_MARGIN * 2 + IMAGE_MARGIN + IMAGE_HEIGHT / 2);
		}
		else {
			image = profile.getImage();
			image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(image, LEFT_MARGIN, TOP_MARGIN * 2 + IMAGE_MARGIN);
		}
		
		status = new GLabel("No current status");
		if (!profile.getStatus().isEmpty()) {
			status.setLabel(profile.getName() + " is " + profile.getStatus());
		}
		status.setFont(PROFILE_STATUS_FONT);
		add(status, LEFT_MARGIN,  TOP_MARGIN * 2 + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN * 2);
		
		
		friend = new GLabel("Friends:");
		friend.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friend,getWidth() / 2, TOP_MARGIN * 2 + IMAGE_MARGIN);
		
		int index = 0;
		for (String friendName : profile.getFriends()) {
			GLabel friendLabel = new GLabel(friendName);
			friendLabel.setFont(PROFILE_FRIEND_FONT);
			add(friendLabel, getWidth() / 2, TOP_MARGIN * 2 + IMAGE_MARGIN + friend.getHeight() + friendLabel.getHeight() * index);
			index++;
		}
		
	}

	
}
