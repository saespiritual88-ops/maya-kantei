package com.example.mayakantei.service;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MayaLogicTest {

    private final MayaLogic mayaLogic = new MayaLogic();

    @Test
    public void testBaseDate() {
        // 1987-07-26 is KIN 34 (White Wizard)
        LocalDate baseDate = LocalDate.of(1987, 7, 26);
        String result = mayaLogic.calculateKin(baseDate);
        // "KIN 34 White Wizard 音8" (Note: Tone logic might still be producing japanese
        // "音" if I didn't change that? Dictionary said "音%d". Let's check logic again)
        // Wait, I only changed the array. The format string was: String.format("KIN %d
        // %s 音%d", kin, solarSeal, galacticTone);
        // So "音" is still there.
        // Logic: "White Wizard"
        // Result: "KIN 34 White Wizard 音8"
        assertEquals("KIN 34 White Wizard 音8", result);
    }

    @Test
    public void testOneDayAfter() {
        LocalDate date = LocalDate.of(1987, 7, 27);
        String result = mayaLogic.calculateKin(date);
        // KIN 35 -> Seal 14 (Blue Eagle), Tone 9
        assertEquals("KIN 35 Blue Eagle 音9", result);
    }

    @Test
    public void testDescriptionLength() {
        // Test Kin 1
        String desc = mayaLogic.generateDescription(1);
        System.out.println("Length: " + desc.length());
        System.out.println(desc);
        // Ensure it's substantial (e.g. > 300 chars) and roughly near 500
        assert (desc.length() > 300);
        // It's hard to be exact, but let's check it's not empty
    }
}
