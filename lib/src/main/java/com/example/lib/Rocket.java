package com.example.lib;

public class Rocket implements SpaceShip {
     int cost ;
     int weight ;
     int Max_weight ;
     int current_weight;
     float Chance_of_launch_explosion ;
     float Chance_of_landing_crash ;
    Rocket(int cost,int weight,int Max){
        this.cost=cost;
        this.weight=weight;
        this.Max_weight=Max;

    }
    @Override
    public boolean launch() {
        return true;
    }

    @Override
    public boolean land() {
        return true;
    }

    @Override
    public boolean canCarry(Item i) {
        Carry(i);
        if(current_weight>Max_weight){
            current_weight-=i.weight;
            return false;
        }
        return true;
    }

    @Override
    public void Carry(Item i) {
        current_weight+=i.weight;
    }
}
