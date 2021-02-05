/*
 * Copyright (c) 2017-2021 Hugo Dupanloup (Yeregorix)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
	public static final int MINIMUM_VERSION = 1;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length == 0)
			return;

		int version = Integer.parseInt(args[0]);
		if (version < MINIMUM_VERSION)
			throw new IllegalArgumentException("version");

		if (args.length < 4)
			throw new IllegalArgumentException("length");

		update(Paths.get(args[1]), Paths.get(args[2]), args[3].equalsIgnoreCase("true"), Arrays.copyOfRange(args, 4, args.length));
	}

	public static void update(Path source, Path target, boolean launch, String[] args) throws IOException, InterruptedException {
		if (!Files.exists(source))
			return;
		move(source, target);
		if (launch)
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
