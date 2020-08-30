/**
 * Copyright 2020 lambdaprime
 * 
 * Allows automatically resolve all conflicts in git repository
 * either by taking theirs changes or ours.
 *
 * It is especially useful in situations when you restore
 * your changes from stash. In that case you cannot specify a strategy
 * how to resolve conflicts like with rebase/cherry-pick (-X option)
 * and have to do that manually.
 * 
 * Requires jeval
 */
boolean THEIRS;

ThrowingConsumer<Path, Exception> resolveConflicts = path -> {
    path = path.toRealPath();
    var file = Files.readAllLines(path).stream()
        .collect(toList());
    boolean inside = false;
    int c = 0;
    while (c < file.size()) {
        var line = file.get(c);
        if (line.startsWith("<<<<<<<")) {
            file.remove(c);
            if (THEIRS) inside = true;
        } else if (line.startsWith(">>>>>>>")) {
            file.remove(c);
            inside = false;
        } else if (line.startsWith("=======")) {
            file.remove(c);
            inside = !inside;
        } else if (inside) {
            file.remove(c);
        } else
            c++;
    }

    Files.write(path, file);

    out.println("Resolved " + path);
};


if (args.length < 2) {
    out.println("Expected arguments: < ours | theirs | stashed > <path_to_repository>");
    exit(0);
}

THEIRS = !args[0].equals("ours");

var repo = Paths.get(args[1]).toAbsolutePath();

new XExec("git status")
    .withDirectory(repo.toString())
    .run()
    .forwardStderr()
    .stdout()
    .filter(l -> l.contains("both modified:"))
    .map(l -> l.replaceAll("\\s*both modified:\\s*(.*)", "$1"))
    .map(l -> repo.resolve(l))
    .forEach(Unchecked.wrapAccept(resolveConflicts));
