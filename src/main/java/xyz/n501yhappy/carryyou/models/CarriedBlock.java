package xyz.n501yhappy.carryyou.models;

import org.bukkit.block.BlockState;

import java.util.UUID;

public record CarriedBlock(UUID owner, BlockState blockState) {}
