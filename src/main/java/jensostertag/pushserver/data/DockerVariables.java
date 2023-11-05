package jensostertag.pushserver.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DockerVariables {
    public static boolean isDocker() {
        try(Stream<String> stream = Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker")) || DockerVariables.isDockerEnforced();
        } catch(IOException e) {
            return false;
        }
    }

    public static boolean isDockerEnforced() {
        return Boolean.parseBoolean(System.getenv("DOCKER"));
    }

    public static String schemaBaseDirectory() {
        if(DockerVariables.isDocker()) {
            return "/app/schema/";
        }

        return "src/resources/schema/";
    }
}
