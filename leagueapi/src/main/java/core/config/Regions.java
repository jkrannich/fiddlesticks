package core.config;

import java.util.Locale;

public class Regions {

    public enum PlatformRegion {
        BR1,
        EUN1,
        EUW1,
        JP1,
        KR,
        LA1,
        LA2,
        NA1,
        OC1,
        TR1,
        RU,
        PH2,
        SG2,
        TH2,
        TW2,
        VN2;
        public String baseUrl() {
            return "https://" + this.name().toLowerCase(Locale.ROOT) + ".api.riotgames.com";
        }
    }

    public enum RegionalRoute {
        AMERICAS, ASIA, EUROPE, SEA;
        public String baseUrl() {
            return "https://" + this.name().toLowerCase(Locale.ROOT) + ".api.riotgames.com";
        }
    }
}
