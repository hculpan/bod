package org.culpan.bod;

import java.util.Random;

public class Utils {
    public enum SuccessLevel { critical_success, success, failure, critical_failure }

    public static final Random random = new Random();

    public static double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    public static SuccessLevel rollSkill(int skillLevel) {
        int roll = random.nextInt(100) + 1;
        if (roll == 100 || (roll == 99 && skillLevel < 100)) {
            return SuccessLevel.critical_failure;
        } else if (roll > skillLevel) {
            return SuccessLevel.failure;
        } else if (roll <= Math.ceil((double)skillLevel / 10d)) {
            return SuccessLevel.critical_success;
        } else {
            return SuccessLevel.success;
        }
    }

    public static boolean isSuccess(SuccessLevel successLevel) {
        return successLevel.ordinal() < 2;
    }
}
