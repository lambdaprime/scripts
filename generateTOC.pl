#! C:\Perl\bin\perl

# lambdaprime-scripts
#
# See README.org
#

if (@ARGV < 1) {
    print "please specify html input file name";
    exit -1;
}

$file = @ARGV[0];
$tmp = @ARGV[0].".bak";
open(FILE, "$file") or die "Unable to open $file: $!\n";
open(TMP, ">$tmp") or die "Unable to open $tmp: $!\n";

@content = ();
$exclude = 0;

print "Reading content...\n";
while (<FILE>) {
    if (/^<h[123]>(.*)<\/h.>$/) {
        chomp;
        $header = $_;
        $header =~ s/^<h.>(.*)<\/h.>$/\1/;
        print "Adding $header\n";
        print TMP "<a name=\"$header\" href=\"#top$header\">$_</a>\n";
        
        $_ =~ s/^<h1>(.*)<\/h1>$/<a class=\"h1link\" name=\"top$header\" href=\"#$header\">\1<\/a>/;
        $_ =~ s/^<h2>(.*)<\/h2>$/<a class=\"h2link\" name=\"top$header\" href=\"#$header\">\1<\/a>/;
        $_ =~ s/^<h3>(.*)<\/h3>$/<a class=\"h3link\" name=\"top$header\" href=\"#$header\">\1<\/a>/;
        push(@content, $_);
    } elsif (/^<exclude>$/) {
        $exclude = 1;
    } elsif (/^<\/exclude>$/) {
        $exclude = 0;
    } elsif (!$exclude) {
        print TMP $_;
    }
}

close(FILE);
close(TMP);
    
if (@content == 0) {
    print "Nothing found";
    unlink($tmp);
    exit -1;
}

open(FILE, ">$file") or die "Unable to open $file: $!\n";
open(TMP, "$tmp") or die "Unable to open $tmp: $!\n";

print "Looking for <content> tag...\n";
while (<TMP>) {
    print FILE $_;
    if (/^<content>$/) {
        print "Found\n";
        print "Filling it with founded headers...\n";
        foreach $header (@content) {
            print FILE "<div>$header</div>\n";
        }
        break;
    }
}

close(FILE);
close(TMP);
unlink($tmp);
