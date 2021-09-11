/**
 * Searches for all video files (mp4) and moves them under "video" subfolder
 * of their original folder.
 * Then it compress them using ffmpeg with default settings.
 * Compressed video saved with name "<file-name>-compressed".
 *
 * Requirements:
 *
 * - jeval 23 <https://github.com/lambdaprime/jeval>
 * - ffmpeg
 *
 * @author lambdaprime <https://github.com/lambdaprime>
 *
 */

if (args.length < 1)
    error("Please specify input folder");

// if we moved a file to new folder we don't want to process
// it again
Set<Path> ignore = new HashSet<>();

Set<Path> outputFolders = new HashSet<>();

ThrowingConsumer<Path, Exception> compress = path -> {
    if (ignore.contains(path)) return;
    out.println("File found " + path);
    if (!"video".equals(path.getParent().getFileName().toString())) {
        Path videoFolder = path.resolveSibling("video");
        videoFolder.toFile().mkdir();
        var newPath = videoFolder.resolve(path.getFileName());
        Files.move(path, newPath);
        path = newPath;
    }
    ignore.add(path);
    var outPath = XPaths.append(path, "-compressed");
    // don't compress twice
    if (outPath.toFile().exists() ||
        path.getFileName().toString().contains("-compressed")) {
        out.println("File was previously compressed, ignoring...");
    } else {
        var proc = new XExec(String.format("ffmpeg -i \"%s\" \"%s\"", path, outPath))
            .run();
        if (proc.code().get() != 0) {
            error(proc.stdoutAsString() + proc.stderrAsString());
        }
        outputFolders.add(path.getParent());
    }
    out.println();
};

Files.walk(Paths.get(args[0]))
    .filter(p -> !p.toFile().isDirectory())
    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".mp4"))
    .forEach(Unchecked.wrapAccept(compress));

if (outputFolders.isEmpty()) {
    out.println("No files were found for compression");
} else {
    out.println();
    out.println("All processed video files moved to the following output locations:");
    outputFolders.stream()
        .forEach(out::println);
}