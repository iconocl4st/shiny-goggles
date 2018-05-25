package org.hallock.dota.model;

import org.hallock.dota.control.AutoPickerBuilder;
import org.hallock.dota.control.Registry;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hero {
    public String id;
    public String display;
    int pickId;
    LinkedList<ImageInformation> imageCache;

    public Hero(JSONObject heroConfig) throws JSONException {
        this.id = heroConfig.getString("name");
        this.pickId = heroConfig.getInt("id");
        this.display = heroConfig.getString("localized_name");
        imageCache = new LinkedList<>();
    }

    public int getIndex() {
        return pickId;
    }

    public LinkedList<ImageInformation> getCache() {
        return imageCache;
    }

    public String toString() {
        return display;
    }

    private Path getImageDirectory() {
        return Paths.get(Registry.getInstance().config.imageDirectory, id);
    }

    private boolean contains(String path) {
        for (ImageInformation info : imageCache) {
            if (info.path.toString().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public void addImage(HeroState state, BufferedImage image) throws IOException {
        int counter = 0;
        Path p;
        while (Files.exists(p = getImageDirectory().resolve(
                String.format("%s_%04d.png", getStateFilename(state), counter)
        ))) {
            counter++;
        }

        ImageIO.write(image, "png", p.toFile());

        imageCache.add(new ImageInformation(
                p,
                image,
                state,
                counter
        ));

        try {
            Registry.getInstance().picker.setIdentifiers(AutoPickerBuilder.builIdentifiers());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateCache() {
        Path path = getImageDirectory();
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        imageCache.removeIf((info) -> !Files.exists(info.path));

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(path);) {
            for (Path p : paths) {
                if (!Files.isRegularFile(p)) {
                    continue;
                }
                if (contains(p.toString())) {
                    continue;
                }
                Matcher matcher = FILENAME_PARSER.matcher(p.getFileName().toString());
                if (!matcher.find()) {
                    continue;
                }
                imageCache.add(new ImageInformation(
                        p,
                        ImageIO.read(p.toFile()),
                        getHeroState(matcher),
                        getFileNumber(matcher)
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage[] getImagesByType(HeroState state) {
        updateCache();
        LinkedList<BufferedImage> images = new LinkedList<>();
        for (ImageInformation info : imageCache) {
            if (!info.state.equals(state)) {
                continue;
            }
            images.add(info.image);
        }
        return images.toArray(new BufferedImage[0]);
    }


    public static class ImageInformation {
        Path path;
        public BufferedImage image;
        public HeroState state;
        int num;

        ImageInformation(Path path, BufferedImage image, HeroState state, int num) {
            this.path = path;
            this.image = image;
            this.state = state;
            this.num = num;
        }
    }


    private static final String BANNED_FILENAME = "banned";
    private static final String RADIANT_PICKED_FILENAME = "radiant_picked";
    private static final String DIRE_PICKED_FILENAME = "dire_picked";
    private static final String AVAILABLE_FILENAME = "available";
    private static final String UNALIABLE_FILENAME = "unavailable";
    private static final String[] IMAGE_TYPES = new String[]{
            BANNED_FILENAME,
            RADIANT_PICKED_FILENAME,
            DIRE_PICKED_FILENAME,
            AVAILABLE_FILENAME,
            UNALIABLE_FILENAME
    };


    private static final Pattern FILENAME_PARSER = Pattern.compile("(" + String.join("|", IMAGE_TYPES) + ")_([0-9]*)\\.png");
    private static HeroState getHeroState(Matcher matcher) {
        switch (matcher.group(1)) {
            case BANNED_FILENAME: return HeroState.Banned;
            case UNALIABLE_FILENAME: return HeroState.Unavailable;
            case RADIANT_PICKED_FILENAME: return HeroState.PickedRadiant;
            case DIRE_PICKED_FILENAME: return HeroState.PickedDire;
            case AVAILABLE_FILENAME: return HeroState.Available;
            default:
                throw new IllegalStateException("Unrecognized file name: " + matcher.group(1));
        }
    }
    private static String getStateFilename(HeroState state) {
        switch (state) {
            case Banned:
                return BANNED_FILENAME;
            case Available:
                return AVAILABLE_FILENAME;
            case PickedRadiant:
                return RADIANT_PICKED_FILENAME;
            case PickedDire:
                return DIRE_PICKED_FILENAME;
            case Unavailable:
                return UNALIABLE_FILENAME;
            case Unidentified:
            case Picked:
            default:
                throw new IllegalStateException("Unexpected image type: " + state);
        }
    }
    private static int getFileNumber(Matcher matcher) {
        return Integer.parseInt(matcher.group(2));
    }
}
