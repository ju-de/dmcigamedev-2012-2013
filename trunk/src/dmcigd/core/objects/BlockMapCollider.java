package dmcigd.core.objects;

import dmcigd.core.enums.*;
import dmcigd.core.objects.maps.BlockMap;

public abstract class BlockMapCollider extends ObjectCollider {
	
	public BlockMap blockMap;
	
	//Creates entity statuses for further manipulation
	public boolean hitGround,isFalling,isDead,isDestroyed,inWater = false;
	
	//Public setters
	public void setBlockMap(BlockMap blockMap) {
		this.blockMap = blockMap;
	}
	
	//Returns collision type of tile of destination block
	public CollisionType tileCollisionType(int v, Direction direction) {
		switch (direction) {
			case DOWN:
			case UP:
				return blockMap.collidesY(getX(), getY(), v, getWidth(), getHeight(), direction);
			default:
				return blockMap.collidesX(getX(), getY(), v, getWidth(), getHeight(), direction);
		}
	}
	
	//Places entity at edge of tile (farthest accessible point) used in case of obstruction
	public void blockMapCollision(Direction direction) {
		switch (direction) {
			case DOWN:
				setVY(0);
			case UP:
				setY(blockMap.rowEdge(getY(), getHeight(), direction));
				break;
			default:
				setX(blockMap.colEdge(getX(), getWidth(), direction));
				break;
		}
	}
	
	//Checks downward collision (behaves differently from other three directions)
	public void checkBlockMapCollisionDown(int v) {
		
		//Move Down
		switch(tileCollisionType(v, Direction.DOWN)) {
		
			//Fall down
			case WATER:
			case NONSOLIDLADDER:
			case NONSOLID:
				isFalling = true;
				checkSolidObjectCollisionDown(v);
				break;
				
			case DESTROY:
				isDestroyed = true;
			case KILL:
				isDead = true;
			//Hit ground
			default:
				blockMapCollision(Direction.DOWN);
				setVY(0);
				rest(tileCollisionType(v, Direction.DOWN));
				break;
				
		}
		
	}
	
	//Checks all other types of collision
	public void checkBlockMapCollision(int v, Direction direction) {
		
		CollisionType collidingBlock = tileCollisionType(v, direction);
		
		//If object is not a solid block, move through (all other objects allow horizontal movement)
		if(collidingBlock.getPriority() > 1) {
			
			if(direction == Direction.UP) {
				checkSolidObjectCollisionUp(v);
			} else {
				checkSolidObjectCollisionX(v);
			}
			
		} else {
			
			if(collidingBlock == CollisionType.DESTROY) {
				isDestroyed = true;
			}
			
			if(collidingBlock == CollisionType.KILL) {
				isDead = true;
			}
			
			blockMapCollision(direction);
			if(direction != Direction.UP) {
				setVX(0);
			}
		}
	}
	
}
