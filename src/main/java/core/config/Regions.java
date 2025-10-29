package core.config;

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
        RU;
        public String baseUrl() {
            return "https://"+this.name().toLowerCase()+".api.riotgames.com";
        }
    }

    public enum RegionalRoute {
        AMERICAS, ASIA, EUROPE, SEA;
        public String baseUrl() {
            return "https://"+this.name().toLowerCase()+".api.riotgames.com";
        }
    }
}
