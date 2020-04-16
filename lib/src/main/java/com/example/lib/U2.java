package com.example.lib;

public class U2 extends Rocket {
int current_weight;
    U2() {
        super(120, 18, 29);
        this.current_weight=0;



    }

    @Override
    public boolean land() {
        Chance_of_landing_crash= 8*(current_weight / Max_weight);
        if(Chance_of_landing_crash<7.5)
            return true;
        return false;
    }

    @Override
    public boolean launch() {
        Chance_of_launch_explosion= 4 * (current_weight / Max_weight);
        if(Chance_of_launch_explosion<3.8)
            return true;
        return false;

    }
}
