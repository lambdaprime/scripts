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

Path fullPath(String path) {
    return Path.of(path.replaceFirst("^~", System.getProperty("user.home"))).toAbsolutePath();
}

void log(String line) {
    try {
        line = "[%s] %s\n".formatted(Instant.now(), line);
        out.print(line);
        Files.writeString(fullPath("~/.cleaner.log"), line,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    } catch (Exception e) {
         err.format("Loggin error: %s\n", e);   
    }
}

void deleteRecursively(String path) {
    var fullPath = fullPath(path);
    try {
        log("Removing " + fullPath);
        if (fullPath.toString().contains("*"))
            XFiles.findFiles(fullPath.toString()).forEach(Unchecked.wrapAccept(Files::delete));
        else
            XFiles.deleteRecursively(fullPath);
    } catch (Exception e) {
        log("Error removing %s: %s".formatted(fullPath, e.getMessage()));
    }
}

void substitute(String path, boolean isPerLine, Map<String, String> mapping) {
    var substitutor = new Substitutor().withRegexpSupport();
    var fullPath = fullPath(path);
    try {
        var updatedFiles = isPerLine? substitutor.substitutePerLine(fullPath, mapping)
            : substitutor.substitute(fullPath, mapping);
        log("Update files: " + updatedFiles);
    } catch (Exception e) {
        log("Error updating %s: %s".formatted(fullPath, e.getMessage()));
    }
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
    deleteRecursively("~/.config/libreoffice/*/user/backup");
    deleteRecursively("~/.config/Microsoft");
    deleteRecursively("~/.java/.userPrefs/tool/JShell/prefs.xml");
    deleteRecursively("~/.xsession-errors");
    deleteRecursively("~/.sqlite_history");
    deleteRecursively("~/.lesshst");
    deleteRecursively("~/.octave_hist");

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
    new XExec("adb kill-server")
        .start()
        .forwardStdoutAsync(true)
        .stderrThrow();
}

void windowsCleanup() {
    log("Run Windows cleanup");
    deleteRecursively("C:\\Users\\All Users\\Adobe\\ARM");
    deleteRecursively("C:\\ProgramData\\Adobe\\ARM");
    deleteRecursively("C:\\ProgramData\\Microsoft\\EdgeUpdate\\Log");
    deleteRecursively("C:\\Program Files (x86)\\Microsoft\\EdgeUpdate");

    new XExec("schtasks.exe",
              "/Run",
              "/TN",
              "\\Microsoft\\Windows\\Servicing\\StartComponentCleanup")
        .start()
        .forwardStdoutAsync(true)
        .stderrThrow();
}

if (XUtils.isWindows()) {
    windowsCleanup();
} else {
    linuxCleanup();
}

log("Complete");