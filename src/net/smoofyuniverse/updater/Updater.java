package net.smoofyuniverse.updater;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Updater {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length < 2)
			return;
		update(Paths.get(args[0]), Paths.get(args[1]), Arrays.copyOfRange(args, 2, args.length));
	}
	
	public static void update(Path source, Path target, String[] args) throws IOException, InterruptedException {
		if (!Files.exists(source))
			return;
		move(source, target);
		launch(target, args);
	}
	
	public static void move(Path source, Path target) throws IOException, InterruptedException {
		for (int i = 0; i < 100; i++) {
			try {
				Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
				return;
			} catch (IOException e) {
				Thread.sleep(100);
			}
		}
		Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public static void launch(Path target, String[] args) throws IOException {
		List<String> l = new ArrayList<>(args.length +3);
		l.add("java");
		l.add("-jar");
		l.add(target.toAbsolutePath().toString());
		for (String arg : args)
			l.add(arg);
		new ProcessBuilder().command(l).start();
	}
}
