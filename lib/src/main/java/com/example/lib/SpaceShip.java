package com.example.lib;

public interface SpaceShip {
   boolean launch();
   boolean land();
   boolean canCarry(Item i);
   void Carry(Item i);
}
