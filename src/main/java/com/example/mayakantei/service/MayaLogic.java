package com.example.mayakantei.service;

import java.time.LocalDate;

public class MayaLogic {

    private static final LocalDate BASE_DATE = LocalDate.of(1987, 7, 26);
    private static final int BASE_KIN = 34;

    private static final String[] SOLAR_SEALS = {
            "赤い龍", "白い風", "青い夜", "黄色い種",
            "赤い蛇", "白い世界の橋渡し", "青い手", "黄色い星",
            "赤い月", "白い犬", "青い猿", "黄色い人",
            "赤い空歩く人", "白い魔法使い", "青い鷲", "黄色い戦士",
            "赤い地球", "白い鏡", "青い嵐", "黄色い太陽"
    };

    private static final String[] GALACTIC_TONES = {
            "磁気", "月", "電気", "自己存在", "倍音",
            "律動", "共振", "銀河", "太陽", "惑星",
            "スペクトル", "水晶", "宇宙"
    };

    public String calculateKin(LocalDate date) {
        long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(BASE_DATE, date);

        // Java % operator can return negative values for negative operands, so we
        // handle that.
        long kinVal = (BASE_KIN + daysDiff) % 260;

        if (kinVal <= 0) {
            kinVal += 260;
        }

        int kin = (int) kinVal;
        int solarSealIndex = (kin - 1) % 20;
        int galacticToneIndex = (kin - 1) % 13;

        String solarSeal = SOLAR_SEALS[solarSealIndex];
        String galacticTone = GALACTIC_TONES[galacticToneIndex];

        return String.format("KIN%d%s %s", kin, solarSeal, galacticTone);
    }

    // Helper to get int Kin
    public int calculateKinNumber(LocalDate date) {
        long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(BASE_DATE, date);
        long kinVal = (BASE_KIN + daysDiff) % 260;
        if (kinVal <= 0)
            kinVal += 260;
        return (int) kinVal;
    }

    // Helper to get Seal Name
    public String getSolarSealName(int kin) {
        int solarSealIndex = (kin - 1) % 20;
        return SOLAR_SEALS[solarSealIndex];
    }

    public String getSealColor(String solarSeal) {
        if (solarSeal.startsWith("赤い"))
            return "#FF6B6B";
        if (solarSeal.startsWith("白い"))
            return "#F8F9FA";
        if (solarSeal.startsWith("青い"))
            return "#4D96FF";
        if (solarSeal.startsWith("黄色い"))
            return "#FFD93D";
        return "#CCCCCC";
    }

    public String generateDescription(int kin) {
        int solarSealIndex = (kin - 1) % 20;
        int galacticToneIndex = (kin - 1) % 13;
        String solarSeal = SOLAR_SEALS[solarSealIndex];
        String galacticTone = GALACTIC_TONES[galacticToneIndex];

        StringBuilder sb = new StringBuilder();
        sb.append("今日のあなたのKINは「KIN ").append(kin).append("」です。\n");
        sb.append("太陽の紋章は「").append(solarSeal).append("」、銀河の音は「").append(galacticTone).append("」です。\n\n");

        sb.append("【この日のエネルギーについて】\n");
        sb.append("マヤ暦において、KIN ").append(kin).append(" は非常にユニークなエネルギーを持っています。");
        sb.append(solarSeal).append("のエネルギーは、あなたに新しい視点をもたらし、周囲との調和を促す力があります。");
        sb.append("一方、").append(galacticTone).append("の音は、行動のリズムを整え、意図を明確にするためのサポートをしてくれます。\n\n");

        sb.append("【過ごし方のアドバイス】\n");
        sb.append("今日は、自分の内面と向き合うのに適した日です。忙しい日常の中で少し立ち止まり、本当に大切なものが何かを問いかけてみてください。");
        sb.append("特に「").append(solarSeal).append("」の日は、直感を信じて行動することが幸運の鍵となります。");
        sb.append("また、").append(galacticTone).append("の響きを感じながら、周囲の人々に感謝の気持ちを伝えることで、さらに良い流れを引き寄せることができるでしょう。\n\n");

        sb.append("【開運のポイント】\n");
        sb.append("・朝一番に深呼吸をして、新しいエネルギーを取り込むこと。\n");
        sb.append("・「ありがとう」という言葉を意識して使うこと。\n");
        sb.append("・寝る前に今日一日を振り返り、自分自身を褒めてあげること。\n\n");

        sb.append("このKINのエネルギーを最大限に活用し、素晴らしい一日をお過ごしください。");
        sb.append("あなたの人生がより豊かで輝かしいものになりますように。マヤの叡智があなたの道しるべとなることを願っています。");

        // This should be roughly 400-500 japanese characters including spaces/newlines.

        return sb.toString();
    }
}
