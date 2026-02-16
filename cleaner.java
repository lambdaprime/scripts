#!/usr/bin/env jeval

/**
 * Clean up system to reclaim disk space
 *
 * Requirements:
 *
 * - jeval v26
 *
 * @author lambdaprime intid@protonmail.com
 */

String resolveHome(String path) {
    return path.replaceFirst("^~", System.getProperty("user.home").replace("\\", "/"));
}

void log(String line) {
    try {
        line = "[%s] %s\n".formatted(Instant.now(), line);
        out.print(line);
        Files.writeString(Path.of(resolveHome("~/.cleaner.log")), line,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    } catch (Exception e) {
         err.format("Log error: %s\n", e);   
    }
}

void deleteRecursively(String path) {
    path = resolveHome(path);
    try {
        log("Remove " + path);
        if (path.contains("*"))
            XFiles.findFiles(path)
                .peek(f -> log("Remove " + f))
                .forEach(Unchecked.wrapAccept(Files::delete));
        else
            XFiles.deleteRecursively(Path.of(path));
    } catch (Exception e) {
        log("Error removing %s: %s".formatted(path, e.getMessage()));
    }
}

void substitute(String path, boolean isPerLine, Map<String, String> mapping) {
    var substitutor = new Substitutor().withRegexpSupport();
    var fullPath = Path.of(resolveHome(path));
    try {
        var updatedFiles = isPerLine? substitutor.substitutePerLine(fullPath, mapping)
            : substitutor.substitute(fullPath, mapping);
        log("Update files: " + updatedFiles);
    } catch (Exception e) {
        log("Error updating %s: %s".formatted(fullPath, e.getMessage()));
    }
}

void run(String... cmd) {
    var exec = new XExec(cmd);
    log("Run " + Arrays.toString(exec.getCommand()));
    exec
        .start()
        .forwardStdoutAsync(true)
        .stderrThrow();
}

void linuxCleanup() {
    log("Run Linux cleanup");
    deleteRecursively("~/.cache/mozilla");
    deleteRecursively("~/.cache/thumbnails");
    deleteRecursively("~/.cache/vlc");
    deleteRecursively("~/.local/share/recently-used.xbel");
    deleteRecursively("~/.thumbnails");
    deleteRecursively("~/.config/xnviewmp/XnView.db");
    deleteRecursively("~/.config/lximage-qt");
    deleteRecursively("~/.config/qpdfview");
    deleteRecursively("~/.config/lxqt/debug.log");
    deleteRecursively("~/.config/libreoffice/*/user/registrymodifications.xcu");
    deleteRecursively("~/.config/libreoffice/*/user/backup/**");
    deleteRecursively("~/.config/Microsoft");
    deleteRecursively("~/.java/.userPrefs/tool/JShell/prefs.xml");
    deleteRecursively("~/.xsession-errors");
    deleteRecursively("~/.sqlite_history");
    deleteRecursively("~/.lesshst");
    deleteRecursively("~/.octave_hist");
    deleteRecursively("~/hs_err*.log");
    deleteRecursively("~/.emacs.d/ellama-sessions/*");

    substitute("~/.config/UMLet/umlet.cfg", true, Map.of(
        "^recent_files=.*", "recent_files="));
    substitute("~/.config/vlc/vlc-qt-interface.conf", true, Map.of(
        "^list=.*", "list="));
    substitute("~/.config/QtProject.conf", true, Map.of(
        "^lastVisited=.*", "lastVisited="));
    substitute("~/.config/Foxit Software/Foxit Reader.conf", false, Map.of(
        "\nrecentFileList=.*", "\nrecentFileList=",
        "\nlastOpenFilePath=.*", "\nlastOpenFilePath=",
        "\nHistory.*", ""));

    // adb keeps running even after you disconnected the phone
    run("adb kill-server");
}

void windowsCleanup() {
    log("Run Windows cleanup");
    deleteRecursively("C:\\Users\\All Users\\Adobe\\ARM");
    deleteRecursively("C:\\ProgramData\\Adobe\\ARM");
    deleteRecursively("C:\\ProgramData\\Microsoft\\EdgeUpdate\\Log");
    deleteRecursively("C:\\Program Files (x86)\\Microsoft\\EdgeUpdate");
    deleteRecursively("C:\\Users\\*\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\*\\storage\\default\\**");
    deleteRecursively("C:\\Users\\*\\AppData\\Local\\Microsoft\\Edge\\**");
    deleteRecursively("C:\\Users\\*\\AppData\\Local\\Microsoft\\Internet Explorer\\**");
    deleteRecursively("C:\\Users\\*\\AppData\\Local\\Microsoft\\Windows\\WebCache\\**");
    deleteRecursively("C:\\Users\\*\\AppData\\Local\\CrashDumps\\**");
    deleteRecursively("C:\\Users\\*\\AppData\\Local\\Microsoft\\OneDrive\\**");
    deleteRecursively("C:\\Users\\*\\AppData\\Local\\Temp\\**");
    deleteRecursively("C:\\Users\\*\\AppData\\Local\\Adobe\\ARM\\**");

    run("schtasks.exe",
              "/Run",
              "/TN",
              "\\Microsoft\\Windows\\Servicing\\StartComponentCleanup");
}

if (XUtils.isWindows()) {
    windowsCleanup();
} else {
    linuxCleanup();
}

log("Complete");