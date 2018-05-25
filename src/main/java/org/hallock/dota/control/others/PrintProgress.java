package org.hallock.dota.control.others;

import org.hallock.dota.control.Registry;
import org.hallock.dota.model.Hero;
import org.hallock.dota.model.HeroState;

public class PrintProgress {

    private static final class Required {
        public final HeroState state;
        public final int num;

        public Required(HeroState state,  int num) {
            this.state = state;
            this.num = num;
        }
    }

    public static void printProgress() {

        boolean printNeeded = true;
        boolean printHave = true;


        int completeCount = 0;
        int totalCount = 0;
        for (Hero hero : Registry.getInstance().heroes.getAll()) {
            System.out.println(hero.id);
            for (Required requirement : new Required[]{
                    new Required(HeroState.PickedDire, 1),
                    new Required(HeroState.PickedRadiant, 1),
                    new Required(HeroState.Banned, 2),
                    new Required(HeroState.Available, 2),
                    new Required(HeroState.Unavailable, 2),
            }) {
                int count = 0;
                for (Hero.ImageInformation info : hero.getCache()) {
                    if (info.state.equals(requirement.state)) {
                        count++;
                    }
                }
                totalCount += requirement.num;
                boolean have = count >= requirement.num;
                if (have) {
                    completeCount += requirement.num;
                } else {
                    completeCount += count;
                }


                if (have) {
                    if (printHave) {
                        System.out.print('\t');
                        System.out.print(requirement.state.name() + ": ");
                        System.out.print("yes");
                        System.out.print(" ");
                    }
                } else {
                    if (printNeeded) {
                        System.out.print('\t');
                        System.out.print(requirement.state.name() + ": ");
                        System.out.print("no ");
                        System.out.print(" ");
                    }
                }
            }

            System.out.println();
        }
        System.out.println(completeCount + " of " + totalCount + ": " + (completeCount / (double) totalCount));
    }
}
