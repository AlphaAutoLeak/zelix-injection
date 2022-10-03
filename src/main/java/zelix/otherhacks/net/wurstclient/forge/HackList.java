package zelix.otherhacks.net.wurstclient.forge;

import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import zelix.otherhacks.net.wurstclient.forge.hacks.*;
import zelix.otherhacks.net.wurstclient.forge.utils.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import zelix.otherhacks.net.wurstclient.forge.settings.*;
import com.google.gson.*;

public final class HackList extends WHackList
{
    public final AutoFarmHack autoFarmHack;
    public final AutoSprintHack autoSprintHack;
    public final AutoSwimHack autoSwimHack;
    public final AutoToolHack autoToolHack;
    public final AutoWalkHack autoWalkHack;
    public final BunnyHopHack bunnyHopHack;
    public final ChestEspHack chestEspHack;
    public final ClickGuiHack clickGuiHack;
    public final FastBreakHack fastBreakHack;
    public final FastLadderHack fastLadderHack;
    public final FastPlaceHack fastPlaceHack;
    public final FlightHack flightHack;
    public final FullbrightHack fullbrightHack;
    public final GlideHack glideHack;
    public final ItemEspHack itemEspHack;
    public final KillauraHack killauraHack;
    public final MobEspHack mobEspHack;
    public final NoFallHack noFallHack;
    public final NoWebHack noWebHack;
    public final NukerHack nukerHack;
    public final PlayerEspHack playerEspHack;
    public final RadarHack radarHack;
    public final RainbowUiHack rainbowUiHack;
    public final SpiderHack spiderHack;
    public final TimerHack timerHack;
    public final TunnellerHack tunnellerHack;
    public final XRayHack xRayHack;
    private final Path enabledHacksFile;
    private final Path settingsFile;
    private boolean disableSaving;
    
    public HackList(final Path enabledHacksFile, final Path settingsFile) {
        this.autoFarmHack = this.register(new AutoFarmHack());
        this.autoSprintHack = this.register(new AutoSprintHack());
        this.autoSwimHack = this.register(new AutoSwimHack());
        this.autoToolHack = this.register(new AutoToolHack());
        this.autoWalkHack = this.register(new AutoWalkHack());
        this.bunnyHopHack = this.register(new BunnyHopHack());
        this.chestEspHack = this.register(new ChestEspHack());
        this.clickGuiHack = this.register(new ClickGuiHack());
        this.fastBreakHack = this.register(new FastBreakHack());
        this.fastLadderHack = this.register(new FastLadderHack());
        this.fastPlaceHack = this.register(new FastPlaceHack());
        this.flightHack = this.register(new FlightHack());
        this.fullbrightHack = this.register(new FullbrightHack());
        this.glideHack = this.register(new GlideHack());
        this.itemEspHack = this.register(new ItemEspHack());
        this.killauraHack = this.register(new KillauraHack());
        this.mobEspHack = this.register(new MobEspHack());
        this.noFallHack = this.register(new NoFallHack());
        this.noWebHack = this.register(new NoWebHack());
        this.nukerHack = this.register(new NukerHack());
        this.playerEspHack = this.register(new PlayerEspHack());
        this.radarHack = this.register(new RadarHack());
        this.rainbowUiHack = this.register(new RainbowUiHack());
        this.spiderHack = this.register(new SpiderHack());
        this.timerHack = this.register(new TimerHack());
        this.tunnellerHack = this.register(new TunnellerHack());
        this.xRayHack = this.register(new XRayHack());
        this.enabledHacksFile = enabledHacksFile;
        this.settingsFile = settingsFile;
    }
    
    public void loadEnabledHacks() {
        JsonArray json;
        try (final BufferedReader reader = Files.newBufferedReader(this.enabledHacksFile)) {
            json = JsonUtils.jsonParser.parse((Reader)reader).getAsJsonArray();
        }
        catch (NoSuchFileException e3) {
            this.saveEnabledHacks();
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveEnabledHacks();
            return;
        }
        this.disableSaving = true;
        for (final JsonElement e2 : json) {
            if (e2.isJsonPrimitive()) {
                if (!e2.getAsJsonPrimitive().isString()) {
                    continue;
                }
                final Hack hack = this.get(e2.getAsString());
                if (hack == null) {
                    continue;
                }
                if (!hack.isStateSaved()) {
                    continue;
                }
                hack.setEnabled(true);
            }
        }
        this.disableSaving = false;
        this.saveEnabledHacks();
    }
    
    public void saveEnabledHacks() {
        if (this.disableSaving) {
            return;
        }
        final JsonArray enabledHacks = new JsonArray();
        for (final Hack hack : this.getRegistry()) {
            if (hack.isEnabled() && hack.isStateSaved()) {
                enabledHacks.add((JsonElement)new JsonPrimitive(hack.getName()));
            }
        }
        try (final BufferedWriter writer = Files.newBufferedWriter(this.enabledHacksFile, new OpenOption[0])) {
            JsonUtils.prettyGson.toJson((JsonElement)enabledHacks, (Appendable)writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadSettings() {
        JsonObject json;
        try (final BufferedReader reader = Files.newBufferedReader(this.settingsFile)) {
            json = JsonUtils.jsonParser.parse((Reader)reader).getAsJsonObject();
        }
        catch (NoSuchFileException e4) {
            this.saveSettings();
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.saveSettings();
            return;
        }
        this.disableSaving = true;
        for (final Map.Entry<String, JsonElement> e2 : json.entrySet()) {
            if (!e2.getValue().isJsonObject()) {
                continue;
            }
            final Hack hack = this.get(e2.getKey());
            if (hack == null) {
                continue;
            }
            final Map<String, Setting> settings = hack.getSettings();
            for (final Map.Entry<String, JsonElement> e3 : e2.getValue().getAsJsonObject().entrySet()) {
                final String key = e3.getKey().toLowerCase();
                if (!settings.containsKey(key)) {
                    continue;
                }
                settings.get(key).fromJson(e3.getValue());
            }
        }
        this.disableSaving = false;
        this.saveSettings();
    }
    
    public void saveSettings() {
        if (this.disableSaving) {
            return;
        }
        final JsonObject json = new JsonObject();
        for (final Hack hack : this.getRegistry()) {
            if (hack.getSettings().isEmpty()) {
                continue;
            }
            final JsonObject settings = new JsonObject();
            for (final Setting setting : hack.getSettings().values()) {
                settings.add(setting.getName(), setting.toJson());
            }
            json.add(hack.getName(), (JsonElement)settings);
        }
        try (final BufferedWriter writer = Files.newBufferedWriter(this.settingsFile, new OpenOption[0])) {
            JsonUtils.prettyGson.toJson((JsonElement)json, (Appendable)writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
