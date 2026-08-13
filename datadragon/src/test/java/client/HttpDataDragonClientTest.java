package client;

import dto.ChampionDto;
import dto.ChampionDetailsDto;
import dto.ChampionsIndexDto;
import dto.ItemDataDto;
import dto.ItemsIndexDto;
import dto.RealmDto;
import dto.RuneTreeDto;
import dto.SummonerSpellDto;
import dto.SummonerSpellsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpDataDragonClientTest {

    private static final String BASE = "https://ddragon.leagueoflegends.com";
    private RecordingTransport transport;
    private HttpDataDragonClient client;

    @BeforeEach
    void setUp() {
        transport = new RecordingTransport();
        client = new HttpDataDragonClient(transport);
    }

    @Test
    void shouldReadRealmAndVersionMetadata() {
        URI uri = URI.create(BASE + "/realms/euw.json");
        transport.respond(uri, """
                {
                  "n": {"champion": "16.16.1", "item": "16.16.1"},
                  "v": "16.16.1",
                  "l": "en_GB",
                  "cdn": "https://ddragon.leagueoflegends.com/cdn",
                  "dd": "16.16.1",
                  "lg": "16.16.1",
                  "css": "16.16.1",
                  "profileiconmax": 28,
                  "store": null
                }
                """);

        RealmDto result = client.realm("euw");

        assertThat(result.version()).isEqualTo("16.16.1");
        assertThat(result.locale()).isEqualTo("en_GB");
        assertThat(result.versions()).containsEntry("champion", "16.16.1");
        assertThat(result.profileIconMax()).isEqualTo(28);
        assertThat(transport.requests).containsExactly(uri);
    }

    @Test
    void shouldReadVersionsLanguagesAndSelectTheLatestVersion() {
        URI versionsUri = URI.create(BASE + "/api/versions.json");
        URI languagesUri = URI.create(BASE + "/cdn/languages.json");
        transport.respond(versionsUri, "[\"16.16.1\", \"16.15.1\"]");
        transport.respond(languagesUri, "[\"en_US\", \"de_DE\"]");

        assertThat(client.versions()).containsExactly("16.16.1", "16.15.1");
        assertThat(client.latestVersion()).isEqualTo("16.16.1");
        assertThat(client.languages()).containsExactly("en_US", "de_DE");
        assertThat(transport.requests).containsExactly(versionsUri, versionsUri, languagesUri);
    }

    @Test
    void shouldReadTheEnrichedChampionIndex() {
        URI uri = URI.create(BASE + "/cdn/16.16.1/data/en_US/champion.json");
        transport.respond(uri, """
                {
                  "type": "champion",
                  "format": "standAloneComplex",
                  "version": "16.16.1",
                  "data": {
                    "Aatrox": {
                      "id": "Aatrox",
                      "key": "266",
                      "name": "Aatrox",
                      "title": "the Darkin Blade",
                      "image": {"full": "Aatrox.png", "sprite": "champion0.png", "group": "champion", "x": 0, "y": 0, "w": 48, "h": 48},
                      "blurb": "A brief summary",
                      "info": {"attack": 8, "defense": 4, "magic": 3, "difficulty": 4},
                      "tags": ["Fighter"],
                      "partype": "Blood Well",
                      "stats": {"hp": 650, "hpperlevel": 114, "attackrange": 175, "attackspeedperlevel": 2.5}
                    }
                  }
                }
                """);

        ChampionsIndexDto result = client.champions("16.16.1", "en_US");

        assertThat(result.data()).containsKey("Aatrox");
        assertThat(result.data().get("Aatrox").image().full()).isEqualTo("Aatrox.png");
        assertThat(result.data().get("Aatrox").info().difficulty()).isEqualTo(4);
        assertThat(result.data().get("Aatrox").stats().hpPerLevel()).isEqualTo(114);
        assertThat(result.data().get("Aatrox").stats().attackspeedPerLevel()).isEqualTo(2.5);
    }

    @Test
    void shouldReadFullChampionDetailsAndExtractTheChampion() {
        URI uri = URI.create(BASE + "/cdn/16.16.1/data/en_US/champion/Aatrox.json");
        transport.respond(uri, """
                {
                  "type": "champion",
                  "format": "standAloneComplex",
                  "version": "16.16.1",
                  "data": {
                    "Aatrox": {
                      "id": "Aatrox",
                      "key": "266",
                      "name": "Aatrox",
                      "title": "the Darkin Blade",
                      "image": {"full": "Aatrox.png", "sprite": "champion0.png", "group": "champion", "x": 0, "y": 0, "w": 48, "h": 48},
                      "skins": [{"id": "266000", "num": 0, "name": "default", "chromas": false}],
                      "lore": "Aatrox lore",
                      "blurb": "Aatrox blurb",
                      "allytips": ["Use the edge of the blade."],
                      "enemytips": ["Dodge the blade."],
                      "tags": ["Fighter"],
                      "partype": "Blood Well",
                      "info": {"attack": 8, "defense": 4, "magic": 3, "difficulty": 4},
                      "stats": {"hp": 650, "hpperlevel": 114},
                      "spells": [{
                        "id": "AatroxQ",
                        "name": "The Darkin Blade",
                        "description": "Aatrox slams his greatsword down.",
                        "tooltip": "Deals physical damage.",
                        "leveltip": {"label": ["Cooldown"], "effect": ["14 -> 6"]},
                        "maxrank": 5,
                        "cooldown": [14, 12, 10, 8, 6],
                        "cooldownBurn": "14/12/10/8/6",
                        "cost": [0, 0, 0, 0, 0],
                        "costBurn": "0",
                        "datavalues": {},
                        "effect": [null, [0, 0, 0, 0, 0]],
                        "effectBurn": [null, "0"],
                        "vars": [],
                        "costType": "No Cost",
                        "maxammo": "-1",
                        "range": [25000, 25000, 25000, 25000, 25000],
                        "rangeBurn": "25000",
                        "image": {"full": "AatroxQ.png", "sprite": "spell0.png", "group": "spell", "x": 0, "y": 0, "w": 48, "h": 48},
                        "resource": "No Cost"
                      }],
                      "passive": {
                        "name": "Deathbringer Stance",
                        "description": "Aatrox's passive.",
                        "image": {"full": "Aatrox_Passive.png", "sprite": "passive0.png", "group": "passive", "x": 0, "y": 0, "w": 48, "h": 48}
                      },
                      "recommended": []
                    }
                  }
                }
                """);

        ChampionDetailsDto response = client.championDetails("16.16.1", "en_US", "Aatrox");
        ChampionDto champion = client.champion("16.16.1", "en_US", "Aatrox");

        assertThat(response.data()).containsKey("Aatrox");
        assertThat(champion.name()).isEqualTo("Aatrox");
        assertThat(champion.skins()).hasSize(1);
        assertThat(champion.spells().getFirst().image().full()).isEqualTo("AatroxQ.png");
        assertThat(champion.passive().name()).isEqualTo("Deathbringer Stance");
        assertThat(transport.requests).containsExactly(uri, uri);
    }

    @Test
    void shouldReadFullItemDataIncludingImagesAndMaps() {
        URI uri = URI.create(BASE + "/cdn/16.16.1/data/en_US/item.json");
        transport.respond(uri, """
                {
                  "type": "item",
                  "version": "16.16.1",
                  "basic": {
                    "name": "",
                    "rune": {"isrune": false, "tier": 1, "type": "red"},
                    "gold": {"base": 0, "total": 0, "sell": 0, "purchasable": false},
                    "description": "",
                    "colloq": ";",
                    "plaintext": "",
                    "consumed": false,
                    "stacks": 1,
                    "depth": 1,
                    "consumeOnFull": false,
                    "from": [],
                    "into": [],
                    "specialRecipe": 0,
                    "inStore": true,
                    "hideFromAll": false,
                    "requiredChampion": "",
                    "requiredAlly": "",
                    "stats": {},
                    "maps": {"1": true}
                  },
                  "data": {
                    "1001": {
                      "name": "Boots",
                      "description": "Slightly increases Move Speed",
                      "colloq": ";",
                      "plaintext": "Move faster",
                      "from": [],
                      "into": ["3006"],
                      "image": {"full": "1001.png", "sprite": "item0.png", "group": "item", "x": 0, "y": 0, "w": 48, "h": 48},
                      "gold": {"base": 300, "total": 300, "sell": 210, "purchasable": true},
                      "tags": ["Boots"],
                      "maps": {"11": true, "12": true},
                      "stats": {"FlatMovementSpeedMod": 25},
                      "depth": 1,
                      "inStore": true
                    }
                  }
                }
                """);

        ItemsIndexDto result = client.items("16.16.1", "en_US");
        ItemDataDto boots = result.data().get("1001");

        assertThat(result.basic().rune().type()).isEqualTo("red");
        assertThat(boots.image().full()).isEqualTo("1001.png");
        assertThat(boots.maps()).containsEntry("11", true);
        assertThat(boots.stats()).containsEntry("FlatMovementSpeedMod", 25.0);
    }

    @Test
    void shouldReadFullSummonerSpellData() {
        URI uri = URI.create(BASE + "/cdn/16.16.1/data/en_US/summoner.json");
        transport.respond(uri, """
                {
                  "type": "summoner",
                  "version": "16.16.1",
                  "data": {
                    "SummonerFlash": {
                      "id": "SummonerFlash",
                      "name": "Flash",
                      "description": "Teleport a short distance.",
                      "tooltip": "Teleport.",
                      "leveltip": {"label": ["Cooldown"], "effect": ["300"]},
                      "maxrank": 1,
                      "cooldown": [300],
                      "cooldownBurn": "300",
                      "cost": [0],
                      "costBurn": "0",
                      "datavalues": {},
                      "effect": [null],
                      "effectBurn": [null],
                      "vars": [],
                      "costType": "No Cost",
                      "maxammo": "-1",
                      "range": [200],
                      "rangeBurn": "200",
                      "image": {"full": "SummonerFlash.png", "sprite": "spell0.png", "group": "spell", "x": 0, "y": 0, "w": 48, "h": 48},
                      "resource": "No Cost",
                      "key": "4",
                      "modes": ["CLASSIC"],
                      "summonerLevel": 1
                    }
                  }
                }
                """);

        SummonerSpellsDto result = client.summonerSpells("16.16.1", "en_US");
        SummonerSpellDto flash = result.data().get("SummonerFlash");

        assertThat(flash.image().full()).isEqualTo("SummonerFlash.png");
        assertThat(flash.cooldown()).containsExactly(300.0);
        assertThat(flash.modes()).containsExactly("CLASSIC");
    }

    @Test
    void shouldReadRunesAndBuildAllDocumentedAssetUrls() {
        URI runesUri = URI.create(BASE + "/cdn/16.16.1/data/en_US/runesReforged.json");
        transport.respond(runesUri, """
                [{
                  "id": 8100,
                  "key": "Domination",
                  "icon": "perk-images/Styles/7200_Domination.png",
                  "name": "Domination",
                  "slots": [{"runes": [{"id": 8112, "key": "Electrocute", "icon": "perk-images/Styles/Domination/Electrocute/Electrocute.png", "name": "Electrocute", "shortDesc": "desc", "longDesc": "long"}]}]
                }]
                """);

        List<RuneTreeDto> runes = client.runes("16.16.1", "en_US");

        assertThat(runes).hasSize(1);
        assertThat(runes.getFirst().slots().getFirst().runes().getFirst().name()).isEqualTo("Electrocute");
        assertThat(client.championPassive("16.16.1", "Aatrox_Passive.png"))
                .isEqualTo(URI.create(BASE + "/cdn/16.16.1/img/passive/Aatrox_Passive.png"));
        assertThat(client.championSpell("16.16.1", "AatroxQ.png"))
                .isEqualTo(URI.create(BASE + "/cdn/16.16.1/img/spell/AatroxQ.png"));
        assertThat(client.sprite("16.16.1", "champion0.png"))
                .isEqualTo(URI.create(BASE + "/cdn/16.16.1/img/sprite/champion0.png"));
        assertThat(client.championSquare("16.16.1", "Aatrox"))
                .isEqualTo(URI.create(BASE + "/cdn/16.16.1/img/champion/Aatrox.png"));
        assertThat(client.championLoading("16.16.1", "Aatrox"))
                .isEqualTo(URI.create(BASE + "/cdn/img/champion/loading/Aatrox_0.jpg"));
        assertThat(client.championSplash("Aatrox", 0))
                .isEqualTo(URI.create(BASE + "/cdn/img/champion/splash/Aatrox_0.jpg"));
        assertThat(client.itemIcon("16.16.1", "1001"))
                .isEqualTo(URI.create(BASE + "/cdn/16.16.1/img/item/1001.png"));
        assertThat(client.summonerSpellIcon("16.16.1", "SummonerFlash"))
                .isEqualTo(URI.create(BASE + "/cdn/16.16.1/img/spell/SummonerFlash.png"));
        assertThat(client.profileIcon("16.16.1", 1))
                .isEqualTo(URI.create(BASE + "/cdn/16.16.1/img/profileicon/1.png"));
        assertThat(client.runeIcon("perk-images/Styles/Domination/Electrocute/Electrocute.png"))
                .isEqualTo(URI.create(BASE + "/cdn/img/perk-images/Styles/Domination/Electrocute/Electrocute.png"));
        assertThat(client.patchArchive("16.16.1"))
                .isEqualTo(URI.create(BASE + "/cdn/dragontail-16.16.1.tgz"));
        assertThat(transport.requests).containsExactly(runesUri);
    }

    @Test
    void shouldDownloadPatchArchiveBytesFromTheDocumentedUrl() {
        URI uri = URI.create(BASE + "/cdn/dragontail-16.16.1.tgz");
        byte[] archive = new byte[]{0x1, 0x2, 0x3};
        transport.respond(uri, archive);

        assertThat(client.downloadPatchArchive("16.16.1")).containsExactly(0x1, 0x2, 0x3);
        assertThat(transport.requests).containsExactly(uri);
    }

    private static final class RecordingTransport implements DataDragonTransport {
        private final Map<URI, byte[]> responses = new HashMap<>();
        private final List<URI> requests = new java.util.ArrayList<>();

        private void respond(URI uri, String body) {
            respond(uri, body.getBytes(StandardCharsets.UTF_8));
        }

        private void respond(URI uri, byte[] body) {
            responses.put(uri, body);
        }

        @Override
        public byte[] get(URI uri) {
            requests.add(uri);
            byte[] response = responses.get(uri);
            if (response == null) {
                throw new AssertionError("No fake response configured for " + uri);
            }
            return response;
        }
    }
}
