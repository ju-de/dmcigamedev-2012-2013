package dmcigd.levels.ogre;

import dmcigd.core.room.Room;

import java.net.URL;

public class One extends Room implements Runnable{
	
	public One(URL codeBase) {
		super(codeBase, "ogre", "One", "grassy");
	}
	
	public void initializeRoom() {
		
	}
}