/**
 * Converts Markdown to HTML.
 *
 * Supports following element conversion (in parentheses is HTML tag used to
 * represent corresponding Markdown syntax):
 *
 * - headers (<h.>)
 * - links (<a>)
 * - **bold text** (<b>)
 * - bash blocks (<cmd>)
 * - text blocks (<pre>)
 * - paragraphs (<p>)
 *
 * Requirements:
 *
 * - jeval 19
 *
 * @author lambdaprime <https://github.com/lambdaprime>
 *
 */

if (args.length < 1)
    error("Please specify input file to convert");

class Line {
    String txt;
    boolean formatted;
    Line(String t) {
        txt = t;
    }
    public String toString() { return txt; }
}

var lines = Files.readAllLines(Paths.get(args[0])).stream()
    .map(Line::new)
    .collect(toList());

Consumer<Line> replaceSpecialSymbols = line -> line.txt = line.txt.replaceAll("<", "&lt;");
Consumer<Line> replaceHeaders = line -> line.txt = line.txt.replaceAll("^#### (.*)$", "<h4>$1</h4>")
    .replaceAll("^### (.*)$", "<h3>$1</h3>")
    .replaceAll("^## (.*)$", "<h2>$1</h2>")
    .replaceAll("^# (.*)$", "<h1>$1</h1>");
Consumer<Line> replaceBold = line -> line.txt = line.txt.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>");
Consumer<Line> replaceLinks = line -> line.txt = line.txt.replaceAll("\\[(.*?)\\]\\((.*?)\\)", "<a href=\"$2\">$1</a>");

void replaceBash(List<Line> lines) {
    boolean inside = false;
    for (int i = 0; i < lines.size(); i++) {
        var line = lines.get(i);
        if (line.txt.matches("^```.?bash$")) {
            var nextLine = lines.get(i + 1);
            nextLine.txt = "<cmd>" + nextLine.txt;
            nextLine.formatted = true;
            lines.remove(i);
            inside = true;
        }
        if (inside) line.formatted = true;
        if (line.txt.matches("^```$") && inside) {
            line.txt = "</cmd>";
            inside = false;
        }
    }
}

void replaceBlocks(List<Line> lines) {
    boolean inside = false;
    for (int i = 0; i < lines.size(); i++) {
        var line = lines.get(i);
        if (inside) line.formatted = true;
        if (!line.txt.matches("^```.*$")) continue;
        if (!inside) {
            var nextLine = lines.get(i + 1);
            nextLine.txt = "<pre>" + nextLine.txt;
            nextLine.formatted = true;
            lines.remove(i);
        } else {
            line.txt = "</pre>";
        }
        inside = !inside;
    }
}

void addParagraphs(List<Line> lines) {
    boolean inside = false;
    for (int i = 0; i < lines.size(); i++) {
        var line = lines.get(i);
        var txt = line.txt.trim();
        if (txt.isEmpty()) continue;
        if (txt.startsWith("<")) continue;
        if (line.formatted) continue;
        line.txt = String.format("<p>%s</p>", txt);
    }
}

var consumer = replaceSpecialSymbols
    .andThen(replaceHeaders);
lines.stream()
    .forEach(consumer);

replaceBash(lines);
replaceBlocks(lines);
addParagraphs(lines);

consumer = replaceBold
    .andThen(replaceLinks);
lines.stream()
    .forEach(consumer);

lines.stream()
    .forEach(out::println);
