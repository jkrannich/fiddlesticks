package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Perks(List<Perkstyle> styles, PerkStatPerks statPerks) {}
