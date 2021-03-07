package net.emirikol.amm.entity.goal;

import net.emirikol.amm.entity.*;

import net.minecraft.block.entity.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.inventory.*;
import net.minecraft.util.math.*;

import java.util.*;

public class GolemExtractItemGoal extends Goal {
	private final AbstractGolemEntity entity;
	
	private Inventory container;
	
	public GolemExtractItemGoal(AbstractGolemEntity entity) {
		this.entity = entity;
	}
	
	public boolean canStart() {
		//Check whether golem has an empty hand, linked block is an inventory, and linked block is close enough to extract from.
		return entity.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty() && linkedBlockIsContainer() && linkedBlockIsNearby() && linkedBlockCanExtract();
	}
	
	public void tick() {
		if (this.entity.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()) {
			for (int i = 0; i < this.container.size(); i++) {
				ItemStack stack = this.container.getStack(i);
				if (!stack.isEmpty()) {
					this.entity.equipStack(EquipmentSlot.MAINHAND, stack.split(1));
				}
			}
		}
	}
	
	private boolean linkedBlockIsContainer() {
		BlockPos pos = this.entity.getLinkedBlockPos();
		if (pos == null) { return false; }
		BlockEntity blockEntity = this.entity.world.getBlockEntity(pos);
		if (blockEntity == null) { return false; }
		if (blockEntity instanceof Inventory) {
			this.container = (Inventory) blockEntity;
			return true;
		} else {
			return false;
		}
	}
	
	private boolean linkedBlockIsNearby() {
		BlockPos pos = this.entity.getLinkedBlockPos();
		if (pos == null) {
			return false;
		}
		return pos.isWithinDistance(this.entity.getPos(), this.getDesiredSquaredDistanceToTarget());
	}
	
	private boolean linkedBlockCanExtract() {
		for (int i = 0; i < this.container.size(); i++) {
			ItemStack stack = this.container.getStack(i);
			if (!stack.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public double getDesiredSquaredDistanceToTarget() {
		return 2.5D;
	}
}