package org.hallock.dota.model;

import org.hallock.dota.control.Registry;
import org.json.JSONException;
import org.json.JSONObject;
import sun.security.x509.AVA;

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
    String id;
    String display;
    int pickId;
    LinkedList<ImageInformation> imageCache;

    public Hero(JSONObject heroConfig) throws JSONException {
        this.id = heroConfig.getString("id");
        this.pickId = heroConfig.getInt("pickId");
        this.display = heroConfig.getString("display");
        imageCache = new LinkedList<>();
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


    private static class ImageInformation {
        Path path;
        BufferedImage image;
        HeroState state;
        int num;

        ImageInformation(Path path, BufferedImage image, HeroState state, int num) {
            this.path = path;
            this.image = image;
            this.state = state;
            this.num = num;
        }
    }


    private static final String BANNED_FILENAME = "banned";
    private static final String PICKED_FILENAME = "picked";
    private static final String AVAILABLE_FILENAME = "available";
    private static final String UNALIABLE_FILENAME = "unavailable";
    private static final String[] IMAGE_TYPES = new String[]{
            BANNED_FILENAME,
            PICKED_FILENAME,
            AVAILABLE_FILENAME,
            UNALIABLE_FILENAME
    };


    private static final Pattern FILENAME_PARSER = Pattern.compile("(" + String.join("|", IMAGE_TYPES) + ")_([0-9])\\.png");
    private static HeroState getHeroState(Matcher matcher) {
        switch (matcher.group(1)) {
            case BANNED_FILENAME: return HeroState.Banned;
            case UNALIABLE_FILENAME: return HeroState.Unavailable;
            case PICKED_FILENAME: return HeroState.Picked;
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
            case Picked:
            case PickedRadiant:
            case PickedDire:
                return PICKED_FILENAME;
            case Unavailable:
                return UNALIABLE_FILENAME;
            case Unidentified:
            default:
                throw new IllegalStateException("Unexpected image type: " + state);
        }
    }
    private static int getFileNumber(Matcher matcher) {
        return Integer.parseInt(matcher.group(2));
    }
}
