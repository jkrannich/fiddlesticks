import client.DataDragonClient;
import client.HttpDataDragonClient;

import java.net.URI;

public class DummyClass {
    public static void main(String[] args) {
        DataDragonClient dd = new HttpDataDragonClient();

        String version = dd.latestVersion();
        System.out.println("Latest version: " + version);

        var champs = dd.champions(version, "en_US");
        System.out.println("Champions: " + champs);

        URI icon = dd.championSquare(version, "Fiddlesticks");
        System.out.println("Fiddle icon: " + icon);
    }
}
