import client.DataDragonClient;
import client.HttpDataDragonClient;

import java.net.URI;
import java.util.Map;

public class DummyClass {
    public static void main(String[] args) {
        try {
            DataDragonClient dd = new HttpDataDragonClient();

            String version = dd.latestVersion();
            System.out.println("Latest version: " + version);
            System.out.println();

            // Test champion endpoints
            System.out.println("=== Champion Endpoints ===");
            var champs = dd.champions(version, "en_US");
            System.out.println("Champions count: " + champs.data().size());

            URI championSquare = dd.championSquare(version, "Fiddlesticks");
            System.out.println("Champion square icon: " + championSquare);

            URI championLoading = dd.championLoading(version, "Fiddlesticks");
            System.out.println("Champion loading screen: " + championLoading);

            URI championSplash = dd.championSplash("Fiddlesticks", 0);
            System.out.println("Champion splash art: " + championSplash);
            System.out.println();

            // Test item endpoints
            System.out.println("=== Item Endpoints ===");
            Map<String, Object> items = dd.items(version, "en_US");
            System.out.println("Items response keys: " + items.keySet());

            URI itemIcon = dd.itemIcon(version, "1001");
            System.out.println("Item icon (Boots): " + itemIcon);
            System.out.println();

            // Test summoner spell endpoints
            System.out.println("=== Summoner Spell Endpoints ===");
            Map<String, Object> summonerSpells = dd.summonerSpells(version, "en_US");
            System.out.println("Summoner spells response keys: " + summonerSpells.keySet());

            URI spellIcon = dd.summonerSpellIcon(version, "SummonerFlash");
            System.out.println("Summoner spell icon (Flash): " + spellIcon);
            System.out.println();

            // Test rune endpoints
            System.out.println("=== Rune Endpoints ===");
            Map<String, Object> runes = dd.runes(version, "en_US");
            System.out.println("Runes response: " + runes.getClass().getSimpleName());

            URI runeIcon = dd.runeIcon("perk-images/Styles/Domination/Electrocute/Electrocute.png");
            System.out.println("Rune icon (Electrocute): " + runeIcon);
            System.out.println();

            // Test profile icon endpoint
            System.out.println("=== Profile Icon Endpoint ===");
            URI profileIcon = dd.profileIcon(version, 1);
            System.out.println("Profile icon: " + profileIcon);

        } catch (Exception e) {
            System.err.println("Error occurred:");
            e.printStackTrace();
        }
    }
}