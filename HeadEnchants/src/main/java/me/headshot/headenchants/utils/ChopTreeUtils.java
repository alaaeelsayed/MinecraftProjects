package me.headshot.headenchants.utils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChopTreeUtils {
	
	public final static HashMap<Player, Block[]> trees = new HashMap<Player, Block[]>();
	protected static boolean moreDamageToTools = false;
	protected static boolean interruptIfToolBreaks = true;
	protected static boolean logsMoveDown = true;
	protected static boolean popLeaves = true;
	protected static int leafRadius = 3;
	  
	public static boolean Chop(Block block, Player player, World world) {
	    List<Block> blocks = new LinkedList<Block>();
	    Block highest = getHighestLog(block);
	    if (isTree(highest, player, block)){
	      getBlocksToChop(block, highest, blocks);
	      if (logsMoveDown) moveDownLogs(block, blocks, world, player);
	      else popLogs(block, blocks, world, player);
	    }
	    else return false;
	    return true;
	  }
	  
	  @SuppressWarnings("deprecation")
	public static void getBlocksToChop(Block block, Block highest, List<Block> blocks)
	  {
	    while (block.getY() <= highest.getY())
	    {
	      if (!blocks.contains(block)) {
	        blocks.add(block);
	      }
	      getBranches(block, blocks, block.getRelative(BlockFace.NORTH));
	      getBranches(block, blocks, block.getRelative(BlockFace.NORTH_EAST));
	      getBranches(block, blocks, block.getRelative(BlockFace.EAST));
	      getBranches(block, blocks, block.getRelative(BlockFace.SOUTH_EAST));
	      getBranches(block, blocks, block.getRelative(BlockFace.SOUTH));
	      getBranches(block, blocks, block.getRelative(BlockFace.SOUTH_WEST));
	      getBranches(block, blocks, block.getRelative(BlockFace.WEST));
	      getBranches(block, blocks, block.getRelative(BlockFace.NORTH_WEST));
	      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH))) {
	        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH));
	      }
	      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST))) {
	        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST));
	      }
	      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST))) {
	        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST));
	      }
	      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST))) {
	        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST));
	      }
	      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH))) {
	        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH));
	      }
	      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST))) {
	        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST));
	      }
	      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST))) {
	        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST));
	      }
	      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST))) {
	        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST));
	      }
	      if ((block.getData() == 3) || (block.getData() == 7) || (block.getData() == 11) || (block.getData() == 15))
	      {
	        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH, 2))) {
	          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH, 2));
	        }
	        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST, 2))) {
	          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST, 2));
	        }
	        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST, 2))) {
	          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST, 2));
	        }
	        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST, 2))) {
	          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST, 2));
	        }
	        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH, 2))) {
	          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH, 2));
	        }
	        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST, 2))) {
	          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST, 2));
	        }
	        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST, 2))) {
	          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST, 2));
	        }
	        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST, 2))) {
	          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST, 2));
	        }
	      }
	      if (((blocks.contains(block.getRelative(BlockFace.UP))) || (block.getRelative(BlockFace.UP).getType() != Material.LOG)) && (block.getRelative(BlockFace.UP).getType() != Material.LOG_2)) {
	        break;
	      }
	      block = block.getRelative(BlockFace.UP);
	    }
	  }
	  
	  public static void getBranches(Block block, List<Block> blocks, Block other)
	  {
	    if ((!blocks.contains(other)) && ((other.getType() == Material.LOG) || (other.getType() == Material.LOG_2))) {
	      getBlocksToChop(other, getHighestLog(other), blocks);
	    }
	  }
	  
	  public static Block getHighestLog(Block block)
	  {
	    boolean isLog = true;
	    while (isLog) {
	      if ((block.getRelative(BlockFace.UP).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getType().equals(Material.LOG_2)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType().equals(Material.LOG_2)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType().equals(Material.LOG_2)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType().equals(Material.LOG_2)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType().equals(Material.LOG_2)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST).getType().equals(Material.LOG_2)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST).getType().equals(Material.LOG_2)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.LOG_2)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.LOG_2)))
	      {
	        if ((block.getRelative(BlockFace.UP).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP);
	        } else if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
	        } else if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
	        } else if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
	        } else if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
	        } else if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
	        } else if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
	        } else if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
	        } else if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.LOG_2))) {
	          block = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
	        }
	      }
	      else isLog = false;
	    }
	    return block;
	  }
	  
	  @SuppressWarnings("deprecation")
	public static boolean isTree(Block block, Player player, Block first)
	  {
	    if (trees.containsKey(player))
	    {
	      Block[] blockarray = trees.get(player);
	      for (int counter = 0; counter < Array.getLength(blockarray); counter++)
	      {
	        if (blockarray[counter] == block) {
	          return true;
	        }
	        if (blockarray[counter] == first) {
	          return true;
	        }
	      }
	    }
	    int counter = 0;
	    if ((block.getRelative(BlockFace.UP).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.DOWN).getType() == Material.LEAVES) || (block.getRelative(BlockFace.DOWN).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.NORTH).getType() == Material.LEAVES) || (block.getRelative(BlockFace.NORTH).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.EAST).getType() == Material.LEAVES) || (block.getRelative(BlockFace.EAST).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.SOUTH).getType() == Material.LEAVES) || (block.getRelative(BlockFace.SOUTH).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if ((block.getRelative(BlockFace.WEST).getType() == Material.LEAVES) || (block.getRelative(BlockFace.WEST).getType() == Material.LEAVES_2)) {
	      counter++;
	    }
	    if (counter >= 2) {
	      return true;
	    }
	    if (block.getData() == 1)
	    {
	      block = block.getRelative(BlockFace.UP);
	      if ((block.getRelative(BlockFace.UP).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType() == Material.LEAVES) || (block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.NORTH).getType() == Material.LEAVES) || (block.getRelative(BlockFace.NORTH).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.EAST).getType() == Material.LEAVES) || (block.getRelative(BlockFace.EAST).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.SOUTH).getType() == Material.LEAVES) || (block.getRelative(BlockFace.SOUTH).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
	      if ((block.getRelative(BlockFace.WEST).getType() == Material.LEAVES) || (block.getRelative(BlockFace.WEST).getType() == Material.LEAVES_2)) {
	        counter++;
	      }
			return counter >= 2;
	    }
	    return false;
	  }
	  
	  @SuppressWarnings("deprecation")
	  public static void popLogs(Block block, List<Block> blocks, World world, Player player) {
	    ItemStack item = new ItemStack(Material.STONE, 1, (short)0);
	    item.setAmount(1);
	    for (int counter = 0; counter < blocks.size(); counter++) {
	      block = blocks.get(counter);
	      item.setType(block.getType());
	      item.setDurability((short)block.getData());
	      block.breakNaturally();
	      if (popLeaves) popLeaves(block);
	      if ((moreDamageToTools) &&(breaksTool(player, player.getItemInHand()))) {
	        player.getInventory().clear(player.getInventory().getHeldItemSlot());
	        if (interruptIfToolBreaks) break;
	      }
	    }
	  }
	  
	  public static void popLeaves(Block block) {
	    for (int y = -leafRadius; y < leafRadius + 1; y++) {
	      for (int x = -leafRadius; x < leafRadius + 1; x++) {
	        for (int z = -leafRadius; z < leafRadius + 1; z++) {
	          Block target = block.getRelative(x, y, z);
	          if ((target.getType().equals(Material.LEAVES)) || (target.getType().equals(Material.LEAVES_2))) target.breakNaturally();
	        }
	      }
	    }
	  }
	  
	  @SuppressWarnings("deprecation")
	public static void moveDownLogs(Block block, List<Block> blocks, World world, Player player){
	    ItemStack item = new ItemStack(Material.STONE, 1, (short)0);
	    item.setAmount(1);
	    
	    List<Block> downs = new LinkedList<Block>();
	    for (int counter = 0; counter < blocks.size(); counter++) {
	      block = blocks.get(counter);
	      Block down = block.getRelative(BlockFace.DOWN);
	      if ((down.getType() == Material.AIR) || (down.getType() == Material.LEAVES) || (down.getType() == Material.LEAVES_2)){
	        down.setType(block.getType());
	        down.setData(block.getData());
	        block.setType(Material.AIR);
	        downs.add(down);
	      } else {
	        item.setType(block.getType());
	        item.setDurability((short)block.getData());
	        block.setType(Material.AIR);
	        world.dropItem(block.getLocation(), item);
	        if ((moreDamageToTools) && 
	          (breaksTool(player, player.getItemInHand()))) {
	          player.getInventory().clear(player.getInventory().getHeldItemSlot());
	        }
	      }
	    }
	    for (int counter = 0; counter < downs.size(); counter++) {
	      block = downs.get(counter);
	      if (isLoneLog(block)) {
	        downs.remove(block);
	        block.breakNaturally();
	        if ((moreDamageToTools) && 
	          (breaksTool(player, player.getItemInHand()))) {
	          player.getInventory().clear(player.getInventory().getHeldItemSlot());
	        }
	      }
	    }
	    if (popLeaves) moveLeavesDown(blocks);
		  trees.remove(player);
	    if (downs.isEmpty()) return;
	    Block[] blockarray = new Block[downs.size()];
	    for (int counter = 0; counter < downs.size(); counter++) blockarray[counter] = downs.get(counter);
	    trees.put(player, blockarray);
	  }
	  
	  @SuppressWarnings("deprecation")
	  public static void moveLeavesDown(List<Block> blocks) {
	   // ChopTreeBlockListener  = this;
	    List<Block> leaves = new LinkedList<Block>();
	    int y = 0;
	    Iterator<Block> blockIterator = blocks.iterator();
	    int x;
	    while ((blockIterator.hasNext()) && (y < leafRadius + 1))  {
	      Block block = blockIterator.next();
	      y = -leafRadius;
	      for (x = -leafRadius; x < leafRadius + 1; x++) {
	        for (int z = -leafRadius; z < leafRadius + 1; z++) {
	          if (((block.getRelative(x, y, z).getType().equals(Material.LEAVES)) || (block.getRelative(x, y, z).getType().equals(Material.LEAVES_2))) && (!leaves.contains(block.getRelative(x, y, z)))) {
	            leaves.add(block.getRelative(x, y, z));
	          }
	        }
	      }
	      y++;
	    }
	    for (Block block : leaves) {
	      if (((block.getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) || (block.getRelative(BlockFace.DOWN).getType().equals(Material.LEAVES)) || (block.getRelative(BlockFace.DOWN).getType().equals(Material.LEAVES_2))) && ((block.getRelative(BlockFace.DOWN, 2).getType().equals(Material.AIR)) || (block.getRelative(BlockFace.DOWN, 2).getType().equals(Material.LEAVES)) || (block.getRelative(BlockFace.DOWN, 2).getType().equals(Material.LEAVES_2)) || (block.getRelative(BlockFace.DOWN, 2).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.DOWN, 2).getType().equals(Material.LOG_2))) && ((block.getRelative(BlockFace.DOWN, 3).getType().equals(Material.AIR)) || (block.getRelative(BlockFace.DOWN, 3).getType().equals(Material.LEAVES)) || (block.getRelative(BlockFace.DOWN, 3).getType().equals(Material.LEAVES_2)) || (block.getRelative(BlockFace.DOWN, 3).getType().equals(Material.LOG)) || (block.getRelative(BlockFace.DOWN, 3).getType().equals(Material.LOG_2)))){
	        block.getRelative(BlockFace.DOWN).setTypeIdAndData(block.getTypeId(), block.getData(), true);
	        block.setType(Material.AIR);
	      } else block.breakNaturally();
	    }
	  }
	  
	  public static boolean breaksTool(Player player, ItemStack item) {
	    if ((item != null)){
	    	short damage = item.getDurability();
	        damage = (short)(damage + 1);
	      if (damage >= item.getType().getMaxDurability()) return true;
	      item.setDurability(damage);
	    }
	    return false;
	  }
	  
	  public static boolean isLoneLog(Block block)
	  {
	    if ((block.getRelative(BlockFace.UP).getType() == Material.LOG) || (block.getRelative(BlockFace.UP).getType() == Material.LOG_2)) {
	      return false;
	    }
	    if (block.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
	      return false;
	    }
	    if (hasHorizontalCompany(block)) {
	      return false;
	    }
	    if (hasHorizontalCompany(block.getRelative(BlockFace.UP))) {
	      return false;
	    }
		  return !hasHorizontalCompany(block.getRelative(BlockFace.DOWN));
	  }
	  
	  public static boolean hasHorizontalCompany(Block block)
	  {
	    if ((block.getRelative(BlockFace.NORTH).getType() == Material.LOG) || (block.getRelative(BlockFace.NORTH).getType() == Material.LOG_2)) {
	      return true;
	    }
	    if ((block.getRelative(BlockFace.NORTH_EAST).getType() == Material.LOG) || (block.getRelative(BlockFace.NORTH_EAST).getType() == Material.LOG_2)) {
	      return true;
	    }
	    if ((block.getRelative(BlockFace.EAST).getType() == Material.LOG) || (block.getRelative(BlockFace.EAST).getType() == Material.LOG_2)) {
	      return true;
	    }
	    if ((block.getRelative(BlockFace.SOUTH_EAST).getType() == Material.LOG) || (block.getRelative(BlockFace.SOUTH_EAST).getType() == Material.LOG_2)) {
	      return true;
	    }
	    if ((block.getRelative(BlockFace.SOUTH).getType() == Material.LOG) || (block.getRelative(BlockFace.SOUTH).getType() == Material.LOG_2)) {
	      return true;
	    }
	    if ((block.getRelative(BlockFace.SOUTH_WEST).getType() == Material.LOG) || (block.getRelative(BlockFace.SOUTH_WEST).getType() == Material.LOG_2)) {
	      return true;
	    }
	    if ((block.getRelative(BlockFace.WEST).getType() == Material.LOG) || (block.getRelative(BlockFace.WEST).getType() == Material.LOG_2)) {
	      return true;
	    }
		  return (block.getRelative(BlockFace.NORTH_WEST).getType() == Material.LOG) || (block.getRelative(BlockFace.NORTH_WEST).getType() == Material.LOG_2);
	  }
	  
}
